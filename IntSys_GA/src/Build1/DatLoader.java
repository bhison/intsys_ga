package Build1;

import java.io.*;
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
			datFile = Files.readAllLines(Paths.get("../../SourceData/datfile.dat"), null);
		}
		catch (IOException e) {
			//IOException
			return null;
		}
		
		List<String[]> returnData = new ArrayList<String[]>();
		for (String line : datFile) {
		    String[] array = new String[]{line};
			returnData.add(array);
		}
		
		return returnData;
	}
}
