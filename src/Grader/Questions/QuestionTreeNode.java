package Grader.Questions;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.tree.DefaultMutableTreeNode;

import Grader.TestCase;
import Grader.TestCaseCollectionFile;

public class QuestionTreeNode extends DefaultMutableTreeNode {
	private static final long serialVersionUID = 1L;

	private File folder;
	private TestCaseCollectionFile testCasesCollection;
	
	public QuestionTreeNode(File folder) {
		super(folder.getName());
		this.folder = folder;
		
		File solutionCodeFile = new File(folder, "Solution.s");
		if(!solutionCodeFile.exists()) {
			try {
				solutionCodeFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		File testCasesFile = new File(folder, "TestCases.json");
		this.testCasesCollection = new TestCaseCollectionFile(testCasesFile);
		
		ArrayList<TestCase> temp = testCasesCollection.getTestCases();
		for(int i = 0; i < temp.size(); i++)
			add(new QuestionTestCaseTreeNode("Test Case " + (i + 1), temp.get(i)));
		
		add(new QuestionSolutionTreeNode(solutionCodeFile));
	}
}
