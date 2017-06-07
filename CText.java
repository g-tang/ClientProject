package application;

import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class CText extends Text{
	static double width;
	static double height;
	public CText(String s, double x, double y){
		super(s);
		setX(x);
		setY(y);
		setId("text");
		setFont(new Font("Avenir Next",27));
	}
	public static void setDim(double x, double y){
		width=x;
		height=y;
	}
}

