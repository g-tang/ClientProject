package application;
	
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

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
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;


public class Main extends Application {
	static ArrayList<Student> students=new ArrayList<Student>();
	ArrayList<Teacher> teachers=Teacher.getTeacherInfo(new File("TeacherEmails.txt"));
	ArrayList<String> reasons=new ArrayList<String>(Arrays.asList("Reason 1","Reason 2","Reason 3","Reason 4", "etc."));
	String ID="";
	Student focus;
	Teacher notify=new Teacher("","","");
	static Teacher administrator=new Teacher("","","");
	Teacher theoretical=new Teacher("","","");
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
			s.initStyle(StageStyle.UNDECORATED);
			s.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
			Platform.setImplicitExit(false);
			s.show();
			
			Date q=new Date();
			File log=new File(q.getMonth()+"_"+q.getDate()+"MCSignInLog");
			PrintWriter pw=new PrintWriter(log);
			
			//Enter Your Student ID Interface Elements
			GridPane enterIDGrid=new GridPane();
			enterIDGrid.setHgap(200);
			enterIDGrid.setVgap(100);
			Scene enterID=new Scene(enterIDGrid, s.getWidth(),s.getHeight());
			
			Text promptID=new Text("Enter your student ID number below \n or scan your card. Then click \"Next\"");
			promptID.setFont(oxygen50);
			enterIDGrid.add(promptID, 3, 2,6,1);
			promptID.setTextAlignment(TextAlignment.CENTER);
			
			TextField inID=new TextField();
			inID.setFont(oxygen30);
			inID.setPromptText("Enter your student ID #");
			enterIDGrid.add(inID, 2, 5, 7,1);
			
			Button enterIDToPickClass=new Button("Next");
			enterIDToPickClass.setFont(oxygen50);
			enterIDGrid.add(enterIDToPickClass, 4, 6,3,1);
			
			Text idError=new Text("Please enter a valid Student ID");
			idError.setFont(oxygen30);
			idError.setVisible(false);
			enterIDGrid.add(idError, 3, 3,6,1);
			
			Button terminate=new Button("Close");
			terminate.setFont(oxygen30);
			enterIDGrid.add(terminate, 9, 0);
			
			//Pick your class Elements
			GridPane pickClassGrid=new GridPane();
			Scene pickClass=new Scene(pickClassGrid, s.getWidth(), s.getHeight());
			pickClassGrid.setHgap(40);
			pickClassGrid.setVgap(20);
			
			Text promptPickClass=new Text();
			promptPickClass.setFont(oxygen50);
			

			Button pickClassToOtherTeacher=new Button("Other");
			pickClassToOtherTeacher.setFont(oxygen50);
			pickClassGrid.add(pickClassToOtherTeacher, 10, 1);
			
			Button pickClassToEnterID=new Button("Back");
			pickClassToEnterID.setFont(oxygen50);
			pickClassGrid.add(pickClassToEnterID,1,1);
			
			ObservableList<Class> classes=FXCollections.observableArrayList();
			ListView<Class> classesList=new ListView<Class>(classes);
			classesList.setMaxSize(s.getWidth()/2, s.getHeight()*0.8);
			classesList.setMinSize(s.getWidth()/2, s.getHeight()*0.8);
			pickClassGrid.getStylesheets().add(this.getClass().getResource("application.css").toExternalForm());
			
			//Other Teacher Elements
			GridPane otherTeacherGrid=new GridPane();
			Scene otherTeacher=new Scene(otherTeacherGrid, s.getWidth(), s.getHeight());
			otherTeacherGrid.setHgap(40);
			otherTeacherGrid.setVgap(20);
			Text promptPickTeacher=new Text("Select the teacher you are with:");
			promptPickTeacher.setFont(oxygen50);
			otherTeacherGrid.add(promptPickTeacher, 5, 0);
			
			Button otherTeacherToPickClass=new Button("Back");
			otherTeacherToPickClass.setFont(oxygen50);
			otherTeacherGrid.add(otherTeacherToPickClass, 2,1);
			
			ObservableList<Teacher> data=FXCollections.observableArrayList();
			ListView<Teacher> listTeacher=new ListView<Teacher>(data);
			listTeacher.setMaxSize(s.getWidth()/4, s.getHeight()*0.8);
			listTeacher.setMinSize(s.getWidth()/4, s.getHeight()*0.8);
			
			data.addAll(teachers);//move to main after parse
			otherTeacherGrid.add(listTeacher, 5, 1);
			
			otherTeacher.getStylesheets().add(this.getClass().getResource("application.css").toExternalForm());
			
			
			//Reasons Elements
			GridPane pickReasonsGrid=new GridPane();
			Scene pickReasons=new Scene(pickReasonsGrid, s.getWidth(), s.getHeight());
			pickReasonsGrid.setHgap(40);
			pickReasonsGrid.setVgap(20);
			
			Text promptReasons=new Text("Why have you visited?");
			promptReasons.setFont(oxygen50);
			pickReasonsGrid.add(promptReasons, 5, 0);
			
			ObservableList<String> reasonsData=FXCollections.observableArrayList();
			ListView<String> listReasons=new ListView<String>(reasonsData);
			listReasons.setMaxSize(s.getWidth()/3, s.getHeight()*0.8);
			listReasons.setMinSize(s.getWidth()/3, s.getHeight()*0.8);
			reasonsData.addAll(reasons);
			pickReasonsGrid.add(listReasons, 5, 1);
			
			pickReasons.getStylesheets().add(this.getClass().getResource("application.css").toExternalForm());
			
			Button reasonsToOtherTeacher=new Button("Back");
			reasonsToOtherTeacher.setFont(oxygen50);
			pickReasonsGrid.add(reasonsToOtherTeacher, 0, 1);
			
			Button submit=new Button("Submit");
			submit.setFont(oxygen50);
			pickReasonsGrid.add(submit, 10, 1);
			submit.setDisable(true);
			
			//Request Admin Password to Close
			
			
			GridPane adminToCloseGrid=new GridPane();
			Scene adminToClose=new Scene(adminToCloseGrid, s.getWidth(), s.getHeight());
			adminToCloseGrid.setHgap(200);
			adminToCloseGrid.setVgap(100);
			
			Text promptPassword=new Text("Enter password to close");
			promptPassword.setFont(oxygen50);
			adminToCloseGrid.add(promptPassword, 3, 2);
			
			PasswordField enterPWordToClose=new PasswordField();
			enterPWordToClose.setFont(oxygen30);
			enterPWordToClose.setPromptText("Enter password here");
			adminToCloseGrid.add(enterPWordToClose, 1,4,4,1);
			
			Button close=new Button("Ok");
			close.setFont(oxygen50);
			adminToCloseGrid.add(close, 4, 6);
			
			Button cancelClose=new Button("Cancel");
			cancelClose.setFont(oxygen50);
			adminToCloseGrid.add(cancelClose, 2, 6);
			
		//Listeners
			
			//Enter ID
			enterIDToPickClass.setOnAction(e->{
				try{
					ID=inID.getText();
					System.out.println(ID);
					focus=searchStudent(ID);
					promptPickClass.setText("Hello, "+focus.getFirstName()+". Please pick the class you are in right now.");
					pickClassGrid.getChildren().remove(promptPickClass);
					pickClassGrid.add(promptPickClass, 3, 0, 6,1);
					classes.clear();
					classes.addAll(focus.schedule1);
					pickClassGrid.getChildren().remove(classesList);
					pickClassGrid.add(classesList, 5, 1);
					s.setScene(pickClass);
				}catch(Exception d){
					idError.setVisible(true);
				}
			});
			inID.setOnAction(e->{
				enterIDToPickClass.fire();
			});
			
			//Pick Class
			pickClassToOtherTeacher.setOnAction(e->{
				s.setScene(otherTeacher);
			});
			pickClassToEnterID.setOnAction(e->{
				inID.clear();
				ID="";
				s.setScene(enterID);	
			});
			classesList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Class>(){
				public void changed(ObservableValue<? extends Class> arg0, Class old, Class newClass) {
					try{
					notify=newClass.getTeacher();
					}catch(Exception e){}
					s.setScene(pickReasons);
					listReasons.getSelectionModel().clearSelection();
					submit.setDisable(true);
				}
			});
			
			//Pick Other Teacher
			otherTeacherToPickClass.setOnAction(e->{
				s.setScene(pickClass);
			});
			listTeacher.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Teacher>(){
				public void changed(ObservableValue<? extends Teacher> arg0, Teacher old, Teacher newTeach) {
					notify=newTeach;
					s.setScene(pickReasons);
					submit.setDisable(true);
					listReasons.getSelectionModel().clearSelection();
				}
			});
			
			//Pick Reasons
			reasonsToOtherTeacher.setOnAction(e ->{
				s.setScene(pickClass);
			});
			listReasons.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>(){
				public void changed(ObservableValue<? extends String> arg0, String old, String newReason) {
					reason=newReason;
					submit.setDisable(false);
				}
			});
			submit.setOnAction(e->{
				try{
				theoretical=Student.getTheoreticalTeacher(focus.getSchedule(1));
				System.out.println("Notify: "+notify);
				System.out.println("Theoretical: "+theoretical);
				theoretical=theoretical.findTeacher(teachers);
				notify=notify.findTeacher(teachers);
				}catch(Exception f){}
				if(theoretical!=null){
					if(theoretical.toString().equals(notify.toString())){
						System.out.println("Send normal email to "+notify);
		//					notify.sendEmail(new File("NormalEmail"), theoretical, notify, focus);
					}else{
						System.out.println("Send mismatch case emails to "+notify+" and "+theoretical);
		//					notify.sendEmail(new File("SignedPassEmail"), theoretical, notify, focus);
		//					theoretical.sendEmail(new File("TheoreticalEmail"), theoretical, notify, focus);
					}
				}else{
					System.out.println("Send Normal email to "+notify);
					//					notify.sendEmail(new File("NormalEmail"), theoretical, notify, focus);
				}
				System.out.println("Send admin email");
				pw.println(focus.getFullName()+"\t"+focus.getID()+"\t"+Teacher.dateToString(false)+"\t"+reason);
//				administrator.sendEmail(new File("AdminEmail"), theoretical, notify, focus);
				writeToDrive("");
				s.setScene(enterID);
				idError.setVisible(false);
				inID.clear();
			});
			//Close Program
			enterPWordToClose.setOnAction(e->{
				close.fire();
			});
			s.setOnCloseRequest(e->{
				e.consume();
				s.setScene(adminToClose);
			});
			close.setOnAction(e->{
				if(enterPWordToClose.getText().equals("Password")){
					try{
					pw.close();
					log.renameTo(new File("F:\\"+log.getName()));
					log.delete();
					Platform.exit();
					}catch(Exception d){
						
					}
				}else{
					Text wrongPassword=new Text("Wrong password!");
					wrongPassword.setFont(oxygen30);
					adminToCloseGrid.add(wrongPassword, 3, 5);
				}
			});
			cancelClose.setOnAction(e->{
				inID.clear();
				s.setScene(enterID);
			});
			terminate.setOnAction(e->{
				s.setScene(adminToClose);
			});
			s.setScene(enterID);
			
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	public static Teacher getAdmin(){
		return administrator;
	}
	public static void parseData(){
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
		parseData();
		launch(args);
	}
}