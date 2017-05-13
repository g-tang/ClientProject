package application;

import java.util.Comparator;

public class Class implements Comparator{
	private String name;
	private Teacher teacher;
	private int pd;
	private String semester;
	public Class(String n, String s, int p, String l, String f){
		name=n;
		teacher=new Teacher(l,f);
		semester=s;
		pd=p;
	}
	public int compare(Object a, Object b) {
		return ((Class)a).pd-((Class)b).pd;
	}
	public String toString(){
		return name+ " "+pd+ " "+semester+teacher;
	}
}
