package application;
	
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;


public class Main extends Application {
	ArrayList<Student> students=new ArrayList<Student>();
	ArrayList<Teacher> teachers=new ArrayList<Teacher>();
	String ID="";
	Student focus=new Student("\"Hon English 10a\",\"S1\",\"Sri Lahari\",\"123438\",\"Tammera\",\"05\",\"Lisa\",\"Kellert\"".split("\",\""));
	Font oxygen30=new Font("Oxygen",30);
	Font oxygen50=new Font("Oxygen", 50);
	public void start(Stage s) {
		try {
			s.setMaximized(true);
			s.show();
			s.setTitle("Media Center Sign In");
			
			
			//Enter Your Student ID Interface Elements
			BorderPane enterIDR=new BorderPane();
			GridPane g=new GridPane();
			g.setHgap(s.getWidth()/10);
			g.setVgap(s.getHeight()/10);
			enterIDR.setCenter(g);
			Scene enterID=new Scene(enterIDR, s.getWidth(),s.getHeight());
			
			Text promptID=new Text("Enter your student ID number below \n or scan your card.");
			promptID.setFont(oxygen50);
			g.add(promptID, 3, 2,6,1);
			
			TextField inID=new TextField();
			inID.setFont(oxygen30);
			inID.setPromptText("Enter your student ID #");
			g.add(inID, 2, 5, 7,1);
			
			Button nextEnterID=new Button("Next");
			nextEnterID.setFont(oxygen50);
			g.add(nextEnterID, 4, 6,3,1);
			
			
			//Pick your class Interface Elements
			BorderPane pickClassR=new BorderPane();
			Scene pickClass=new Scene(pickClassR, s.getWidth(), s.getHeight());
			GridPane h=new GridPane();
			h.setHgap(s.getWidth()/50);
			h.setVgap(s.getHeight()/50);
			pickClassR.setLeft(h);
			
			ArrayList<Button> classButtons=new ArrayList<Button>();
			
			Text promptPickClass=new Text();
			promptPickClass.setFont(oxygen50);
			pickClassR.setTop(promptPickClass);
			
			
			//Listeners
			nextEnterID.setOnAction(e->{
				try{
					ID=inID.getText();
					focus=searchStudent(ID);
					promptPickClass.setText("Hello, "+focus.getFirstName()+". Please pick the class you are in right now.");
					for(Class c:focus.getSchedule(1)){
						try{
							classButtons.add(new Button(c.getClassName()));
							classButtons.get(classButtons.size()-1).setFont(oxygen30);
							h.add(classButtons.get(classButtons.size()-1), 1,classButtons.size(), 5, 1);
						}catch(Exception f){System.out.println("Error");}
					}
					s.setScene(pickClass);
				}catch(Exception d){
					Text idError=new Text("Please enter a valid Student ID");
					idError.setFont(oxygen50);
					g.add(idError, 3, 3,6,1);
				}
			});
			
			//Actual Main
			parseData();
			s.setScene(enterID);
			
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void parseData(){
		Scanner s = null;
		String id="";
		String [] quals;
		String in="";
		try {
			s=new Scanner(new File("courses.mer"));
			s.nextLine();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		in=s.nextLine();
		quals=in.substring(1,in.length()-1).split("\",\"");
		while(s.hasNext()){
			students.add(new Student(quals));
			id=quals[3];
			students.get(students.size()-1).addClass(quals);
			in=s.nextLine();
			quals=in.substring(1,in.length()-1).split("\",\"");
			do{
				students.get(students.size()-1).addClass(quals);
				in=s.nextLine();
				quals=in.substring(1,in.length()-1).split("\",\"");
			}while(id.equals(quals[3])&&s.hasNext());
			students.get(students.size()-1).sortClass();
		}
	}
	
	public Student searchStudent(String findThisID){
		for(Student s: students){
			if(s.getID().equals(findThisID)){
				return s;
			}
		}
		return null;
	}
	
	public static void main(String[] args) {
		launch(args);
		
		
	}
}
