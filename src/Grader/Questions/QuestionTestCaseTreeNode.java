package Grader.Questions;

import javax.swing.tree.DefaultMutableTreeNode;

import Grader.TestCase;

public class QuestionTestCaseTreeNode extends DefaultMutableTreeNode {
	private static final long serialVersionUID = 1L;

	private TestCase testCase;
	
	public QuestionTestCaseTreeNode(String testCaseName, TestCase testCase) {
		super(testCaseName);
		
		this.testCase = testCase;
	}

	public TestCase getTestCase() { return testCase; }
}
