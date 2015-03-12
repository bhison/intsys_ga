package build1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;

import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;


public class Algorithm2 {
	
	private static final int POLY_BITS = 16;
	private static final int RANDOM_RANGE = 65536; //MAX: 65536	
	private static final int RANDOM_LIMIT = RANDOM_RANGE / 2; //MAX: 32768
	private static final double GIVE_UP_AFTER_CYCLES = 1000000000;
	private static final int CHART_UPDATE_FREQUENCY = 10000;
	private static final double STARTING_FITNESS = 9E50;
	private static final int CYCLES_BEFORE_UPDATE = 10000;
	//Generation change rates
	private static final double BOTH_RATE = 0.1;
	private static final double CROSSOVER_RATE = 0.5;
	private static final double MUTATION_RATE = 0.2;
	//If selected, what rate do they undergo
	private static final double DEFAULT_CHARACTER_MUTATION_CHANCE = 0.10;
	private double CHARACTER_MUTATION_CHANCE = DEFAULT_CHARACTER_MUTATION_CHANCE;
	private static final double SPLICE_POINT_CHANCE = 0.1;
	//Tournament rules
	private static final int TOURNAMENT_SIZE = 10;
	private static final int TOURNAMENT_WINNERS = 2;
	//Other important things
	private static final int POPULATION_SIZE = 100;
	private static final int CONSOLE_UPDATE_FREQUENCY = 1000;
	private static final int GIVE_UP_TIMER = 300000;
	
	//Data types
	private DatLoader datLoader;
	private int dataSize;
	private Double[] targetYValues;
	private ArrayList<Double> xValues;
	private static Random random = new Random();
	private LineChart chart;
	//Best info
	private String bestChromosome;
	private double bestFitness = STARTING_FITNESS; //The lower the better, 0 is success
	private double[] bestYValues;
	private boolean newBest = false;
	//Population info
	private String[] population;
	private double[] populationFitness;
	private int[] populationRank;
	private ArrayList<String> nextGeneration;
	//Console things
	private int improvements = 0;
	private int generationOfLastImprovement;
	private long timeOfLastUpdate = 0;
	private boolean doUpdates = false;
	private double cycles = 0;
	private double cycles_remaining = GIVE_UP_AFTER_CYCLES; //TODO have this decremented/reset
	private int generation = 0;
	private long startTime = System.currentTimeMillis();
	
	/**
	 * The main constructor
	 * @param datLoader
	 */
	public Algorithm2(DatLoader datLoader, LineChart lineChart) {
		this.datLoader = datLoader;
		chart = lineChart;
		dataSize = datLoader.getDataSize();
		xValues = new ArrayList<Double>(Arrays.asList(datLoader.getXValues()));
		targetYValues = datLoader.getYValues();
		mainLoop();
	}
	
	/**
	 * The main algorithm loop
	 */
	private synchronized void mainLoop() {
		randomNextGeneration();
		boolean keepGoing = true;
		while(keepGoing){
			generation ++;
			population = nextGeneration.toArray(new String[nextGeneration.size()]);
			populationFitness = new double[POPULATION_SIZE];
			populationRank = new int[POPULATION_SIZE];
			analysePopulation();
			nextGeneration = new ArrayList<String>();
			while(nextGeneration.size() < POPULATION_SIZE){
				addToNextGeneration();
			}
			int rand = random.nextInt(100);
			String[] bonus = doCrossover(bestChromosome, population[rand]);
			if(generation % 1000 == 0) {
				nextGeneration.set(rand, bonus[0]);
			}
			nextGeneration.set(98, doMutate(bonus[1]));
			updateChart();
			if(generation % CONSOLE_UPDATE_FREQUENCY == 0) { giveUpdate(); }
		}
	}
	
	/**
	 * Create a string array of coefficient binaries in nextGeneration
	 */
	private void randomNextGeneration(){
		nextGeneration = new ArrayList<String>();
		for(int i = 0; i < POPULATION_SIZE; i++){
			nextGeneration.add(randomChromosome());
		}
	}
	
	private String randomChromosome(){
		StringBuilder sb = new StringBuilder();
		for(int j = 0; j < 6; j++){
			sb.append(randPoly().b());
		}
		return sb.toString();
	}
	
	/**
	 * Take record of fitness and rank of current population
	 */
	private void analysePopulation() {
		//Calculate fitness
		for(int i = 0; i < population.length; i++){
			populationFitness[i] = getFitness(population[i]);
		}
		for(int i = 0; i < population.length; i++){
			int rank = population.length-1;
			double f = populationFitness[i];
			for(int j = 0; j < population.length; j++){
				if(f < populationFitness[j]) rank --;
			}
			populationRank[i] = rank;
		}
	}
	
	/**
	 * Use tournament selection to get next generation
	 */
	private synchronized void addToNextGeneration() {
		Integer[] contenders = new Integer[TOURNAMENT_SIZE];
		ArrayList<Integer> winners = new ArrayList<Integer>();
		for(int i = 0; i < TOURNAMENT_SIZE; i++){
			int n = random.nextInt(population.length);
			while(Arrays.asList(contenders).contains(n) ||
					Arrays.asList(nextGeneration).contains(n)){
				n = random.nextInt(population.length);
			}
		}
		boolean repeat = true;
		int i = 0;
		while(winners.size() < TOURNAMENT_WINNERS){
			for(int c = 0; c < TOURNAMENT_SIZE; c++){
				if(populationRank[c] == i){
					winners.add(c);
				}
			}
			i++;
		}
		double changeChance = random.nextDouble();
		
		String[] children = new String[] { population[winners.get(0)], population[winners.get(1)] };
		if(changeChance < CROSSOVER_RATE + BOTH_RATE){
			children = doCrossover(children[0], children[1]);
		} 
		if (changeChance > 1 - MUTATION_RATE || (changeChance > CROSSOVER_RATE 
				&& changeChance < CROSSOVER_RATE + BOTH_RATE) ) {
			for(int j = 0; j < children.length; j++){
				children[j] = doMutate(children[j]);
			}
		}
		if(nextGeneration.size() == POPULATION_SIZE - 1) {
			nextGeneration.add(children[0]);
		} else {
			int factor = Math.round(random.nextInt() % 3);
			switch(factor){
				case 0:
					nextGeneration.add(children[0]);
					break;
				case 1:
					nextGeneration.add(children[1]);
					break;
				case 2:
					nextGeneration.add(children[0]);
					nextGeneration.add(children[1]);
			}
		}
	}
	
	/**
	 * Input coefficients, get returned a list of y values for sample x values
	 * @param coefficients
	 * @return the y values array
	 */
	private synchronized double[] runCheck(String chromosome){
		Poly[] coefficients = chromosomeToPolyArray(chromosome);
		double[] results = new double[dataSize];
		xValues = new ArrayList<Double>(Arrays.asList(datLoader.getXValues()));
		Iterator<Double> it = xValues.iterator();
		int i = 0;
		while(it.hasNext()){
			double x, a, b, c, d, e, f;
			x = it.next();
			a = coefficients[0].i();
			b = coefficients[1].i() * x;
			c = coefficients[2].i() * Math.pow(x, 2);
			d = coefficients[3].i() * Math.pow(x, 3);
			e = coefficients[4].i() * Math.pow(x, 4);
			f = coefficients[5].i() * Math.pow(x, 5);
			results[i] = (a+b+c+d+e+f);
			i++;
		}
		it.remove();
		return results;
	}
	
	/**
	 * Return a chromsome's fitness
	 * @param chromosome
	 * @return
	 */
	private double getFitness(String chromosome){
		double[] resultYValues = runCheck(chromosome);
		double result = 0;
		for(int i = 0; i < dataSize; i++){
			result += Math.sqrt(Math.pow(resultYValues[i] - targetYValues[i], 2));
		}
		if(result < bestFitness) updateBest(chromosome, result, resultYValues);
		return result;
	}
		
	/**
	 * When best is found, record it
	 * @param newBestChromosome
	 * @param newBestFitness
	 */
	private void updateBest(String newBestChromosome, Double newBestFitness, double[] yValues) {
		bestChromosome = newBestChromosome;
		bestFitness = newBestFitness;
		bestYValues = yValues;
		generationOfLastImprovement = generation;
		improvements ++;
		newBest = true;
		long wait = System.currentTimeMillis() - timeOfLastUpdate;
		timeOfLastUpdate = System.currentTimeMillis();
		System.out.println("******************************************************");
		System.out.println("=====New bestFitness = " + newBestFitness + "======");
		System.out.println("============Improvement took " + wait/1000 + "seconds===========");
		System.out.println("******************************************************");
	}
	
	public synchronized String doMutate(String chromosome){
		StringBuilder sb = new StringBuilder(chromosome);
		for(int i = 0; i < sb.length(); i++) {
			if(random.nextDouble() < CHARACTER_MUTATION_CHANCE) {
				char c = sb.charAt(i) == '0' ? '1' : '0';
				sb.setCharAt(i, c);
			}
		}
		return sb.toString();
	}
	
	public String[] doCrossover(String mum, String dad) {
		int splicePoint = 0;
		while(random.nextDouble() < SPLICE_POINT_CHANCE && splicePoint < 16){
			splicePoint++;
		}
		String mum1, mum2, dad1, dad2;
		mum1 = mum.substring(0, splicePoint);
		mum2 = mum.substring(splicePoint);
		dad1 = dad.substring(0, splicePoint);
		dad2 = dad.substring(splicePoint);
		
		return new String[]{mum1 + dad2, dad1 + mum2};
	}
		
	
/////////////////////////////////////////////////////////////	
///////////STABLE STUFF - DON'T WORRY ABOUT THIS/////////////
/////////////////////////////////////////////////////////////
	
	/**
	 * Generate set of Poly with random value between 0 and RANDOM_LIMIT
	 * @param n The size of the set
	 * @return random poly set
	 */
	public Poly[] randPoly(int n){
		Poly[] returnSet = new Poly[n];
		for(int i = 0; i < n; i++){
			returnSet[i] = randPoly();
		}
		return returnSet;
	}
	
	/**
	 * Generates 2 dim array
	 * @param n
	 * @return 2 dim array of random polys
	 */
	public Poly[][] randPolySet(int n){
		Poly[][] returnSet = new Poly[n][0];
		for(int i = 0; i < n; i++){
			returnSet[i] = randPoly(6);
		}
		return returnSet;
	}
	
	/**
	 * Generate Poly with random value between 0 and RANDOM_LIMIT
	 * @return
	 */
	public Poly randPoly(){
		int r = random.nextInt(RANDOM_RANGE) - RANDOM_LIMIT;
		return new Poly(r);
	}
	
	private String polyArrayToChromosone(Poly[] array) {
		StringBuilder chromosome = new StringBuilder();
		for(Poly p : array){
			chromosome.append(p.b());
		}
		return chromosome.toString();
	}
	
	private Poly[] chromosomeToPolyArray(String chromosome){
		int size = chromosome.length() / POLY_BITS;
		Poly[] returnArray = new Poly[size];
		for(int i = 0; i < size; i++) {
			returnArray[i] = new Poly(chromosome.substring(i * POLY_BITS, ((i + 1) * POLY_BITS)));
		}
		return returnArray;
	}
	
	private void giveUpdate() {
		System.out.println("Generation " + generation);
		System.out.println("Best fitness is: " + bestFitness);
		Poly[] bestSet = chromosomeToPolyArray(bestChromosome);
		System.out.println("Best coefficients are: " +
				bestSet[0].s() +" "+
				bestSet[1].s() +" "+
				bestSet[2].s() +" "+
				bestSet[3].s() +" "+
				bestSet[4].s() +" "+
				bestSet[5].s());
		System.out.println(improvements + " improvements have been made over " +
				(System.currentTimeMillis() - startTime) / 1000 +
				" seconds");
		System.out.println("Last improvement " +
				(System.currentTimeMillis() - timeOfLastUpdate) / 1000 +
				" seconds ago");
		CHARACTER_MUTATION_CHANCE = DEFAULT_CHARACTER_MUTATION_CHANCE;
		doUpdates = false;
	}
	
	/**
	 * Stopping criteria check
	 * @return Whether loop should stop
	 */
	private boolean continueLoop(double cyclesLeft){
		if(bestFitness == 1 || System.currentTimeMillis() - timeOfLastUpdate > GIVE_UP_TIMER) {
			System.out.println("GIVE UP");
			return false;
		}
		else return true;
	}
	
	/**
	 * TODO Do this, it'll be cool
	 */
	private void updateChart(){

		if(generation % CHART_UPDATE_FREQUENCY == 0 && newBest){
			double[] x = new double[] { Arrays.asList(xValues).toArray() };
			chart.updatePlot(xValues, bestYValues);
			
			
			XYSeries series = new XYSeries("Best Guess");
			for(int i = 0; i < bestYValues.length; i++){
				double x = xValues.get(i);
				double y = bestYValues[i];
				series.add(x,y);
			}
			chart.updatePlot(series);
		};
	}

}
