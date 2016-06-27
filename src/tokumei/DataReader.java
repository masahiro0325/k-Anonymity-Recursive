package tokumei;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public class DataReader {

	public static AnonymityDataSet readData(String index) throws FileNotFoundException,IOException{

		ArrayList<String[]> dataArray = readArray(index);

		AnonymityDataSet ads = new AnonymityDataSet();
		int dataSize = dataArray.get(0).length;
		int arraySize = dataArray.size();

		AnonymityRecord[] arSet = new AnonymityRecord[arraySize];
		for(int i=0;i<arraySize;i++){
			String[] attribute = new String[dataSize-1];
			int[] pattern = new int[dataSize-1];
			String[] attributeArray = dataArray.get(i);
			for(int j = 0;j<dataSize-1;j++){
				pattern[j] = 0;
			}

			System.arraycopy(attributeArray, 0, attribute, 0, dataSize-1);

			arSet[i]=new AnonymityRecord(attribute,pattern,attributeArray[dataSize-1]);
		}

		ads.addAllRecord(arSet);
		return ads;
	}

	private static ArrayList<String[]> readArray(String index) throws FileNotFoundException,IOException{
		ArrayList<String[]> resultData=new ArrayList<String[]>();
		File file = new File(index);
		BufferedReader br = new BufferedReader(new FileReader(file));
		String str;
		while((str = br.readLine()) != null){
			String[] strlist =str.split(",");

			for(int i=0; i<strlist.length;i++){
				int x = strlist[i].length();
				for(int j=0; j<4-x;j++){
					strlist[i] = "0"+strlist[i];
				}
			}

			resultData.add(strlist);
		}
		br.close();
		return resultData;
	}
}
