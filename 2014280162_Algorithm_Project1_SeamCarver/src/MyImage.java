import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import javax.swing.*;

public final class MyImage {
	private BufferedImage image;
	private int width;
	private int height;
	private JFrame frame;
	private int x = 0;
	private int y = 0;
	private String fileName;

	public MyImage(int w, int h) {
		width = w;
		height = h;
		image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
	}

	public MyImage(BufferedImage img, String filename) {
		this.image = img;
		width = img.getWidth(null);
		height = img.getHeight(null);
		this.fileName = filename;
	}

	public MyImage(MyImage img) {
		width = img.width();
		height = img.height();
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		for (int x = 0; x < width(); x++)
			for (int y = 0; y < height(); y++)
				image.setRGB(x, y, img.get(x, y).getRGB());
	}

	public void viewImage() {
		if (frame == null) {
			frame = new JFrame();
			frame.setTitle(this.fileName);
			frame.setMaximumSize(new Dimension(width, height));
			frame.setContentPane(new JLabel(new ImageIcon(image)));
			frame.pack();
			frame.setResizable(false);
			frame.setLocation(x, y);
			frame.setVisible(true);
		}
		frame.repaint();
	}

	public int height() {
		return this.height;
	}

	public int width() {
		return this.width;
	}

	public Color get(int x, int y) {
		return new Color(image.getRGB(x, y));
	}

	public void set(int x, int y, Color color) {
		image.setRGB(x, y, color.getRGB());
	}

	public void setLocation(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
}
