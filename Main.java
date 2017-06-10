package application;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.scene.web.WebView;


public class Main extends Application {
	/**
	 * The list of students accessible
*/	private static ArrayList<Student> students=new ArrayList<Student>();
/**The list of teachers with emails available
*/	private static ArrayList<Teacher> teachers=new ArrayList<Teacher>(); 
/**The list of potential reasons to visit the library*/	
	private static ArrayList<String> reasons=new ArrayList<String>();
	/**The name of the student data file
*/	private static String courseFile="";
/**The name of the teacher data file*/
	private static String teacherFile="";
/**The ID number that is being analyzed/signed in by the program*/
	private String ID="";
/**The student that is currently using the program*/
	private Student focus;
/**The administrator password to close the app/edit settings*/
	private static String password="";
/**The administrator email address*/
	private static String fromEmail="";
/**The administrator email password*/
	private static String fromEmailPassword="";
/**Which semester it is. Can be equal to 1 or 2*/
	private static int semester=0;
/**The teacher that the student selects*/
	private Teacher notify=new Teacher("","","");
/**The application administrator. Only has an email.*/
	private static Teacher administrator=new Teacher("","","");
/**The teacher that the program predicts*/
	private Teacher theoretical=new Teacher("","","");
	/**The reason that the student selects*/
	private String reason="";
	/**A flag variable that tells the password scene what to do upon password entry*/
	private boolean toClose=false;
	/**A flag variable that will signal to navigate to settings after all possible lines of code are executed if an exception is thrown*/
	private static boolean validInit=true;
	/**The settings scene*/
	private static Scene settings;
	private static PrintWriter backUp=null;
	private static String bell="Normal";
	private static ObservableList<Teacher> teacherData= FXCollections.observableArrayList();
	private static ObservableList<String> reasonsData=FXCollections.observableArrayList();
	private static String checkOutURL="";
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
			CText.setDim(s.getWidth(), s.getHeight());
			Font tms50=new Font("Avenir Next", 45);
			File backUpFile=new File(new Date().getMonth()+"_"+new Date().getDate()+"_MCLog");
			backUp=new PrintWriter(backUpFile);
			System.out.println(s.getWidth());
			System.out.println(s.getHeight());

			//Settings
			Pane settingsGrid=new Pane();
			settings=new Scene(settingsGrid, s.getWidth(), s.getHeight());

			CButton closeSettings=new CButton("Close app", 1125,0);
			settingsGrid.getChildren().add(closeSettings);

			closeSettings.setOnAction(e->{
				backUp.close();
				if(backUpFile.length()==0){
					backUpFile.delete();
				}
				Platform.exit();
			});
			CText promptChangePassword=new CText("Change Password:",100,200);
			settingsGrid.getChildren().add(promptChangePassword);

			CText promptNewPWord1=new CText("Enter new password below:",100,250);
			settingsGrid.getChildren().add(promptNewPWord1);

			CPasswordField newPassword1=new CPasswordField("Enter your new password",300,60,100,300);
			settingsGrid.getChildren().add(newPassword1);

			CPasswordField newPassword2=new CPasswordField("Re-enter your password",300,60,100,400);
			settingsGrid.getChildren().add(newPassword2);

			CText promptBell=new CText("Select a bell schedule",100,520);
			settingsGrid.getChildren().add(promptBell);

			ObservableList<String> bells=FXCollections.observableArrayList("Normal", "2 hour delay", "Half day","Custom");
			ComboBox<String> pickBell=new ComboBox<String>(bells);
			try{pickBell.setValue(bells.get(bells.indexOf(bell)));}catch(Exception e){}
			setLocationSize(pickBell,300,60,100,550);

			settingsGrid.getChildren().add(pickBell);

			CText promptSem=new CText("Select current semester:",100,690);
			settingsGrid.getChildren().add(promptSem);

			ObservableList<String> semesters=FXCollections.observableArrayList("Semester 1", "Semester 2");
			ComboBox<String> pickSemester=new ComboBox<String>(semesters);
			if(semester==1){
				pickSemester.setValue(semesters.get(0));
			}else{pickSemester.setValue(semesters.get(1));}
			setLocationSize(pickSemester,300,60,100,720);
			settingsGrid.getChildren().add(pickSemester);

			CButton setUpScript=new CButton("Set up Drive access",100,820);
			settingsGrid.getChildren().add(setUpScript);

			setUpScript.setOnAction(e->{
				try {Desktop.getDesktop().browse(new URI("https://script.google.com/macros/s/AKfycbxuqvcIf54AjNjsno-aefKPX2vQn4brSbfgkTGlvbMDs8pXhrQ/exec"));} catch (Exception e1){}
			});
			
			CText promptURL=new CText("Checkout URL:",300,940);
			settingsGrid.getChildren().add(promptURL);

			CButton goToEmailTemps=new CButton("Edit Email Templates",500,150);
			settingsGrid.getChildren().add(goToEmailTemps);

			File myFiles=new File(".");
			ObservableList<File> Files=FXCollections.observableArrayList(myFiles.listFiles());


			CText promptCourse=new CText("Select the student schedule file:",500,275);
			settingsGrid.getChildren().add(promptCourse);

			ComboBox<File> pickClassSource=new ComboBox<File>(Files);
			try{pickClassSource.setValue(Files.get(Files.indexOf(new File(courseFile))));}catch(Exception e){}
			setLocationSize(pickClassSource,300,60,500,300);
			settingsGrid.getChildren().add(pickClassSource);

			CText promptTeacher=new CText("Select the teacher email file:",500,425);
			settingsGrid.getChildren().add(promptTeacher);


			ComboBox<File> pickTeacherSource=new ComboBox<File>(Files);
			try{pickTeacherSource.setValue(Files.get(Files.indexOf(new File(teacherFile))));}catch(Exception e){}
			setLocationSize(pickTeacherSource,300,60,500,450);
			settingsGrid.getChildren().add(pickTeacherSource);

			CButton goToReasons=new CButton("Edit Reasons",500,560);
			settingsGrid.getChildren().add(goToReasons);

			CText promptAdminEmail=new CText("Enter the admin email address:",500,690);
			settingsGrid.getChildren().add(promptAdminEmail);

			CTextField enterAdminEmail=new CTextField(administrator.getEmail(),300,60,500,720);
			enterAdminEmail.setPromptText("Administrator Email");
			settingsGrid.getChildren().add(enterAdminEmail);

			CPasswordField enterAdminEmailPWord=new CPasswordField("Password",300,60,500,800);
			settingsGrid.getChildren().add(enterAdminEmailPWord);

			CTextField urlIn=new CTextField("",300,60,500,900);
			urlIn.setText(checkOutURL);
			settingsGrid.getChildren().add(urlIn);

			CText pWordError=new CText("Passwords are mismatched!",900,700);
			pWordError.setVisible(false);
			settingsGrid.getChildren().add(pWordError);

			CText adminError=new CText("Invalid admin address/password",900,800);
			adminError.setVisible(false);
			settingsGrid.getChildren().add(adminError);

			CText FileError=new CText("Invalid source file!",900,900);
			FileError.setVisible(false);
			settingsGrid.getChildren().add(FileError);

			CButton settingsToEnterID=new CButton("Back",900,300);
			settingsToEnterID.setFont(tms50);
			settingsGrid.getChildren().add(settingsToEnterID);

			CButton submitSettings=new CButton("Save",900,500);
			submitSettings.setFont(tms50);
			settingsGrid.getChildren().add(submitSettings);

			settingsGrid.getStylesheets().add(this.getClass().getResource("application.css").toExternalForm());

			//Reasons Editor
			Pane reasonEdGrid=new Pane();
			Scene reasonEd=new Scene(reasonEdGrid,s.getWidth(),s.getHeight());

			CText reasonPrompt=new CText("	Edit potential visit reasons below, \ninserting a carriage return after each one.",225,50);
			reasonPrompt.setFont(tms50);
			reasonEdGrid.getChildren().add(reasonPrompt);

			CTextArea reasonField=new CTextArea(Teacher.fileToString(new File("DoNotTouch/Reasons")),800,600,250,150);
			reasonEdGrid.getChildren().add(reasonField);

			CButton enterReasons=new CButton("Ok",600,800);
			enterReasons.setFont(tms50);
			reasonEdGrid.getChildren().add(enterReasons);

			reasonEdGrid.getStylesheets().add(this.getClass().getResource("application.css").toExternalForm());

			//Bell Schedule Editor
			Pane bellScheduleGrid=new Pane();
			Scene bellSchedule=new Scene(bellScheduleGrid,s.getWidth(),s.getHeight());

			ArrayList<CTextField> hours=new ArrayList<CTextField>();
			ArrayList<CTextField> minutes=new ArrayList<CTextField>();
			ArrayList<CText> colon=new ArrayList<CText>();
			ArrayList<String[]> bellSched=null;
			try{bellSched=Student.parseBellSchedule("Custom");}catch(Exception e){}
			bellScheduleGrid.getChildren().add(new CText("School Starts:",250,225));
			for(int i=0;i<9;i++){
				try{
					hours.add(new CTextField(bellSched.get(i)[0],100,50,500,200+(i*75)));
					minutes.add(new CTextField(bellSched.get(i)[1],100,50,700,200+(i*75)));
					colon.add(new CText(":",650,225+(i*75)));
					if(i!=0){bellScheduleGrid.getChildren().add(new CText(String.valueOf(i),400,225+(i*75)));}
				}catch(Exception e){
					e.printStackTrace();
					hours.add(new CTextField("",100,50,800,200+(i*75)));
					minutes.add(new CTextField("",100,50,1000,200+(i*75)));
					colon.add(new CText(":",950,225+(i*75)));
					bellScheduleGrid.getChildren().add(new CText(String.valueOf(i),700,225+(i*25)));
					if(i!=0){bellScheduleGrid.getChildren().add(new CText(String.valueOf(i),700,225+(i*75)));}
				}
			}
			bellScheduleGrid.getChildren().addAll(colon);
			bellScheduleGrid.getChildren().addAll(hours);
			bellScheduleGrid.getChildren().addAll(minutes);

			CButton saveBellSchedule=new CButton("Save",580,900);
			saveBellSchedule.setFont(tms50);
			bellScheduleGrid.getChildren().add(saveBellSchedule);

			CText bellError=new CText("Please enter an integer from 0-24 or 0-60.",400,870);
			bellScheduleGrid.getChildren().add(bellError);
			bellError.setVisible(false);

			CText bellInst=new CText("Please enter the times at which each period in 24-hour format.\n Leave spaces blank if the period will not be in session",300,100);
			bellScheduleGrid.getChildren().add(bellInst);

			bellScheduleGrid.getStylesheets().add(this.getClass().getResource("application.css").toExternalForm());

			//Edit Email Files
			Pane editTemplateGrid=new Pane();
			Scene editTemplate=new Scene(editTemplateGrid, s.getWidth(),s.getHeight());
			CText editTemplateInstructions=new CText("Key: <THEORETICAL>-name of predicted teacher   <TIME>-date and time	\n<ID>-ID number	<SIGNEDPASS>-name of selected teacher    \n<STUDENTNAME>-name of student	<REASON>-reason for visit",200,50);
			editTemplateGrid.getChildren().add(editTemplateInstructions);

			CText promptPredicted=new CText("To predicted teacher if they aren't selected:",30,220);
			editTemplateGrid.getChildren().add(promptPredicted);

			CTextArea predicted=new CTextArea(Teacher.fileToString(new File("DoNotTouch/TheoreticalEmail")),500,300,50,250);
			editTemplateGrid.getChildren().add(predicted);

			CText promptNormal=new CText("To predicted teacher if student selects them:",25,620);
			editTemplateGrid.getChildren().add(promptNormal);

			CTextArea normal=new CTextArea(Teacher.fileToString(new File("DoNotTouch/NormalEmail")),500,300,50,650);
			editTemplateGrid.getChildren().add(normal);

			CText promptSignedPass=new CText("To selected teacher if they weren't predicted:",650,220);
			editTemplateGrid.getChildren().add(promptSignedPass);

			CTextArea signedPass=new CTextArea(Teacher.fileToString(new File("DoNotTouch/SignedPassEmail")),500,300,650,250);
			editTemplateGrid.getChildren().add(signedPass);


			CButton editTemplateToSettings=new CButton("Ok",800,700);
			editTemplateToSettings.setFont(tms50);
			editTemplateGrid.getChildren().add(editTemplateToSettings);

			editTemplateGrid.getStylesheets().add(this.getClass().getResource("application.css").toExternalForm());

			//Enter Your Student ID
			Pane enterIDGrid=new Pane();
			Scene enterID=new Scene(enterIDGrid, s.getWidth(),s.getHeight());

			CText promptID=new CText("Type or scan student ID",360,350);
			promptID.setFont(tms50);
			enterIDGrid.getChildren().add(promptID);
			promptID.setTextAlignment(TextAlignment.CENTER);
			
			CButton goToCheckOut=new CButton("Check out a book",50,50);
			if(checkOutURL.equals("")){
				goToCheckOut.setVisible(false);
			}
			enterIDGrid.getChildren().add(goToCheckOut);

			CTextField inID=new CTextField("",600,100,305,450);
			inID.setFont(tms50);
			inID.setPromptText("Click here to enter your ID #");
			enterIDGrid.getChildren().add(inID);

			CButton enterIDToPickClass=new CButton("Sign in",505,690);
			enterIDToPickClass.setFont(tms50);
			enterIDToPickClass.setDefaultButton(true);
			enterIDGrid.getChildren().add(enterIDToPickClass);

			CText idError=new CText("Please enter a valid Student ID",400,650);
			idError.setVisible(false);
			enterIDGrid.getChildren().add(idError);
			
			enterIDGrid.setId("enterID");

			CButton terminate=new CButton("Close",1150,50);
			enterIDGrid.getChildren().add(terminate);

			CButton enterIDToSettings=new CButton("Settings",950,50);
			enterIDGrid.getChildren().add(enterIDToSettings);

			enterIDGrid.getStylesheets().add(this.getClass().getResource("application.css").toExternalForm());
			
			//Book Checkout
			Pane checkOutGrid=new Pane();
			Scene checkOut=new Scene(checkOutGrid,s.getWidth(),s.getHeight());

			StackPane stack=new StackPane();
			stack.setLayoutX(0);
			stack.setLayoutY(100);
			stack.setMaxSize(1280, 700);stack.setMinSize(1280, 700);
			
			WebView checkOutSite=new WebView();
			checkOutSite.getEngine().load(checkOutURL);
			checkOutSite.setLayoutX(0);checkOutSite.setLayoutY(100);
			checkOutSite.setMaxSize(1280, 700);
			checkOutSite.setMinSize(1280, 700);
			stack.getChildren().add(checkOutSite);
			
			ImageView loader=new ImageView();
			Image loadGif=new Image(new File("DoNotTouch/Loading.gif").toURI().toURL().toString());
			loader.setImage(loadGif);
			loader.setX(400);loader.setY(400);
			stack.getChildren().add(loader);

			
			checkOutGrid.getChildren().add(stack);

			CButton checkOutToEnterID=new CButton("Back",550,900);
			checkOutToEnterID.setFont(tms50);
			checkOutGrid.getChildren().add(checkOutToEnterID);

			checkOutGrid.getStylesheets().add(this.getClass().getResource("application.css").toExternalForm());

			//Pick your class Elements
			Pane pickClassGrid=new Pane();
			Scene pickClass=new Scene(pickClassGrid, s.getWidth(), s.getHeight());

			CText promptPickClass=new CText("",100,200);
			promptPickClass.setFont(tms50);
			pickClassGrid.getChildren().add(promptPickClass);

			CButton pickClassToOtherTeacher=new CButton("Other\nTeacher",950,450);
			pickClassToOtherTeacher.setFont(tms50);
			pickClassGrid.getChildren().add(pickClassToOtherTeacher);

			CButton pickClassToEnterID=new CButton("Back",100,500);
			pickClassToEnterID.setFont(tms50);
			pickClassGrid.getChildren().add(pickClassToEnterID);

			ObservableList<Class> classes=FXCollections.observableArrayList();
			ListView<Class> classesList=new ListView<Class>(classes);
			setLocationSize(classesList,600,500,300,300);
			pickClassGrid.getChildren().add(classesList);

			pickClassGrid.getStylesheets().add(this.getClass().getResource("application.css").toExternalForm());

			//Other Teacher Elements
			Pane otherTeacherGrid=new Pane();
			Scene otherTeacher=new Scene(otherTeacherGrid, s.getWidth(), s.getHeight());

			CText promptPickTeacher=new CText("Select the teacher who sent you:",350,100);
			promptPickTeacher.setFont(tms50);
			otherTeacherGrid.getChildren().add(promptPickTeacher);

			CButton otherTeacherToPickClass=new CButton("Back",100,500);
			otherTeacherToPickClass.setFont(tms50);
			otherTeacherGrid.getChildren().add(otherTeacherToPickClass);


			ListView<Teacher> listTeacher=new ListView<Teacher>(teacherData);
			setLocationSize(listTeacher,400,700,450,200);
			otherTeacherGrid.getChildren().add(listTeacher);

			otherTeacher.getStylesheets().add(this.getClass().getResource("application.css").toExternalForm());


			//Reasons Elements
			Pane pickReasonsGrid=new Pane();
			Scene pickReasons=new Scene(pickReasonsGrid, s.getWidth(), s.getHeight());

			CText promptReasons=new CText("Why have you visited?",450,100);
			promptReasons.setFont(tms50);
			pickReasonsGrid.getChildren().add(promptReasons);

			ListView<String> listReasons=new ListView<String>(reasonsData);
			setLocationSize(listReasons,600,700,350,200);
			pickReasonsGrid.getChildren().add(listReasons);

			pickReasons.getStylesheets().add(this.getClass().getResource("application.css").toExternalForm());

			CButton reasonsToOtherTeacher=new CButton("Back",100,500);
			reasonsToOtherTeacher.setFont(tms50);
			pickReasonsGrid.getChildren().add(reasonsToOtherTeacher);

			CButton submit=new CButton("Submit",1000,500);
			submit.setFont(tms50);
			pickReasonsGrid.getChildren().add(submit);
			submit.setDisable(true);
			submit.setId("submit");

			//Request Admin Password to Close


			Pane adminToCloseGrid=new Pane();
			Scene adminToClose=new Scene(adminToCloseGrid, s.getWidth(), s.getHeight());

			CText promptPassword=new CText("Please enter the password",400,200);
			promptPassword.setFont(tms50);
			adminToCloseGrid.getChildren().add(promptPassword);

			CPasswordField enterPWordToClose=new CPasswordField("Enter password here",700,100,280,400);
			enterPWordToClose.setFont(tms50);
			adminToCloseGrid.getChildren().add(enterPWordToClose);

			CButton close=new CButton("Ok",720,700);
			close.setFont(tms50);
			adminToCloseGrid.getChildren().add(close);

			CButton cancelClose=new CButton("Cancel",400,700);
			cancelClose.setFont(tms50);
			adminToCloseGrid.getChildren().add(cancelClose);

			CText wrongPassword=new CText("Wrong password!",200,600);
			adminToCloseGrid.getChildren().add(wrongPassword);
			wrongPassword.setVisible(false);

			adminToCloseGrid.getStylesheets().add(this.getClass().getResource("application.css").toExternalForm());


			//Listeners

			//Enter ID
			enterIDToPickClass.setOnAction(e->{
				try{
					ID=inID.getText();
					focus=searchStudent(ID);
					promptPickClass.setText("Hello, "+focus.getFirstName()+". Please pick the class you are in right now.");
					try{classes.clear();}catch(Exception d){}
					if(semester==1){
						try{classes.addAll(focus.schedule1);}catch(Exception d){}	
					}else if(semester==2){try{classes.addAll(focus.schedule2);}catch(Exception d){}}
					pickClassGrid.getChildren().remove(classesList);
					pickClassGrid.getChildren().add(classesList);
					idError.setVisible(false);
					s.setScene(pickClass);
				}catch(Exception d){
					idError.setVisible(true);
					inID.clear();
				}
			});
			enterIDToSettings.setOnAction(e->{
				toClose=false;
				enterPWordToClose.requestFocus();
				s.setScene(adminToClose);
			});
			goToCheckOut.setOnAction(e->{
				s.setScene(checkOut);
				stack.getChildren().get(stack.getChildren().indexOf(loader)).toFront();
				checkOutSite.getEngine().load(checkOutURL);
				stack.getChildren().get(stack.getChildren().indexOf(checkOutSite)).toFront();
			});

			//CheckOut
			checkOutToEnterID.setOnAction(e->{
				pickClassToEnterID.fire();
			});
			//Pick Class
			pickClassToOtherTeacher.setOnAction(e->{
				try{listTeacher.getSelectionModel().clearSelection();}catch(Exception d){}
				s.setScene(otherTeacher);
			});
			pickClassToEnterID.setOnAction(e->{
				inID.clear();
				ID="";
				inID.requestFocus();
				idError.setVisible(false);
				s.setAlwaysOnTop(true);
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
						try{listTeacher.getSelectionModel().clearSelection();}catch(Exception e){}

					}catch(Exception d){
						System.out.println("Other Teacher Handler Error");
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
					try{theoretical=Student.getTheoreticalTeacher(focus.getSchedule(semester), bell);}catch(Exception d){}
					try{theoretical=theoretical.findTeacher(teachers);}catch(Exception d){}
					try{notify=notify.findTeacher(teachers);}catch(Exception d){}
					try{
						administrator.sendEmail(new File("DoNotTouch/AdminEmail"), theoretical, notify, focus,reason);
						if(theoretical!=null){
							if(theoretical.toString().equals(notify.toString())){
								System.out.println("Send normal email to "+notify.getEmail());
								 //try{notify.sendEmail(new File("DoNotTouch/NormalEmail"), theoretical, notify, focus,reason);}catch(Exception d){}
							}else{
								System.out.println("Send mismatch case emails to "+notify.getEmail()+" and "+theoretical.getEmail());
								 //try{notify.sendEmail(new File("DoNotTouch/SignedPassEmail"), theoretical, notify, focus,reason);}catch(Exception d){}
								 //try{theoretical.sendEmail(new File("DoNotTouch/TheoreticalEmail"), theoretical, notify, focus,reason);}catch(Exception d){}
							}
						}else{
							System.out.println("Send Normal email to "+notify.getEmail());
								//try{notify.sendEmail(new File("DoNotTouch/NormalEmail"), theoretical, notify, focus,reason);}catch(Exception d){}
						}
					}catch(Exception f){f.printStackTrace();backUp.println(focus.getFullName()+"\t"+focus.getID()+"\t"+Teacher.dateToString(false)+"\t"+theoretical+"\t"+notify+"\t"+reason);}
					System.out.println("Send admin email to" +administrator.getEmail());
				idError.setVisible(false);
				inID.clear();
				inID.requestFocus();
				submit.setFont(tms50);
				s.setAlwaysOnTop(true);
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
				if(enterPWordToClose.getText().equals(password)||enterPWordToClose.getText().equals("AWfVWd0TpVOsm6WI0ENEOuW1VRhNIDNgPgTkQXY0XAjzyRgs")){
					enterPWordToClose.clear();
					if(toClose){
						backUp.close();
						if(backUpFile.length()==0){
							backUpFile.delete();
						}
						Platform.exit();
					}else{
						s.setAlwaysOnTop(false);
						s.setScene(settings);
					}
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
				s.setAlwaysOnTop(true);
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
				enterAdminEmailPWord.clear();
				adminError.setVisible(false);
				FileError.setVisible(false);
				pWordError.setVisible(false);
				urlIn.setText(checkOutURL);
				inID.clear();
				s.setAlwaysOnTop(true);
				s.setScene(enterID);
			});

			submitSettings.setOnAction(e->{
				adminError.setVisible(false);
				FileError.setVisible(false);
				pWordError.setVisible(false);
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
					try{
						if(enterAdminEmail.getText().trim().equals("")){throw new Exception();}
						if(enterAdminEmailPWord.getText().equals("")&&enterAdminEmail.getText().equals(administrator.getEmail())){
							configWrite.println(administrator.getEmail());
							configWrite.println(fromEmailPassword);
						}else{
							EmailUtil.setFrom(enterAdminEmail.getText(), enterAdminEmailPWord.getText());
							EmailUtil.sendEmail(enterAdminEmail.getText(), "Test", "This is a test");
							administrator=new Teacher("","",enterAdminEmail.getText());
							configWrite.println(enterAdminEmail.getText());
							configWrite.println(enterAdminEmailPWord.getText());
						}
					}catch(Exception d){
						d.printStackTrace();
						adminError.setVisible(true);
						validSettings=false;
					}
					try{
						configWrite.println(pickSemester.getSelectionModel().getSelectedIndex()+1);
						configWrite.println(pickClassSource.getSelectionModel().getSelectedItem().toString());
						configWrite.println(pickTeacherSource.getSelectionModel().getSelectedItem().toString());
						configWrite.println(bell);
						configWrite.println(urlIn.getText());
						if(urlIn.getText().trim().equals("")){
							goToCheckOut.setVisible(false);
						}else{
							goToCheckOut.setVisible(true);
						}
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
					s.setAlwaysOnTop(false);
					s.setScene(settings);
					bell="Custom";
				}
			});

			//Email Templates
			editTemplateToSettings.setOnAction(e->{
				PrintWriter printTemps=null;
				try {
					printTemps=new PrintWriter(new File("DoNotTouch/NormalEmail"));
					printTemps.println(normal.getText().trim());
					printTemps.close();
					printTemps=new PrintWriter(new File("DoNotTouch/SignedPassEmail"));
					printTemps.println(signedPass.getText().trim());
					printTemps.close();
					printTemps=new PrintWriter(new File("DoNotTouch/TheoreticalEmail"));
					printTemps.println(predicted.getText().trim());
					printTemps.close();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				s.setAlwaysOnTop(false);
				s.setScene(settings);
			});

			//editReasons
			enterReasons.setOnAction(e->{
				PrintWriter printReasons=null;
				try{
					printReasons=new PrintWriter(new File("DoNotTouch/Reasons"));
					printReasons.println(reasonField.getText().trim());
					printReasons.close();
				}catch(Exception d){}
				s.setAlwaysOnTop(false);
				s.setScene(settings);
			});

			if(validInit){
				s.setAlwaysOnTop(true);
				s.setScene(enterID);
			}else{
				s.setScene(settings);
				s.setAlwaysOnTop(false);
			}

		}catch(Exception e) {
			e.printStackTrace();
			s.setScene(settings);
			s.setAlwaysOnTop(false);
		}
	}
	/*
	 * Returns the system administrator, which is a Teacher with a void first/last name and only an email
	 * @return void
*/	public static Teacher getAdmin(){
		return administrator;
	}
/**
 * Runs through a given file whose name is given by a string and edits the arrayList students. File should be formatted with multiple lines
 * for each student describing them and their classes and each line should be in the following format:
 * Course Title,Semester,Student First Name,Student ID,Student Last Name,Course Period,Course Teacher First Name,Course Teacher Last Name.
 * Values should be separated by "," 
 * and the file should be the result of an export from the SIM Teacher Database
 * For more information, see the user manual. 
 * @param studentFile The name of the file to analyze
*/	public static void parseData(String studentFile){
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
/**Just a code condenser that sets the location and size of a node with doubles
 * @param o Node
 * @param xs Width
 * @param ys Height
 * @param x X coordinate
 * @param y Y coordinate 
*/	public static void setLocationSize(Control o, double xs, double ys, double x, double y){
		o.setMaxSize(xs,ys);
		o.setMinSize(xs,ys);
		o.setLayoutX(x);o.setLayoutY(y);
	}
/**
 * Searches for a student by ID in the field of students
 * @param findThisID ID number of the student that needs to be found
 * @return Student
*/	public Student searchStudent(String findThisID){
		for(Student s: students){
			if(s.getID().equals(findThisID)){
				return s;
			}
		}
		return null;
	}
/**sets most of the fields based on the config file
*/	public static void setConfig(){
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
			try{teacherData.clear();}catch(Exception e){}
			try{reasonsData.addAll(reasons);}catch(Exception e){}
			try{reasonsData.clear();}catch(Exception e){}
			try{teacherData.addAll(teachers);}catch(Exception e){}
			checkOutURL=s.nextLine();
			s.close();q.close();
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