package build1;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class DatLoader {
	
	private List<String[]> dataList;
	private XYSeries xySeries;
	private XYSeriesCollection xySeriesCollection;
	
	public DatLoader() {
		List<String> datFile;
		try {
			datFile = Files.readAllLines(Paths.get("datfile.dat"), StandardCharsets.US_ASCII);
			System.out.println("loadDat: Data loaded internally");
		}
		catch (IOException e) {
			System.out.println("IOException: " + e.toString());
			return;
		}
		dataList = new ArrayList<String[]>();
		for (String line : datFile) {
		    String[] array = line.split("\\s+");
		    dataList.add(array);
		}
		xySeries = createSeries(dataList);
		xySeriesCollection = new XYSeriesCollection();
		xySeriesCollection.addSeries(xySeries);
	}
	
	private XYSeries createSeries(List<String[]> data){
		XYSeries returnSeries = new XYSeries("TargetCurve");
		for(String[] entry : data){
			returnSeries.add(Double.parseDouble(entry[0]), Double.parseDouble(entry[1]));
		}
		return returnSeries;
	}
	
	public XYSeries getXYSeries(){
		return xySeries;
	}
	
	public XYSeriesCollection getXYSeriesCollection(){
		return xySeriesCollection;
	}
	
	
}
