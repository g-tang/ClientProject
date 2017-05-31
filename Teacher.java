package application;

import java.util.Date;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Teacher implements Comparable<Teacher>{
	private String first="";
	private String last="";
	private String email="";
	public Teacher(String f, String l, String e){
		for(int i=0;i<f.length();i++){
			if(Character.isLetter(f.charAt(i))){
				first = first + f.charAt(i);
			}
		}
		for(int i=0;i<l.length();i++){
			if(Character.isLetter(l.charAt(i))){
				last = last + l.charAt(i);
			}
		}
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
			} catch (Exception e) {
			}
			message=message.replaceAll("<SIGNEDPASS>", signedPass.toString());
			message=message.replaceAll("<THEORETICAL>", theoretical.toString());
			message=message.replaceAll("<STUDENTNAME>", st.getFullName());
			message=message.replaceAll("<TIME>", dateToString(true));
			System.out.println(message);
			EmailUtil.sendEmail(this.email, "Media Center Sign In", message,null);
		}catch(Exception e){
			sendErrorMessage(new File("ErrorEmail"), this);
		}
	}
	public void sendErrorMessage(File template, Teacher about){
		String message=fileToString(template);
		Scanner s=null;
		try {
			message=message.replaceAll("<TEACHER>", about.toString());
			System.out.println(message);
			EmailUtil.sendEmail(Main.getAdmin().email, "Media Center Sign In Error", message,null);
		} catch (Exception e) {}
	}
	public static String fileToString(File f){
		String message="";
		try{
			Scanner s=new Scanner(f);
			while(s.hasNextLine()){
				message+=s.nextLine()+"\n";
			}
		}catch(Exception e){}
		return message;
	}
	public static String dateToString(boolean addDate){
		String date="";
		Date d=new Date();
		if(addDate){date+=(d.getMonth()+1)+"/"+d.getDate()+" at ";}
		if(d.getHours()==0){
			date+="12:"+Integer.toString(d.getMinutes()+100).substring(1);
		}else if(d.getHours()<=12){
			date+=d.getHours()+":"+Integer.toString(d.getMinutes()+100).substring(1);
		}else{
			date+=(d.getHours()-12)+":"+Integer.toString(d.getMinutes()+100).substring(1);
		}
		if(d.getHours()<12){
			date+=" AM";
		}else{date+=" PM";}
		return date;
	}
	public String toString(){
		return first.trim()+" "+last.trim();
	}
	public String getEmail(){
		return email;
	}
	public boolean equals(Teacher t){
		if(first.equals(t.first)&&last.equals(t.last)){
			return true;
		}
		return false;
	}
	public static ArrayList<Teacher> getTeacherInfo(File fileName){
		ArrayList<Teacher> teachers = new ArrayList<Teacher>();
		try{
			Scanner kb = new Scanner(fileName);	
			while(kb.hasNextLine()){
				String email = kb.nextLine();
				String[] emailList = email.split("_");
				String first = emailList[0];
				String end = emailList[emailList.length-1];
				String last = end.substring(0, end.indexOf("@"));
				teachers.add(new Teacher(first, last, email));
			}
			kb.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		Collections.sort(teachers);
		for(int i=0;i<teachers.size();i++){
			for(int j=i+1;j<teachers.size();j++){
				if(teachers.get(i).equals(teachers.get(j))){
					teachers.remove(j);
					j--;
				}
			}
		}
		return teachers;
	}
	public Teacher findTeacher(ArrayList<Teacher> teachers){
		for(Teacher t:teachers){
			if(t.toString().equals(this.toString())){
				return t;
			}
		}
		return this;
	}
	public int compareTo(Teacher arg0) {
		return last.compareTo(arg0.last);
	}
}
