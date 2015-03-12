package build1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;

import org.jfree.data.xy.XYSeries;

//Hall of fame, top 10 solutions to be added back in randomly

public class Algorithm {

	private DatLoader datLoader;
	private Random random;
	private int dataSize;
	private Double[] targetYValues;
	private Poly[] bestSet;
	private double bestFitness; //The lower the better, 0 is success
	private boolean newBest;
	private int improvements;
	private long timeOfLastUpdate;
	private boolean doUpdates;
	private double cycles;
	private long startTime;
	private Poly[][] population;
	private Poly[][] nextGeneration;
	
	private static final int POLY_BITS = 16;
	private static final int RANDOM_RANGE = 65536; //MAX: 65536	
	private static final int RANDOM_LIMIT = RANDOM_RANGE / 2; //MAX: 32768
	private static final double IMPROVEMENT_WAIT = 1000000000;
	private static final int CHART_UPDATE_FREQUENCY = 10000;
	private static final double STARTING_FITNESS = 9E50;
	private static final int CYCLES_BEFORE_UPDATE = 10000;
	
	private static final double BOTH_RATE = 0.1;
	private static final double CROSSOVER_RATE = 0.7;
	private static final double MUTATION_RATE = 0.1;
	
	private static final double CHARACTER_MUTATION_CHANCE = 0.05;
	private static final double POLY_CROSSOVER_CHANCE = 0.5;	
	private static final int TOURNAMENT_SIZE = 10;
	private static final int TOURNAMENT_WINNERS = 2;
	
	public Algorithm(DatLoader datLoader) {
		this.datLoader = datLoader;
		startTime = System.currentTimeMillis();
		random = new Random();		
		dataSize = datLoader.getDataSize();
		targetYValues = datLoader.getYValues();
		bestFitness = STARTING_FITNESS;
		newBest = false;
		improvements = 0;
		doUpdates = false;
		algoLoop();
	}
	
	//Test constructor - doesn't run the algorithm
	public Algorithm(Boolean isTest) {
		datLoader = new DatLoader();
		random = new Random();		
		dataSize = datLoader.getDataSize();
		targetYValues = datLoader.getYValues();
		bestFitness = STARTING_FITNESS;
		newBest = false;
		improvements = 0;
	}
	
	/**
	 * The main algorithm loop
	 */
	private synchronized void algoLoop() {
		//Setup variables
		Poly[][] champions = new Poly[0][0];
		boolean keepGoing = true;
		cycles = 0;
		double cyclesLeft = IMPROVEMENT_WAIT; //TODO have this decremented, reset somewhere
		
		while(keepGoing){
			cycles ++;
			if(cycles % CYCLES_BEFORE_UPDATE == 0) { 
				doUpdates = true; 
				System.out.println("!!!!!!!!!!!!!!!!!!!!-----UPDATES-----!!!!!!!!!!!!!!!!!!!!!");
			}
			champions = runTournament(champions);
			double changeFactor = random.nextDouble();
			boolean mutate = false;
			boolean crossover = false;
			if(changeFactor < MUTATION_RATE) { mutate = true; } else
			if(changeFactor > 1 - CROSSOVER_RATE) { crossover = true; } else
			if(changeFactor < BOTH_RATE + MUTATION_RATE) { mutate = true; crossover = true; }
			if(doUpdates) System.out.println("Mutate: " + mutate + ", Crossover: " + crossover);
			if(random.nextDouble() >= 0.5){
				if(mutate) {
					champions[0] = doMutate(champions[0]);
				}
				if(crossover){
					Poly[][] crossed = doCrossover(champions[0], champions[1]);
					champions[0] = crossed[0];
					champions[1] = crossed[1];
				}
			} else {
				if(crossover){
					Poly[][] crossed = doCrossover(champions[0], champions[1]);
					champions[0] = crossed[0];
					champions[1] = crossed[1];
				}
				if(mutate) {
					champions[0] = doMutate(champions[0]);
				}
			}
			updateChart(cycles);
			keepGoing = continueLoop(cyclesLeft);
			if(doUpdates) giveUpdate();
		}
		updateChart(cycles);
	}
	
	/**
	 * Run tournament with champions and new challengers, returning winners
	 * @param champions Sets to be included by default
	 * @return the winners
	 */
	private synchronized Poly[][] runTournament(Poly[][] champions) {
		Poly[][] competitors = new Poly[TOURNAMENT_SIZE][6];
		Poly[][] challengers = randPolySet(TOURNAMENT_SIZE - champions.length);
		
		for(int i = 0; i < champions.length; i++) {
			competitors[i] = champions[i];
		}
		for(int i = 0; i < challengers.length; i++){
			competitors[i + champions.length] = challengers[i];
		}
		
		double[] fitnessScores = new double[TOURNAMENT_SIZE];
		for(int i = 0; i < TOURNAMENT_SIZE; i++){
			Double[] resultYValues = runCheck(competitors[i]);
			fitnessScores[i] = getFitness(resultYValues, competitors[i]);
		}
		
		int[] rankings = new int[TOURNAMENT_SIZE];
		for(int i = 0; i < TOURNAMENT_SIZE; i++){
			double c = fitnessScores[i];
			int position = TOURNAMENT_SIZE - 1;
			for(int j = 0; j < TOURNAMENT_SIZE; j++){
				if(c < fitnessScores[j]) position --;
			}
			rankings[position] = i;
		}
		
		Poly[][] winners = new Poly[TOURNAMENT_WINNERS][6];
		for(int i = 0; i < TOURNAMENT_WINNERS; i++){
			winners[i] = competitors[rankings[i]];
		}
		
		return winners;
	}
	
	/**
	 * Input coefficients, get returned a list of y values for sample x values
	 * @param coefficients
	 * @return the y values array
	 */
	private synchronized Double[] runCheck(Poly[] coefficients){
		Double[] results = new Double[dataSize];
		ArrayList<Double> xValues = new ArrayList<Double>(Arrays.asList(datLoader.getXValues()));
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
	 * Get normalised total variance from target values
	 * @param resultYValues
	 * @return fitnessValue
	 */
	private double getFitness(Double[] resultYValues, Poly[] coefficients){
		double result = 0;
		for(int i = 0; i < dataSize; i++){
			result += Math.sqrt(Math.pow(resultYValues[i] - targetYValues[i], 2));
		}
		if(result < bestFitness) updateBest(coefficients, result);
		if(doUpdates) System.out.println("Fitness calculation: " + result);
		return result;
	}
	
	/**
	 * Generate Poly with random value between 0 and RANDOM_LIMIT
	 * @return
	 */
	public Poly randPoly(){
		int r = random.nextInt(RANDOM_RANGE) - RANDOM_LIMIT;
		return new Poly(r);
	}
	
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
	
	
	private void updateBest(Poly[] newBestSet, Double newBestFitness) {
		bestSet = newBestSet;
		bestFitness = newBestFitness;
		improvements ++;
		newBest = true;
		long wait = System.currentTimeMillis() - timeOfLastUpdate;
		timeOfLastUpdate = System.currentTimeMillis();
		System.out.println("******************************************************");
		System.out.println("=====New bestFitness = " + newBestFitness + "======");
		System.out.println("============Improvement took " + wait/1000 + "seconds===========");
		System.out.println("******************************************************");
	}
	
	private void updateChart(double cycles){
		if(cycles % CHART_UPDATE_FREQUENCY == 0 && newBest){
			//Code to update chart			
			newBest = false;
		};
	}
	
	/**
	 * Stopping criteria check
	 * @return Whether loop should stop
	 */
	private boolean continueLoop(double cyclesLeft){
		if(bestFitness == 1 || cyclesLeft == 0) {
			System.out.println("Stop loop - Fitness: " + bestFitness + ", Remaining cycles: " + cyclesLeft);
			return false;
		}
		else return true;
	}
	
	public synchronized Poly[] doMutate(Poly[] candidates){
		StringBuilder chromosome = new StringBuilder(polyArrayToChromosone(candidates));
		for(int i = 0; i < chromosome.length(); i++) {
			double diceRoll = random.nextDouble();
			if(diceRoll < CHARACTER_MUTATION_CHANCE) {
				char c = chromosome.charAt(i) == '0' ? '1' : '0';
				chromosome.setCharAt(i, c);
				if(doUpdates) {
					System.out.println("Successful mutation, DR:" + diceRoll + ", CMC:" + CHARACTER_MUTATION_CHANCE);
				}
			}
		}
		return chromosomeToPolyArray(chromosome.toString());
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
	
	public Poly[][] doCrossover(Poly[] set1, Poly[] set2) {
		Poly bench;
		int crossed = 0;
		for(int i = 0; i < set1.length; i++){
			if(random.nextDouble() < POLY_CROSSOVER_CHANCE) {
				bench = set1[i];
				set1[i] = set2[i];
				set2[i] = bench;
				crossed ++;
				if(doUpdates) System.out.println(">Crossed over data");
			}
		}
		//System.out.println(crossed + " elements crossed over.");
		return new Poly[][]{set1, set2};
	}

	private void giveUpdate() {
		System.out.println("Cycle " + cycles);
		System.out.println("Best fitness is: " + bestFitness);
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
		doUpdates = false;
	}
}
