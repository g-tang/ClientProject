package application;

import java.io.File;
import java.io.PrintWriter;
import java.util.Date;
import java.util.ArrayList;
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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.scene.web.WebView;


public class Main extends Application {
	static ArrayList<Student> students=new ArrayList<Student>();
	static ArrayList<Teacher> teachers=new ArrayList<Teacher>();
	static ArrayList<String> reasons=new ArrayList<String>();
	static String courseFile="";
	static String teacherFile="";
	String ID="";
	Student focus;
	static String password="";
	static String fromEmail="";
	static String fromEmailPassword="";
	static int semester=0;
	Teacher notify=new Teacher("","","");
	static Teacher administrator=new Teacher("","","");
	Teacher theoretical=new Teacher("","","");
	String reason="";
	Font tms50=new Font("Trebuchet MS", 50);
	boolean toClose=false;
	static boolean validInit=true;
	static Scene settings;
	static PrintWriter backUp=null;
	static String bell="Normal";
	static ObservableList<Teacher> data= FXCollections.observableArrayList();
	static ObservableList<String> reasonsData=FXCollections.observableArrayList();;
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
			CText.setDims(s.getWidth(), s.getHeight());
			File backUpFile=new File(new Date().getMonth()+"_"+new Date().getDate()+"_MCLog");
			backUp=new PrintWriter(backUpFile);
			System.out.println(s.getWidth());
			System.out.println(s.getHeight());

			//Settings
			Pane settingsGrid=new Pane();
			settings=new Scene(settingsGrid, s.getWidth(), s.getHeight());

			CText promptChangePassword=new CText("Change Password:",200,200);
			settingsGrid.getChildren().add(promptChangePassword);

			CText promptNewPWord1=new CText("Enter new password below:",200,250);
			settingsGrid.getChildren().add(promptNewPWord1);

			CPasswordField newPassword1=new CPasswordField("Enter your new password",300,50,200,300);
			settingsGrid.getChildren().add(newPassword1);

			CPasswordField newPassword2=new CPasswordField("Re-enter your password",300,50,200,400);
			settingsGrid.getChildren().add(newPassword2);

			CText promptBell=new CText("Select a bell schedule",200,520);
			settingsGrid.getChildren().add(promptBell);

			ObservableList<String> bells=FXCollections.observableArrayList("Normal", "2 hour delay", "Half day","Custom");
			ComboBox<String> pickBell=new ComboBox<String>(bells);
			try{pickBell.setValue(bells.get(bells.indexOf(bell)));}catch(Exception e){}
			pickBell.setLayoutX(200);pickBell.setLayoutY(550);
			settingsGrid.getChildren().add(pickBell);

			CText promptSem=new CText("Select current semester:",200,690);
			settingsGrid.getChildren().add(promptSem);

			ObservableList<String> semesters=FXCollections.observableArrayList("Semester 1", "Semester 2");
			ComboBox<String> pickSemester=new ComboBox<String>(semesters);
			if(semester==1){
				pickSemester.setValue(semesters.get(0));
			}else{pickSemester.setValue(semesters.get(1));}
			pickSemester.setLayoutX(200);pickSemester.setLayoutY(720);
			settingsGrid.getChildren().add(pickSemester);

			Button goToEmailTemps=new Button("Edit Email Templates");
			goToEmailTemps.setLayoutX(900);goToEmailTemps.setLayoutY(100);
			settingsGrid.getChildren().add(goToEmailTemps);

			File myFiles=new File(".");//TODO
			ObservableList<File> Files=FXCollections.observableArrayList(myFiles.listFiles());


			CText promptCourse=new CText("Select the student schedule file:",900,225);
			settingsGrid.getChildren().add(promptCourse);

			ComboBox<File> pickClassSource=new ComboBox<File>(Files);
			try{pickClassSource.setValue(Files.get(Files.indexOf(new File(courseFile))));}catch(Exception e){e.printStackTrace();}
			pickClassSource.setLayoutX(900);pickClassSource.setLayoutY(250);
			settingsGrid.getChildren().add(pickClassSource);

			CText promptTeacher=new CText("Select the teacher email file:",900,375);
			settingsGrid.getChildren().add(promptTeacher);


			ComboBox<File> pickTeacherSource=new ComboBox<File>(Files);
			try{pickTeacherSource.setValue(Files.get(Files.indexOf(new File(teacherFile))));}catch(Exception e){e.printStackTrace();}
			pickTeacherSource.setLayoutX(900);pickTeacherSource.setLayoutY(400);
			settingsGrid.getChildren().add(pickTeacherSource);
			
			Button goToReasons=new Button("Edit Reasons");
			goToReasons.setLayoutX(900);goToReasons.setLayoutY(550);
			settingsGrid.getChildren().add(goToReasons);

			CText promptAdminEmail=new CText("Enter the admin email address:",900,690);
			settingsGrid.getChildren().add(promptAdminEmail);

			TextField enterAdminEmail=new TextField(administrator.getEmail());
			enterAdminEmail.setPromptText("Administrator Email");
			enterAdminEmail.setMinSize(300, 50);
			enterAdminEmail.setLayoutX(900);enterAdminEmail.setLayoutY(720);
			settingsGrid.getChildren().add(enterAdminEmail);

			CPasswordField enterAdminEmailPWord=new CPasswordField("Password",300,50,900,800);
			settingsGrid.getChildren().add(enterAdminEmailPWord);

			CText pWordError=new CText("Passwords are mismatched!",1400,100);
			pWordError.setVisible(false);
			settingsGrid.getChildren().add(pWordError);

			CText adminError=new CText("Invalid admin address/password",1400,300);
			adminError.setVisible(false);
			settingsGrid.getChildren().add(adminError);

			CText FileError=new CText("Invalid source File!",1400,200);
			FileError.setVisible(false);
			settingsGrid.getChildren().add(FileError);

			Button settingsToEnterID=new Button("Back");
			settingsToEnterID.setFont(tms50);
			settingsToEnterID.setLayoutX(1400);settingsToEnterID.setLayoutY(400);
			settingsGrid.getChildren().add(settingsToEnterID);

			Button submitSettings=new Button("Save");
			submitSettings.setFont(tms50);
			submitSettings.setLayoutX(1400);submitSettings.setLayoutY(600);
			settingsGrid.getChildren().add(submitSettings);

			settingsGrid.getStylesheets().add(this.getClass().getResource("application.css").toExternalForm());
			
			//Reasons Editor
			Pane reasonEdGrid=new Pane();
			Scene reasonEd=new Scene(reasonEdGrid,s.getWidth(),s.getHeight());
			
			CText reasonPrompt=new CText("Edit potential visit reasons below, \ninserting a carriage return after each one.",500,50);
			reasonEdGrid.getChildren().add(reasonPrompt);
			
			TextArea reasonField=new TextArea(Teacher.fileToString(new File("DoNotTouch/Reasons")));
			reasonField.setMaxSize(800, 700);reasonField.setMinSize(800, 700);
			reasonField.setLayoutX(400);
			reasonField.setLayoutY(150);
			reasonEdGrid.getChildren().add(reasonField);
			
			Button enterReasons=new Button("Ok");
			enterReasons.setFont(tms50);
			enterReasons.setLayoutX(900);enterReasons.setLayoutY(900);
			reasonEdGrid.getChildren().add(enterReasons);
			
			reasonEdGrid.getStylesheets().add(this.getClass().getResource("application.css").toExternalForm());

			//Bell Schedule Editor
			Pane bellScheduleGrid=new Pane();
			Scene bellSchedule=new Scene(bellScheduleGrid,s.getWidth(),s.getHeight());

			ArrayList<TextField> hours=new ArrayList<TextField>();
			ArrayList<TextField> minutes=new ArrayList<TextField>();
			ArrayList<CText> colon=new ArrayList<CText>();
			ArrayList<String[]> bellSched=Student.parseBellSchedule("Custom");
			bellScheduleGrid.getChildren().add(new CText("School Starts:",700,225));
			for(int i=0;i<9;i++){
				try{
					hours.add(new TextField(bellSched.get(i)[0]));
					minutes.add(new TextField(bellSched.get(i)[1]));
					colon.add(new CText(":",950,225+(i*75)));
					if(i!=0){bellScheduleGrid.getChildren().add(new CText(String.valueOf(i),700,225+(i*75)));}
				}catch(Exception e){
					e.printStackTrace();
					hours.add(new TextField());
					minutes.add(new TextField());
					colon.add(new CText(":",950,225+(i*75)));
					bellScheduleGrid.getChildren().add(new CText(String.valueOf(i),700,225+(i*25)));
					if(i!=0){bellScheduleGrid.getChildren().add(new CText(String.valueOf(i),700,225+(i*75)));}
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
			saveBellSchedule.setFont(tms50);
			saveBellSchedule.setLayoutX(880);saveBellSchedule.setLayoutY(900);
			bellScheduleGrid.getChildren().add(saveBellSchedule);

			CText bellError=new CText("Please enter an integer from 0-24 or 0-60.",600,850);
			bellScheduleGrid.getChildren().add(bellError);
			bellError.setVisible(false);

			CText bellInst=new CText("Please enter the times at which each period in 24-hour format.\n Leave spaces blank if the period will not be in session",600,100);
			bellScheduleGrid.getChildren().add(bellInst);

			bellScheduleGrid.getStylesheets().add(this.getClass().getResource("application.css").toExternalForm());

			//Book Checkout
			Pane checkOutGrid=new Pane();
			Scene checkOut=new Scene(checkOutGrid,s.getWidth(),s.getHeight());
			
			WebView checkOutSite=new WebView();
			checkOutSite.getEngine().load("http://google.com");
			checkOutSite.setLayoutX(500);checkOutSite.setLayoutY(100);
			checkOutSite.setMaxSize(1000, 800);checkOutSite.setMinSize(1000, 800);
			checkOutGrid.getChildren().add(checkOutSite);
			
			Button checkOutToEnterID=new Button("Back");
			checkOutToEnterID.setFont(tms50);
			checkOutToEnterID.setLayoutX(900);checkOutToEnterID.setLayoutY(950);
			checkOutGrid.getChildren().add(checkOutToEnterID);
			
			checkOutGrid.getStylesheets().add(this.getClass().getResource("application.css").toExternalForm());
			
			//Edit Email Files
			Pane editTemplateGrid=new Pane();
			Scene editTemplate=new Scene(editTemplateGrid, s.getWidth(),s.getHeight());
			CText editTemplateInstructions=new CText("    Key: <THEORETICAL>-name of predicted teacher   <TIME>-date and time\n<SIGNEDPASS>-name of selected teacher    <STUDENTNAME>-name of student",500,100);
			editTemplateGrid.getChildren().add(editTemplateInstructions);

			CText promptPredicted=new CText("To predicted teacher if student does not select him/her:",100,220);
			editTemplateGrid.getChildren().add(promptPredicted);

			TextArea predicted=new TextArea(Teacher.fileToString(new File("DoNotTouch/TheoreticalEmail")));
			predicted.setMaxSize(800, 300);predicted.setMinSize(800, 300);
			predicted.setWrapText(true);
			predicted.setLayoutX(100);
			predicted.setLayoutY(250);
			editTemplateGrid.getChildren().add(predicted);

			CText promptNormal=new CText("To predicted teacher if student selects him/her:",100,620);
			editTemplateGrid.getChildren().add(promptNormal);

			TextArea normal=new TextArea(Teacher.fileToString(new File("DoNotTouch/NormalEmail")));
			normal.setMaxSize(800, 300);normal.setMinSize(800, 300);
			normal.setWrapText(true);
			normal.setLayoutX(100);normal.setLayoutY(650);
			editTemplateGrid.getChildren().add(normal);

			CText promptSignedPass=new CText("To selected teacher if they are not predicted teacher:",1000,220);
			editTemplateGrid.getChildren().add(promptSignedPass);

			TextArea signedPass=new TextArea(Teacher.fileToString(new File("DoNotTouch/SignedPassEmail")));
			signedPass.setMaxSize(800, 300);signedPass.setMinSize(800, 300);
			signedPass.setWrapText(true);
			signedPass.setLayoutX(1000);signedPass.setLayoutY(250);
			editTemplateGrid.getChildren().add(signedPass);


			Button editTemplateToSettings=new Button("Ok");
			editTemplateToSettings.setFont(tms50);
			editTemplateToSettings.setLayoutX(1300);editTemplateToSettings.setLayoutY(600);
			editTemplateGrid.getChildren().add(editTemplateToSettings);

			editTemplateGrid.getStylesheets().add(this.getClass().getResource("application.css").toExternalForm());

			//Enter Your Student ID Interface Elements
			Pane enterIDGrid=new Pane();
			Scene enterID=new Scene(enterIDGrid, s.getWidth(),s.getHeight());

			CText promptID=new CText("To sign in, enter your student ID number below or \nscan your card. Then press \"Enter\" or click \"Sign in\".",400,300);
			promptID.setFont(tms50);
			enterIDGrid.getChildren().add(promptID);
			promptID.setTextAlignment(TextAlignment.CENTER);

			TextField inID=new TextField();
			inID.setPromptText("Enter your student ID #");
			inID.setLayoutX(500);inID.setLayoutY(500);
			inID.setMinSize(800, 20);
			enterIDGrid.getChildren().add(inID);

			Button enterIDToPickClass=new Button("Sign in");
			enterIDToPickClass.setFont(tms50);
			enterIDToPickClass.setLayoutX(800);enterIDToPickClass.setLayoutY(750);
			enterIDGrid.getChildren().add(enterIDToPickClass);
			
			Button goToCheckOut=new Button("Check out a book");
			goToCheckOut.setFont(tms50);
			goToCheckOut.setLayoutX(680);goToCheckOut.setLayoutY(850);
			enterIDGrid.getChildren().add(goToCheckOut);

			CText idError=new CText("Please enter a valid Student ID",500,650);
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

			CText promptPickClass=new CText("",300,200);
			promptPickClass.setFont(tms50);
			pickClassGrid.getChildren().add(promptPickClass);

			Button pickClassToOtherTeacher=new Button("Other");
			pickClassToOtherTeacher.setFont(tms50);
			pickClassToOtherTeacher.setLayoutX(1500);pickClassToOtherTeacher.setLayoutY(500);
			pickClassGrid.getChildren().add(pickClassToOtherTeacher);

			Button pickClassToEnterID=new Button("Back");
			pickClassToEnterID.setFont(tms50);
			pickClassToEnterID.setLayoutX(100);pickClassToEnterID.setLayoutY(500);
			pickClassGrid.getChildren().add(pickClassToEnterID);

			ObservableList<Class> classes=FXCollections.observableArrayList();
			ListView<Class> classesList=new ListView<Class>(classes);
			classesList.setMaxSize(700, 500);classesList.setMinSize(800, 600);
			classesList.setLayoutX(500);classesList.setLayoutY(300);
			pickClassGrid.getStylesheets().add(this.getClass().getResource("application.css").toExternalForm());

			//Other Teacher Elements
			Pane otherTeacherGrid=new Pane();
			Scene otherTeacher=new Scene(otherTeacherGrid, s.getWidth(), s.getHeight());

			CText promptPickTeacher=new CText("Select the teacher you are with:",650,100);
			promptPickTeacher.setFont(tms50);
			otherTeacherGrid.getChildren().add(promptPickTeacher);

			Button otherTeacherToPickClass=new Button("Back");
			otherTeacherToPickClass.setFont(tms50);
			otherTeacherToPickClass.setLayoutX(400);otherTeacherToPickClass.setLayoutY(500);
			otherTeacherGrid.getChildren().add(otherTeacherToPickClass);

			ListView<Teacher> listTeacher=new ListView<Teacher>(data);
			listTeacher.setMaxSize(500, 700);listTeacher.setMinSize(500, 700);
			listTeacher.setLayoutX(750);listTeacher.setLayoutY(200);

			otherTeacherGrid.getChildren().add(listTeacher);

			otherTeacher.getStylesheets().add(this.getClass().getResource("application.css").toExternalForm());


			//Reasons Elements
			Pane pickReasonsGrid=new Pane();
			Scene pickReasons=new Scene(pickReasonsGrid, s.getWidth(), s.getHeight());

			CText promptReasons=new CText("Why have you visited?",750,100);
			promptReasons.setFont(tms50);
			pickReasonsGrid.getChildren().add(promptReasons);

			ListView<String> listReasons=new ListView<String>(reasonsData);
			listReasons.setMaxSize(500, 700);listReasons.setMinSize(500, 700);
			listReasons.setLayoutX(750);listReasons.setLayoutY(200);
			pickReasonsGrid.getChildren().add(listReasons);

			pickReasons.getStylesheets().add(this.getClass().getResource("application.css").toExternalForm());

			Button reasonsToOtherTeacher=new Button("Back");
			reasonsToOtherTeacher.setFont(tms50);
			reasonsToOtherTeacher.setLayoutX(400);reasonsToOtherTeacher.setLayoutY(500);
			pickReasonsGrid.getChildren().add(reasonsToOtherTeacher);

			Button submit=new Button("Submit");
			submit.setFont(tms50);
			submit.setLayoutX(1500);submit.setLayoutY(500);
			pickReasonsGrid.getChildren().add(submit);
			submit.setDisable(true);
			submit.setId("submit");

			//Request Admin Password to Close


			Pane adminToCloseGrid=new Pane();
			Scene adminToClose=new Scene(adminToCloseGrid, s.getWidth(), s.getHeight());

			CText promptPassword=new CText("Please enter the password",700,200);
			promptPassword.setFont(tms50);
			adminToCloseGrid.getChildren().add(promptPassword);

			CPasswordField enterPWordToClose=new CPasswordField("Enter password here",1000,100,480,400);
			adminToCloseGrid.getChildren().add(enterPWordToClose);

			Button close=new Button("Ok");
			close.setFont(tms50);
			close.setLayoutX(1020);close.setLayoutY(700);
			adminToCloseGrid.getChildren().add(close);

			Button cancelClose=new Button("Cancel");
			cancelClose.setFont(tms50);
			cancelClose.setLayoutX(700);cancelClose.setLayoutY(700);
			adminToCloseGrid.getChildren().add(cancelClose);

			CText wrongPassword=new CText("Wrong password!",500,600);
			adminToCloseGrid.getChildren().add(wrongPassword);
			wrongPassword.setVisible(false);

			adminToCloseGrid.getStylesheets().add(this.getClass().getResource("application.css").toExternalForm());


			//Listeners

			//Enter ID
			enterIDToPickClass.setOnAction(e->{
				try{
					ID=inID.getText();
					System.out.println(ID);
					focus=searchStudent(ID);
					promptPickClass.setText("Hello, "+focus.getFirstName()+". Please pick the class you are in right now.");
					try{classes.clear();}catch(Exception d){}
					if(semester==1){
						try{classes.addAll(focus.schedule1);}catch(Exception d){}	
					}else if(semester==2){try{}catch(Exception d){classes.addAll(focus.schedule2);}}
					pickClassGrid.getChildren().remove(classesList);
					pickClassGrid.getChildren().add(classesList);
					idError.setVisible(false);
					s.setScene(pickClass);
				}catch(Exception d){
					idError.setVisible(true);
					inID.clear();
				}
			});
			inID.setOnAction(e->{
				enterIDToPickClass.fire();
			});
			enterIDToSettings.setOnAction(e->{
				toClose=false;
				enterPWordToClose.requestFocus();
				s.setScene(adminToClose);
			});
			goToCheckOut.setOnAction(e->{
				s.setScene(checkOut);
			});
			
			//CheckOut
			checkOutToEnterID.setOnAction(e->{
				pickClassToEnterID.fire();
			});
			//Pick Class
			pickClassToOtherTeacher.setOnAction(e->{
				try{listTeacher.getSelectionModel().clearSelection();}catch(Exception d){System.out.println("CLEAR ERROR 497!!!");}
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
					try{notify=newClass.getTeacher();}catch(Exception e){System.out.println("Class Lookup Error");}
					s.setScene(pickReasons);
					try{reasonsData.clear();}catch(Exception e){}
					try{reasonsData.addAll(reasons);}catch(Exception e){}
					try{listReasons.getSelectionModel().clearSelection();}catch(Exception e){System.out.println("CLEAR ERROR 515!!!");}
					submit.setDisable(true);
				}
			});
			classesList.setOnKeyPressed(e->{
				e.consume();
			});

			//Pick Other Teacher
			otherTeacherToPickClass.setOnAction(e->{
				enterIDToPickClass.fire();
			});
			listTeacher.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Teacher>(){
				public void changed(ObservableValue<? extends Teacher> arg0, Teacher old, Teacher newTeach) {
					try{
						notify=newTeach;
						s.setScene(pickReasons);
						try{reasonsData.clear();}catch(Exception e){}
						try{reasonsData.addAll(reasons);}catch(Exception e){}
						submit.setDisable(true);
						try{listTeacher.getSelectionModel().clearSelection();}catch(Exception e){System.out.println("CLEAR ERROR 531!!!");}

					}catch(Exception d){
						System.out.println("Other Teacher Handler Errror");
					}
				}
			});
			listTeacher.setOnKeyPressed(e->{
				e.consume();
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
				try{
					try{theoretical=Student.getTheoreticalTeacher(focus.getSchedule(1), bell);}catch(Exception d){}
					try{theoretical=theoretical.findTeacher(teachers);}catch(Exception d){}
					try{notify=notify.findTeacher(teachers);}catch(Exception d){}
					administrator.sendEmail(new File("DoNotTouch/AdminEmail"), theoretical, notify, focus,reason);
					System.out.println("Send admin email to" +administrator.getEmail());
					if(theoretical!=null){
						if(theoretical.toString().equals(notify.toString())){
							System.out.println("Send normal email to "+notify.getEmail());
							//					notify.sendEmail(new File("DoNotTouch/NormalEmail"), theoretical, notify, focus,reason);
						}else{
							System.out.println("Send mismatch case emails to "+notify.getEmail()+" and "+theoretical.getEmail());
							//					notify.sendEmail(new File("SignedPassEmail"), theoretical, notify, focus,reason);
							//					theoretical.sendEmail(new File("DoNotTouch/TheoreticalEmail"), theoretical, notify, focus,reason);
						}
					}else{
						System.out.println("Send Normal email to "+notify.getEmail());
						//		notify.sendEmail(new File("DoNotTouch/NormalEmail"), theoretical, notify, focus);
					}
				}catch(Exception f){
					backUp.println(focus.getFullName()+"\t"+focus.getID()+"\t"+Teacher.dateToString(false)+"\t"+theoretical+"\t"+notify+"\t"+reason);
				}

				inID.requestFocus();
				idError.setVisible(false);
				inID.clear();
				s.setScene(enterID);
			});
			//Close Program
			enterPWordToClose.setOnAction(e->{
				close.fire();
			});
			s.setOnCloseRequest(e->{
				e.consume();
				toClose=true;
				s.setScene(adminToClose);
				enterPWordToClose.requestFocus();
				enterPWordToClose.clear();
				wrongPassword.setVisible(false);
			});
			close.setOnAction(e->{
				wrongPassword.setVisible(false);
				if(enterPWordToClose.getText().equals(password)){
					enterPWordToClose.clear();
					if(toClose){
						backUp.close();
						if(backUpFile.length()==0){
							backUpFile.delete();
						}
						Platform.exit();
					}else{s.setScene(settings);}
				}else{
					enterPWordToClose.clear();
					wrongPassword.setVisible(true);
				}
			});
			cancelClose.setOnAction(e->{
				inID.clear();
				idError.setVisible(false);
				enterPWordToClose.clear();
				wrongPassword.setVisible(false);
				s.setScene(enterID);
			});
			terminate.setOnAction(e->{
				wrongPassword.setVisible(false);
				enterPWordToClose.clear();
				toClose=true;
				enterPWordToClose.requestFocus();
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
				//try{pickReasonSource.setValue(Files.get(Files.indexOf(new File(reasonFile))));}catch(Exception d){d.printStackTrace();}
				enterAdminEmailPWord.clear();
				adminError.setVisible(false);
				FileError.setVisible(false);
				pWordError.setVisible(false);
				inID.clear();
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
					if(enterAdminEmailPWord.getText().equals("")&&enterAdminEmail.getText().equals(administrator.getEmail())){
						configWrite.println(administrator.getEmail());
						configWrite.println(fromEmailPassword);
					}else{
						try{
							EmailUtil.setFrom(enterAdminEmail.getText(), enterAdminEmailPWord.getText());
							EmailUtil.sendEmail(enterAdminEmail.getText(), "Test", "This is a test");
							administrator=new Teacher("","",enterAdminEmail.getText());
							configWrite.println(enterAdminEmail.getText());
							configWrite.println(enterAdminEmailPWord.getText());
						}catch(Exception d){
							d.printStackTrace();
							adminError.setVisible(true);
							validSettings=false;
						}
					}
					try{
						configWrite.println(pickSemester.getSelectionModel().getSelectedIndex()+1);
						configWrite.println(pickClassSource.getSelectionModel().getSelectedItem().toString());
						configWrite.println(pickTeacherSource.getSelectionModel().getSelectedItem().toString());
						//configWrite.println(pickReasonSource.getSelectionModel().getSelectedItem().toString());
						configWrite.println(bell);
					}catch(Exception d){
						d.printStackTrace();
						FileError.setVisible(true);
						validSettings=false;
					}
					if(validSettings){
						adminError.setVisible(false);
						FileError.setVisible(false);
						pWordError.setVisible(false);
						configWrite.close();
						setConfig();
						newPassword1.clear();
						newPassword2.clear();
						enterAdminEmailPWord.clear();
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
			goToReasons.setOnAction(e->{
				s.setScene(reasonEd);
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
				for(int i=0;i<9;i++){
					if(hours.get(i).getText().trim().equals("")&&minutes.get(i).getText().trim().equals("")){
						pwBell.println(" : ");
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
					printTemps=new PrintWriter(new File("DoNotTouch/NormalEmail"));
					printTemps.println(normal.getText());
					printTemps.close();
					printTemps=new PrintWriter(new File("DoNotTouch/SignedPassEmail"));
					printTemps.println(signedPass.getText());
					printTemps.close();
					printTemps=new PrintWriter(new File("DoNotTouch/TheoreticalEmail"));
					printTemps.println(predicted.getText());
					printTemps.close();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				s.setScene(settings);
			});
			
			//editReasons
			enterReasons.setOnAction(e->{
				PrintWriter printReasons=null;
				try{
					printReasons=new PrintWriter(new File("DoNotTouch/Reasons"));
					printReasons.println(reasonField.getText());
					printReasons.close();
				}catch(Exception d){}
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
	public static void setConfig(){
		try {
			Scanner s=new Scanner(new File("DoNotTouch/config"));
			System.out.println(new File("DoNotTouch/config").getAbsolutePath());
			password=s.nextLine();
			fromEmail=s.nextLine();
			administrator=new Teacher("","",fromEmail);
			fromEmailPassword=s.nextLine();
			EmailUtil.setFrom(fromEmail, fromEmailPassword);
			semester=Integer.parseInt(s.nextLine());
			courseFile=s.nextLine();
			parseData(courseFile);
			teacherFile=s.nextLine();
			Teacher.parseTeacher(new File(teacherFile));
			teachers=Teacher.getTeacherInfo(new File(teacherFile));
			Scanner q=new Scanner(new File("DoNotTouch/Reasons"));
			reasons=new ArrayList<String>();
			while(q.hasNextLine()){
				reasons.add(q.nextLine());
			}
			bell=s.nextLine();
			try{data.clear();}catch(Exception e){}
			try{reasonsData.addAll(reasons);}catch(Exception e){System.out.println("Reason add error");}
			try{reasonsData.clear();}catch(Exception e){System.out.println("Reason add error");}
			try{data.addAll(teachers);}catch(Exception e){}

		} catch (Exception e) {
			e.printStackTrace();
			// show settings screen
			validInit=false;
		}
		System.out.println("complete");
	}
	public static void main(String[] args) {
		setConfig();
		launch(args);
	}
}