package application;

import java.util.ArrayList;
//CRSTITLE,DUR,FIRST,ID,LAST,PD,TCHF,TCHL
public class Student {
	private String fName;
	private String lName;
	private int id;
	ArrayList<Class> schedule1;
	ArrayList<Class> schedule2;
	String[] quals;
	public Student(String[] s){
		fName=s[2];
		lName=s[4];
		id=Integer.parseInt(s[3]);
		addClass(s);
	}
	public void addClass(String[] s){

		if(s[1].equals("S1")){
			schedule1.add(new Class(s[0], s[1], Integer.parseInt(s[5]), s[6], s[7]));
		}
		if(s[1].equals("S2")){
			schedule2.add(new Class(s[0], s[1], Integer.parseInt(s[5]), s[6], s[7]));
		}
	}
}
