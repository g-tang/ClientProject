package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;

public class Teacher {
	private String first;
	private String last;
	private String email="";
	public Teacher(String f, String l, String e){
		first=f;
		last=l;
		email=e;
	}
	public void sendEmail(File template, Teacher theoretical, Teacher signedPass, Student st){
		try{	
			String message="";
			Scanner s=null;
			try {
				s = new Scanner(template);
				while(s.hasNextLine()){
					message+=s.nextLine()+"\n";
				}
			} catch (FileNotFoundException e) {
			}
			message=message.replaceAll("<SIGNEDPASS>", signedPass.toString());
			message=message.replaceAll("<THEORETICAL>", theoretical.toString());
			message=message.replaceAll("<STUDENTNAME>", st.getFullName());
			message=message.replaceAll("<TIME>", dateToString());
			System.out.println(message);
			EmailUtil.sendEmail("gracetang021@gmail.com", "Media Center Sign In", message);
		}catch(Exception e){
			sendErrorMessage(new File("ErrorEmail"), this);
		}
	}
	public void sendErrorMessage(File template, Teacher about){
		String message="";
		Scanner s=null;
		try {
			s = new Scanner(template);
			while(s.hasNextLine()){
				message+=s.nextLine()+"\n";
			}
			message=message.replaceAll("<TEACHER>", about.toString());
			System.out.println(message);
			EmailUtil.sendEmail(Main.getAdmin().email, "Media Center Sign In Error", message);
		} catch (Exception e) {}
	}
	public static String dateToString(){
		String date="on ";
		Date d=new Date();
		date+=(d.getMonth()+1)+"/"+d.getDate()+" at ";
		if(d.getHours()==0){
			date+="12:"+d.getMinutes();
		}else if(d.getHours()<=12){
			date+=d.getHours()+":"+d.getMinutes();
		}else{
			date+=(d.getHours()-12)+":"+d.getMinutes();
		}
		if(d.getHours()<12){
			date+=" AM";
		}else{date+=" PM";}
		return date;
	}
	public String toString(){
		return first+" "+last;
	}
	public Teacher findTeacher(ArrayList<Teacher> teachers){
		for(Teacher t:teachers){
			if(t.toString().equals(this.toString())){
				return t;
			}
		}
		return this;
	}
}
