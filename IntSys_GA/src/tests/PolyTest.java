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
		Poly x = new Poly(123);
		Poly y = new Poly("123");
		Poly z = new Poly("01111011");
		assertEquals(x.s(), "123");
		assertEquals(y.b(), "01111011");
		assertEquals(z.i(), (int)123);
		assertEquals(new Poly(8).b(), "00001000");
	}

}
