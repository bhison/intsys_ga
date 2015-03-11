package build1;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

public class LineChart extends ApplicationFrame {

	JFreeChart lineChart;
	
	public LineChart(String applicationTitle, String chartTitle, XYSeriesCollection dataset) {
		super(applicationTitle);
		buildChart(chartTitle, dataset);
	}
	
	private void buildChart(String chartTitle, XYSeriesCollection dataset)
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
		lineChart.getXYPlot().setRenderer(new XYSplineRenderer());

		ChartPanel chartPanel = new ChartPanel(lineChart);
		chartPanel.setPreferredSize(new java.awt.Dimension(800, 600));
		setContentPane(chartPanel);	
	}
}
