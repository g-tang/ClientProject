package application;

public class Teacher {
	private String first;
	private String last;
	private String email;
	public Teacher(String f, String l){
		first=f;
		last=l;
	}
	public void sendEmail(String message){
		
	}
	public String toString(){
		return " "+first+"\t"+last;
	}
	
}
