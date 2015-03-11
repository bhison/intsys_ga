package build1;
//This may end up being useful: new ArrayList<Element>(Arrays.asList(array))
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class DatLoader {
	
	private Double[][] dataList;
	private Double[] xValues;
	private Double[] yValues;
	private XYSeries xySeries;
	private XYSeriesCollection xySeriesCollection;
	private int dataSize;

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
		
		dataSize = datFile.size();
		
		dataList = new Double[dataSize][2];
		xValues = new Double[dataSize];
		yValues = new Double[dataSize];
		int i = 0;
		for (String line : datFile) {
		    String[] array = line.split("\\s+");
		    xValues[i] = Double.parseDouble(array[0]);
		    yValues[i] = Double.parseDouble(array[1]);
		    dataList[i] = new Double[]{xValues[i], yValues[i]};
		    i++;
		}
		
		xySeries = createSeries(dataList);
		xySeriesCollection = new XYSeriesCollection(xySeries);
	}
	
	private XYSeries createSeries(Double[][] data){
		XYSeries returnSeries = new XYSeries("TargetCurve");
		for(Double[] entry : data){
			returnSeries.add(entry[0], entry[1]);
		}
		return returnSeries;
	}
	
	public Double[][] getDataList(){
		return dataList;
	}
	
	public XYSeries getXYSeries(){
		return xySeries;
	}
	
	public XYSeriesCollection getXYSeriesCollection(){
		return xySeriesCollection;
	}
	
	public Double[] getXValues(){
		return xValues;
	}
	
	public Double[] getYValues(){
		return yValues;
	}
	
	public int getDataSize(){
		return dataSize;
	}
	
}
