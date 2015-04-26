import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.swing.*;

public class FileApplication extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1599674549388998007L;
	private JFileChooser fc;
	private JFileChooser fc1;
	private JTextField inputFileName = new JTextField(10);
	private JTextField outputFileName = new JTextField(10);
	private JTextArea resultText = new JTextArea();
	private JButton open = new JButton("Choose input file");
	private JButton openOutputputFile = new JButton("Choose output file");
	private JButton go = new JButton("Go");
	private MyFileReader inputFileReader;
	private MyFileReader outputFileReader;

	// private JScrollPane scroll;

	public FileApplication() {
		fc = new JFileChooser();
		fc1 = new JFileChooser();
		inputFileName.setEditable(false);
		// scroll = new JScrollPane(resultText,
		// JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
		// JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		//resultText.setBounds(5, 35, 385, 330);
		//resultText.setLineWrap(true);
		//resultText.setWrapStyleWord(true);
		// scroll.setBounds(20, 30, 100, 40);
		this.setLayout(new GridLayout(3, 2));
		this.setSize(500, 500);
		open.addActionListener(this);
		openOutputputFile.addActionListener(this);
		go.addActionListener(this);
		JPanel panel = new JPanel();
		panel.add(resultText);
		// panel.add(scroll);
		this.getContentPane().add(inputFileName);
		this.getContentPane().add(open);
		this.getContentPane().add(outputFileName);
		this.getContentPane().add(openOutputputFile);
		this.getContentPane().add(panel);
		this.getContentPane().add(go);

	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == go) {
			if (inputFileReader != null && outputFileReader != null) {
				SeamCarver sc = new SeamCarver(inputFileReader.energyMatrix,
						inputFileReader.colNum, inputFileReader.rowNum,
						inputFileReader.seamNum);
				int[][] newMatrix = sc.removeAllSeam();
				System.out.println("Removed Seams");
				// for (int i = 0; i < newMatrix.length; i++) {
				// for (int j = 0; j < newMatrix[i].length; j++) {
				// System.out.print(newMatrix[i][j] + " ");
				// }
				// System.out.println();
				// }
				System.out.println("----");
				int count = 0;
				boolean result = true;
				for (int i = 0; i < outputFileReader.energyMatrix.length; i++) {
					for (int j = 0; j < outputFileReader.energyMatrix[i].length; j++) {
						if (outputFileReader.energyMatrix[i][j] != newMatrix[i][j]) {
							result = false;
							count++;
							System.out.println(i + ", " + j + " In file "
									+ outputFileReader.energyMatrix[i][j]
									+ ", My result" + newMatrix[i][j]);
						}
					}
				}
				if (result)
					resultText.setText("Matched all data");
				else
					resultText.setText(count + " mismatched");
				System.out.println(result);
			} else {
				Utility.showErrorDialog("Please select input file first and then output file. After that click GO");
			}
		} else if (e.getSource() == open) {
			int selected = fc.showOpenDialog(this.getContentPane());
			if (selected == JFileChooser.APPROVE_OPTION) {
				String filename = fc.getSelectedFile().getAbsolutePath();
				inputFileName.setText("You have selected file: "
						+ fc.getSelectedFile().getName());
				File file = new File(filename);
				if (file.isFile()) {
					inputFileReader = new MyFileReader(file.getAbsolutePath());
					try {
						inputFileReader.readFile();
					} catch (IOException ex) {
						// TODO Auto-generated catch block
						ex.printStackTrace();
					}

					System.out.println("Input reading finished");
				}
			}

		} else if (e.getSource() == openOutputputFile) {
			if (inputFileReader != null) {
				int selected = fc1.showOpenDialog(this.getContentPane());
				if (selected == JFileChooser.APPROVE_OPTION) {
					String filename = fc1.getSelectedFile().getAbsolutePath();
					outputFileName.setText("You have selected file: "
							+ fc1.getSelectedFile().getName());
					File file = new File(filename);
					if (file.isFile()) {
						outputFileReader = new MyFileReader(
								file.getAbsolutePath(), inputFileReader.rowNum,
								inputFileReader.colNum
										- inputFileReader.seamNum);
						try {
							outputFileReader.checkFile();
						} catch (IOException ex) {
							// TODO Auto-generated catch block
							ex.printStackTrace();
						}

						System.out.println("Output reading finished");
					}
				}
			} else {
				Utility.showErrorDialog("Please select input file first and then output file");
			}
		}
	}
}
