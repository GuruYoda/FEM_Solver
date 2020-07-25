package FileHandling;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class read {
	
	public static void main(String[] args) {
		try {
			// Creating an object of the file for reading the data
			File myObj = new File("C:\\Users\\Gursimran Singh\\Desktop\\RUB\\2nd_Semester\\OOFEM\\SolverResults.txt");  
			Scanner myReader = new Scanner(myObj);
			while (myReader.hasNextLine()) {
				String data = myReader.nextLine();
				System.out.println(data);
			}
			myReader.close();
		} 
		catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}

}
