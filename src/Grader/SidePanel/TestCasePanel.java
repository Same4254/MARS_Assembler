package Grader.SidePanel;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.HashSet;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import Grader.TestCase;
import mars.mips.hardware.Register;
import mars.mips.hardware.RegisterFile;

public class TestCasePanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private TestCase testCase;
	
	private JScrollPane scrollPane;
	private JPanel scrollViewport;
	
	private HashSet<String> trackedRegisters;
	private ArrayList<JCheckBox> trackedRegisterButtons;
	private JPanel trackedRegistersButtonPanel;
	
	private JPanel controlsPanel;
	
	public TestCasePanel() {
		setLayout(new BorderLayout());
		
		//********** Scroll Content
		scrollPane = new JScrollPane();
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		
		scrollViewport = new JPanel();
		scrollViewport.setLayout(new BoxLayout(scrollViewport, BoxLayout.Y_AXIS));
		
		//***** Tracked Registers
		trackedRegisters = new HashSet<String>();
		
		JLabel trackedRegistersLabel = new JLabel("Tracked Registers");
		trackedRegistersLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		scrollViewport.add(trackedRegistersLabel);
		
		trackedRegistersButtonPanel = new JPanel();
		trackedRegistersButtonPanel.setLayout(new GridLayout(8, 4));
		
		this.trackedRegisterButtons = new ArrayList<JCheckBox>();
		for(Register r : RegisterFile.getRegisters()) {
			JCheckBox j = new JCheckBox(r.getName());
			j.addItemListener(event -> {
				if(event.getStateChange() == ItemEvent.SELECTED)
					trackedRegisters.add(j.getText());
				else
					trackedRegisters.remove(j.getText());
			});
			
			trackedRegisterButtons.add(j);
			trackedRegistersButtonPanel.add(j);
		}
		
		scrollViewport.add(trackedRegistersButtonPanel);
		scrollPane.setViewportView(scrollViewport);
		
		//********** Controls
		controlsPanel = new JPanel();
		
		JButton saveButton = new JButton("Save");
		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(testCase != null) {
					testCase.setTrackedRegisters(trackedRegisters);
					
					testCase.getTestCaseCollection().save();
				}
			}
		});
		
		controlsPanel.add(saveButton);
		
		add(scrollPane, BorderLayout.CENTER);
		add(controlsPanel, BorderLayout.SOUTH);
	}
	
	public void setTestCase(TestCase testCase ) {
		this.testCase = testCase;
		
		for(JCheckBox b : trackedRegisterButtons) {
			if(testCase.isRegisterTracked(b.getText()))
				b.setSelected(true);
			else
				b.setSelected(false);
		}
	}
}
