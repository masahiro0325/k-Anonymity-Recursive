package tokumei;

import java.util.HashMap;


public class AnonymityDataSet implements Cloneable {

	private AnonymityRecord[] anonymityDataSet = null;

	private HashMap<AnonymityAttribute,AnonymityDataSet> splitDataMap = new HashMap<AnonymityAttribute,AnonymityDataSet>();

	private double usability=0.0;

	private int anonymity = 0;

	private boolean haveSplitDataMap = false;

	private boolean haveUsability = false;

	private boolean haveAnonymity = false;

	private boolean isFullDomainAnonymity = true;

	public AnonymityDataSet(){

		haveSplitDataMap = false;
		haveUsability = false;
		haveAnonymity = false;
	}

	public AnonymityRecord[] getAnonymityDataSet() {
		return anonymityDataSet;
	}

	public void addRecord(AnonymityRecord anonymityRecord){
		haveSplitDataMap = false;
		haveUsability = false;
		haveAnonymity = false;
		int dataSize=0;
		if(anonymityDataSet==null){
			this.anonymityDataSet =  new AnonymityRecord[1];
			this.anonymityDataSet[0] = anonymityRecord;
		}else{
			dataSize=this.anonymityDataSet.length;
			AnonymityRecord[] newAnonymityDataSet = new AnonymityRecord[dataSize+1];
			System.arraycopy(this.anonymityDataSet,0,newAnonymityDataSet,0,dataSize);
			newAnonymityDataSet[dataSize] = anonymityRecord;
			this.anonymityDataSet = newAnonymityDataSet;
		}
	}

	public void removeRecord(AnonymityRecord anonymityRecord){
		int removeNum=0;
		for(int i=0;i<anonymityDataSet.length;i++){
			if(anonymityDataSet[i].equals(anonymityRecord)){
				removeNum=i;
				break;
			}
		}
		AnonymityRecord[] newDataSet = new AnonymityRecord[anonymityDataSet.length-1];
		System.arraycopy(anonymityDataSet,0,newDataSet,0,removeNum);
		System.arraycopy(anonymityDataSet,removeNum+1,newDataSet,removeNum,anonymityDataSet.length-1-removeNum);
		this.anonymityDataSet=newDataSet;
	}

	public void addAllRecord(AnonymityRecord[] anonymityRecordSet){
		haveSplitDataMap = false;
		haveUsability = false;
		haveAnonymity = false;
		int dataSize=0;
		if(anonymityRecordSet==null){
			return ;
		}
		int addDataSize = anonymityRecordSet.length;
		if(anonymityDataSet==null){
			this.anonymityDataSet =  new AnonymityRecord[addDataSize];
			System.arraycopy(anonymityRecordSet,0,this.anonymityDataSet,0,addDataSize);
		}else{
			dataSize=this.anonymityDataSet.length;
			AnonymityRecord[] newAnonymityDataSet = new AnonymityRecord[dataSize+addDataSize];
			System.arraycopy(this.anonymityDataSet,0,newAnonymityDataSet,0,dataSize);
			System.arraycopy(anonymityRecordSet,0,newAnonymityDataSet,dataSize,addDataSize);
			this.anonymityDataSet = newAnonymityDataSet;
		}
	}

	public void clearThisData(){
		anonymityDataSet = null;
		resetAllFlags();
	}

	public void setAnonymityDataSet(AnonymityRecord[] anonymityDataSet) {
		haveSplitDataMap = false;
		haveUsability = false;
		haveAnonymity = false;
		this.anonymityDataSet = anonymityDataSet;
	}

	public double getUsability(){
		if(!haveUsability){
			setUsability();
		}
		return usability;
	}

	public void setUsability(){
		haveUsability = true;
		double resultUsability = 0.0;
		int dataSize = anonymityDataSet.length;
		for(int i=0;i<dataSize;i++){
			resultUsability += anonymityDataSet[i].getAnonymityAttribute().getUsability();
		}
		this.usability = resultUsability / dataSize;
	}

	public int getAnonymity() {
		if(!haveAnonymity){
			setAnonymity();
		}
		return anonymity;
	}

	public void setAnonymity() {
		haveAnonymity = true;

		if(!haveSplitDataMap){
			this.setSplitDataMap();
		}
		int resultAnonymity = 999999;

		for(AnonymityAttribute key : splitDataMap.keySet()){
			int dataSize = splitDataMap.get(key).getAnonymityDataSet().length;
			if(dataSize==1){
				resultAnonymity = 1;
				break;
			}else if(resultAnonymity > dataSize){
				resultAnonymity = dataSize;
			}
		}
		this.anonymity = resultAnonymity;
	}

	public HashMap<AnonymityAttribute,AnonymityDataSet> getSplitDataMap() {
		if(!haveSplitDataMap){
			this.setSplitDataMap();
		}
		return splitDataMap;
	}

	public void setSplitDataMap() {
		haveSplitDataMap = true;
		splitDataMap = new HashMap<AnonymityAttribute,AnonymityDataSet>();
		int dataSize = this.anonymityDataSet.length;
		for(int i=0;i<dataSize;i++){
			AnonymityDataSet findAnonymityDataSet = splitDataMap.get((AnonymityAttribute)anonymityDataSet[i].getAnonymityAttribute());
			if(findAnonymityDataSet==null){
				findAnonymityDataSet = new AnonymityDataSet();
			}
			findAnonymityDataSet.addRecord(anonymityDataSet[i]);
			splitDataMap.put((AnonymityAttribute)anonymityDataSet[i].getAnonymityAttribute().clone(),findAnonymityDataSet);
		}
	}

	public boolean haveSplitDataMap() {
		return haveSplitDataMap;
	}

	public void combinationSplitDataMap(){
		if(!haveSplitDataMap){
			throw new IllegalArgumentException("not have split data.(at AnonymityDataSet.comninationSplitDataMap)");
		}
		isFullDomainAnonymity=false;
		haveUsability = false;
		haveAnonymity = false;
		anonymityDataSet = null;
		for(AnonymityAttribute key : splitDataMap.keySet() ){
			this.addAllRecord(splitDataMap.get(key).getAnonymityDataSet());
		}

	}

	public boolean isFullDomainAnonymity() {
		return isFullDomainAnonymity;
	}

	public void resetAllFlags(){
		splitDataMap = new HashMap<AnonymityAttribute,AnonymityDataSet>();
		usability=0.0;
		anonymity = 0;
		haveSplitDataMap = false;
		haveUsability = false;
		haveAnonymity = false;
		isFullDomainAnonymity=true;
	}

	@Override
	public String toString(){
		StringBuilder resultString = new StringBuilder("\n");
		int dataSize =  this.anonymityDataSet.length;
		for(int i = 0;i < dataSize;i++){
			resultString.append(this.anonymityDataSet[i].toString());
			resultString.append("\n");
		}
		resultString.append("\nUsability: ");
		resultString.append(this.getUsability());
		resultString.append("\n");
		return resultString.toString();
	}

	@Override
	public Object clone() {
		AnonymityDataSet newADS = new AnonymityDataSet();
		newADS.addAllRecord(this.anonymityDataSet);
		return newADS;
	}
}
