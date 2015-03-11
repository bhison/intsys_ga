package tests;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import build1.DatLoader;

public class DatLoaderTest {

	DatLoader dl;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		dl = new DatLoader();
		System.out.println("New DatLoader");
	}

	@Test
	public void testLoadDat() {
		System.out.println("testLoadDat");
		Double[][] data = dl.getDataList();
		System.out.println("ArrayList Made");
		for(Double[] entry : data){
			System.out.println("LDT:" + entry[0] + " " + entry[1]);
		}
		assertTrue(true);
	}
}
