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
	public void start(Stage s) {
		try {
			Font oxygen20=new Font("Oxygen",20);
			Font oxygen50=new Font("Oxygen", 50);
			//Please Wait/Initial Window
			s.setMaximized(true);
			BorderPane pleaseWaitR = new BorderPane();
			Scene pleaseWait = new Scene(pleaseWaitR,s.getWidth(),s.getHeight());
			Text pwText=new Text("Please wait while your data is loaded");
			pwText.setFont(oxygen50);
			pleaseWaitR.setCenter(pwText);
			
			s.setTitle("Media Center Sign In");
			s.setScene(pleaseWait);
			s.show();
			parseData();
			Thread.sleep(2000);
			
			
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
			inID.setFont(oxygen20);
			inID.setPromptText("Enter your student ID #");
			g.add(inID, 2, 5, 7,1);
			
			Button nextEnterID=new Button("Next");
			nextEnterID.setFont(oxygen50);
			g.add(nextEnterID, 4, 6,3,1);
			
			//Pick your class Interface Elements
			BorderPane pickClassR=new BorderPane();
			Scene pickClass=new Scene(pickClassR, s.getWidth(), s.getHeight());
//			GridPane h=new GridPane();
//			h.setHgap(s.getWidth()/10);
//			h.setVgap(s.getHeight()/10);
//			pickClassR.setLeft(h);
//			
//			ArrayList<Button> classButtons=new ArrayList<Button>();
//			for(int i=1;i<=8;i++){
//				try{
//					classButtons.get(i-1).setFont(oxygen50);
//					h.add(classButtons.get(i-1), 1, i+1, 5, 1);
//				}catch(Exception e){}
//			}
			//Listeners
			nextEnterID.setOnAction(e->{
				ID=inID.getText();
				System.out.println(ID);
				s.setScene(pickClass);
			});
			
			//Actual Main
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
			s=new Scanner(new File("MiniTestCourse.txt"));
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
	public static void main(String[] args) {
		launch(args);
	}
}
