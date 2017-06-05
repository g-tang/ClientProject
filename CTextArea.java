package application;

import javafx.scene.control.TextArea;
import javafx.scene.text.Font;

public class CTextArea extends TextArea{
	public CTextArea(String s, double sx, double sy, double x, double y){
		super(s);
		setWrapText(true);
		setMinSize((sx/1920)*CText.width,(sy/1080)*CText.height);
		setMaxSize((sx/1920)*CText.width,(sy/1080)*CText.height);
		setLayoutX((x/1920)*CText.width);
		setLayoutY((y/1080)*CText.height);
		setFont(new Font("Trebuchet MS",(30.0/1920)*CText.width));
	}
}
