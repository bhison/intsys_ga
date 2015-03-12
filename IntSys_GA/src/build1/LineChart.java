package build1;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;

//To use: chart.getXYPlot().setRenderer(new XYSplineRenderer());
//Double to binary: http://stackoverflow.com/questions/6359847/convert-double-to-binary-representation

public class LineChart extends ApplicationFrame {

	JFreeChart lineChart;
	XYPlot plot;
	XYSeries guess;
	XYSeriesCollection dataset;
	String chartTitle;
	ChartPanel chartPanel;
	
	public LineChart(String applicationTitle, String chartTitle, XYSeriesCollection dataset) {
		super(applicationTitle);
		this.dataset = dataset;
		guess = new XYSeries("Guess");
		dataset.addSeries(guess);
		buildChart(chartTitle);
	}
	
	private void buildChart(String chartTitle)
	{
		lineChart = ChartFactory.createXYLineChart(
				chartTitle,
				"x",
				"f(x)",
				dataset,
				PlotOrientation.VERTICAL,
				true,
				true,
				false
				);
		plot = lineChart.getXYPlot();
		plot.setRenderer(new XYSplineRenderer());
		NumberAxis xAxis = (NumberAxis) plot.getDomainAxis();
		ValueAxis yAxis = plot.getRangeAxis();
		xAxis.setRange(-30.00, 60.00);;
		xAxis.setTickUnit(new NumberTickUnit(10));
		yAxis.setRange(-2e+06, 1.5e+06);

		chartPanel = new ChartPanel(lineChart);
		chartPanel.setPreferredSize(new java.awt.Dimension(800, 600));
		setContentPane(chartPanel);
	}
	
	public void updatePlot(XYSeries series){
		//DataItem item = new DataItem();
		//series,
	}
}
