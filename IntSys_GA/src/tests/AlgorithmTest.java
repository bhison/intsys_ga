package tests;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import build1.Algorithm;
import build1.Poly;

public class AlgorithmTest {
	
	Algorithm algo;
	Poly[] polyArray;
	
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
	}

	@Test
	public void testMutate() {		
		Poly[] changedArray = algo.mutate(polyArray);
		for(Poly p : changedArray){
			System.out.println(p.b());
		}
	}

}
