package application;

import javafx.scene.control.TextField;
import javafx.scene.text.Font;

public class CTextField extends TextField{
	public CTextField(String s, double sx, double sy, double x, double y){
		super(s);
		setMinSize((sx/1920)*CText.width,(sy/1080)*CText.height);
		setMaxSize((sx/1920)*CText.width,(sy/1080)*CText.height);
		setLayoutX((x/1920)*CText.width);
		setLayoutY((y/1080)*CText.height);
		setFont(new Font("Trebuchet MS",(30.0/1920)*CText.width));
	}
}
