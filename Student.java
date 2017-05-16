package application;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
//CRSTITLE,DUR,FIRST,ID,LAST,PD,TCHF,TCHL
public class Student {
	private String fName;
	private String lName;
	private String id;
	ArrayList<Class> schedule1=new ArrayList<Class>();
	ArrayList<Class> schedule2=new ArrayList<Class>();
	String[] quals;
	
	public Student(String[] s){
		fName=s[2];
		lName=s[4];
		id=s[3];
	}
	
	public void addClass(String[] s){

		if(s[1].equals("S1")){
			schedule1.add(new Class(s[0], s[1], Integer.parseInt(s[5]), s[6], s[7]));
		}
		if(s[1].equals("S2")){
			schedule2.add(new Class(s[0], s[1], Integer.parseInt(s[5]), s[6], s[7]));
		}
	}
	
	public String toString(){
		System.out.println( fName+" "+lName+", #"+id);
		for(Class c:schedule1){
			System.out.println(c);
		}
		for(Class d:schedule2){
			System.out.println(d);
		}
		return "";
	}
	
	public void sortClass(){
		Collections.sort((List<Class>)schedule1);
		Collections.sort((List<Class>)schedule2);
		
	}
	
	public String getLastName(){
		return lName;
	}
	
	public String getFirstName(){
		return fName;
	}
	
	public String getID(){
		return id;
	}
	
	public ArrayList<Class> getSchedule(int sem){
		if(sem==1){
			return schedule1;
		}
		return schedule2;
	}
}
