package Build1;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DatLoader {

	public DatLoader() {
	}

	//Reading text file http://stackoverflow.com/questions/2788080/reading-a-text-file-in-java
	//Space regex http://stackoverflow.com/questions/4731055/whitespace-matching-regex-java
		
	public List<String[]> loadDat() throws IOException{
		List<String> datFile;
		
		try {
			System.out.println("THATCHCOCK");
			datFile = Files.readAllLines(Paths.get("datfile.dat"), StandardCharsets.US_ASCII);
			System.out.println("SPATCHCOCK");
		}
		catch (IOException e) {
			System.out.println("IOException: " + e.toString());
			return null;
		}
		
		List<String[]> returnData = new ArrayList<String[]>();
		for (String line : datFile) {
		    String[] array = new String[]{line};
			returnData.add(array);
		}
		
		return returnData;
	}
	
    public void loadDat2() throws IOException
    {
//        Path path = FileSystems.getDefault().getPath("datfile.dat");
//        Object[] myLines = Files.readAllLines(path, StandardCharsets.US_ASCII).toArray();
//        System.out.println(Arrays.toString(myLines));
    }
}
