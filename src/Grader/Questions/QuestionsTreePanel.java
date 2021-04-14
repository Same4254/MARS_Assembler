package Grader.Questions;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import Grader.Grader;

public class QuestionsTreePanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private JTree tree;
	private DefaultMutableTreeNode root; 
	
	public QuestionsTreePanel(Grader grader) {
		root = new DefaultMutableTreeNode();
		tree = new JTree(root);
		
		tree.setRootVisible(false);
		tree.setShowsRootHandles(true);
		
		tree.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				super.mousePressed(e);
				
				if(e.getClickCount() == 2) {
					TreePath path = tree.getPathForLocation(e.getX(), e.getY());
					
					if(path != null) {
						DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
						if(node instanceof QuestionTestCaseTreeNode) {
							grader.getSidePanel().setTestCasePanel(((QuestionTestCaseTreeNode) node).getTestCase());
						}
					}
				}
			}
		});
		
		setLayout(new BorderLayout());
		add(tree, BorderLayout.CENTER);
	}
	
	/**
	 * Folder Format
	 * 	- Questions -> This is the folder passed in
	 * 		- Q1
	 * 			- Solution.s
	 * 			- Test Cases.json
	 * 			- Submissions
	 * 				- Student1.s
	 * 				- Student2.s
	 * 		- Q2
	 * 			- Solution.s
	 * 			- Test Cases.json
	 * 			- Submissions
	 * 				- Student1.s
	 * 				- Student2.s
	 * 
	 * @param folderPath -> Path to the folder (Questions folder)
	 */
	public void setFolder(String folderPath) {
		File questionsFolder = new File(folderPath);
		for(File questionFolder : questionsFolder.listFiles()) {
			root.add(new QuestionTreeNode(questionFolder));
		}
		
		((DefaultTreeModel) tree.getModel()).reload();
	}
}
