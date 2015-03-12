package tests;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import build1.Algorithm;
import build1.Poly;

public class AlgorithmTest {
	
	Algorithm algo;
	Poly[] polyArray;
	Poly[][] polyArrayArray;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		algo = new Algorithm(true);
		polyArray = new Poly[]{
				new Poly("0000000000000000"),
				new Poly("1111111111111111")
		};
		polyArrayArray = new Poly[][]{
				algo.randPoly(6),
				algo.randPoly(6),
		};
	}

	
	public void testMutate() {		
		Poly[] changedArray = algo.mutate(polyArray);
		for(Poly p : changedArray){
			System.out.println(p.b());
		}
	}
	
	@Test
	public void testCrossover() {
		System.out.println("BEFORE:");
		printArrayArray();
		polyArrayArray = algo.crossOver(polyArrayArray);
		System.out.println("AFTER");
		printArrayArray();
	}
	
	private void printArrayArray(){
		int i = 0;
		int j = 0;
		for(Poly[] array : polyArrayArray){
			for(Poly p : array) {
				System.out.println("["+i+"]["+j+"]" + p.s());
				j ++;
			}
			i ++;
			j = 0;
		}
	}

}
