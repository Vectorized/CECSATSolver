package sat;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Pattern;

import sat.formula.*;



/**
 * This is the file to parse a given text file in the CNF (Conjunctive Normal Form) format into a Formula representing it.
 * 
 * @author Kang Yue Sheng Benjamin
 *
 */
public class CNFParser {

	public static final String COMMENT_LINE_REGEX = "c.*";
	public static final String PROBLEM_LINE_REGEX = "p cnf [0-9]+ [0-9]+";

	public static final String CLAUSE_REGEX = "[-]?[^ ]+"; 
	public static final String UNSIGNED_INTEGER_REGEX = "[0-9]+";
	public static final String EMPTY_LINE_REGEX = "[ ]*";
	public static final String CONSECUTIVE_SPACE_REGEX = "\\s{2,}";
	
	/**
	 * A method to return a Formula based on the CNF file specified by fileName
	 * 
	 * @param fileName
	 * @param swapLiteralsAndClausePositions TRUE if the CNF file has swapped the positions of number of Literals and Clauses in the 
	 * problem line
	 * @return A Formula representing the CNF
	 */
	public static Formula formulaFromFile(String fileName, boolean swapLiteralsAndClausePositions) {
		try {
			
			List<String> lines = linesFromUTF8File(fileName);
			String problemLine = null;
			ArrayList<String> literalList = new ArrayList<String>();
			
			int numberOfClauses = 0;
			int numberOfLiterals = 0;
			 
			for (String string:lines) {	
				String s = string.trim().replaceAll(CONSECUTIVE_SPACE_REGEX, " ");
				if (Pattern.matches(COMMENT_LINE_REGEX, s) || Pattern.matches(EMPTY_LINE_REGEX, s)) {
					//Do nothing
				} else if (Pattern.matches(PROBLEM_LINE_REGEX, s)) { //The current line matches a problem line
					if (problemLine!=null) { 
						throw new CNFParseException("More than one Problem Lines exists in the CNF file.");
					}
					problemLine = s;
					String[] problemLineTokens = problemLine.split(" ");
					if (problemLineTokens.length!=4) {
						throw new CNFParseException("The Problem Line is not well formed.");
					}
					if (!problemLineTokens[1].toLowerCase().equals("cnf")) {
						throw new CNFParseException("The Problem Line does not specify CNF as the format.");
					}
					
					int j = swapLiteralsAndClausePositions? 3 : 2;
					int k = swapLiteralsAndClausePositions? 2 : 3;
					if (Pattern.matches(UNSIGNED_INTEGER_REGEX, problemLineTokens[j])) {
						numberOfLiterals = Integer.parseInt(problemLineTokens[j]);
					} else {
						throw new CNFParseException("The number of Literals in the Problem Line is not a positive integer.");
					}
					
					if (Pattern.matches(UNSIGNED_INTEGER_REGEX, problemLineTokens[k])) {
						numberOfClauses = Integer.parseInt(problemLineTokens[k]);
					} else {
						throw new CNFParseException("The number of Clauses in the Problem Line is not a positive Integer.");
					}
				} else {
					String[] literalsInLine = s.split("\\s");
					for (String literal:literalsInLine) {
						if (Pattern.matches(CLAUSE_REGEX, literal)) {
							literalList.add(literal);
						} else {
							throw new CNFParseException("The CNF file contains an invalid sequence of characters.");
						}
					}
				}
			} //End of for each line
			
			Formula	formula 		= new Formula();
			Clause 	currentClause 	= new Clause();
			HashSet<String> literalsEncountered = new HashSet<String>();
			
			for (String literal:literalList) {
				if (literal.equals("0")) {
					formula = formula.addClause(currentClause);
					currentClause = new Clause();
				} else if (literal.charAt(0)=='-') {
					String nonNegativePart = literal.substring(1);
					literalsEncountered.add(nonNegativePart);
					currentClause = currentClause.add(NegLiteral.make(nonNegativePart));
				} else {
					literalsEncountered.add(literal);
					currentClause = currentClause.add(PosLiteral.make(literal));
				}
			}
			if (currentClause.isEmpty()==false){ //In case the last line is not terminated by 0
				formula = formula.addClause(currentClause);
			}
			
			//Checking the file for correctness
			if (formula.getSize()>numberOfClauses) {
				throw new CNFParseException("The number of Clauses exceeded the amount stated in the problem statement: " + formula.getSize() + " vs " + numberOfClauses);
			}
			if (literalsEncountered.size()>numberOfLiterals) {
				throw new CNFParseException("The number of Literals exceeded the amount stated in the problem statement: " + literalsEncountered.size() + " vs " + numberOfLiterals);
			}
			return formula;
			
		} catch (CNFParseException e) {
			System.out.println(e);
			return null;
		}
	}
	
	/**
	 * An Exception that occurs if the CNF file has errors
	 * @author User
	 *
	 */
	static class CNFParseException extends Exception {
		private static final long serialVersionUID = 1L;
		public CNFParseException(String message) {
			super(message);
		}
	}
	
	/**
	 * Returns a list of Strings representing each line in the text file specified by fileName
	 * @param fileName
	 * @return
	 */
	private static List<String> linesFromUTF8File(String fileName) {
		try {
		    FileReader file = new FileReader(fileName);
		    BufferedReader reader = new BufferedReader(file);
		    List<String> lines = new ArrayList<String>();
		    String s;
		    while ((s = reader.readLine()) != null) {
		      lines.add(s);
		    }
		    reader.close();
		    file.close();
		    return lines;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
}
