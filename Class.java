package application;


public class Class implements Comparable<Class>{
	private String name;
	private Teacher teacher;
	private int pd;
	private String semester;
	
	public Class(String n, String s, int p, String f, String l){
		name=n;
		teacher=new Teacher(f,l,"");
		semester=s;
		pd=p;
	}
	
	public String toString(){
		return pd+ "\t"+name+"\t"+teacher;
	}
	
	public int compareTo(Class arg0) {
		return pd-(arg0).pd;
	}
	
	public int getPd(){
		return pd;
	}
	
	public String getClassName(){
		return name;
	}
	public Teacher getTeacher(){
		return teacher;
	}
	public String getSemester(){
		return semester;
	}
}
