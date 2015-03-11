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
	private double bestFitness;
	
	private boolean newBest;
	
	private static final int RANDOM_LIMIT = 255;
	private static final double MUTATION_RATE = 0.7;
	private static final double CROSSOVER_RATE = 0.5;
	private static final int IMPROVEMENT_WAIT = 100;
	private static final int CHART_UPDATE_FREQUENCY = 25;
	
	public Algorithm(DatLoader datLoader) {
		this.datLoader = datLoader;
		random = new Random();		
		dataSize = datLoader.getDataSize();
		targetYValues = datLoader.getYValues();
		bestFitness = 0;
		newBest = false;
		algoLoop();
	}
	
	/**
	 * The main algorithm loop
	 */
	private void algoLoop() {
		//Setup variables
		Poly[] activeCoefSet = new Poly[] {
				randPoly(), randPoly(), randPoly(), randPoly(), randPoly()
			};
		boolean keepGoing = true;
		int cycles = 0;
		int cyclesLeft = IMPROVEMENT_WAIT;
		
		while(keepGoing){
			cycles ++;
			Double[] resultXValues = runCheck(activeCoefSet);
			Double fitness = getFitness(resultXValues);
			if(fitness < bestFitness) cyclesLeft --;
			else {
				cyclesLeft = IMPROVEMENT_WAIT;
				updateBest(activeCoefSet,fitness);
			}
			updateChart(cycles);
			keepGoing = continueLoop(cyclesLeft);
		}
		updateChart(cycles);
	}
	
	/**
	 * Input coefficients, get returned a list of f(x) for sample x values
	 * @param coefficients
	 * @return
	 */
	private Double[] runCheck(Poly[] coefficients){
		Double[] results = new Double[dataSize];
		ArrayList<Double> xValues = new ArrayList<Double>(Arrays.asList(datLoader.getXValues()));
		Iterator<Double> it = xValues.iterator();
		int i = 0;
		while(it.hasNext()){
			double a, b, c, d, e, f, x;
			x = it.next();
			a = coefficients[0].i();
			b = coefficients[1].i() * x;
			c = Math.pow(coefficients[2].i() * x, 2);
			d = Math.pow(coefficients[3].i() * x, 3);
			e = Math.pow(coefficients[4].i() * x, 4);
			f = Math.pow(coefficients[5].i() * x, 5);
			results[i] = (a+b+c+d+e+f);
		}
		it.remove();
		return results;
	}
	
	/**
	 * Get normalised total variance from target values
	 * @param resultXList
	 * @return fitnessValue
	 */
	private double getFitness(Double[] resultXList){
		
		return 0.0d;
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
		int r = random.nextInt(RANDOM_LIMIT);
		return new Poly(r);
	}
	
	private void updateBest(Poly[] newBestSet, Double newBestFitness) {
		bestSet = newBestSet;
		bestFitness = newBestFitness;
		newBest = true;
		System.out.println("New bestFitness = " + newBestFitness);
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

}
