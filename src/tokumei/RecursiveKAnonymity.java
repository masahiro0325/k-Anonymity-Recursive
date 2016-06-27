package tokumei;

import java.util.HashMap;
import java.util.Set;


public class RecursiveKAnonymity {

	static int count =0;

	public static boolean haveOutPrint = true;

	public static void makeRecursiveKAnonymity(int k,AnonymityDataSet dataSet){

		long timeFDKA = System.currentTimeMillis();
		if(count==0){
			FDKAnonymityRecursive.makeKAnonymity(k,dataSet);
		}else{
			FDKAnonymityRecursive.makeKAnonymity(k,dataSet.getAnonymityDataSet()[0].getAnonymityAttribute().getAnonymityPattern(),dataSet);
		}
		timeFDKA = System.currentTimeMillis() - timeFDKA;
		if(haveOutPrint)System.out.println("timeFDKA: "+timeFDKA);

		HashMap<AnonymityAttribute,AnonymityDataSet> splitDataSet = dataSet.getSplitDataMap();

		Set<AnonymityAttribute> dataKeySet = splitDataSet.keySet();
		if(dataKeySet.size()==1){
			return ;
		}

		for(AnonymityAttribute key : dataKeySet ){
			count++;
			if(haveOutPrint)for(int i=0;i<count;i++){System.out.print("-");}
			if(haveOutPrint)System.out.println("l:"+splitDataSet.get(key).getAnonymityDataSet().length);
			makeRecursiveKAnonymity(k,splitDataSet.get(key));
			count--;
		}

		dataSet.combinationSplitDataMap();
	}
}
