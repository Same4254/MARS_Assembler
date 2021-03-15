package Grader;

import mars.Globals;

public class Grader {
	public static void main(String[] args) {
		Globals.initialize(false);
		
		TestCase c = new TestCase();
		c.setPointValue(5);
		c.getInputRegisterValues().put("$t0", 5);
		c.getTrackedRegisters().add("$t0");
		
		c.generateSolutionConditions("Solution.s");
		
		JSONFileIO.writeJSONObject("TestCases.json", c.generateJSONObject());
		
		TestCase readCase = new TestCase(JSONFileIO.readJSONObject("TestCases.json"));
		
		System.out.println(readCase.testStudentCode("Student.s"));
	}
}
