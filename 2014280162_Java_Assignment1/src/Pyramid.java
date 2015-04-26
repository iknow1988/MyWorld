import acm.graphics.GRect;
import acm.program.*;

public class Pyramid extends GraphicsProgram {

	private static final double BRICK_WIDTH = 50;
	private static final double BRICK_HEIGHT = 20;
	private static final int BRICK_IN_BASE = 14;

	public void run() {
		this.setSize(800, 600);

		try {
			Thread.sleep(1000);
		} catch (InterruptedException ex) {
			Thread.currentThread().interrupt();
		}

		for (int row = 0; row < BRICK_IN_BASE; row++) {
			int rectsInARow = BRICK_IN_BASE - row;

			for (int rect = 0; rect < rectsInARow; rect++) {
				double x = (this.getWidth() - (BRICK_WIDTH * rectsInARow)) / 2
						+ rect * BRICK_WIDTH;
				double y = this.getHeight() - BRICK_HEIGHT * (row + 1);
				GRect drawRect = new GRect(x, y, BRICK_WIDTH, BRICK_HEIGHT);
				add(drawRect);
			}
		}
	}
}
