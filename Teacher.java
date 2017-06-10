package application;

import java.util.Date;
import java.io.File;
import java.io.FileNotFoundException;

import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import javax.mail.MessagingException;


public class Teacher implements Comparable<Teacher>{
	private String first="";
	private String last="";
	private String email="";
	public Teacher(String f, String l, String e){
		for(int i=0;i<f.length();i++){
			if(Character.isLetter(f.charAt(i))||f.charAt(i)=='-'){
				first = first + f.charAt(i);
			}
		}
		for(int i=0;i<l.length();i++){
			if(Character.isLetter(l.charAt(i))||l.charAt(i)=='-'){
				last = last + l.charAt(i);
			}
		}
		email=e;
	}
	/**
	 * Sends an email to the teacher
	 * @param template Name of file composing the message- Can be AdminEmail, NormalEmail, SignedPassEmail, or TheoreticalEmail
	 * @param theoretical Predicted teacher
	 * @param signedPass Cited teacher
	 * @param st Student who is signing in
	 * @param r Reason for visit
	 * @throws Exception If email sending fails
	 */
	public void sendEmail(File template, Teacher theoretical, Teacher signedPass, Student st, String r) throws Exception{
		String message="";
		try{
			Scanner s=null;
			try {
				s = new Scanner(template);
				while(s.hasNextLine()){
					message+=s.nextLine()+"\n";
				}
			} catch (Exception e) {e.printStackTrace();System.out.println("Template error");}
			message=message.replaceAll("<SIGNEDPASS>", signedPass.toString());
			if(theoretical!=null){message=message.replaceAll("<THEORETICAL>", theoretical.toString());}else{message=message.replaceAll("<THEORETICAL>", "N/A");}
			message=message.replaceAll("<STUDENTNAME>", st.getFullName());
			message=message.replaceAll("<TIME>", dateToString(true));
			message=message.replaceAll("<REASON>", r);
			message=message.replaceAll("<ID>", st.getID());
			System.out.println(message);
			EmailUtil.sendEmail(this.email, "A student has signed into the PHS Library", message);
		}catch(UnsupportedEncodingException | MessagingException e){
			e.printStackTrace();
			System.out.println("Email Error");
			EmailUtil.sendEmail(Main.getAdmin().getEmail(),"Failed notification: Does "+this+" have a valid email?", message);
			throw new Exception();
		}
	}
	/**
	 * Parses a template to a string
	 * @param f Name of file
	 * @return content of file
	 */
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
	/**
	 * Parses current date/time to string
	 * @param addDate Whether to return the date and time or just the time
	 * @return Current date/time as string
	 */
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
	/**
	 * Returns name of teacher
	 */
	public String toString(){
		return last.trim()+", "+first.trim();
	}
	/**
	 * Returns email of teacher
	 * @return email
	 */
	public String getEmail(){
		return email;
	}
	/**
	 * Compares 2 teachers
	 * @param t Teacher to compare to
	 * @return Whether the 2 teachers are equal or not
	 */
	public boolean equals(Teacher t){
		if(first.equals(t.first)&&last.equals(t.last)){
			return true;
		}
		return false;
	}
	/**
	 * Pulls teacher names/emails from a list of their emails into a list
	 * @param fileName Name of file
	 * @return ArrayList of teachers
	 */
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
	/**
	 * Finds teacher in a list of teachers. Used as a way to update teacher info from an ArrayList with more useful fields
	 * @param teachers List of updated teachers
	 * @return updated teacher
	 */
	public Teacher findTeacher(ArrayList<Teacher> teachers){
		for(Teacher t:teachers){
			if(t.toString().equals(this.toString())){
				return t;
			}
		}
		return this;
	}
	/**
	 * Compares teachers by last name to order them
	 */
	public int compareTo(Teacher arg0) {
		return last.compareTo(arg0.last);
	}
	/**
	 * Runs through a file and removes every line that does not contain an email with the format First_Last@mcpsmd.org or First_Middle_Last@mcpsmd.org
	 * @param perm Name of file to overwrite
	 */
	public static void parseTeacher(File perm){
		Scanner kb = null;
		PrintWriter pw=null;
		try {
			kb = new Scanner(perm);
		} catch (FileNotFoundException e1) {
		}
		ArrayList<String> temp=new ArrayList<String>();
		while(kb.hasNextLine()){
			String line = kb.nextLine();
			int isEmail = line.indexOf("@mcpsmd");
			if(isEmail!=-1){
				temp.add(line);
			}
		}kb.close();
		try {
			pw=new PrintWriter(perm);
		} catch (FileNotFoundException e) {}
		for(String s:temp){
			pw.println(s);
		}
		pw.close();
	}
}
