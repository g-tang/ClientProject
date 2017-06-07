package application;

import javafx.scene.control.PasswordField;
import javafx.scene.text.Font;

public class CPasswordField extends PasswordField{
	public CPasswordField(String s, double sx, double sy, double x, double y){
		super();
		setPromptText(s);
		setMinSize(sx,sy);
		setMaxSize(sx,sy);
		setLayoutX(x);
		setLayoutY(y);
		setFont(new Font("Avenir Next",27));
	}
}
