package build1;

import java.io.IOException;
import java.util.ArrayList;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

//To use: chart.getXYPlot().setRenderer(new XYSplineRenderer());
//Double to binary: http://stackoverflow.com/questions/6359847/convert-double-to-binary-representation

public class DisplayGraph extends ApplicationFrame {

	public DisplayGraph(String applicationTitle, String chartTitle) {
		super(applicationTitle);
		
		XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(getData());
		JFreeChart lineChart = ChartFactory.createXYLineChart(
				chartTitle,
				"x",
				"f(x)",
				dataset,
				PlotOrientation.VERTICAL,
				true,
				true,
				false
				);
		lineChart.getXYPlot().setRenderer(new XYSplineRenderer());

		ChartPanel chartPanel = new ChartPanel(lineChart);
		chartPanel.setPreferredSize(new java.awt.Dimension(560, 367));
		setContentPane(chartPanel);
	}

	private XYSeries getData() {
		DatLoader loader = new DatLoader();
		ArrayList<String[]> data = new ArrayList<String[]>();
		try {
			loader.loadDat();
		} catch (IOException e) {
			System.out.println(e.toString());
		}
		return createSeries(data);
	}
	
	private XYSeries createSeries(ArrayList<String[]> data){
		XYSeries returnSeries = new XYSeries("TargetCurve");
		for(String[] entry : data){
			returnSeries.add(Double.parseDouble(entry[0]), Double.parseDouble(entry[1]));
		}
		return returnSeries;
	}
	
	public static void main(String[] args) {
		DisplayGraph chart = new DisplayGraph("Intelligent Systems Project", "GA Graph");
		
		chart.pack();
	    RefineryUtilities.centerFrameOnScreen(chart);
	    chart.setVisible(true);
	    
	    System.out.println("All done...");
	}
}
