package newIndex;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;


/**
 * A collection of utility methods for text processing.
 */
public class Utilities {
	/**
	 * Reads the input text file and splits it into alphanumeric tokens.
	 * Returns an ArrayList of these tokens, ordered according to their
	 * occurrence in the original text file.
	 * 
	 * Non-alphanumeric characters delineate tokens, and are discarded.
	 *
	 * Words are also normalized to lower case. 
	 * 
	 * Example:
	 * 
	 * Given this input string
	 * "An input string, this is! (or is it?)"
	 * 
	 * The output list of strings should be
	 * ["an", "input", "string", "this", "is", "or", "is", "it"]
	 * 
	 * @param input The file to read in and tokenize.
	 * @return The list of tokens (words) from the input file, ordered by occurrence.
	 */
	public static ArrayList<String> tokenizeFile(File input) {
		try
		{
			BufferedReader in = new BufferedReader( new FileReader(input));
			String FileLine = "";
			ArrayList<String> TokenList = new ArrayList<String>();
			
			
			while( (FileLine = in.readLine()) != null)
			{
				String [] TokenLine  = FileLine.split("[^a-zA-Z0-9]+");
				for (int i =0; i<TokenLine.length; i++)
				{
					if(!TokenLine[i].equals("")) //Delete Empty String
							TokenList.add(TokenLine[i].toLowerCase());
			    }
				
			}
			in.close();
			return TokenList;
		}
		catch(Exception e)
		{
			System.out.println("Exception while reading input file: " + e);  
			return null;
		}
	}
	
	/**
	 * Takes a list of {@link Frequency}s and prints it to standard out. It also
	 * prints out the total number of items, and the total number of unique items.
	 * 
	 * Example one:
	 * 
	 * Given the input list of word frequencies
	 * ["sentence:2", "the:1", "this:1", "repeats:1",  "word:1"]
	 * 
	 * The following should be printed to standard out
	 * 
	 * Total item count: 6
	 * Unique item count: 5
	 * 
	 * sentence	2
	 * the		1
	 * this		1
	 * repeats	1
	 * word		1
	 * 
	 * 
	 * Example two:
	 * 
	 * Given the input list of 2-gram frequencies
	 * ["you think:2", "how you:1", "know how:1", "think you:1", "you know:1"]
	 * 
	 * The following should be printed to standard out
	 * 
	 * Total 2-gram count: 6
	 * Unique 2-gram count: 5
	 * 
	 * you think	2
	 * how you		1
	 * know how		1
	 * think you	1
	 * you know		1
	 * 
	 * @param frequencies A list of frequencies.
	 */
	public static void printFrequencies(List<Frequency> frequencies, Integer Calledby) {
		Integer UniqueCount = frequencies.size();
		Integer TotalCount = 0;
		
		for(Frequency term: frequencies)
		{
			TotalCount+=term.getFrequency();
		}
		switch(Calledby){
			case 1: {
				System.out.println("Total item count: "+ TotalCount);
				System.out.println("Unique item count: "+ UniqueCount);
				break;
			}
			case 2:{
				System.out.println("Total 2-gram count: "+ TotalCount);
				System.out.println("Unique 2-gram count: "+ UniqueCount);
				break;
			}
			case 3:
			{
				System.out.println("Total Palindrome count: "+ TotalCount);
				System.out.println("Unique Palindrome count: "+ UniqueCount);
				break;
			}
			default:
			{
				System.out.println("Unknown Function");
			}
		}

		System.out.println(" ");
		for(Frequency term: frequencies)
		{
			System.out.println(term.toString());
		}
	}
}
