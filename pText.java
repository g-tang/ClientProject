package application;

import javafx.scene.text.Text;
import javafx.stage.Stage;

public class pText extends Text{
	private static double width=0;
	private static double height=0;
	public pText(String s, double x, double y){
		super(s);
		setX((x/1920)*width);
		setY((y/1080)*height);
		setId("text");
	}
	public static void setDims(double a, double b){
		width=a;height=b;
	}
}
