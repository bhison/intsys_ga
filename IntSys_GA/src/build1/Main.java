package build1;

import org.jfree.ui.RefineryUtilities;

public class Main {
	DatLoader datLoader;
	LineChart lineChart;

	public Main() {
		datLoader = new DatLoader();
		lineChart = new LineChart("Intelligent Systems Project", "GA Graph", datLoader.getXYSeriesCollection());
		lineChart.pack();
	    RefineryUtilities.centerFrameOnScreen(lineChart);
	    lineChart.setVisible(true); 
	    System.out.println("All done...");
	    //new Algorithm(datLoader);
	}

	public static void main(String[] args) {
		new Main();
	}
}


