package build1;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import org.jfree.ui.RefineryUtilities;

public class Main {
	DatLoader datLoader;
	LineChart lineChart;
	Random random;

	public Main() {
		datLoader = new DatLoader();
		lineChart = new LineChart("Intelligent Systems Project", "GA Graph", datLoader.getXYSeriesCollection());
		lineChart.pack();
	    RefineryUtilities.centerFrameOnScreen(lineChart);
	    lineChart.setVisible(true); 
	    System.out.println("All done...");
	    random = new Random();
	    startYourEngines();
	}
	
	private void startYourEngines(){
		Poly[] startingSet = new Poly[] {
			randPoly(), randPoly(), randPoly(), randPoly(), randPoly()
		};
		ArrayList<Double> results = runCheck(startingSet);
	}
	
	private ArrayList<Double> runCheck(Poly[] coefficients){
		ArrayList<Double> results = new ArrayList<Double>();
		ArrayList<Integer> xValues = datLoader.getXValues();
//		Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
//		int i = 0;
//		while(it.hasNext()){
//			Map.Entry<String, String> pair = (Map.Entry<String, String>)it.next();
//			columnsArray[i] = pair.getKey();
//			valuesArray[i] = pair.getValue();
//			i++;
//		}
//		it.remove();
		Iterator<Integer> it = xValues.iterator();
		while(it.hasNext()){
			double a, b, c, d, e, f, x;
			x = it.next();
			a = coefficients[0].i();
			b = coefficients[1].i() * x;
			c = Math.pow(coefficients[2].i() * x, 2);
			d = Math.pow(coefficients[3].i() * x, 3);
			e = Math.pow(coefficients[4].i() * x, 4);
			f = Math.pow(coefficients[5].i() * x, 5);
			results.add(a+b+c+d+e+f);
		}
		it.remove();
		
		return results;
	}
	
	private Poly randPoly(){
		int r = random.nextInt(255);
		return new Poly(r);
	}

	public static void main(String[] args) {
		new Main();
	}
}


