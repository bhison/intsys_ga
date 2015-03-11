package build1;

import org.jfree.ui.RefineryUtilities;

public class Main {
	DatLoader datLoader;
	LineChart lineChart;

	public Main(Boolean runAlgo) {
		datLoader = new DatLoader();
		lineChart = new LineChart("Intelligent Systems Project", "GA Graph", datLoader.getXYSeriesCollection());
		lineChart.pack();
	    RefineryUtilities.centerFrameOnScreen(lineChart);
	    lineChart.setVisible(true); 
	    System.out.println("All done...");
	    if(runAlgo) { runAlgo(); }
	}
	
	private void runAlgo(){
		new Algorithm(datLoader);
	}

	public static void main(String[] args) {
		boolean runAlgo = true;
		if(args[0] == "0") runAlgo = false;
		new Main(runAlgo);	
	}
}


