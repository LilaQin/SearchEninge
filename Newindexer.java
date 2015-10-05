package newIndex;

import newIndex.Frequency;
import newIndex.Utilities;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;

public class Newindexer {
	// in this format: Map<term,Map<filename, TermDocIndex>>
	private static Map<String, HashMap<String, TermDocIndex>> TermsMap = new HashMap<String, HashMap<String, TermDocIndex>>();
	//private static Map<String, TermDocIndex> DocsMap = new HashMap<String, TermDocIndex>();
	private static int totalDocsNum=0;
	
	public static void main(String[] args) throws FileNotFoundException {
		File targetDirec = new File(args[0]);
		//Build Index
		traversal(targetDirec);
		getTFIDF();
		sortMap();
		
		ArrayList<Integer> currentIndex = new ArrayList<Integer>();
		Integer termFrequency;
		Float tfidf;
		Iterator<Entry<String, HashMap<String, TermDocIndex>>> iter = TermsMap.entrySet().iterator();
		while (iter.hasNext()) {
	        Map.Entry<String, HashMap<String, TermDocIndex>> pairs = (Map.Entry<String, HashMap<String, TermDocIndex>>)iter.next();
	        
	        System.out.println(pairs.getKey());
	        Iterator<Entry<String, TermDocIndex>> iter2 = pairs.getValue().entrySet().iterator();
	        while (iter2.hasNext()) {
	        	Map.Entry<String, TermDocIndex> pairs2 = (Map.Entry<String,  TermDocIndex>)iter2.next();
	        	System.out.print(pairs2.getKey() + "|" );
	        	termFrequency=pairs2.getValue().getTermFrequency();
	        	System.out.print(termFrequency+ "  ");
	        	tfidf=pairs2.getValue().get_tfidf();
	        	System.out.print(tfidf+ "  ");
	        	currentIndex = pairs2.getValue().getPositions();
	        	for (int pos : currentIndex){
	        		System.out.print(pos+ " ");
	        	}
	        System.out.println(" ");
	        }
	        
	        
		}
		

	//	int i=0;
		System.out.println("-------------------------------");
		System.out.println("-------------------------------");
		System.out.println(TermsMap.size());
		/*
		for(Frequency term: frequencies)
		{
			if (i>=500)
				break;
			System.out.println(term.toString());
			i++;
		}*/
		//getTFIDF();
	}
	
	private static void traversal(File targetDirec) throws FileNotFoundException{
		if (!targetDirec.isDirectory()) {
			System.out.println("ERROR: Target is not a Directory");
			return;
		}
		//.....debug Use.................
		//String folderName = targetDirec.getName();
		//System.out.println("In folder: "+ folderName);
		//...............................
		File[] files = targetDirec.listFiles(); 
		File stopFile = new File("stopWords.txt");
		List<String> stopWords = Utilities.tokenizeFile(stopFile);
		HashMap<String, TermDocIndex> DocsMap = new HashMap<String, TermDocIndex>();
	//	HashMap<String, TermDocIndex> newDocsMap = new HashMap<String, TermDocIndex>();
		String fileName;
		String CurrentTerm;
		Integer position;
		TermDocIndex tempIndex = new TermDocIndex();
		
		
		for (File f : files) {
			
			if (f.isDirectory()) {
				traversal(f); 
		
			} else { 
				if(!f.isHidden())
				{
				totalDocsNum++;
				List<String> inputWords = Utilities.tokenizeFile(f);
				//System.out.println(inputWords);
				//delete stop words
				inputWords.removeAll(stopWords);
				//System.out.println(inputWords);
				
				for (ListIterator<String> iter = inputWords.listIterator(); iter.hasNext(); )
				{
					//if (CurrentTerm.matches("[0-9]+")){
					//	continue;
					//}
					position = iter.nextIndex();// position
					CurrentTerm = iter.next();
					//tempIndex.clear();
					fileName = f.getName();
					if(TermsMap.containsKey(CurrentTerm))
					{
						DocsMap = TermsMap.get(CurrentTerm);
						if(DocsMap.containsKey(fileName))
						{
							tempIndex = DocsMap.get(fileName);

						}
						else
						{
							tempIndex = new TermDocIndex();
						}

						tempIndex.termFreqIncrease();
						tempIndex.pushPosition(position);
						DocsMap.put(fileName, tempIndex);
						TermsMap.put(CurrentTerm, DocsMap);
					
					}
					else
					{
						tempIndex = new TermDocIndex();
						HashMap<String, TermDocIndex> newDocsMap = new HashMap<String, TermDocIndex>();
						tempIndex.termFreqIncrease();
						tempIndex.pushPosition(position);
						newDocsMap.put(fileName, tempIndex);
						TermsMap.put(CurrentTerm, newDocsMap);
					//	newDocsMap.clear();
					}
					
					
				}
				}
			}

		}
	}
	
	public static void getTFIDF(){
        Iterator<Entry<String, HashMap<String, TermDocIndex>>> iter = TermsMap.entrySet().iterator();
        while(iter.hasNext()){
        	HashMap<String,TermDocIndex> tempDocsMap=new HashMap<String,TermDocIndex>();
            int docsNum;
        //	Map.Entry entry = (Map.Entry)iter.next();
            Map.Entry<String, HashMap<String, TermDocIndex>> entry = (Map.Entry<String, HashMap<String, TermDocIndex>>)iter.next();
            String term = entry.getKey().toString();
          tempDocsMap = TermsMap.get(term);
         //   tempDocsMap =(HashMap<String, TermDocIndex>) entry.getValue();
            docsNum=tempDocsMap.size();
            Iterator<Entry<String, TermDocIndex>> inerIter = tempDocsMap.entrySet().iterator();
            while(inerIter.hasNext()){
       //    Map.Entry inerentry = (Map.Entry)iter.next();
          	Map.Entry<String, TermDocIndex> inerEntry = (Map.Entry<String, TermDocIndex>)inerIter.next();

            String Docs = inerEntry.getKey().toString();
            Float tfidf;
            Integer tf;
            TermDocIndex tempIndex=new TermDocIndex();
 
            tempIndex = tempDocsMap.get(Docs);
            tf=tempIndex.getTermFrequency();
            tfidf=(float)((float)(1+Math.log(tf))*Math.log((float)totalDocsNum/docsNum));
            tempIndex.add_tfidf(tfidf);
            }     
        }

		
	}
	public static  void sortMap(){
		Map<String, HashMap<String, TermDocIndex>> finalTermsMap = new LinkedHashMap<String, HashMap<String, TermDocIndex>>();
		Object[] termList=  TermsMap.keySet().toArray();
		Arrays.sort(termList);
		for(Object key : termList) {
			//String term=(String)key;
			String term=key.toString();
			HashMap<String, TermDocIndex> temp=TermsMap.get(term);
			finalTermsMap.put(term,temp);			
		}
		TermsMap=finalTermsMap;
	}

}
