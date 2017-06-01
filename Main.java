package application;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

import javax.mail.MessagingException;

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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;


public class Main extends Application {
	static ArrayList<Student> students=new ArrayList<Student>();
	static ArrayList<Teacher> teachers=new ArrayList<Teacher>();
	static ArrayList<String> reasons=new ArrayList<String>();
	static String courseFile="";
	static String teacherFile="";
	static String reasonFile="";
	String ID="";
	Student focus;
	static String password="";
	static String fromEmail="smcs2019.ssmg@gmail.com";
	static String fromEmailPassword="";
	static int semester=0;
	Teacher notify=new Teacher("","","");
	static Teacher administrator=new Teacher("","","");
	Teacher theoretical=new Teacher("","","");
	String reason="";
	Font oxygen50=new Font("Oxygen", 50);
	boolean toClose=false;
	static boolean validInit=true;
	static Scene settings;
	static PrintWriter backUp=null;
	static String bell="";
	public static void setConfig(){
		try {
			Scanner s=new Scanner(new File("DoNotTouch/config"));
			System.out.println(new File("DoNotTouch/config").getAbsolutePath());
			password=s.nextLine();
			administrator=new Teacher("","",s.nextLine());
			fromEmailPassword=s.nextLine();
			EmailUtil.setFrom(fromEmail, fromEmailPassword);
			semester=Integer.parseInt(s.nextLine());
			courseFile=s.nextLine();
			parseData(courseFile);
			teacherFile=s.nextLine();
			Teacher.parseTeacher(new File(teacherFile));
			teachers=Teacher.getTeacherInfo(new File(teacherFile));
			reasonFile=s.nextLine();
			Scanner q=new Scanner(new File(reasonFile));
			reasons=new ArrayList<String>();
			while(q.hasNextLine()){
				reasons.add(q.nextLine());
			}
			bell=s.nextLine();
		} catch (Exception e) {
			e.printStackTrace();
			// show settings screen
			validInit=false;
		}
		System.out.println("complete");
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
			pText.setDims(s.getWidth(), s.getHeight());
			File backUpFile=new File(new Date().getMonth()+"_"+new Date().getDate()+"_MCLog");
			backUp=new PrintWriter(backUpFile);
			System.out.println(s.getWidth());
			System.out.println(s.getHeight());

			//Settings
			Pane settingsGrid=new Pane();
			settings=new Scene(settingsGrid, s.getWidth(), s.getHeight());

			pText promptChangePassword=new pText("Change Password:",200,200);
			settingsGrid.getChildren().add(promptChangePassword);

			pText promptNewPWord1=new pText("Enter new password below:",200,250);
			settingsGrid.getChildren().add(promptNewPWord1);

			PasswordField newPassword1=new PasswordField();
			newPassword1.setPromptText("Enter your new password");
			newPassword1.setMinSize(300, 50);
			newPassword1.setLayoutX(200);newPassword1.setLayoutY(300);
			settingsGrid.getChildren().add(newPassword1);

			PasswordField newPassword2=new PasswordField();
			newPassword2.setPromptText("Re-enter your password");
			newPassword2.setMinSize(300, 50);
			newPassword2.setLayoutX(200);newPassword2.setLayoutY(400);
			settingsGrid.getChildren().add(newPassword2);

			pText promptBell=new pText("Select a bell schedule",200,500);
			settingsGrid.getChildren().add(promptBell);

			ObservableList<String> bells=FXCollections.observableArrayList("Normal", "2 hour delay", "Half day","Custom");
			ComboBox<String> pickBell=new ComboBox<String>(bells);
			try{pickBell.setValue(bells.get(bells.indexOf(bell)));}catch(Exception e){}
			pickBell.setLayoutX(200);pickBell.setLayoutY(530);
			settingsGrid.getChildren().add(pickBell);

			pText promptAdminEmail=new pText("Enter your administrator's email address:",200,640);
			settingsGrid.getChildren().add(promptAdminEmail);

			TextField enterAdminEmail=new TextField(administrator.getEmail());
			enterAdminEmail.setPromptText("Administrator Email");
			enterAdminEmail.setMinSize(300, 50);
			enterAdminEmail.setLayoutX(200);enterAdminEmail.setLayoutY(670);
			settingsGrid.getChildren().add(enterAdminEmail);

			pText promptSem=new pText("Select current semester:",200,800);
			settingsGrid.getChildren().add(promptSem);

			ObservableList<String> semesters=FXCollections.observableArrayList("Semester 1", "Semester 2");
			ComboBox<String> pickSemester=new ComboBox<String>(semesters);
			if(semester==1){
				pickSemester.setValue(semesters.get(0));
			}else{pickSemester.setValue(semesters.get(1));}
			pickSemester.setLayoutX(200);pickSemester.setLayoutY(830);
			settingsGrid.getChildren().add(pickSemester);

			pText pWordError=new pText("Passwords are mismatched!",200,100);
			pWordError.setVisible(false);
			settingsGrid.getChildren().add(pWordError);

			pText adEmailError=new pText("Invalid admin email!",200,850);
			adEmailError.setVisible(false);
			settingsGrid.getChildren().add(adEmailError);

			Button goToEmailTemps=new Button("Edit Email Templates");
			goToEmailTemps.setLayoutX(900);goToEmailTemps.setLayoutY(100);
			settingsGrid.getChildren().add(goToEmailTemps);

			File myFiles=new File(".");//TODO
			ObservableList<File> Files=FXCollections.observableArrayList(myFiles.listFiles());


			pText promptCourse=new pText("Select the student schedule file:",900,225);
			settingsGrid.getChildren().add(promptCourse);

			ComboBox<File> pickClassSource=new ComboBox<File>(Files);
			try{pickClassSource.setValue(Files.get(Files.indexOf(new File(courseFile))));}catch(Exception e){e.printStackTrace();}
			pickClassSource.setLayoutX(900);pickClassSource.setLayoutY(250);
			settingsGrid.getChildren().add(pickClassSource);

			pText promptTeacher=new pText("Select the teacher email file:",900,375);
			settingsGrid.getChildren().add(promptTeacher);


			ComboBox<File> pickTeacherSource=new ComboBox<File>(Files);
			try{pickTeacherSource.setValue(Files.get(Files.indexOf(new File(teacherFile))));}catch(Exception e){e.printStackTrace();}
			pickTeacherSource.setLayoutX(900);pickTeacherSource.setLayoutY(400);
			settingsGrid.getChildren().add(pickTeacherSource);

			pText promptReason=new pText("Select the visit reasons file:",900,525);
			settingsGrid.getChildren().add(promptReason);

			ComboBox<File> pickReasonSource=new ComboBox<File>(Files);
			try{pickReasonSource.setValue(Files.get(Files.indexOf(new File(reasonFile))));}catch(Exception e){e.printStackTrace();}
			pickReasonSource.setLayoutX(900);pickReasonSource.setLayoutY(550);
			settingsGrid.getChildren().add(pickReasonSource);

			pText promptBaseEmail=new pText("Enter the password of the \nnotification email account:",900,700);
			settingsGrid.getChildren().add(promptBaseEmail);

			PasswordField enterBaseEmailPWord=new PasswordField();
			enterBaseEmailPWord.setPromptText("Password");
			enterBaseEmailPWord.setMinSize(300, 50);
			enterBaseEmailPWord.setLayoutX(900);enterBaseEmailPWord.setLayoutY(750);
			settingsGrid.getChildren().add(enterBaseEmailPWord);

			pText baseError=new pText("Invalid notification address/password",900,850);
			baseError.setVisible(false);
			settingsGrid.getChildren().add(baseError);

			pText FileError=new pText("Invalid source File!",1400,200);
			FileError.setVisible(false);
			settingsGrid.getChildren().add(FileError);

			Button settingsToEnterID=new Button("Back");
			settingsToEnterID.setFont(oxygen50);
			settingsToEnterID.setLayoutX(1400);settingsToEnterID.setLayoutY(400);
			settingsGrid.getChildren().add(settingsToEnterID);

			Button submitSettings=new Button("Save");
			submitSettings.setFont(oxygen50);
			submitSettings.setLayoutX(1400);submitSettings.setLayoutY(600);
			settingsGrid.getChildren().add(submitSettings);

			settingsGrid.getStylesheets().add(this.getClass().getResource("application.css").toExternalForm());

			//Bell Schedule Editor
			Pane bellScheduleGrid=new Pane();
			Scene bellSchedule=new Scene(bellScheduleGrid,s.getWidth(),s.getHeight());

			ArrayList<TextField> hours=new ArrayList<TextField>();
			ArrayList<TextField> minutes=new ArrayList<TextField>();
			ArrayList<pText> colon=new ArrayList<pText>();
			ArrayList<String[]> bellSched=Student.parseBellSchedule(bell);
			for(int i=0;i<8;i++){
				try{
					hours.add(new TextField(bellSched.get(i)[0]));
					minutes.add(new TextField(bellSched.get(i)[1]));
					colon.add(new pText(":",950,225+(i*75)));
				}catch(Exception e){
					e.printStackTrace();
					hours.add(new TextField());
					minutes.add(new TextField());
					colon.add(new pText(":",950,225+(i*75)));
				}
				hours.get(i).setMinSize(100, 50);hours.get(i).setMaxSize(100, 50);
				hours.get(i).setLayoutX(800);hours.get(i).setLayoutY(200+(i*75));
				minutes.get(i).setMinSize(100, 50);minutes.get(i).setMaxSize(100, 50);
				minutes.get(i).setLayoutX(1000);minutes.get(i).setLayoutY(200+(i*75));
			}
			bellScheduleGrid.getChildren().addAll(colon);
			bellScheduleGrid.getChildren().addAll(hours);
			bellScheduleGrid.getChildren().addAll(minutes);

			Button saveBellSchedule=new Button("Save");
			saveBellSchedule.setFont(oxygen50);
			saveBellSchedule.setLayoutX(880);saveBellSchedule.setLayoutY(900);
			bellScheduleGrid.getChildren().add(saveBellSchedule);

			pText bellError=new pText("Please enter an integer from 0-24 or 0-60.",600,850);
			bellScheduleGrid.getChildren().add(bellError);
			bellError.setVisible(false);

			pText bellInst=new pText("Please enter the times at which each period ends in 24-hour format.\nIf there are not 8 periods, leave extra spaces at the end blank.",600,100);
			bellScheduleGrid.getChildren().add(bellInst);

			bellScheduleGrid.getStylesheets().add(this.getClass().getResource("application.css").toExternalForm());


			//Edit Email Files
			Pane editTemplateGrid=new Pane();
			Scene editTemplate=new Scene(editTemplateGrid, s.getWidth(),s.getHeight());
			pText editTemplateInstructions=new pText("    Key: <THEORETICAL>-name of predicted teacher   <TIME>-date and time\n<SIGNEDPASS>-name of selected teacher    <STUDENTNAME>-name of student",500,100);
			editTemplateGrid.getChildren().add(editTemplateInstructions);

			pText promptPredicted=new pText("To predicted teacher if student does not select him/her:",100,220);
			editTemplateGrid.getChildren().add(promptPredicted);

			TextArea predicted=new TextArea(Teacher.fileToString(new File("TheoreticalEmail")));
			predicted.setMaxSize(800, 300);predicted.setMinSize(800, 300);
			predicted.setWrapText(true);
			predicted.setLayoutX(100);
			predicted.setLayoutY(250);
			editTemplateGrid.getChildren().add(predicted);

			pText promptNormal=new pText("To predicted teacher if student selects him/her:",100,620);
			editTemplateGrid.getChildren().add(promptNormal);

			TextArea normal=new TextArea(Teacher.fileToString(new File("NormalEmail")));
			normal.setMaxSize(800, 300);normal.setMinSize(800, 300);
			normal.setWrapText(true);
			normal.setLayoutX(100);normal.setLayoutY(650);
			editTemplateGrid.getChildren().add(normal);

			pText promptSignedPass=new pText("To selected teacher if they are not predicted teacher:",1000,220);
			editTemplateGrid.getChildren().add(promptSignedPass);

			TextArea signedPass=new TextArea(Teacher.fileToString(new File("SignedPassEmail")));
			signedPass.setMaxSize(800, 300);signedPass.setMinSize(800, 300);
			signedPass.setWrapText(true);
			signedPass.setLayoutX(1000);signedPass.setLayoutY(250);
			editTemplateGrid.getChildren().add(signedPass);

			pText promptAdmin=new pText("To the administrator:",1000,620);
			editTemplateGrid.getChildren().add(promptAdmin);

			TextArea admin=new TextArea(Teacher.fileToString(new File("AdminEmail")));
			admin.setMaxSize(800, 300);admin.setMinSize(800, 300);
			admin.setWrapText(true);
			admin.setLayoutX(1000);admin.setLayoutY(650);
			editTemplateGrid.getChildren().add(admin);

			Button editTemplateToSettings=new Button("Ok");
			editTemplateToSettings.setFont(oxygen50);
			editTemplateToSettings.setLayoutX(900);editTemplateToSettings.setLayoutY(960);
			editTemplateGrid.getChildren().add(editTemplateToSettings);

			editTemplateGrid.getStylesheets().add(this.getClass().getResource("application.css").toExternalForm());

			//Enter Your Student ID Interface Elements
			Pane enterIDGrid=new Pane();
			Scene enterID=new Scene(enterIDGrid, s.getWidth(),s.getHeight());

			pText promptID=new pText("Enter your student ID number below \n or scan your card. Then click \"Next\"",500,300);
			promptID.setFont(oxygen50);
			enterIDGrid.getChildren().add(promptID);
			promptID.setTextAlignment(TextAlignment.CENTER);

			TextField inID=new TextField();
			inID.setPromptText("Enter your student ID #");
			inID.setLayoutX(500);inID.setLayoutY(500);
			inID.setMinSize(800, 20);
			enterIDGrid.getChildren().add(inID);

			Button enterIDToPickClass=new Button("Next");
			enterIDToPickClass.setFont(oxygen50);
			enterIDToPickClass.setLayoutX(800);enterIDToPickClass.setLayoutY(750);
			enterIDGrid.getChildren().add(enterIDToPickClass);

			pText idError=new pText("Please enter a valid Student ID",500,700);
			idError.setVisible(false);
			enterIDGrid.getChildren().add(idError);

			Button terminate=new Button("Close");
			terminate.setLayoutX(1750);terminate.setLayoutY(50);
			enterIDGrid.getChildren().add(terminate);

			Button enterIDToSettings=new Button("Settings");
			enterIDToSettings.setLayoutX(1550);enterIDToSettings.setLayoutY(50);
			enterIDGrid.getChildren().add(enterIDToSettings);

			enterIDGrid.getStylesheets().add(this.getClass().getResource("application.css").toExternalForm());


			//Pick your class Elements
			Pane pickClassGrid=new Pane();
			Scene pickClass=new Scene(pickClassGrid, s.getWidth(), s.getHeight());

			pText promptPickClass=new pText("",300,200);
			promptPickClass.setFont(oxygen50);

			Button pickClassToOtherTeacher=new Button("Other");
			pickClassToOtherTeacher.setFont(oxygen50);
			pickClassToOtherTeacher.setLayoutX(1500);pickClassToOtherTeacher.setLayoutY(500);
			pickClassGrid.getChildren().add(pickClassToOtherTeacher);

			Button pickClassToEnterID=new Button("Back");
			pickClassToEnterID.setFont(oxygen50);
			pickClassToEnterID.setLayoutX(100);pickClassToEnterID.setLayoutY(500);
			pickClassGrid.getChildren().add(pickClassToEnterID);

			ObservableList<Class> classes=FXCollections.observableArrayList();
			ListView<Class> classesList=new ListView<Class>(classes);
			classesList.setMaxSize(700, 500);classesList.setMinSize(700, 500);
			classesList.setLayoutX(500);classesList.setLayoutY(300);
			pickClassGrid.getStylesheets().add(this.getClass().getResource("application.css").toExternalForm());

			//Other Teacher Elements
			Pane otherTeacherGrid=new Pane();
			Scene otherTeacher=new Scene(otherTeacherGrid, s.getWidth(), s.getHeight());

			pText promptPickTeacher=new pText("Select the teacher you are with:",650,100);
			promptPickTeacher.setFont(oxygen50);
			otherTeacherGrid.getChildren().add(promptPickTeacher);

			Button otherTeacherToPickClass=new Button("Back");
			otherTeacherToPickClass.setFont(oxygen50);
			otherTeacherToPickClass.setLayoutX(400);otherTeacherToPickClass.setLayoutY(500);
			otherTeacherGrid.getChildren().add(otherTeacherToPickClass);

			ObservableList<Teacher> data=FXCollections.observableArrayList();
			ListView<Teacher> listTeacher=new ListView<Teacher>(data);
			listTeacher.setMaxSize(500, 700);listTeacher.setMinSize(500, 700);
			listTeacher.setLayoutX(750);listTeacher.setLayoutY(200);

			data.addAll(teachers);//move to main after parse
			otherTeacherGrid.getChildren().add(listTeacher);

			otherTeacher.getStylesheets().add(this.getClass().getResource("application.css").toExternalForm());


			//Reasons Elements
			Pane pickReasonsGrid=new Pane();
			Scene pickReasons=new Scene(pickReasonsGrid, s.getWidth(), s.getHeight());

			pText promptReasons=new pText("Why have you visited?",750,100);
			promptReasons.setFont(oxygen50);
			pickReasonsGrid.getChildren().add(promptReasons);

			ObservableList<String> reasonsData=FXCollections.observableArrayList();
			ListView<String> listReasons=new ListView<String>(reasonsData);
			listReasons.setMaxSize(500, 700);listReasons.setMinSize(500, 700);
			reasonsData.addAll(reasons);
			listReasons.setLayoutX(750);listReasons.setLayoutY(200);
			pickReasonsGrid.getChildren().add(listReasons);

			pickReasons.getStylesheets().add(this.getClass().getResource("application.css").toExternalForm());

			Button reasonsToOtherTeacher=new Button("Back");
			reasonsToOtherTeacher.setFont(oxygen50);
			reasonsToOtherTeacher.setLayoutX(400);reasonsToOtherTeacher.setLayoutY(500);
			pickReasonsGrid.getChildren().add(reasonsToOtherTeacher);

			Button submit=new Button("Submit");
			submit.setFont(oxygen50);
			submit.setLayoutX(1500);submit.setLayoutY(500);
			pickReasonsGrid.getChildren().add(submit);
			submit.setDisable(true);

			//Request Admin Password to Close


			Pane adminToCloseGrid=new Pane();
			Scene adminToClose=new Scene(adminToCloseGrid, s.getWidth(), s.getHeight());

			pText promptPassword=new pText("Please enter the password",700,200);
			promptPassword.setFont(oxygen50);
			adminToCloseGrid.getChildren().add(promptPassword);

			PasswordField enterPWordToClose=new PasswordField();
			enterPWordToClose.setPromptText("Enter password here");
			enterPWordToClose.setLayoutX(480);enterPWordToClose.setLayoutY(400);
			enterPWordToClose.setMinSize(1000, 100);
			adminToCloseGrid.getChildren().add(enterPWordToClose);

			Button close=new Button("Ok");
			close.setFont(oxygen50);
			close.setLayoutX(1020);close.setLayoutY(700);
			adminToCloseGrid.getChildren().add(close);

			Button cancelClose=new Button("Cancel");
			cancelClose.setFont(oxygen50);
			cancelClose.setLayoutX(700);cancelClose.setLayoutY(700);
			adminToCloseGrid.getChildren().add(cancelClose);

			pText wrongPassword=new pText("Wrong password!",500,700);
			adminToCloseGrid.getChildren().add(wrongPassword);
			wrongPassword.setVisible(false);

			adminToCloseGrid.getStylesheets().add(this.getClass().getResource("application.css").toExternalForm());


			//Thank you!
			Pane thankYouGrid=new Pane();
			Scene thankYou=new Scene(thankYouGrid,s.getWidth(),s.getHeight());

			pText tyTxt=new pText("Thank you!",600,500);
			tyTxt.setFont(oxygen50);
			thankYouGrid.getChildren().add(tyTxt);

			thankYouGrid.getStylesheets().add(this.getClass().getResource("application.css").toExternalForm());


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
					if(semester==1){
						classes.addAll(focus.schedule1);
					}else if(semester==2){classes.addAll(focus.schedule2);}
					pickClassGrid.getChildren().remove(classesList);
					pickClassGrid.getChildren().add(classesList);
					s.setScene(pickClass);
					idError.setVisible(false);
				}catch(Exception d){
					d.printStackTrace();
					idError.setVisible(true);
					inID.clear();
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
				try{listTeacher.getSelectionModel().clearSelection();}catch(Exception d){d.printStackTrace();}
				s.setScene(otherTeacher);
			});
			pickClassToEnterID.setOnAction(e->{
				inID.clear();
				ID="";
				inID.requestFocus();
				idError.setVisible(false);
				s.setScene(enterID);	
			});
			classesList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Class>(){
				public void changed(ObservableValue<? extends Class> arg0, Class old, Class newClass) {
					try{
						notify=newClass.getTeacher();
					}catch(Exception e){System.out.println("Class Lookup Error");}
					s.setScene(pickReasons);
					try{listReasons.getSelectionModel().clearSelection();}catch(Exception e){e.printStackTrace();}
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
					try{listTeacher.getSelectionModel().clearSelection();}catch(Exception e){e.printStackTrace();}
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
					submit.setDisable(false);
				}
			});
			submit.setOnAction(e->{
				//TODO figure out thankyou
				//Thread.sleep(1000);
				try{
					theoretical=Student.getTheoreticalTeacher(focus.getSchedule(1), bell);
					try{theoretical=theoretical.findTeacher(teachers);}catch(Exception d){}
					try{notify=notify.findTeacher(teachers);}catch(Exception d){}
				}catch(Exception f){}
				if(theoretical!=null){
					if(theoretical.toString().equals(notify.toString())){
						System.out.println("Send normal email to "+notify.getEmail());
						//					notify.sendEmail(new File("NormalEmail"), theoretical, notify, focus);
					}else{
						System.out.println("Send mismatch case emails to "+notify.getEmail()+" and "+theoretical.getEmail());
						//					notify.sendEmail(new File("SignedPassEmail"), theoretical, notify, focus);
						//					theoretical.sendEmail(new File("TheoreticalEmail"), theoretical, notify, focus);
					}
				}else{
					System.out.println("Send Normal email to "+notify.getEmail());
					//		notify.sendEmail(new File("NormalEmail"), theoretical, notify, focus);
				}
				System.out.println("Send admin email");

				backUp.println(focus.getFullName()+"\t"+focus.getID()+"\t"+Teacher.dateToString(false)+"\t"+reason);
				//		administrator.sendEmail(new File("AdminEmail"), theoretical, notify, focus);
				inID.requestFocus();
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
				if(enterPWordToClose.getText().equals(password)){
					enterPWordToClose.clear();
					if(toClose){
						backUp.close();
						try{
							EmailUtil.sendEmail(fromEmail, "MediaCenterLogData", "",backUpFile);
							backUpFile.delete();
							Platform.exit();
						}catch(Exception d){d.printStackTrace();}
					}else{s.setScene(settings);}
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

			//Settings
			settingsToEnterID.setOnAction(e->{
				newPassword1.clear();
				newPassword2.clear();
				enterAdminEmail.setText(administrator.getEmail());
				pickSemester.getSelectionModel().select(semester-1);
				try{pickClassSource.setValue(Files.get(Files.indexOf(new File(courseFile))));}catch(Exception d){d.printStackTrace();}
				try{pickTeacherSource.setValue(Files.get(Files.indexOf(new File(teacherFile))));}catch(Exception d){d.printStackTrace();}
				try{pickReasonSource.setValue(Files.get(Files.indexOf(new File(reasonFile))));}catch(Exception d){d.printStackTrace();}
				enterBaseEmailPWord.clear();
				baseError.setVisible(false);
				FileError.setVisible(false);
				adEmailError.setVisible(false);
				pWordError.setVisible(false);
				s.setScene(enterID);
			});

			submitSettings.setOnAction(e->{
				boolean validSettings=true;
				try {
					PrintWriter configWrite=new PrintWriter("DoNotTouch/config");
					if(newPassword1.getText().equals("")&&newPassword2.getText().equals("")){
						configWrite.println(password);
					}else{
						if(newPassword1.getText().equals(newPassword2.getText())){
							configWrite.println(newPassword1.getText());
						}else{
							pWordError.setVisible(true);
							validSettings=false;
						}
					}
					if(enterAdminEmail.getText().equals(administrator.getEmail())){
						configWrite.println(administrator.getEmail());
					}else{
						try{
							EmailUtil.sendEmail(enterAdminEmail.getText(), "Test", "This is a test", null);
							administrator=new Teacher("","",enterAdminEmail.getText());
							configWrite.println(enterAdminEmail.getText());
						}catch(Exception d){
							d.printStackTrace();
							adEmailError.setVisible(true);
							validSettings=false;
						}
					}
					if(enterBaseEmailPWord.getText().equals("")){
						configWrite.println(fromEmailPassword);
					}else{
						try{
							EmailUtil.setFrom(fromEmail, enterBaseEmailPWord.getText());
							EmailUtil.sendEmail(enterAdminEmail.getText(), "Test", "This is a test",null);
							configWrite.println(enterBaseEmailPWord.getText());
						}catch(Exception d){
							d.printStackTrace();
							baseError.setVisible(true);
							validSettings=false;
						}
					}
					try{
						configWrite.println(pickSemester.getSelectionModel().getSelectedIndex()+1);
						configWrite.println(pickClassSource.getSelectionModel().getSelectedItem().toString());
						configWrite.println(pickTeacherSource.getSelectionModel().getSelectedItem().toString());
						configWrite.println(pickReasonSource.getSelectionModel().getSelectedItem().toString());
						configWrite.println(bell);
					}catch(Exception d){
						d.printStackTrace();
						FileError.setVisible(true);
						validSettings=false;
					}
					if(validSettings){
						baseError.setVisible(false);
						FileError.setVisible(false);
						adEmailError.setVisible(false);
						pWordError.setVisible(false);
						configWrite.close();
						setConfig();
						newPassword1.clear();
						newPassword2.clear();
						pickClassToEnterID.fire();
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			});
			pickBell.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>(){
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					try{
                                        bell=newValue;
					if(bells.indexOf(newValue)==3){
						s.setScene(bellSchedule);
					}
                                        }catch(Exception d){
                                            s.setScene(bellSchedule);
                                            try{pickBell.setValue(bells.get(bells.indexOf(bell)));}catch(Exception e){}
                                        }

				}
			});
			goToEmailTemps.setOnAction(e->{
				s.setScene(editTemplate);
			});

			//Bell Schedule	
			saveBellSchedule.setOnAction(e->{
				bellError.setVisible(false);
				boolean validSchedule=true;
				PrintWriter pwBell=null;
				try {
					pwBell=new PrintWriter(new File("DoNotTouch/Custom"));
				} catch (Exception e1) {e1.printStackTrace();}
				for(int i=0;i<8;i++){
					if(hours.get(i).getText().equals("")&&minutes.get(i).getText().equals("")){
						pwBell.println();
					}else{
						try{
							if(!(Integer.parseInt(hours.get(i).getText())<=24&&Integer.parseInt(hours.get(i).getText())>=0&&Integer.parseInt(minutes.get(i).getText())<=60&&Integer.parseInt(minutes.get(i).getText())>=0)){throw new NumberFormatException();}
							pwBell.println(hours.get(i).getText()+":"+minutes.get(i).getText());
						}catch(Exception d){
							bellError.setVisible(true);
							d.printStackTrace();
							validSchedule=false;
						}
					}
				}
				if(validSchedule){
					pwBell.close();
					s.setScene(settings);
                                        bell="Custom";
				}
			});

			//Email Templates
			editTemplateToSettings.setOnAction(e->{
				PrintWriter printTemps=null;
				try {
					printTemps=new PrintWriter(new File("NormalEmail"));
					printTemps.println(normal.getText());
					printTemps.close();
					printTemps=new PrintWriter(new File("AdminEmail"));
					printTemps.println(admin.getText());
					printTemps.close();
					printTemps=new PrintWriter(new File("SignedPassEmail"));
					printTemps.println(signedPass.getText());
					printTemps.close();
					printTemps=new PrintWriter(new File("TheoreticalEmail"));
					printTemps.println(predicted.getText());
					printTemps.close();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				s.setScene(settings);
			});


			if(validInit){
				s.setScene(enterID);
			}else{
				s.setScene(settings);
			}

		}catch(Exception e) {
			e.printStackTrace();
			s.setScene(settings);
		}
	}
	public static Teacher getAdmin(){
		return administrator;
	}
	public static void parseData(String File){
		students=new ArrayList<Student>();
		Scanner s = null;
		String id="";
		String [] quals;
		String in="";
		try {
			s=new Scanner(new File(courseFile));
			s.nextLine();
		} catch (Exception e) {
			e.printStackTrace();
		}
		in=s.nextLine();
		quals=in.substring(1,in.length()-1).split("\",\"");
		while(s.hasNextLine()){
			students.add(new Student(quals));
			id=quals[3];
			students.get(students.size()-1).addClass(quals);
			in=s.nextLine();
			quals=in.substring(1,in.length()-1).split("\",\"");
			do{
				students.get(students.size()-1).addClass(quals);
				in=s.nextLine();
				quals=in.substring(1,in.length()-1).split("\",\"");
			}while(id.equals(quals[3])&&s.hasNextLine());
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
		setConfig();
		launch(args);
	}
}