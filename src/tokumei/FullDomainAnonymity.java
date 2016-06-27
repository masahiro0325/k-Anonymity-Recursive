package tokumei;

import java.text.NumberFormat;


public class FullDomainAnonymity {

	private static int counter=0;

	private static int lineCounter=0;

	private static long timeCounter=0;

	public static void anonymiyDataSet(AnonymityDataSet dataSet,int[] anonymityPattern){
		if(anonymityPattern==null){
			throw new IllegalArgumentException("Pattern is null.(at FullDomainAnonymit.anonymityDataSet)");
		}
		long tmpTimeCounter = System.currentTimeMillis();
		int dataSize = dataSet.getAnonymityDataSet().length;
		for(int i=0;i<dataSize;i++){
			dataSet.getAnonymityDataSet()[i].getAnonymityAttribute().setAnonymityPattern(anonymityPattern);
		}
		dataSet.resetAllFlags();
		tmpTimeCounter = System.currentTimeMillis() - tmpTimeCounter;
		counter++;
		lineCounter = lineCounter + dataSize;
		timeCounter = timeCounter + tmpTimeCounter;
	}

	public static int getCounter() {
		return counter;
	}

	public static void setCounterZero() {
		FullDomainAnonymity.counter = 0;
	}

	public static int getLineCounter() {
		return lineCounter;
	}

	public static void setLineCounterZero() {
		FullDomainAnonymity.lineCounter = 0;
	}

	public static long getTimeCounter() {
		return timeCounter;
	}

	public static void setTimeCounterZero() {
		FullDomainAnonymity.timeCounter = 0;
	}

	public static void reSetAllCounter(){
		counter=0;
		lineCounter=0;
		timeCounter=0;
	}

	public static String getCountStatusFomat(){
		NumberFormat nf = NumberFormat.getNumberInstance();
		return " run num: "+nf.format(counter)+" / Sum(run line): "+nf.format(lineCounter)
				+" / Time: "+timeCounter+"ms";
	}

	public static String getCountStatus(){
		return timeCounter+","+counter+","+lineCounter;
	}
}
