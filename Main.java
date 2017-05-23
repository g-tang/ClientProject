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
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;


public class Main extends Application {
	static ArrayList<Student> students=new ArrayList<Student>();
	static ArrayList<Teacher> teachers=Teacher.getTeacherInfo(new File("TeacherEmails.txt"));
	static ArrayList<String> reasons=new ArrayList<String>();
	static String courseFile="";
	static String teacherFile="";
	static String reasonFile="";
	String ID="";
	Student focus;
	static String password="";
	static String fromEmail="";
	static String fromEmailPassword="";
	static int semester=0;
	Teacher notify=new Teacher("","","");
	static Teacher administrator=null;
	Teacher theoretical=new Teacher("","","");
	String reason="";
	Font oxygen30=new Font("Oxygen",30);
	Font oxygen50=new Font("Oxygen", 50);
	boolean toClose=false;
	public static void setConfig(){
		try {
			Scanner s=new Scanner(new File("config"));
			password=s.nextLine();
			administrator=new Teacher("","",s.nextLine());
			fromEmail=s.nextLine();
			fromEmailPassword=s.nextLine();
			semester=Integer.parseInt(s.nextLine());
			courseFile=s.nextLine();
			teachers=Teacher.getTeacherInfo(new File(s.nextLine()));
			reasonFile=s.nextLine();
			Scanner q=new Scanner(new File(reasonFile));
			while(q.hasNextLine()){
				reasons.add(q.nextLine());
			}
		} catch (FileNotFoundException e) {
			// show settings screen
			e.printStackTrace();
		}
	}
	public void start(Stage s) {
		try {
			s.setMaximized(true);
			s.setTitle("Media Center Sign In");
			//s.setAlwaysOnTop(true);
			s.setFullScreen(true);
			s.setResizable(false);
			s.initStyle(StageStyle.UNDECORATED);
			s.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
			Platform.setImplicitExit(false);
			s.show();
			System.out.println(s.getWidth());
			System.out.println(s.getHeight());
			Date q=new Date();
			File log=new File(q.getMonth()+"_"+q.getDate()+"MCSignInLog");
			PrintWriter pw=new PrintWriter(log);
			
			//Enter Your Student ID Interface Elements
			Pane enterIDGrid=new Pane();
			Scene enterID=new Scene(enterIDGrid, s.getWidth(),s.getHeight());
			
			Text promptID=new Text("Enter your student ID number below \n or scan your card. Then click \"Next\"");
			promptID.setFont(oxygen50);
			promptID.setX(500);
			promptID.setY(200);
			enterIDGrid.getChildren().add(promptID);
			promptID.setTextAlignment(TextAlignment.CENTER);
			
			TextField inID=new TextField();
			inID.setFont(oxygen30);
			inID.setPromptText("Enter your student ID #");
			inID.setLayoutX(500);
			inID.setLayoutY(500);
			inID.setMinSize(800, 20);
			enterIDGrid.getChildren().add(inID);
			
			Button enterIDToPickClass=new Button("Next");
			enterIDToPickClass.setFont(oxygen50);
			enterIDToPickClass.setLayoutX(500);
			enterIDToPickClass.setLayoutY(900);
			enterIDGrid.getChildren().add(enterIDToPickClass);
			
			Text idError=new Text("Please enter a valid Student ID");
			idError.setFont(oxygen30);
			idError.setVisible(false);
			idError.setX(500);
			idError.setY(700);
			enterIDGrid.getChildren().add(idError);
			
			Button terminate=new Button("Close");
			terminate.setLayoutX(1800);
			terminate.setLayoutY(0);
			terminate.setFont(oxygen30);
			enterIDGrid.getChildren().add(terminate);
			
			Button enterIDToSettings=new Button("Settings");
			enterIDToSettings.setLayoutX(1600);
			enterIDToSettings.setLayoutY(0);
			enterIDToSettings.setFont(oxygen30);
			enterIDGrid.getChildren().add(enterIDToSettings);
			
			//Pick your class Elements
			Pane pickClassGrid=new Pane();
			Scene pickClass=new Scene(pickClassGrid, s.getWidth(), s.getHeight());
			
			Text promptPickClass=new Text();
			promptPickClass.setFont(oxygen50);
			promptPickClass.setX(500);
			promptPickClass.setY(100);

			Button pickClassToOtherTeacher=new Button("Other");
			pickClassToOtherTeacher.setFont(oxygen50);
			pickClassToOtherTeacher.setLayoutX(1500);
			pickClassToOtherTeacher.setLayoutY(500);
			pickClassGrid.getChildren().add(pickClassToOtherTeacher);
			
			Button pickClassToEnterID=new Button("Back");
			pickClassToEnterID.setFont(oxygen50);
			pickClassToEnterID.setLayoutX(100);
			pickClassToEnterID.setLayoutY(500);
			pickClassGrid.getChildren().add(pickClassToEnterID);
			
			ObservableList<Class> classes=FXCollections.observableArrayList();
			ListView<Class> classesList=new ListView<Class>(classes);
			classesList.setMaxSize(s.getWidth()/2, s.getHeight()*0.8);
			classesList.setMinSize(s.getWidth()/2, s.getHeight()*0.8);
			classesList.setLayoutX(500);
			classesList.setLayoutY(200);
			pickClassGrid.getStylesheets().add(this.getClass().getResource("application.css").toExternalForm());
			
			//Other Teacher Elements
			Pane otherTeacherGrid=new Pane();
			Scene otherTeacher=new Scene(otherTeacherGrid, s.getWidth(), s.getHeight());
			
			Text promptPickTeacher=new Text("Select the teacher you are with:");
			promptPickTeacher.setFont(oxygen50);
			promptPickTeacher.setX(200);
			promptPickTeacher.setY(100);
			otherTeacherGrid.getChildren().add(promptPickTeacher);
			
			Button otherTeacherToPickClass=new Button("Back");
			otherTeacherToPickClass.setFont(oxygen50);
			otherTeacherToPickClass.setLayoutX(800);
			otherTeacherToPickClass.setLayoutY(300);
			otherTeacherGrid.getChildren().add(otherTeacherToPickClass);
			
			ObservableList<Teacher> data=FXCollections.observableArrayList();
			ListView<Teacher> listTeacher=new ListView<Teacher>(data);
			listTeacher.setMaxSize(s.getWidth()/4, s.getHeight()*0.8);
			listTeacher.setMinSize(s.getWidth()/4, s.getHeight()*0.8);
			listTeacher.setLayoutX(200);
			listTeacher.setLayoutY(200);
			
			data.addAll(teachers);//move to main after parse
			otherTeacherGrid.getChildren().add(listTeacher);
			
			otherTeacher.getStylesheets().add(this.getClass().getResource("application.css").toExternalForm());
			
			
			//Reasons Elements
			Pane pickReasonsGrid=new Pane();
			Scene pickReasons=new Scene(pickReasonsGrid, s.getWidth(), s.getHeight());
			
			Text promptReasons=new Text("Why have you visited?");
			promptReasons.setFont(oxygen50);
			promptReasons.setX(100);
			promptReasons.setY(100);
			pickReasonsGrid.getChildren().add(promptReasons);
			
			ObservableList<String> reasonsData=FXCollections.observableArrayList();
			ListView<String> listReasons=new ListView<String>(reasonsData);
			listReasons.setMaxSize(s.getWidth()/3, s.getHeight()*0.8);
			listReasons.setMinSize(s.getWidth()/3, s.getHeight()*0.8);
			reasonsData.addAll(reasons);
			listReasons.setLayoutX(100);
			listReasons.setLayoutY(200);
			pickReasonsGrid.getChildren().add(listReasons);
			
			pickReasons.getStylesheets().add(this.getClass().getResource("application.css").toExternalForm());
			
			Button reasonsToOtherTeacher=new Button("Back");
			reasonsToOtherTeacher.setFont(oxygen50);
			reasonsToOtherTeacher.setLayoutX(800);
			reasonsToOtherTeacher.setLayoutY(300);
			pickReasonsGrid.getChildren().add(reasonsToOtherTeacher);
			
			Button submit=new Button("Submit");
			submit.setFont(oxygen50);
			submit.setLayoutX(800);
			submit.setLayoutY(500);
			pickReasonsGrid.getChildren().add(submit);
			submit.setDisable(true);
			
			//Request Admin Password to Close
			
			
			Pane adminToCloseGrid=new Pane();
			Scene adminToClose=new Scene(adminToCloseGrid, s.getWidth(), s.getHeight());
			
			Text promptPassword=new Text("Please enter the password");
			promptPassword.setFont(oxygen50);
			promptPassword.setX(500);
			promptPassword.setY(200);
			adminToCloseGrid.getChildren().add(promptPassword);
			
			PasswordField enterPWordToClose=new PasswordField();
			enterPWordToClose.setFont(oxygen30);
			enterPWordToClose.setPromptText("Enter password here");
			enterPWordToClose.setLayoutX(200);
			enterPWordToClose.setLayoutY(400);
			enterPWordToClose.setMinSize(1000, 100);
			adminToCloseGrid.getChildren().add(enterPWordToClose);
			
			Button close=new Button("Ok");
			close.setFont(oxygen50);
			close.setLayoutX(800);
			close.setLayoutY(800);
			adminToCloseGrid.getChildren().add(close);
			
			Button cancelClose=new Button("Cancel");
			cancelClose.setFont(oxygen50);
			cancelClose.setLayoutX(500);
			cancelClose.setLayoutY(800);
			adminToCloseGrid.getChildren().add(cancelClose);
			
			Text wrongPassword=new Text("Wrong password!");
			wrongPassword.setFont(oxygen30);
			wrongPassword.setX(500);
			wrongPassword.setY(700);
			adminToCloseGrid.getChildren().add(wrongPassword);
			wrongPassword.setVisible(false);
			
			
			//Settings
			Pane settingsGrid=new Pane();
			Scene settings=new Scene(settingsGrid, s.getWidth(), s.getHeight());
			
			Text promptChangePassword=new Text("Change Password");
			promptChangePassword.setFont(oxygen30);
			promptChangePassword.setX(200);
			promptChangePassword.setY(100);
			settingsGrid.getChildren().add(promptChangePassword);
			
			PasswordField newPassword1=new PasswordField();
			newPassword1.setFont(oxygen30);
			newPassword1.setPromptText("Enter your new password");
			newPassword1.setMinSize(300, 100);
			newPassword1.setLayoutX(200);
			newPassword1.setLayoutY(300);
			settingsGrid.getChildren().add(newPassword1);
			
			PasswordField newPassword2=new PasswordField();
			newPassword2.setFont(oxygen30);
			newPassword2.setPromptText("Re-enter your password");
			newPassword2.setMinSize(300, 100);
			newPassword2.setLayoutX(200);
			newPassword2.setLayoutY(500);
			settingsGrid.getChildren().add(newPassword2);
			
			Button goToBellSchedule=new Button("Bell Schedule");
			goToBellSchedule.setFont(oxygen30);
			goToBellSchedule.setLayoutX(200);
			goToBellSchedule.setLayoutY(700);
			settingsGrid.getChildren().add(goToBellSchedule);
			
			TextField enterAdminEmail=new TextField();
			enterAdminEmail.setPromptText("Enter your administrator's email address");
			enterAdminEmail.setFont(oxygen30);
			enterAdminEmail.setMinSize(300, 100);
			enterAdminEmail.setLayoutX(200);
			enterAdminEmail.setLayoutY(900);
			settingsGrid.getChildren().add(enterAdminEmail);
			
			ObservableList<String> semesters=FXCollections.observableArrayList("Semester 1", "Semester 2");
			ComboBox<String> pickSemester=new ComboBox<String>(semesters);
			if(semester==1){
				pickSemester.setValue(semesters.get(0));
			}else{pickSemester.setValue(semesters.get(1));}
			pickSemester.setLayoutX(800);
			pickSemester.setLayoutY(100);
			settingsGrid.getChildren().add(pickSemester);
			
			Button goToEmailTemps=new Button("Edit Email Templates");
			goToEmailTemps.setFont(oxygen30);
			goToEmailTemps.setLayoutX(800);
			goToEmailTemps.setLayoutY(300);
			settingsGrid.getChildren().add(goToEmailTemps);
			
			File myFiles=new File(".");
			ObservableList<File> files=FXCollections.observableArrayList(myFiles.listFiles());
			
			ComboBox<File> pickClassSource=new ComboBox<File>(files);
			pickClassSource.setValue(new File(courseFile));
			pickClassSource.setLayoutX(800);
			pickClassSource.setLayoutY(500);
			settingsGrid.getChildren().add(pickClassSource);
			
			ComboBox<File> pickTeacherSource=new ComboBox<File>(files);
			pickTeacherSource.setValue(new File(teacherFile));
			pickTeacherSource.setLayoutX(800);
			pickTeacherSource.setLayoutY(700);
			settingsGrid.getChildren().add(pickTeacherSource);
			
			ComboBox<File> pickReasonSource=new ComboBox<File>(files);
			pickReasonSource.setValue(new File(reasonFile));
			pickReasonSource.setLayoutX(800);
			pickReasonSource.setLayoutY(900);
			settingsGrid.getChildren().add(pickReasonSource);
			
			Button settingsToEnterID=new Button("Back");
			settingsToEnterID.setFont(oxygen50);
			settingsToEnterID.setLayoutX(1200);
			settingsToEnterID.setLayoutY(400);
			settingsGrid.getChildren().add(settingsToEnterID);
			
			settingsGrid.getStylesheets().add(this.getClass().getResource("application.css").toExternalForm());
			
		//Listeners
			
			//Enter ID
			enterIDToPickClass.setOnAction(e->{
				try{
					ID=inID.getText();
					System.out.println(ID);
					focus=searchStudent(ID);
					promptPickClass.setText("Hello, "+focus.getFirstName()+". Please pick the class you are in right now.");
					pickClassGrid.getChildren().remove(promptPickClass);
					pickClassGrid.getChildren().add(promptPickClass);
					classes.clear();
					classes.addAll(focus.schedule1);
					pickClassGrid.getChildren().remove(classesList);
					pickClassGrid.getChildren().add(classesList);
					s.setScene(pickClass);
					idError.setVisible(false);
				}catch(Exception d){
					idError.setVisible(true);
				}
			});
			inID.setOnAction(e->{
				enterIDToPickClass.fire();
			});
			enterIDToSettings.setOnAction(e->{
				toClose=false;
				s.setScene(adminToClose);
			});
			//Pick Class
			pickClassToOtherTeacher.setOnAction(e->{
				try{listTeacher.getSelectionModel().clearSelection();}catch(Exception f){}
				s.setScene(otherTeacher);
			});
			pickClassToEnterID.setOnAction(e->{
				inID.clear();
				ID="";
				idError.setVisible(false);
				s.setScene(enterID);	
			});
			classesList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Class>(){
				public void changed(ObservableValue<? extends Class> arg0, Class old, Class newClass) {
					try{
					notify=newClass.getTeacher();
					}catch(Exception e){}
					s.setScene(pickReasons);
					try{listReasons.getSelectionModel().clearSelection();}catch(Exception e){}
					submit.setDisable(true);
				}
			});
			
			//Pick Other Teacher
			otherTeacherToPickClass.setOnAction(e->{
				enterIDToPickClass.fire();
			});
			listTeacher.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Teacher>(){
				public void changed(ObservableValue<? extends Teacher> arg0, Teacher old, Teacher newTeach) {
					notify=newTeach;
					s.setScene(pickReasons);
					submit.setDisable(true);
					try{listReasons.getSelectionModel().clearSelection();}catch(Exception e){}
				}
			});
			
			//Pick Reasons
			reasonsToOtherTeacher.setOnAction(e ->{
				enterIDToPickClass.fire();
			});
			listReasons.setOnKeyPressed(e->{e.consume();});
			listReasons.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>(){
				public void changed(ObservableValue<? extends String> arg0, String old, String newReason) {
					reason=newReason;
					//try{listReasons.getSelectionModel().clearSelection();}catch(Exception e){}
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
					//		notify.sendEmail(new File("NormalEmail"), theoretical, notify, focus);
				}
				System.out.println("Send admin email");
				pw.println(focus.getFullName()+"\t"+focus.getID()+"\t"+Teacher.dateToString(false)+"\t"+reason);
		//		administrator.sendEmail(new File("AdminEmail"), theoretical, notify, focus);
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
				toClose=true;
				s.setScene(adminToClose);
				enterPWordToClose.clear();
				wrongPassword.setVisible(false);
			});
			close.setOnAction(e->{
				wrongPassword.setVisible(false);
				if(enterPWordToClose.getText().equals("Password")){
					if(toClose){
						try{
							pw.close();
							log.renameTo(new File("F:\\"+log.getName()));
							log.delete();
							Platform.exit();
						}catch(Exception d){

						}}else{s.setScene(settings);}
						}else{
							enterPWordToClose.clear();
							wrongPassword.setVisible(true);
						}
				});
			cancelClose.setOnAction(e->{
				inID.clear();
				enterPWordToClose.clear();
				wrongPassword.setVisible(false);
				s.setScene(enterID);
			});
			terminate.setOnAction(e->{
				wrongPassword.setVisible(false);
				enterPWordToClose.clear();
				toClose=true;
				s.setScene(adminToClose);
			});
			s.setScene(enterID);

			//Settings
			settingsToEnterID.setOnAction(e->{
				s.setScene(enterID);
			});
			}catch(Exception e) {
				e.printStackTrace();
			}
	}
	public static Teacher getAdmin(){
		return administrator;
	}
	public static void parseData(String file){
		Scanner s = null;
		String id="";
		String [] quals;
		String in="";
		try {
			s=new Scanner(new File(file));
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
		setConfig();
		parseData(courseFile);
		launch(args);
	}
}