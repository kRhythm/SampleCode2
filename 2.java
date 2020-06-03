import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Vector;
import com.github.javaparser.ParseException;
import com.github.javaparser.ParseProblemException;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import gumtree.spoon.AstComparator;
import gumtree.spoon.diff.Diff;

public class graph_diff {
	public static String main(String[] args) throws Exception {
		
		File OringinalFile = new File("C:\\Users\\rkanchug\\Desktop\\Original.java");
		File RevisedFile = new File("C:\\Users\\rkanchug\\Desktop\\Revised.java");
		final Diff result = new AstComparator().compare(OringinalFile, RevisedFile);
		File diff = new File("C:\\Users\\rkanchug\\Desktop\\Difference.txt");
		PrintStream he = new PrintStream(diff);
		PrintStream console = System.out;
		System.setOut(he);
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		try {
			System.out.println(result.toString());
		}
		catch (NullPointerException npe) {
		    // It's fine
		}
		File MethodLineInfo= new File("C:\\Users\\rkanchug\\Desktop\\Mline.txt");
    	PrintStream hi = new PrintStream(MethodLineInfo);
    	System.setOut(hi);
		getMethodLineNumbers(OringinalFile);
		Vector<Integer> StartLineNumbers = new Vector<Integer>();
		Vector<Integer> EndLineNumbers =new Vector<Integer>();
		Vector<String> MethodNames = new Vector<String>();
		BufferedReader Mr = new BufferedReader(new FileReader(MethodLineInfo));
		String mline = null;
		while(  (mline = Mr.readLine()) != null )
		{
			String[] WordsInLine = mline.split(" ",3);
			int StarT =  Integer.parseInt(WordsInLine[0]);
			int EnD = Integer.parseInt(WordsInLine[1]);
			String MNaMe = WordsInLine[2];
			StartLineNumbers.add(StarT);
			EndLineNumbers.add(EnD);
			MethodNames.add(MNaMe);
		}
		Mr.close();
		System.setOut(console);
		BufferedReader br = new BufferedReader(new FileReader(diff));  
		String Entities = null;
		String line = null;
		StringBuffer inputBuffer = new StringBuffer();
		while ((line = br.readLine()) != null)  
		{  
			//System.setOut(console);
			//System.out.println(line);
		     String [] WordsOfLine = line.split(" ",3);
		     if(WordsOfLine[0].equals("Move"))
		     {
		    		 String ExtractLineNumber = extractInt(line);
		    		 if(!ExtractLineNumber.equals("-1"))
		    		 {		 
		    			 String[] WordsInLine = ExtractLineNumber.split(" ");
		    			 int LengthWordsInLine = WordsInLine.length;
		    			 int num1 = 0;
		    			 if(LengthWordsInLine==2)
		    			 {
			    			 num1 = Integer.parseInt(WordsInLine[0]);
			    			 int l1 = binarySearch(StartLineNumbers,0,StartLineNumbers.size()-1,num1);
			    			 line = "";
			    			 if(l1>=0) 
			    			 {
				    			 if(num1 <= EndLineNumbers.get(l1))
				    			 {
				    				 String H = MethodNames.get(l1);
				    				 if(!map.containsKey(H))
				    				 {
				    					 map.put(H, 1);
				    					 Entities = H+ " " + Entities;
				    				 }
				    				 
				    			 } 
			    			 }
			    			 int num2 = Integer.parseInt(WordsInLine[1]);
			    			 l1 = binarySearch(StartLineNumbers,0,StartLineNumbers.size()-1,num2);
			    			 if(l1>=0)
			    			 {
				    			 if(num2 <= EndLineNumbers.get(l1))
				    			 {
				    				 String H = MethodNames.get(l1);
				    				 if(!map.containsKey(H))
				    				 {
				    					 map.put(H, 1);
				    					 Entities = H+ " " + Entities;
				    				 }
				    			 } 
			    			 }
		    			 }
		    			 else if(LengthWordsInLine==4)
		    			 {
		    				 num1 = Integer.parseInt(WordsInLine[1]);
			    			 int l1 = binarySearch(StartLineNumbers,0,StartLineNumbers.size()-1,num1);
			    			 line = "";
			    			 if(l1>=0) 
			    			 {
				    			 if(num1 <= EndLineNumbers.get(l1))
				    			 {
				    				 String H = MethodNames.get(l1);
				    				 if(!map.containsKey(H))
				    				 {
				    					 map.put(H, 1);
				    					 Entities = H+ " " + Entities;
				    				 }
				    			 } 
			    			 }
			    			 int num2 = Integer.parseInt(WordsInLine[3]);
			    			 l1 = binarySearch(StartLineNumbers,0,StartLineNumbers.size()-1,num2);
			    			 if(l1>=0)
			    			 {
				    			 if(num2 <= EndLineNumbers.get(l1))
				    			 {
				    				 String H = MethodNames.get(l1);
				    				 if(!map.containsKey(H))
				    				 {
				    					 map.put(H, 1);
				    					 Entities = H+ " " + Entities;
				    				 }
				    			 } 
			    			 }
		    			 }
		    		 }	
		    		 else
		    	 	 {
		    			 line = "";
		    	 	 } 
		    	 
		     }
		     else if( WordsOfLine[0].equals("Update") || WordsOfLine[0].equals("Insert") || WordsOfLine[0].equals("Delete") )
		     {
		    	 
		    		 String ExtractLineNumber = extractInt(line);
		    		 
		    		 if(!ExtractLineNumber.equals("-1"))
		    		 {
		    			 String[] WordsInLine = ExtractLineNumber.split(" ");
		    			 int LengthWordsInLine = WordsInLine.length;
		    			 int LineNumberInRevisedFile = Integer.parseInt(WordsInLine[LengthWordsInLine-1]);
		    			 int l1 = binarySearch(StartLineNumbers,0,StartLineNumbers.size()-1,LineNumberInRevisedFile);
		    			 if(l1>=0)
		    			 {
			    			 if(LineNumberInRevisedFile <= EndLineNumbers.get(l1))
			    			 {
			    				 line = "";
			    				 String H = MethodNames.get(l1);
			    				 if(!map.containsKey(H))
			    				 {
			    					 map.put(H, 1);
			    					 Entities = H+ " " + Entities;
			    				 }
			    			 }
			    			 else line = "";
		    			 }
		    			 else line = "";
		    		 }
		    		 line = "";
		     } 
		    inputBuffer.append(line);
	        inputBuffer.append('\n');
		}
		br.close();
		FileOutputStream fileOut = new FileOutputStream("C:\\Users\\rkanchug\\Desktop\\Difference.txt");
		fileOut.write(inputBuffer.toString().getBytes()); 
		fileOut.close();
		//System.setOut(console);
		return Entities;
		
		}
		private static String extractInt(String str) {
			str = str.replaceAll("[^\\d]", " "); 
            str = str.trim(); 
            str = str.replaceAll(" +", " "); 
            if (str.equals("")) 
                return "-1"; 
            return str;
            
		}
	 public static String getEntityOfTheLineNumber(Integer lineNumber,ArrayList<EntityInfo> listOfTheFile)
    {
        int indexOfTheLineNumber = binarySearch(listOfTheFile,0,listOfTheFile.size()-1,lineNumber);
        EntityInfo Ei = listOfTheFile.get(indexOfTheLineNumber);
        int start = Integer.parseInt(Ei.getStartLineNumber().toString().split(" |\\,")[1]);
        int end = Integer.parseInt(Ei.getEndLineNumber().toString().split(" |\\,")[1]);
        String EntityName = Ei.getEntityName();
        if(  (lineNumber >= start) && (lineNumber <=end ))
        {
            return EntityName;
        }
        return null;
    }
		private static int binarySearch(Vector<Integer> arr, int l, int r, int x) 
	    { 
	        if (r >= l) { 
	            int mid = l + (r - l) / 2; 
	            if (arr.get(mid) == x) 
	                return mid; 
	            if (arr.get(mid) > x) 
	                return binarySearch(arr, l, mid - 1, x); 
	            return binarySearch(arr, mid + 1, r, x); 
	        } 
	        return r; 
	    }
		private static void getMethodLineNumbers(File src) throws ParseException,ParseProblemException, IOException {
	        CompilationUnit cu = StaticJavaParser.parse(src);
	        	new MethodVisitor().visit(cu, null);
	    }
	    public static class MethodVisitor extends VoidVisitorAdapter {
	        @Override	    
	        public void visit(MethodDeclaration m, Object arg)
	        {
	        	System.out.println(m.getRange().get().begin.line + " "  + m.getRange().get().end.line + " " + m.getName());
	        	return;
	        }
	    }	
}
