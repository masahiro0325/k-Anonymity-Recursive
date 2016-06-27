package tokumei;


public class AnonymityRecord{

	private String identify="";

	AnonymityAttribute anonymityAttribute = null;

	public AnonymityRecord(String[] defData,String identify){
		int dataSize = defData.length;
		int[] zeroPattern = new int[dataSize];
		for(int i=0;i<dataSize;i++){
			zeroPattern[i] = 0;
		}
		anonymityAttribute = new AnonymityAttribute(defData,zeroPattern);
		setIdentify(identify);
	}


	public AnonymityRecord(String[] defData,int[] anonymityPattern,String identify){
		anonymityAttribute = new AnonymityAttribute(defData,anonymityPattern);
		setIdentify(identify);
	}

	public String getIdentify() {
		return identify;
	}

	public void setIdentify(String identify) {
		if(identify==null){
			throw new IllegalArgumentException("identify is null.(at AnonymityRecord.setIdentify)");
		}
		this.identify = identify;
	}

	public AnonymityAttribute getAnonymityAttribute() {
		return anonymityAttribute;
	}

	@Override
	public String toString(){
		return new StringBuilder(anonymityAttribute.toString()).append(getIdentify()).toString();
	}
}
