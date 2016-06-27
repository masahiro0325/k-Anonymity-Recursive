package tokumei;


public class Attribute {

	private String[] defData = null;

	private int[] defPattern = null;

	public Attribute(String[] defData){

		if(defData==null){
			throw new IllegalArgumentException("defult data is null.(at Attribute(LinkedList<String))");
		}

		this.setDefData(defData);
	}

	public String[] getDefData() {
		return defData;
	}

	public void setDefData(String[] defData) {
		this.defData = defData;
		setDefPattern();
	}

	public int[] getDefPattern() {
		return defPattern;
	}

	private void setDefPattern() {
		int dataSize = defData.length;
		defPattern = new int[dataSize];
		for (int i=0;i<dataSize;i++){
			defPattern[i] =defData[i].length();
		}
	}

	@Override
	public String toString(){
		StringBuilder str=new StringBuilder();
		int dataSize = defData.length;
		for(int i=0;i<dataSize;i++){
			str.append(defData[i]);
			str.append(" ");
		}
		return str.toString();
	}

	@Override
	public int hashCode() {
		return toString().hashCode();
	}

	@Override
	public boolean equals(Object obj){
		if (obj == null){
			return false;
		}
		if (!(obj instanceof Attribute)){
			return false;
		}
		String[] objData = ((Attribute)obj).getDefData();
		int dataSize = defData.length;
		if(objData.length!=dataSize){
			return false;
		}
		for(int i=0;i<dataSize;i++){
			if(!defData.equals(objData[i])){
				return false;
			}
		}
		return true;
	}
}
