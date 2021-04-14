package Grader.Questions;

import java.io.File;

import javax.swing.tree.DefaultMutableTreeNode;

public class QuestionSolutionTreeNode extends DefaultMutableTreeNode {
	private static final long serialVersionUID = 1L;

	private File solutionFile;
	
	public QuestionSolutionTreeNode(File solutionFile) {
		super(solutionFile.getName());
		
		this.solutionFile = solutionFile;
	}
	
	public File getSolutionFile() { return solutionFile; }
}
