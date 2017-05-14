package application;


public class Class implements Comparable{
	private String name;
	private Teacher teacher;
	private int pd;
	private String semester;
	
	public Class(String n, String s, int p, String f, String l){
		name=n;
		teacher=new Teacher(f,l);
		semester=s;
		pd=p;
	}
	
	public String toString(){
		return pd+ "\t"+semester+teacher;
	}
	
	public int compareTo(Object arg0) {
		return pd-((Class) arg0).pd;
	}
	
	public int getPd(){
		return pd;
	}
	
	public String getClassName(){
		return name;
	}
	
	public String getSemester(){
		return semester;
	}
}
