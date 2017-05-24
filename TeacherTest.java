package application;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Test;

public class TeacherTest{

	@Test
	public void testDateToString() {
		assertEquals(Teacher.dateToString(true), "5/22 at 11:55 PM");
		assertEquals(Teacher.dateToString(false), "11:55 PM");
	}

}
