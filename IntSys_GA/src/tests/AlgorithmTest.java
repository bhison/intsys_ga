//package tests;
//
//import org.junit.Before;
//import org.junit.BeforeClass;
//import org.junit.Test;
//
//import build1.Algorithm;
//import build1.Poly;
//
//public class AlgorithmTest {
//	
//	Algorithm algo;
//	Poly[] polyArray;
//	Poly[][] polyArrayArray;
//	
//	@BeforeClass
//	public static void setUpBeforeClass() throws Exception {
//	}
//
//	@Before
//	public void setUp() throws Exception {
//		algo = new Algorithm(true);
//		polyArray = new Poly[]{
//				new Poly("0000000000000000"),
//				new Poly("1111111111111111")
//		};
//		polyArrayArray = new Poly[][]{
//				algo.randPoly(6),
//				algo.randPoly(6),
//		};
//	}
//
//	
//	public void testMutate() {		
//		Poly[] changedArray = algo.doMutate(polyArray);
//		for(Poly p : changedArray){
//			System.out.println(p.b());
//		}
//	}
//	
//	@Test
//	public void testCrossover() {
//		System.out.println("BEFORE:");
//		printArrayArray();
//		polyArrayArray = algo.doCrossover(polyArrayArray[0], polyArrayArray[1]);
//		System.out.println("AFTER");
//		printArrayArray();
//	}
//	
//	private void printArrayArray(){
//		int i = 0;
//		int j = 0;
//		for(Poly[] array : polyArrayArray){
//			for(Poly p : array) {
//				System.out.println("["+i+"]["+j+"]" + p.s());
//				j ++;
//			}
//			i ++;
//			j = 0;
//		}
//	}
//	
//	@Test
//	public void testArrays(){
//		Poly[][] p = new Poly[1][2];
//		p[0][1] = new Poly(5);
//	}
//
//}
//
/////*
////	/**
////	 * The main algorithm loop
////	 */
////	private synchronized void algoLoop() {
////		//Setup variables
////		Poly[][] competitors = new Poly[6][5];
////		Poly[] activeCoefSet = randPoly(6);
////		boolean keepGoing = true;
////		int cycles = 0;
////		double cyclesLeft = IMPROVEMENT_WAIT;
////		
////		while(keepGoing){
////			cycles ++;
////			if(cycles % 10000 == 0) System.out.println("Cycle " + cycles + ", " + improvements + " improvements");
////			Double[] resultYValues = runCheck(activeCoefSet);
////			Double fitness = getFitness(resultYValues);
////			if(fitness > bestFitness) { cyclesLeft --; } //If higher as lower number = better fitness
////			else {
////				cyclesLeft = IMPROVEMENT_WAIT;
////				updateBest(activeCoefSet,fitness);
////			}
////			activeCoefSet = randPoly(6);
////			updateChart(cycles);
////			keepGoing = continueLoop(cyclesLeft);
////		}
////		updateChart(cycles);
////	}
////	
//
