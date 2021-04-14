package Grader;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

import com.formdev.flatlaf.FlatDarkLaf;

import Grader.Questions.QuestionsTreePanel;
import Grader.SidePanel.SidePanel;
import mars.Globals;

public class Grader extends JPanel {
	private static final long serialVersionUID = 1L;

	private QuestionsTreePanel questionsTreePanel;
	private SidePanel sidePanel;
	
	public Grader() {
		this.questionsTreePanel = new QuestionsTreePanel(this);
		this.questionsTreePanel.setFolder("TestAssignment");
		
		this.sidePanel = new SidePanel();
		
		setLayout(new BorderLayout());
		add(questionsTreePanel, BorderLayout.CENTER);
		add(sidePanel, BorderLayout.EAST);
	}
	
	public QuestionsTreePanel getQuestionsTreePanel() { return questionsTreePanel; }
	public SidePanel getSidePanel() { return sidePanel; }

	public static void main(String[] args) {
		Globals.initialize(false);
		
		try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception e) {
        	e.printStackTrace();
        }
		
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(800, 600);
		
		frame.add(new Grader());
		
		frame.setVisible(true);
		
//		TestCase c = new TestCase();
//		c.setPointValue(5);
//		c.getInputRegisterValues().put("$t0", 5);
//		c.getTrackedRegisters().add("$t0");
//		
//		c.generateSolutionConditions("Solution.s");
//		
//		JSONFileIO.writeJSONObject("TestCases.json", c.generateJSONObject());
//		
//		TestCase readCase = new TestCase(JSONFileIO.readJSONObject("TestCases.json"));
//		
//		System.out.println(readCase.testStudentCode("Student.s"));
	}
}
