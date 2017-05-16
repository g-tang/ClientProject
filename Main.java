package application;
	
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import com.sun.javafx.scene.control.skin.VirtualFlow;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Cell;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;


public class Main extends Application {
	ArrayList<Student> students=new ArrayList<Student>();
	ArrayList<Teacher> teachers=new ArrayList<Teacher>(Arrays.asList(new Teacher("person", "a"),new Teacher("person", "b")));
	ArrayList<String> reasons=new ArrayList<String>(Arrays.asList("Reason 1","Reason 2","Reason 3","Reason 4", "etc."));
	String ID="";
	Student focus;
	Teacher notify=new Teacher("","");
	String reason="";
	Font oxygen30=new Font("Oxygen",30);
	Font oxygen50=new Font("Oxygen", 50);
	public void start(Stage s) {
		try {
			s.setMaximized(true);
			s.setTitle("Media Center Sign In");
			s.setAlwaysOnTop(true);
			s.setFullScreen(true);
			s.setResizable(false);
			s.initStyle(StageStyle.UTILITY);
			Platform.setImplicitExit(false);
			s.show();
			
			//Enter Your Student ID Interface Elements
			BorderPane enterIDR=new BorderPane();
			GridPane g=new GridPane();
			g.setHgap(200);
			g.setVgap(100);
			enterIDR.setCenter(g);
			Scene enterID=new Scene(enterIDR, s.getWidth(),s.getHeight());
			
			Text promptID=new Text("Enter your student ID number below \n or scan your card. Then click \"Next\"");
			promptID.setFont(oxygen50);
			g.add(promptID, 3, 2,6,1);
			promptID.setTextAlignment(TextAlignment.CENTER);
			
			TextField inID=new TextField();
			inID.setFont(oxygen30);
			inID.setPromptText("Enter your student ID #");
			g.add(inID, 2, 5, 7,1);
			
			Button enterIDToPickClass=new Button("Next");
			enterIDToPickClass.setFont(oxygen50);
			g.add(enterIDToPickClass, 4, 6,3,1);
			
			
			//Pick your class Elements
			BorderPane pickClassR=new BorderPane();
			Scene pickClass=new Scene(pickClassR, s.getWidth(), s.getHeight());
			GridPane h=new GridPane();
			h.setHgap(40);
			h.setVgap(20);
			pickClassR.setCenter(h);
			
			Text promptPickClass=new Text();
			promptPickClass.setFont(oxygen50);
			

			Button pickClassToOtherTeacher=new Button("Other");
			pickClassToOtherTeacher.setFont(oxygen50);
			h.add(pickClassToOtherTeacher, 10, 1);
			
			Button pickClassToEnterID=new Button("Back");
			pickClassToEnterID.setFont(oxygen50);
			h.add(pickClassToEnterID,1,1);
			
			ObservableList<Class> classes=FXCollections.observableArrayList();
			ListView<Class> classesList=new ListView<Class>(classes);
			classesList.setMaxSize(s.getWidth()/2, s.getHeight()*0.8);
			classesList.setMinSize(s.getWidth()/2, s.getHeight()*0.8);
			pickClassR.getStylesheets().add(this.getClass().getResource("application.css").toExternalForm());
			
			//Other Teacher Elements
			BorderPane otherTeacherR=new BorderPane();
			Scene otherTeacher=new Scene(otherTeacherR, s.getWidth(), s.getHeight());
			GridPane i=new GridPane();
			i.setHgap(40);
			i.setVgap(20);
			otherTeacherR.setCenter(i);
			Text promptPickTeacher=new Text("Select the teacher you are with:");
			promptPickTeacher.setFont(oxygen50);
			i.add(promptPickTeacher, 5, 0);
			
			Button otherTeacherToPickClass=new Button("Back");
			otherTeacherToPickClass.setFont(oxygen50);
			i.add(otherTeacherToPickClass, 2,1);
			
			ObservableList<Teacher> data=FXCollections.observableArrayList();
			ListView<Teacher> listTeacher=new ListView<Teacher>(data);
			listTeacher.setMaxSize(s.getWidth()/4, s.getHeight()*0.8);
			listTeacher.setMinSize(s.getWidth()/4, s.getHeight()*0.8);
			
			data.addAll(teachers);//move to main after parse
			i.add(listTeacher, 5, 1);
			
			otherTeacher.getStylesheets().add(this.getClass().getResource("application.css").toExternalForm());
			
			
			//Reasons Elements
			BorderPane pickReasonsR=new BorderPane();
			Scene pickReasons=new Scene(pickReasonsR, s.getWidth(), s.getHeight());
			GridPane j=new GridPane();
			j.setHgap(40);
			j.setVgap(20);
			pickReasonsR.setCenter(j);
			
			Text promptReasons=new Text("Why have you visited?");
			promptReasons.setFont(oxygen50);
			j.add(promptReasons, 5, 0);
			
			ObservableList<String> reasonsData=FXCollections.observableArrayList();
			ListView<String> listReasons=new ListView<String>(reasonsData);
			listReasons.setMaxSize(s.getWidth()/3, s.getHeight()*0.8);
			listReasons.setMinSize(s.getWidth()/3, s.getHeight()*0.8);
			reasonsData.addAll(reasons);
			j.add(listReasons, 5, 1);
			
			pickReasons.getStylesheets().add(this.getClass().getResource("application.css").toExternalForm());
			
			Button reasonsToOtherTeacher=new Button("Back");
			reasonsToOtherTeacher.setFont(oxygen50);
			j.add(reasonsToOtherTeacher, 0, 1);
			
			Button submit=new Button("Submit");
			submit.setFont(oxygen50);
			j.add(submit, 10, 1);
			submit.setDisable(true);
			
			//Request Admin Password to Close
			
			
			BorderPane adminToCloseR=new BorderPane();
			Scene adminToClose=new Scene(adminToCloseR, s.getWidth(), s.getHeight());
			GridPane k=new GridPane();
			k.setHgap(200);
			k.setVgap(100);
			adminToCloseR.setCenter(k);
			
			Text promptPassword=new Text("Enter password to close");
			promptPassword.setFont(oxygen50);
			k.add(promptPassword, 3, 2);
			
			PasswordField enterPWordToClose=new PasswordField();
			enterPWordToClose.setFont(oxygen30);
			enterPWordToClose.setPromptText("Enter password here");
			k.add(enterPWordToClose, 2, 4,4,1);
			
			Button close=new Button("Ok");
			close.setFont(oxygen50);
			k.add(close, 4, 6);
			
			Button cancelClose=new Button("Cancel");
			cancelClose.setFont(oxygen50);
			k.add(cancelClose, 2, 6);
			
			//Listeners
			enterIDToPickClass.setOnAction(e->{
				try{
					ID=inID.getText();
					System.out.println(ID);
					focus=searchStudent(ID);
					promptPickClass.setText("Hello, "+focus.getFirstName()+". Please pick the class you are in right now.");
					h.getChildren().remove(promptPickClass);
					h.add(promptPickClass, 3, 0, 6,1);
					classes.clear();
					classes.addAll(focus.schedule1);
					h.getChildren().remove(classesList);
					h.add(classesList, 5, 1);
					s.setScene(pickClass);
				}catch(Exception d){
					Text idError=new Text("Please enter a valid Student ID");
					idError.setFont(oxygen30);
					g.add(idError, 3, 3,6,1);
				}
			});
			inID.setOnAction(e->{
				enterIDToPickClass.fire();
			});
			enterPWordToClose.setOnAction(e->{
				close.fire();
			});
			close.setOnAction(e->{
				if(enterPWordToClose.getText().equals("Password")){
					Platform.exit();
				}else{
					Text wrongPassword=new Text("Wrong password!");
					wrongPassword.setFont(oxygen30);
					k.add(wrongPassword, 3, 5);
				}
			});
			cancelClose.setOnAction(e->{
				inID.clear();
				s.setScene(enterID);
			});
			pickClassToOtherTeacher.setOnAction(e->{
				s.setScene(otherTeacher);
			});
			otherTeacherToPickClass.setOnAction(e->{
				s.setScene(pickClass);
			});
			pickClassToEnterID.setOnAction(e->{
				inID.clear();
				ID="";
				s.setScene(enterID);	
			});
			reasonsToOtherTeacher.setOnAction(e ->{
				s.setScene(pickClass);
			});
			submit.setOnAction(e->{
				//notify.sendEmail("");
				writeToDrive("");
				s.setScene(enterID);
				inID.clear();
			});
			listTeacher.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Teacher>(){
				public void changed(ObservableValue<? extends Teacher> arg0, Teacher old, Teacher newTeach) {
					notify=newTeach;
					s.setScene(pickReasons);
					submit.setDisable(true);
					listReasons.getSelectionModel().clearSelection();
				}
			});
			classesList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Class>(){
				public void changed(ObservableValue<? extends Class> arg0, Class old, Class newClass) {
					try{
					notify=newClass.getTeacher();
					}catch(Exception e){
					}
					System.out.println(notify);
					s.setScene(pickReasons);
					listReasons.getSelectionModel().clearSelection();
					submit.setDisable(true);
				}
			});
			listReasons.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>(){
				public void changed(ObservableValue<? extends String> arg0, String old, String newReason) {
					reason=newReason;
					submit.setDisable(false);
				}
			});
			s.setOnCloseRequest(e->{
				s.setScene(adminToClose);
				e.consume();
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