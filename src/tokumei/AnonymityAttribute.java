package tokumei;



public class AnonymityAttribute extends Attribute implements Comparable<Object>,Cloneable{

	private String[] anonymityData= null;

	private int[] anonymityPattern= null;

	private String compareAnonymityData = null;

	private double usability=0.0;

	private boolean haveUsability = false;

	public AnonymityAttribute(String[] defData,int[] anonymityPattern){

		super(defData);

		this.setAnonymityPattern(anonymityPattern);
	}

	public String[] getAnonymityData() {
		return anonymityData;
	}

	private void setAnonymityData(int[] anonymityPattern) {
		int dataSize = getDefData().length;
		this.anonymityData = new String[dataSize];

		StringBuilder sbCompareAnonymityData = new StringBuilder();
		for(int i=0;i<dataSize;i++){
			int astLen = anonymityPattern[i];
			if(astLen==0){
				this.anonymityData[i]=getDefData()[i];
			}else{
				StringBuilder sb = new StringBuilder(getDefData()[i]);
				StringBuilder astStr = new StringBuilder();

				for(int j = 0;j<astLen;j++){
					astStr.append("*");
				}
				int strLen = sb.length();

				sb.replace(strLen-astLen,strLen ,astStr.toString());
				this.anonymityData[i]=(sb.toString());
			}
			sbCompareAnonymityData.append(this.anonymityData[i]);
		}

		this.compareAnonymityData = sbCompareAnonymityData.toString();
	}

	public int[] getAnonymityPattern() {
		return anonymityPattern;
	}

	public void setAnonymityPattern(int[] anonymityPattern) {

		if(anonymityPattern==null){
			throw new IllegalArgumentException("anonymity pattern is null.(at AnonymityAttribute.setAnonymityPattern)");
		}
		if(!isTrueAnonymityPattern(anonymityPattern)){
			throw new IllegalArgumentException("anonymity pattern is not collect.(at AnonymityAttribute.setAnonymityPattern)");
		}
		this.anonymityPattern = anonymityPattern;

		setAnonymityData(anonymityPattern);

		setUsability();
	}

	public double getUsability() {
		if(!haveUsability){
			setUsability();
		}
		return usability;
	}

	private void setUsability() {
		haveUsability = true;
		double usabilityResult = 0.0;
		int dataSize=anonymityPattern.length;
		for (int i=0;i<dataSize;i++) {
			usabilityResult += 1-((double)anonymityPattern[i]/(double)getDefPattern()[i]);
		}
		usability = usabilityResult / dataSize;
	}

	public String getCompareAnonymityData() {
		return compareAnonymityData;
	}

	private boolean isTrueAnonymityPattern(int[] anonymityPattern){
		int dataSize=0;
		for(int i=0;i<dataSize;i++){
			if(anonymityPattern[i]>getDefPattern()[i]||anonymityPattern[i]<0){
				return false;
			}
		}
		return true;
	}

	@Override
	public String toString(){
		int dataSize = anonymityData.length;

		StringBuilder resultStr = new StringBuilder();
		resultStr.append("[ ");
		for (int i=0;i<dataSize;i++) {
			resultStr.append(anonymityData[i]);
			resultStr.append("(");
			resultStr.append(getDefData()[i]);
			resultStr.append(") ");
		}
		resultStr.append("] ");
		return resultStr.toString();
	}

	@Override
	public int hashCode() {
		return compareAnonymityData.hashCode();
	}

	@Override
	public boolean equals(Object obj){
		if (obj == null){
			return false;
		}
		if (!(obj instanceof AnonymityAttribute)){
			return false;
		}
		int dataSize = anonymityData.length;
		String[] objAnonymityData =((AnonymityAttribute)obj).getAnonymityData();
		if(objAnonymityData.length!=dataSize){
			return false;
		}
		for(int i=0;i<dataSize;i++){
			if(!anonymityData[i].equals(objAnonymityData[i])){
				return false;
			}
		}
		return true;
	}

	@Override
	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			throw new InternalError(e.toString()+"(at AnonymityAttribute.clone)");
		}
	}

	@Override
	public int compareTo(Object obj){
			return this.getCompareAnonymityData().compareTo(((AnonymityAttribute)obj).getCompareAnonymityData());
	}
}