package application;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class ParseTeacherEmails {

	public static void main(String[] args) {
		
		Scanner kb = null;
		try {
			kb = new Scanner(new File("Teachers.txt"));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(new File("Teacher Emails.txt"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		while(kb.hasNextLine()){
			String line = kb.nextLine();
			System.out.println(line);
			int isEmail = line.indexOf("@mcpsmd");
			if(isEmail!=-1){
				writer.println(line);
			}
		}kb.close();
		writer.close();

	}

}