package sat;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import sat.env.Environment;

/** 
 * A class to managed the results of a SATSolver given by an instance of Environment
 * 
 * @author Kang Yue Sheng Benjamin
 *
 */
public class EnvironmentProcessor {
	
	/**
	 * Prints out satisfiable if the environment is not null
	 * @param environment
	 */
	public static void printWhetherSatisfiableOrNot(Environment environment) {
		System.out.println((environment!=null)?"satisfiable":"not satisfiable");
	}
	
	/**
	 * Writes the result specified in the instance of environment into a file specified by fileName
	 * @param environment
	 * @param fileName
	 */
	public static void writeEnvironmentIntoFile(Environment environment, String fileName){
		if (environment!=null) {
			String environmentString =  environment.toString();
			environmentString = environmentString.substring(13, environmentString.length()-1).replaceAll("->", ":").replaceAll(", ","\n");
			
			try {
			      FileWriter output = new FileWriter(fileName);
			      BufferedWriter writer = new BufferedWriter(output);
			      writer.write(environmentString);
			      writer.close();
			      output.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
