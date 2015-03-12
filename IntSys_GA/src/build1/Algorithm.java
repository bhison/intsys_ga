package build1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;

import org.jfree.data.xy.XYSeries;

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
	
	private static final int POLY_BITS = 16;
	private static final int RANDOM_RANGE = 65536;	
	private static final int RANDOM_LIMIT = 32768;
	private static final int IMPROVEMENT_WAIT = 100000;
	private static final int CHART_UPDATE_FREQUENCY = 10000;
	private static final double STARTING_FITNESS = 9E50;
	
	private static final double BOTH_RATE = 0.2;
	private static final double CROSSOVER_RATE = 0.5;
	private static final double MUTATION_RATE = 0.7;
	
	private static final double CHARACTER_MUTATION_CHANCE = 0.15;
	private static final int TOURNAMENT_SIZE = 10;
	private static final int TOURNAMENT_WINNERS = 2;
	
	public Algorithm(DatLoader datLoader) {
		this.datLoader = datLoader;
		random = new Random();		
		dataSize = datLoader.getDataSize();
		targetYValues = datLoader.getYValues();
		bestFitness = STARTING_FITNESS;
		newBest = false;
		improvements = 0;
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
		Poly[] activeCoefSet = randPoly(6);
		boolean keepGoing = true;
		int cycles = 0;
		int cyclesLeft = IMPROVEMENT_WAIT;
		
		while(keepGoing){
			cycles ++;
			if(cycles % 10000 == 0) System.out.println("Cycle " + cycles + ", " + improvements + " improvements");
			Double[] resultYValues = runCheck(activeCoefSet);
			Double fitness = getFitness(resultYValues);
			if(fitness > bestFitness) { /*cyclesLeft --;*/ } //If higher as lower number = better fitness
			else {
				cyclesLeft = IMPROVEMENT_WAIT;
				updateBest(activeCoefSet,fitness);
			}
			activeCoefSet = randPoly(6);
			updateChart(cycles);
			keepGoing = continueLoop(cyclesLeft);
		}
		updateChart(cycles);
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
	private double getFitness(Double[] resultYValues){
		double result = 0;
		for(int i = 0; i < dataSize; i++){
			result += Math.sqrt(Math.pow(resultYValues[i] - targetYValues[i], 2));
		}
		return result;
	}
	
	private Poly[] runCrossover(Poly[] activeSet){
		return new Poly[]{};
	}
	
	private Poly[] runMutation(Poly[] activeSet){
		return new Poly[]{};		
	}
	
	/**
	 * Generate Poly with random value between 0 and RANDOM_LIMIT
	 * @return
	 */
	private Poly randPoly(){
		int r = random.nextInt(RANDOM_RANGE) - RANDOM_LIMIT;
		return new Poly(r);
	}
	
	/**
	 * Generate set of Polys with random value between 0 and RANDOM_LIMIT
	 * @param n The size of the set
	 * @return random poly set
	 */
	private Poly[] randPoly(int n){
		Poly[] returnSet = new Poly[n];
		for(int i = 0; i < n; i++){
			returnSet[i] = randPoly();
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
	
	private void updateChart(int cycles){
		if(cycles % CHART_UPDATE_FREQUENCY == 0 && newBest){
			//Code to update chart			
			newBest = false;
		};
	}
	
	/**
	 * Stopping criteria check
	 * @return Whether loop should stop
	 */
	private boolean continueLoop(int cyclesLeft){
		if(bestFitness == 1 || cyclesLeft == 0) {
			System.out.println("Stop loop - Fitness: " + bestFitness + ", Remaining cycles: " + cyclesLeft);
			return false;
		}
		else return true;
	}
	
	public Poly[] mutate(Poly[] candidates){
		StringBuilder chromosome = new StringBuilder(polyArrayToChromosone(candidates));
		for(int i = 0; i < chromosome.length(); i++) {
			double diceRoll = random.nextDouble();
			System.out.println("Char at " + i + ", MC:" + CHARACTER_MUTATION_CHANCE + 
					" DR:" + diceRoll);
			if(diceRoll < CHARACTER_MUTATION_CHANCE) {
				char c = chromosome.charAt(i) == '0' ? '1' : '0';
				chromosome.setCharAt(i, c);
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
	
	public Poly[][] crossOver(Poly[][] pair) {
		
	}

}
