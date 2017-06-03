package application;

import javafx.scene.text.Text;
import javafx.stage.Stage;

public class CText extends Text{
	public static double width=0;
	public static double height=0;
	public CText(String s, double x, double y){
		super(s);
		setX((x/1920)*width);
		setY((y/1080)*height);
		setId("text");
	}
	public static void setDims(double a, double b){
		width=a;height=b;
	}
}
