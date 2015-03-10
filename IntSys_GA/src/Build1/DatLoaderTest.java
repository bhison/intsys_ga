package Build1;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

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
		List<String[]> data = new ArrayList<String[]>();
		System.out.println("ArrayList Made");
		try { 
			data = dl.loadDat(); 
		} catch (IOException e) {
			System.out.println(e.toString()); 
		}
		System.out.println(data.get(1)[0]);
		System.out.println(data.get(2)[0]);
		System.out.println(data.get(3)[0]);
		
		assertTrue(true);
	}
	
//	@Test
//	public void testLoadDat2(){
//		try { 
//			dl.loadDat2(); 
//		} catch (IOException e) {
//			System.out.println(e.toString()); 
//		}
//	}
}
