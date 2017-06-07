package application;

import javafx.scene.control.Button;
import javafx.scene.text.Font;

public class CButton extends Button{
	public CButton(String s, double x, double y){
		super(s);
		setLayoutX(x);
		setLayoutY(y);
		setFont(new Font("Avenir Next",27));
		setId("button");
	}
}
