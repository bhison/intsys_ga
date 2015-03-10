package Build1;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class DatLoader {

	public DatLoader() {
	}

	//Reading text file http://stackoverflow.com/questions/2788080/reading-a-text-file-in-java
	//Space regex http://stackoverflow.com/questions/4731055/whitespace-matching-regex-java
		
	public List<String[]> loadDat() throws IOException{
		List<String> datFile;
		
		try {
			datFile = Files.readAllLines(Paths.get("datfile.dat"), StandardCharsets.US_ASCII);
			System.out.println("loadDat: Data loaded internally");
		}
		catch (IOException e) {
			System.out.println("IOException: " + e.toString());
			return null;
		}
		
		List<String[]> returnData = new ArrayList<String[]>();
		for (String line : datFile) {
		    String[] array = line.split("\\s+");
			returnData.add(array);
		}
		
		return returnData;
	}
}
