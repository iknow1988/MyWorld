/*
 * File: NameSurfer.java
 * ---------------------
 * When it is finished, this program will implements the viewer for
 * the baby-name database described in the assignment handout.
 */

import java.awt.event.*;
import javax.swing.*;

@SuppressWarnings("serial")
public class NameSurfer extends JFrame implements NameSurferConstants,
		ActionListener {

	private NameSurferGraph graph;
	private JLabel label;
	private JTextField nameTextbox;
	private JComboBox<String> genderCombo;
	private JButton graphButton;
	private JButton clearButton;
	private INameSurferDataBase database;

	private void initUI() {
		this.setTitle("NameSurfer");
		this.setSize(APPLICATION_WIDTH, APPLICATION_HEIGHT);
		String[] comboOptions = { GENDER_MALE, GENDER_FEMALE };

		graph = new NameSurferGraph();
		label = new JLabel("Name ");
		nameTextbox = new JTextField(25);
		genderCombo = new JComboBox<String>(comboOptions);
		graphButton = new JButton("Graph");
		clearButton = new JButton("Clear");
		database = new NameSurferDataBase(NAMES_DATA_FOLDER);

		JPanel southPanel = new JPanel();

		southPanel.add(label);
		southPanel.add(nameTextbox);
		southPanel.add(genderCombo);
		southPanel.add(graphButton);
		southPanel.add(clearButton);

		this.getContentPane().add("South", southPanel);
		this.getContentPane().add("Center", graph);
	}

	private void initializeListeners() {
		nameTextbox.addActionListener(this);
		graphButton.addActionListener(this);
		clearButton.addActionListener(this);
	}

	public static void main(String args[]) {
		JFrame frame = new NameSurfer();
		frame.setVisible(true);
	}

	public NameSurfer() {
		initUI();
		initializeListeners();
	}

	/* Method: actionPerformed(e) */
	/**
	 * This class is responsible for detecting when the buttons are clicked, so
	 * you will have to define a method to respond to button actions.
	 */

	public void actionPerformed(ActionEvent e) {

		String name = nameTextbox.getText();
		if (name.length() == 0 && e.getSource() != clearButton) {
			Utility.showErrorDialog("Invalid Name, Please enter a name.");
			return;
		} else if ((nameTextbox.getText().length() != 0 && e.getSource() == graphButton)
				|| e.getSource() != clearButton) {
			INameSurferEntry entry = database.findEntry(name,
					(String) genderCombo.getSelectedItem());
			if (entry != null) {
				graph.addEntry(entry);
				graph.update();
			} else {
				Utility.showErrorDialog("Invalid Name, Name not found in database.");
				return;
			}
		} else if (e.getSource() == clearButton) {
			graph.clear();
			graph.update();
		} else {
			Utility.showErrorDialog("Invalid Name, Name not found in database.");
		}
		nameTextbox.setText("");
	}
}
