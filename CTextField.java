package application;

import javafx.scene.control.TextField;
import javafx.scene.text.Font;

public class CTextField extends TextField{
	public CTextField(String s, double sx, double sy, double x, double y){
		super(s);
		setMinSize(sx,sy);
		setMaxSize(sx,sy);
		setLayoutX(x);
		setLayoutY(y);
		setFont(new Font("Avenir Next",27));
	}
}
