package tokumei;


import java.util.ArrayList;


public class FDKAnonymityRecursive {

	private static ArrayList<int[]> andOverKPatternSet=null;

	private static ArrayList<int[]> equalAndOverKPatternSet=null;

	private static ArrayList<int[]> lessThanKPatternSet=null;

	public static void makeKAnonymity(int k,AnonymityDataSet dataSet){
		makeKAnonymity(k,dataSet.getAnonymityDataSet()[0].getAnonymityAttribute().getDefPattern(),dataSet);
	}

	public static void makeKAnonymity(int k,int[] endPattern,AnonymityDataSet dataSet){
		andOverKPatternSet=new ArrayList<int[]>();
		equalAndOverKPatternSet=new ArrayList<int[]>();
		lessThanKPatternSet=new ArrayList<int[]>();
		if(endPattern==null){
			throw new IllegalArgumentException("Pattern is null.(at FDKAnonymitRecursive.makeAnonymity(int,LinkedList,AnonymityDataSet))");
		}
		int patternSize = dataSet.getAnonymityDataSet()[0].getAnonymityAttribute().getAnonymityPattern().length;
		int[] startPattern = new int[patternSize];
		for(int i=0;i<patternSize;i++){
			startPattern[i]=0;
		}
		recursiveMakeKAnonymity(k,startPattern,endPattern,dataSet);
		getBestDataSet(dataSet,andOverKPatternSet);
	}

	public static void recursiveMakeKAnonymity(int k,int[] startPattern,int[] endPattern,AnonymityDataSet dataSet){

		int patternSize = startPattern.length;
		int[] centerPattern = new int[patternSize];
		for(int i=0;i<patternSize;i++){

			centerPattern[i]=(int)(endPattern[i] + startPattern[i] + 1)/2;
		}

		int andOverSize = andOverKPatternSet.size();
		for(int i=0;i<andOverSize;i++){
			PatternRelation start2over = comparePattern(startPattern,andOverKPatternSet.get(i));
			if(start2over==PatternRelation.DOWNER_CASE||start2over==PatternRelation.EQUAL_CASE){
				return ;
			}
		}

		int lessThanSize = lessThanKPatternSet.size();
		for(int i=0;i<lessThanSize;i++){
			PatternRelation end2less = comparePattern(endPattern,lessThanKPatternSet.get(i));
			if(end2less==PatternRelation.UPPER_CASE||end2less==PatternRelation.EQUAL_CASE){
				return ;
			}
		}

		boolean isAnonymited = false;
		boolean centerIsKAnonymity = false;
		if(!isAnonymited){
			for(int i=0;i<andOverSize;i++){
				PatternRelation center2over = comparePattern(centerPattern,andOverKPatternSet.get(i));
				if(center2over==PatternRelation.DOWNER_CASE||center2over==PatternRelation.EQUAL_CASE){
					isAnonymited = true;
					centerIsKAnonymity = true;
					break;
				}
			}
		}
		if(!isAnonymited){
			for(int i=0;i<lessThanSize;i++){
				PatternRelation center2less = comparePattern(centerPattern,lessThanKPatternSet.get(i));
				if(center2less==PatternRelation.UPPER_CASE||center2less==PatternRelation.EQUAL_CASE){
					isAnonymited = true;
					centerIsKAnonymity = false;
					break;
				}
			}
		}

		if(!isAnonymited){

			FullDomainAnonymity.anonymiyDataSet(dataSet, centerPattern);

			int dataK = dataSet.getAnonymity();
			if(dataK>=k){
				centerIsKAnonymity = true;
			}else if(dataK<k){
				centerIsKAnonymity = false;
			}else{
				throw new IllegalArgumentException("What's error??(at KAnonymityRecursive.recursiveMakeKAnonymity)");
			}
		}

		if(centerIsKAnonymity){

			if(!isAnonymited){
				andOverKPatternSet.add(centerPattern);
			}

			for(int i = 0; i < Math.pow(2, patternSize)-1;i++){
				int[] nextStartPattern = new int[patternSize];
				int[] nextEndPattern = new int[patternSize];
				for(int j=0;j<patternSize;j++){
					int caseNum = ((i/(int)Math.pow(2,j))%2);
					if(caseNum==0){
						nextStartPattern[j]=startPattern[j];
						nextEndPattern[j]=centerPattern[j]-1;
					}else if(caseNum==1){
						nextStartPattern[j]=centerPattern[j];
						nextEndPattern[j]=endPattern[j];
					}
				}
				recursiveMakeKAnonymity(k,nextStartPattern,nextEndPattern,dataSet);
			}
		}else{

			if(!isAnonymited){
				lessThanKPatternSet.add(centerPattern);
			}

			for(int i = (int)Math.pow(2, patternSize)-1; i > 0;i--){
				int[] nextStartPattern = new int[patternSize];
				int[] nextEndPattern = new int[patternSize];
				for(int j=0;j<patternSize;j++){
					int caseNum = ((i/(int)Math.pow(2,j))%2);
					if(caseNum==0){
						nextStartPattern[j]=startPattern[j];
						nextEndPattern[j]=centerPattern[j]-1;
					}else if(caseNum==1){
						nextStartPattern[j]=centerPattern[j];
						nextEndPattern[j]=endPattern[j];
					}
				}
				recursiveMakeKAnonymity(k,nextStartPattern,nextEndPattern,dataSet);
			}
		}
	}

	public static PatternRelation comparePattern(int[] p1,int[] p2){
		int patternSize = p1.length;
		if(patternSize!=p2.length){
			throw new IllegalArgumentException("pattern length is not equal.(at KAnonymityRecursive.comparePattern)");
		}
		int plusCount=0;
		int minusCount=0;
		int equalCount=0;
		for(int i=0;i<patternSize;i++){
			if(p1[i]==p2[i]){
				equalCount++;
				plusCount++;
				minusCount++;
			}else if(p1[i]<p2[i]){
				plusCount++;
			}else if(p1[i]>p2[i]){
				minusCount++;
			}
		}
		if(equalCount==patternSize){
			return PatternRelation.EQUAL_CASE;
		}else if(plusCount==patternSize){
			return PatternRelation.UPPER_CASE;
		}else if(minusCount==patternSize){
			return PatternRelation.DOWNER_CASE;
		}else{
			return PatternRelation.OTHER_CASE;
		}
	}

	private static void getBestDataSet(AnonymityDataSet dataSet,ArrayList<int[]> andOverP){
		if(andOverP.size()==0){
			System.out.println("Cannot find K Anonymity Pattern.(at KAnonymityRecursive.(private method)GBDS)");
			return;
		}
		double usability=0.0;
		int patternSize = andOverP.size();
		int[] defPattern = dataSet.getAnonymityDataSet()[0].getAnonymityAttribute().getDefPattern();
		int[] bestPattern = null;
		for(int i=0;i<patternSize;i++){
			double nowDataUsability = culUsability(andOverP.get(i),defPattern);
			if(usability < nowDataUsability){
				usability = nowDataUsability;
				bestPattern = andOverP.get(i);
				equalAndOverKPatternSet.clear();
			}
		}
		FullDomainAnonymity.anonymiyDataSet(dataSet,bestPattern);
	}


	private static double culUsability(int[] anonymityPattern,int[] defPattern){
		double usability=0.0;
		int patternSize =anonymityPattern.length;
		if(patternSize!=defPattern.length){
			throw new IllegalArgumentException("pattern length is not equal.(at KAnonymityRecursive.(private method)CU)");
		}
		for(int i=0;i<patternSize;i++){
			usability += 1-((double)anonymityPattern[i]/(double) defPattern[i]);
		}
		return usability / patternSize;
	}
}
