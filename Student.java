package application;

import java.util.ArrayList;

public class Student {
	private String firstName;
	private String lastName;
	private int studentID;
	ArrayList<Class> schedule1; //semester 1
	ArrayList<Class> schedule2; //semester 2
	
	public Student(String first, String last, int id, ArrayList<Class> sem1, ArrayList<Class> sem2){
		firstName = first;
		lastName = last;
		studentID = id;
		schedule1 = sem1;
		schedule2 = sem2;
	}
	
	public void addClass(Class addThisClass, int semester){
		if(semester == 1){
			schedule1.add(addThisClass);
		}else if(semester == 2){
			schedule2.add(addThisClass);
		}else{
			System.out.println("Please specify the semester as 1 or 2.");
		}
	}
}
