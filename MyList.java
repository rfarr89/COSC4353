package R_Farrell.SWD_HW3;

import org.apache.commons.cli.*;

/* Assignment:	HW3 
 * File Name:	MyList.java
 * Author:		Ryan Farrell (1488274)
 * Course:		COSC 4353, Fall 2017
 * File Description: 
 * 		In this assignment, we want to extend the first assignment in two ways:
 *
 * 		In addition to searching a sorted list of integer, it should be able to search a sorted list of strings. 
 * 		Strings are lexicographically sorted. We want to use the binSearch with minimal modification.  
 *		A solution may change the signature of function binSearch from  binSearch(int[] aList, int key) 
 *		to  binSearch(Comparable[] aList, Comparable key)  where Comparable is an interface in JDK 
 *		(https://docs.oracle.com/javase/8/docs/api/java/lang/Comparable.html)
 *		
 *		It must provide the following command-line arguments using Apache Commons CLI library 
 *		(https://commons.apache.org/proper/commons-cli/): “--type”, to allow a user to specify types of 
 *		input: “i” for integer and “s” for string, “--key” specifies the value of the search key,
 *		“--list”  specifies the sorted list of integers or strings. 
 *
 *		Examples: 
 *		$ java Main --type i --key 6 --list 3 4 5 6
 *		 Denotes that the list is of type integer, the search key is 6, and the list is 3 4 5 6 
 *		
 *		$ java Main --type s --key ab --list a aa b  cc
 *		Denotes that the list is type string, the key is “ab” and the list to perform search on is: a aa b cc
 *		
 * */

public class MyList  {
	// main function takes the list and the key from StdIn and returns the result to StdOut
	public static void main(String[] args) {
		int returnVal = -1;								// int returnVal holds the value to be printed or "returned"
		// set options for command line input using Apache CLI
		Options options = new Options();
		// all three specified options are required for this program
		// build option for input data type
		Option typeOpt = Option.builder("T")
				.required()
				.longOpt("type")
				.desc("specifies the type of input: 'i' for integer and 's' for string")
				.hasArg()
				.argName("TYPE")
				.build();
		options.addOption( typeOpt );
		// build option for key to search for
		Option keyOpt = Option.builder("K")
				.required()
				.longOpt("key")
				.desc("specifies the value of the search key")
				.hasArg()
		    	.argName("KEY")
		    	.build();
		options.addOption( keyOpt );
		// build option for list to search through
		Option listOpt = Option.builder("L")
				.required()
				.longOpt("list")
				.desc("specifies the sorted list of integers or strings")
				.hasArgs()
				.argName("LIST")
				.build();
		options.addOption( listOpt );
		CommandLineParser parser = new DefaultParser();	// create the command line parser
		try {	
		    // parse the command line arguments using Apache Commons CLI
		    CommandLine line = parser.parse( options, args );
		    String inputType = line.getOptionValue("type");
		    String inputKey = line.getOptionValue("key");
		    String[] inputList = line.getOptionValues("list");
		    if (inputType.equalsIgnoreCase("i")) {
		    	// type is designated as int, but maybe the user is wrong and it's really a string
		    	// attempt to parse string to integers
		    	try {											
		    		// convert and set searchKey and inList values
		    		Integer searchKey = Integer.parseInt(inputKey);
			    	int listLength = inputList.length;
			    	Integer[] inList = new Integer[listLength];
		    		for (int i = 0; i < listLength; i++) {
			    		inList[i] = Integer.parseInt(inputList[i]);
			    	}
			    	if (binSearch(inList, searchKey)) {			// search the list for the key
			    		returnVal = 1;
			    	}
			    	else {
			    		returnVal = 0;
			    	}
		    	} 
		    	catch (NumberFormatException numExcept) {
		    		    System.out.println("You said these were numbers but they are letters!!! Exception: " + numExcept);
		    		    System.out.println("Terminating program. Please verify your input next time.");
		    		    System.exit(1);
		    	}
		    }
		    else if (inputType.equalsIgnoreCase("s")) {
		    	// set type to string
		    	// inputKey and inputList are already of type String so no conversion necessary
		    	// if the user incorrectly labels the list of integers as strings, good news! it will still search
		    	String searchKey = inputKey;
		    	String[] inList = inputList;
		    	if (binSearch(inList, searchKey)) {				// search the list for the key
		    		returnVal = 1;
		    	}
		    	else {
		    		returnVal = 0;
		    	}
		    }
		    System.out.println(returnVal);				// print return value
		}
		catch( ParseException e ) {
		    System.out.println("Unexpected exception:" + e.getMessage());
		    System.out.println("Unable to parse command line. Please try again.");
		}
		// Fin.
	}
		
	private static <T extends Comparable<T>> boolean binSearch(T[] aList, T key) {
		int first = 0;						// first holds the index of the first entry in the sub-array
		int last = aList.length - 1;		// last holds the index of the last entry in the sub-array
		int mid = (first + last)/2;			// mid holds the index of the middle entry in the sub-array
		T midValue = aList[mid];			// midValue holds the value of the entry at aList[mid] for quick reference
		while (first <= last) {				// loop until key is found or we have looked everywhere it could be found
			if (key.compareTo(midValue) == 0) {
				return true;				// key is found, success!
			}
			else if (key.compareTo(midValue) < 0) {
				// first remains the same
				last = mid - 1;				// reassign value of last to the index before mid
			}
			else if (key.compareTo(midValue) > 0) {
				first = mid + 1;			// reassign value of first to the index after mid
				// last remains the same
			}
			mid = (first + last)/2;			// unless key == midValue reassign values of mid and midValue
			midValue = aList[mid];
		}
		return false;// if a match for key is not found, return false
	}
}