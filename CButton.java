package application;

import javafx.scene.control.Button;
import javafx.scene.text.Font;

public class CButton extends Button{
	public CButton(String s, double x, double y){
		super(s);
		setLayoutX((x/1920)*CText.width);
		setLayoutY((y/1080)*CText.height);
		setFont(new Font("Trebuchet MS",(30.0/1920)*CText.width));
		setId("button");
	}
}
