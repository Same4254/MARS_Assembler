package Grader.SidePanel;

import java.awt.CardLayout;

import javax.swing.JPanel;

import Grader.TestCase;

public class SidePanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private static enum PanelChoice {
		TEST_CASE_PANEL("TestCasePanel");
		
		private String string;
		PanelChoice(String s) {
			this.string = s;
		}
	}
	
	private CardLayout layout;
	private TestCasePanel testCasePanel;
	
	public SidePanel() {
		layout = new CardLayout();
		
		setLayout(layout);
		
		this.testCasePanel = new TestCasePanel();
		
		add(PanelChoice.TEST_CASE_PANEL.string, testCasePanel);
	}
	
	public void setTestCasePanel(TestCase testCase) {
		testCasePanel.setTestCase(testCase);
		layout.show(this, PanelChoice.TEST_CASE_PANEL.string);
	}
}
