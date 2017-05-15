package application;
	
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import com.sun.javafx.scene.control.skin.VirtualFlow;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Cell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;


public class Main extends Application {
	ArrayList<Student> students=new ArrayList<Student>();
	ArrayList<Teacher> teachers=new ArrayList<Teacher>(Arrays.asList(new Teacher("person", "a"),new Teacher("person", "b")));
	ArrayList<String> reasons=new ArrayList<String>(Arrays.asList("dhrgfaseur","larwiygfeos","fhawekgvralyw","uasaghoew"));
	String ID="";
	Student focus;
	Teacher notify;
	Font oxygen30=new Font("Oxygen",30);
	Font oxygen50=new Font("Oxygen", 50);
	public void start(Stage s) {
		try {
			s.setMaximized(true);
			s.setTitle("Media Center Sign In");
			s.show();
			
			//Enter Your Student ID Interface Elements
			BorderPane enterIDR=new BorderPane();
			GridPane g=new GridPane();
			g.setHgap(s.getWidth()/10);
			g.setVgap(s.getHeight()/10);
			enterIDR.setCenter(g);
			Scene enterID=new Scene(enterIDR, s.getWidth(),s.getHeight());
			
			Text promptID=new Text("Enter your student ID number below \n or scan your card. Then click \"Next\"");
			promptID.setFont(oxygen50);
			g.add(promptID, 3, 2,6,1);
			
			TextField inID=new TextField();
			inID.setFont(oxygen30);
			inID.setPromptText("Enter your student ID #");
			g.add(inID, 2, 5, 7,1);
			
			Button nextEnterID=new Button("Next");
			nextEnterID.setFont(oxygen50);
			g.add(nextEnterID, 4, 6,3,1);
			
			
			//Pick your class Elements
			BorderPane pickClassR=new BorderPane();
			Scene pickClass=new Scene(pickClassR, s.getWidth(), s.getHeight());
			GridPane h=new GridPane();
			h.setHgap(s.getWidth()/50);
			h.setVgap(s.getHeight()/50);
			pickClassR.setLeft(h);
			
			Text promptPickClass=new Text();
			promptPickClass.setFont(oxygen50);
			pickClassR.setTop(promptPickClass);
			

			Button goToOtherTeacher=new Button("Other");
			goToOtherTeacher.setFont(oxygen50);
			h.add(goToOtherTeacher, 10, 5,5,1);
			
			Button goBackToID=new Button("Back");
			goBackToID.setFont(oxygen50);
			h.add(goBackToID, 10, 8,5,1);
			
			ObservableList<Class> classes=FXCollections.observableArrayList();
			ListView<Class> classesList=new ListView<Class>(classes);
			classesList.setMaxSize(s.getWidth()/2, s.getHeight()*0.8);
			classesList.setMinSize(s.getWidth()/2, s.getHeight()*0.8);
			pickClassR.getStylesheets().add(this.getClass().getResource("application.css").toExternalForm());
			
			//Other Teacher Elements
			BorderPane otherTeacherR=new BorderPane();
			Scene otherTeacher=new Scene(otherTeacherR, s.getWidth(), s.getHeight());
			
			Text promptPickTeacher=new Text("Select the teacher you are with:");
			promptPickTeacher.setFont(oxygen50);
			otherTeacherR.setTop(promptPickTeacher);
			
			Button goBackToPickClass=new Button("Back");
			goBackToPickClass.setFont(oxygen50);
			otherTeacherR.setRight(goBackToPickClass);
			
			ObservableList<Teacher> data=FXCollections.observableArrayList();
			ListView<Teacher> listTeacher=new ListView<Teacher>(data);
			listTeacher.setMaxSize(s.getWidth()/2, s.getHeight()*0.8);
			listTeacher.setMinSize(s.getWidth()/4, s.getHeight()*0.8);
			
			data.addAll(teachers);//move to main after parse
			otherTeacherR.setLeft(listTeacher);
			
			otherTeacher.getStylesheets().add(this.getClass().getResource("application.css").toExternalForm());
			
			
			//Reasons Elements
			BorderPane pickReasonsR=new BorderPane();
			Scene pickReasons=new Scene(pickReasonsR, s.getWidth(), s.getHeight());
			Text promptReasons=new Text("Why have you visited?");
			promptReasons.setFont(oxygen50);
			pickReasonsR.setTop(promptReasons);
			
			ObservableList<String> reasonsData=FXCollections.observableArrayList();
			ListView<String> listReasons=new ListView<String>(reasonsData);
			listReasons.setMaxSize(s.getWidth()/2, s.getHeight()*0.8);
			listReasons.setMinSize(s.getWidth()/2, s.getHeight()*0.8);
			reasonsData.addAll(reasons);
			pickReasonsR.setLeft(listReasons);
			
			pickReasons.getStylesheets().add(this.getClass().getResource("application.css").toExternalForm());
			
			Button goBackToPickClassFinal=new Button("Back");
			goBackToPickClassFinal.setFont(oxygen50);
			pickReasonsR.setRight(goBackToPickClassFinal);
			//Listeners
			nextEnterID.setOnAction(e->{
				try{
					ID=inID.getText();
					focus=searchStudent(ID);
					promptPickClass.setText("Hello, "+focus.getFirstName()+". Please pick the class you are in right now.");
					classes.clear();
					classes.addAll(focus.schedule1);
					pickClassR.setCenter(classesList);
					s.setScene(pickClass);
				}catch(Exception d){
					Text idError=new Text("Please enter a valid Student ID");
					idError.setFont(oxygen50);
					g.add(idError, 3, 3,6,1);
				}
			});
			goToOtherTeacher.setOnAction(e->{
				s.setScene(otherTeacher);
			});
			goBackToPickClass.setOnAction(e->{
				s.setScene(pickClass);
			});
			goBackToID.setOnAction(e->{
				s.setScene(enterID);	
			});
			goBackToPickClassFinal.setOnAction(e ->{
				s.setScene(pickClass);
			});
			listTeacher.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Teacher>(){
				public void changed(ObservableValue<? extends Teacher> arg0, Teacher old, Teacher newTeach) {
					notify=newTeach;
					s.setScene(pickReasons);
				}
			});
			classesList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Class>(){
				public void changed(ObservableValue<? extends Class> arg0, Class old, Class newClass) {
					notify=newClass.getTeacher();
					System.out.println(notify);
					s.setScene(pickReasons);
				}
			});
			listReasons.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>(){
				public void changed(ObservableValue<? extends String> arg0, String old, String newReason) {
					notify.sendEmail("");
					writeToDrive("");
					s.setScene(enterID);
					inID.clear();
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
	public void writeToDrive(String s){
		
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