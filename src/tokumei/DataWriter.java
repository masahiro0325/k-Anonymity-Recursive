package tokumei;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;



public class DataWriter {

	public static void writeDataToFile(String index,AnonymityDataSet ads,WriteFileMode fileMode,WriteDataMode dataMode){

		if(index == null){
			throw new IllegalArgumentException("index is null.(at DataWriter.writeDataToFile)");
		}
		if(fileMode == WriteFileMode.ONE_FILE){

			writeDataToOneFile(index,ads,dataMode);
		}else if(fileMode == WriteFileMode.SPLIT_FILE){

			writeDataToSplitFile(index,ads,dataMode);
		}
	}

	private static void writeDataToSplitFile(String index,AnonymityDataSet ads,WriteDataMode dataMode){

		HashMap<AnonymityAttribute,AnonymityDataSet> spd = ads.getSplitDataMap();
		int fileCounter = 0;
		for(AnonymityAttribute key:spd.keySet()){

			writeDataToOneFile(index+"."+fileCounter,spd.get(key),dataMode);
			fileCounter++;
		}
	}

	private static void writeDataToOneFile(String index,AnonymityDataSet ads,WriteDataMode dataMode){
		try{

			File file = new File(index+".txt");
			PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));

			AnonymityRecord[] ar = ads.getAnonymityDataSet();
			for(int i=0;i<ar.length;i++){
				AnonymityAttribute aa = ar[i].getAnonymityAttribute();
				for(int j=0;j<aa.getAnonymityPattern().length;j++){
					if(dataMode == WriteDataMode.NORMAL){

						pw.print(aa.getDefData()[j]+",");
					}else if(dataMode == WriteDataMode.ANONYMITY){

						pw.print(aa.getAnonymityData()[j]+",");
					}else if(dataMode == WriteDataMode.ANONYMITY_AND_NORMAL){

						pw.print(aa.getAnonymityData()[j]+"("+aa.getDefData()[j]+"),");
					}
				}
				pw.println(ar[i].getIdentify());
			}
			pw.close();
		}catch(IOException e){
			System.out.println(e);
		}
	}

}
