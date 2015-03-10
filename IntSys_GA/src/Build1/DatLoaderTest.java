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
	}

	@Test
	public void testLoadDat() {
		List<String[]> data = new ArrayList<String[]>();
		try { data = dl.loadDat(); } catch (IOException e) { System.out.println(e); }
		System.out.println(data.get(1));
	}

}
