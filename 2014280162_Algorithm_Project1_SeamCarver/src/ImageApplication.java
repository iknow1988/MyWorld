import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.*;

public class ImageApplication extends JFrame implements ActionListener {
	public MyImage myImage;
	private static final long serialVersionUID = 1L;
	private JFileChooser fc;
	private JTextField fileName = new JTextField(10);
	private JTextField horizontalSeam = new JTextField(10);
	private JTextField verticalSeam = new JTextField(10);
	private JButton b = new JButton("Open file");
	private JButton go = new JButton("Go");
	private BufferedImage image;

	public ImageApplication() {
		fc = new JFileChooser();
		fileName.setEditable(false);
		this.setLayout(new GridLayout(4, 2));
		this.setSize(300, 300);
		b.addActionListener(this);
		go.addActionListener(this);
		this.getContentPane().add(fileName);
		this.getContentPane().add(b);
		this.getContentPane().add(new JLabel("Horizontal Seam"));
		this.getContentPane().add(horizontalSeam);
		this.getContentPane().add(new JLabel("Vertical Seam"));
		this.getContentPane().add(verticalSeam);
		this.getContentPane().add(go);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == b) {
			int selected = fc.showOpenDialog(this.getContentPane());
			if (selected == JFileChooser.APPROVE_OPTION) {
				String filename = fc.getSelectedFile().getAbsolutePath();
				fileName.setText("You have selected file: "
						+ fc.getSelectedFile().getName());
				try {
					File file = new File(filename);
					if (file.isFile()) {
						image = ImageIO.read(file);
					} else {
						URL url = getClass().getResource(filename);
						if (url == null) {
							url = new URL(filename);
						}
						image = ImageIO.read(url);
					}
					myImage = new MyImage(image, "Input Image");
				} catch (IOException ex) {
					// e.printStackTrace();
					throw new RuntimeException("Could not open file: "
							+ filename);
				}
			}
			myImage.viewImage();
		} else if (e.getSource() == go) {
			int hn = 0;
			int vn = 0;
			if (!horizontalSeam.getText().isEmpty()
					|| !verticalSeam.getText().isEmpty()) {
				if (!horizontalSeam.getText().isEmpty()
						&& !verticalSeam.getText().isEmpty()) {
					try {
						hn = Integer.parseInt(horizontalSeam.getText());
						vn = Integer.parseInt(verticalSeam.getText());
					} catch (NumberFormatException ex) {
						Utility.showErrorDialog("Please provide valid integer");
					}
					if (hn >= myImage.height() - 100
							|| vn >= myImage.width() - 100) {
						Utility.showErrorDialog("Seam is too big");
					} else {
						SeamCarver sc = new SeamCarver(myImage,
								Integer.parseInt(horizontalSeam.getText()));
						sc.removeAllSeam();
						sc.setSeam(Integer.parseInt(verticalSeam.getText()));
						sc.transpose();
						sc.removeAllSeam();
						sc.transpose();
						MyImage outImg = sc.getImage();
						outImg.setFileName("Output");
						outImg.setLocation(myImage.width(), 0);
						outImg.viewImage();
					}
				} else if (verticalSeam.getText().isEmpty()) {
					try {
						hn = Integer.parseInt(horizontalSeam.getText());
					} catch (NumberFormatException ex) {
						Utility.showErrorDialog("Please provide valid integer");
					}
					if (hn >= myImage.height() - 100
							|| vn >= myImage.width() - 100) {
						Utility.showErrorDialog("Seam is too big");
					} else {
						SeamCarver sc = new SeamCarver(myImage,
								Integer.parseInt(horizontalSeam.getText()));
						sc.transpose();
						sc.removeAllSeam();
						sc.transpose();
						MyImage outImg = sc.getImage();
						outImg.setFileName("Output");
						outImg.setLocation(myImage.width(), 0);
						outImg.viewImage();
					}
				} else {
					try {
						vn = Integer.parseInt(verticalSeam.getText());
					} catch (NumberFormatException ex) {
						Utility.showErrorDialog("Please provide valid integer");
					}
					if (hn >= myImage.height() - 100
							|| vn >= myImage.width() - 100) {
						Utility.showErrorDialog("Seam is too big");
					} else {
						SeamCarver sc = new SeamCarver(myImage,
								Integer.parseInt(verticalSeam.getText()));
						sc.removeAllSeam();
						MyImage outImg = sc.getImage();
						outImg.setFileName("Output");
						outImg.setLocation(myImage.width(), 0);
						outImg.viewImage();
					}
				}
			} else {
				Utility.showErrorDialog("Please give a valid input");
			}
		}
	}

}
