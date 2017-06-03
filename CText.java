package application;

import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class CText extends Text{
	static double width;
	static double height;
	public CText(String s, double x, double y){
		super(s);
		setX((x/1920)*width);
		setY((y/1080)*height);
		setId("text");
		setFont(new Font("Trebuchet MS",(30.0/1920)*width));
	}
	public static void setDim(double x, double y){
		width=x;
		height=y;
	}
}

