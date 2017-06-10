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
	/**
	 * Pads class and teacher information to a consistent length
	 */
	public String toString(){
		return pd+ "\t"+(name+"                                                                    ").substring(0, 35)+"\t"+teacher;
	}
	/**
	 * Compares classes by period
	 */
	public int compareTo(Class arg0) {
		return pd-(arg0).pd;
	}
	/**
	 * Returns period of a class
	 * @return period
	 */
	public int getPd(){
		return pd;
	}
	/**
	 * Returns name of class
	 * @return class title
	 */
	public String getClassName(){
		return name;
	}
	/**
	 * Returns teacher of class
	 * @return teacher
	 */
	public Teacher getTeacher(){
		return teacher;
	}
	/**
	 * Returns semester of class
	 * @return semester
	 */
	public String getSemester(){
		return semester;
	}
}
