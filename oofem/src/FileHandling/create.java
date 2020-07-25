package FileHandling;

import java.io.File;
import java.io.IOException;

public class create {
	
	public static void main(String[] args) {
		try {
			// Creating an object of a file
			File myObj = new File("C:\\Users\\Gursimran Singh\\Desktop\\RUB\\2nd_Semester\\OOFEM\\SolverResults.txt"); 
			if (myObj.createNewFile()) {
				System.out.println("File created: " + myObj.getName());
			} 
			else {
				System.out.println("File already exists.");
			}
		} 
		catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
			}
		}

}
