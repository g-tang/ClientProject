package application;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;

public class TeacherTestLookup {

	@Test
	public void testGetTeacherInfo() {
		assertEquals(Teacher.getTeacherInfo(new File("TestCaseNormalTeachEmail")),new ArrayList<Teacher>(Arrays.asList(new Teacher("Katherine", "Horan","Katherine_E_Horan@mcpsmd.org"),new Teacher("Annemarie", "Sartschev","Annemarie_Sartschev@mcpsmd.org"),new Teacher("Joanne", "Zachrel","Joanne_S_Zachrel@mcpsmd.org"))));
		assertEquals(Teacher.getTeacherInfo(new File("TestCaseRepeatTeachEmail")),new ArrayList<Teacher>(Arrays.asList(new Teacher("Mark", "Curran","Mark_W_Curran@mcpsmd.org"),new Teacher("Katherine", "Horan","Katherine_E_Horan@mcpsmd.org"),new Teacher("Annemarie", "Sartschev","Annemarie_Sartschev@mcpsmd.org"),new Teacher("Joanne", "Zachrel","Joanne_S_Zachrel@mcpsmd.org"))));

	}

}
