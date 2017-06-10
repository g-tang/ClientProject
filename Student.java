package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Scanner;
//CRSTITLE,DUR,FIRST,ID,LAST,PD,TCHF,TCHL
public class Student {
	private String fName;
	private String lName;
	private String id;
	ArrayList<Class> schedule1=new ArrayList<Class>();
	ArrayList<Class> schedule2=new ArrayList<Class>();
	String[] quals;
	/**
	 * Pulls the name and ID number from a line of data
	 * @param s-The array of strings from splitting a line in the Main's parseData method
	 */
	public Student(String[] s){
		fName=s[2];
		lName=s[4];
		id=s[3];
	}
	/**
	 * Pulls the name, teacher, semester, and period of a class from a line of data
	 * @param s The array of strings from splitting a line in the Main's parseData method
	 */
	public void addClass(String[] s){

		if(s[1].equals("S1")){
			schedule1.add(new Class(s[0], s[1], Integer.parseInt(s[5]), s[6], s[7]));
		}
		if(s[1].equals("S2")){
			schedule2.add(new Class(s[0], s[1], Integer.parseInt(s[5]), s[6], s[7]));
		}
	}
	/**
	 * Calls the sort method on the arrays of classes (fields)
	 */
	public void sortClass(){
		Collections.sort(schedule1);
		Collections.sort(schedule2);
	}
	/**
	 * Returns the full name of a student
	 * @return Full name
	 */
	public String getFullName(){
		return fName+ " "+lName;
	}
	/**
	 * Retrieves a list of hours and minutes for the ending times of each class in an ArrayList
	 * @param sched The name of the schedule file-Normal, 2 Hour Delay, Half Day, Custom. Defaults to Normal
	 * @return The schedule file contents formatted as an arrayList of String[]s with a length of 2- 1 for hours, 1 for minutes
	 */
	public static ArrayList<String[]> parseBellSchedule(String sched){
		ArrayList<String[]> bellSchedule=new ArrayList<String[]>();
		Scanner s=null;
		try {
			s=new Scanner(new File("DoNotTouch/Normal"));
			s = new Scanner(new File("DoNotTouch/"+sched));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		while(s.hasNextLine()){
			bellSchedule.add(s.nextLine().split(":"));
		}
		return bellSchedule;
	}
	/**
	 * Returns a predicted teacher based on when each class ends
	 * @param c The list of current classes for a student
	 * @param sched The name of the schedule file-Normal, 2 Hour Delay, Half Day, Custom. Defaults to Normal
	 * @return The predicted teacher
	 */
	public static Teacher getTheoreticalTeacher(ArrayList<Class> c, String sched){
		ArrayList<String[]> bellSchedule=parseBellSchedule(sched);
		Date d=new Date();
		try{
			if(d.getHours()<Integer.parseInt(bellSchedule.get(0)[0])){
				return null;
			}else if(d.getHours()==Integer.parseInt(bellSchedule.get(0)[0])){
				if(d.getMinutes()<=Integer.parseInt(bellSchedule.get(0)[1])){
					return null;
				}
			}
		}catch(Exception e){}
		for(int i=1;i<bellSchedule.size();i++){
			try{
				if(d.getHours()<Integer.parseInt(bellSchedule.get(i)[0])){
					return(searchClass(c, i).getTeacher());
				}else if(d.getHours()==Integer.parseInt(bellSchedule.get(i)[0])){
					if(d.getMinutes()<=Integer.parseInt(bellSchedule.get(i)[1])){
						return(searchClass(c,i).getTeacher());
					}
				}
			}catch(Exception e){}
		}
		return null;
	}
	/**
	 * Returns a class given the period
	 * @param classes The list of a student's current classes
	 * @param pd The period of the class to find
	 * @return The class that the student has for Period pd
	 */
	private static Class searchClass(ArrayList<Class> classes, int pd){
		for(Class c:classes){
			if(c.getPd()==pd){
				return c;
			}
		}
		return null;
	}
	/**
	 * Get the student's first name
	 * @return First name
	 */
	public String getFirstName(){
		return fName;
	}
	/**
	 * Get the student's ID number
	 * @return ID number
	 */
	public String getID(){
		return id;
	}
	/**
	 * Get the student's schedule given a semester
	 * @param sem The semester
	 * @return The list of classes
	 */
	public ArrayList<Class> getSchedule(int sem){
		if(sem==1){
			return schedule1;
		}
		return schedule2;
	}
}
