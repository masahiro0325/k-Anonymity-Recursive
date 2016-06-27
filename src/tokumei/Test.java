package tokumei;

import java.io.IOException;

public class Test {


	public static void main(String[] args)throws IOException {

		long timeRead = System.currentTimeMillis();
		AnonymityDataSet ads = DataReader.readData("./data/rand_100.txt");
		System.out.println("Recursive\nread file time : "+ (System.currentTimeMillis() - timeRead) +"ms");

		long timeAno = System.currentTimeMillis();
		RecursiveKAnonymity.makeRecursiveKAnonymity(13,ads);

		timeAno=(System.currentTimeMillis() - timeAno);

		DataWriter.writeDataToFile("./src/tokumei/result", ads, WriteFileMode.ONE_FILE, WriteDataMode.ANONYMITY);
		System.out.print ("Usability:"+ads.getUsability());
		System.out.print(" rAnonymity time : \n"+ timeAno +",");


		System.out.print (FullDomainAnonymity.getCountStatus());
		System.out.print (FullDomainAnonymity.getCountStatusFomat());
	}

}
