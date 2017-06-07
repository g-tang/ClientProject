package application;

import javafx.scene.control.TextArea;
import javafx.scene.text.Font;

public class CTextArea extends TextArea{
	public CTextArea(String s, double sx, double sy, double x, double y){
		super(s);
		setWrapText(true);
		setMinSize(sx,sy);
		setMaxSize(sx,sy);
		setLayoutX(x);
		setLayoutY(y);
		setFont(new Font("Avenir Next",27));
	}
}
