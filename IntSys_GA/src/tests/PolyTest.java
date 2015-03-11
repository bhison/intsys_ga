package tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import build1.Poly;

public class PolyTest {

	Poly poly;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testEncode() {		
		Poly a = new Poly(-128);
		System.out.println(a.b());
		System.out.println(a.s());
		System.out.println(a.i());
		a = new Poly("1000000010000000");
		System.out.println(a.b());
		System.out.println(a.s());
		System.out.println(a.i());
		a = new Poly("-128");
		System.out.println(a.b());
		System.out.println(a.s());
		System.out.println(a.i());
	}
}
