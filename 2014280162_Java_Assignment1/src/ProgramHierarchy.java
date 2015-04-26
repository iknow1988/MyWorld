import acm.graphics.*;
import acm.program.*;

public class ProgramHierarchy extends GraphicsProgram {

	private static final int BOX_WIDTH = 150;
	private static final int BOX_HEIGHT = 60;

	public void run() {

		this.setSize(800, 600);

		try {
			Thread.sleep(1000);
		} catch (InterruptedException ex) {
			Thread.currentThread().interrupt();
		}

		drawProgramBox();
		drawSubClasses();
		drawLines();
	}

	private void drawSubClasses() {
		drawConsoleBox();
		drawDialogBox();
		drawGraphicsBox();
	}

	private void drawLines() {
		drawProgramToConsoleLine();
		drawProgramToDialogLine();
		drawprogramToGraphicsLine();
	}

	private void drawProgramBox() {
		int x = getWidth() / 2 - BOX_WIDTH / 2;
		int y = getHeight() / 2 - BOX_HEIGHT;

		GRect drawProgramBox = new GRect(x, y, BOX_WIDTH, BOX_HEIGHT);
		add(drawProgramBox);

		GLabel labelProgram = new GLabel("Program", x, y);
		add(labelProgram);
		labelProgram.move((BOX_WIDTH / 2 - labelProgram.getWidth() / 2),
				(BOX_HEIGHT / 2 + labelProgram.getAscent() / 2));
	}

	private void drawConsoleBox() {
		int x = getWidth() / 2 - BOX_WIDTH / 2;
		int y = getHeight() / 2 + BOX_HEIGHT;

		GRect drawConsoleBox = new GRect(x, y, BOX_WIDTH, BOX_HEIGHT);
		add(drawConsoleBox);

		GLabel labelConsole = new GLabel("ConsoleProgram", x, y);
		add(labelConsole);
		labelConsole.move((BOX_WIDTH / 2 - labelConsole.getWidth() / 2),
				(BOX_HEIGHT / 2 + labelConsole.getAscent() / 2));
	}
	
	private void drawDialogBox() {
		int x = getWidth() / 2 + BOX_WIDTH;
		int y = getHeight() / 2 + BOX_HEIGHT;
		GRect drawDialogBox = new GRect(x, y, BOX_WIDTH, BOX_HEIGHT);
		add(drawDialogBox);

		GLabel labelDialog = new GLabel("DialogProgram", x, y);
		add(labelDialog);
		labelDialog.move((BOX_WIDTH / 2 - labelDialog.getWidth() / 2),
				(BOX_HEIGHT / 2 + labelDialog.getAscent() / 2));
	}

	private void drawGraphicsBox() {
		int x = getWidth() / 2 - 2 * BOX_WIDTH;
		int y = getHeight() / 2 + BOX_HEIGHT;
		GRect drawGraphicsBox = new GRect(x, y, BOX_WIDTH, BOX_HEIGHT);
		add(drawGraphicsBox);

		GLabel labelGraphics = new GLabel("GraphicsProgram", x, y);
		add(labelGraphics);
		labelGraphics.move((BOX_WIDTH / 2 - labelGraphics.getWidth() / 2),
				(BOX_HEIGHT / 2 + labelGraphics.getAscent() / 2));
	}
	
	private void drawProgramToConsoleLine() {
		int x1 = getWidth() / 2;
		int y1 = getHeight() / 2;
		int x2 = getWidth() / 2;
		int y2 = getHeight() / 2 + BOX_HEIGHT;
		GLine drawLine = new GLine(x1, y1, x2, y2);
		add(drawLine);
	}

	private void drawProgramToDialogLine() {
		int x1 = getWidth() / 2;
		int y1 = getHeight() / 2;
		int x2 = getWidth() / 2 + 3 * (BOX_WIDTH / 2);
		int y2 = getHeight() / 2 + BOX_HEIGHT;
		GLine drawLine = new GLine(x1, y1, x2, y2);
		add(drawLine);
	}

	private void drawprogramToGraphicsLine() {
		int x1 = getWidth() / 2;
		int y1 = getHeight() / 2;
		int x2 = getWidth() / 2 - 3 * (BOX_WIDTH / 2);
		int y2 = getHeight() / 2 + BOX_HEIGHT;
		GLine drawLine = new GLine(x1, y1, x2, y2);
		add(drawLine);
	}
}