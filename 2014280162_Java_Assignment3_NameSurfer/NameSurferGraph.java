/*
 * File: NameSurferGraph.java
 * ---------------------------
 * This class represents the canvas on which the graph of
 * names is drawn. This class is responsible for updating
 * (redrawing) the graphs whenever the list of entries changes or the window is resized.
 */

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;

import javax.swing.*;

public class NameSurferGraph extends JComponent implements NameSurferConstants,
		ComponentListener {

	private int Height;
	private int Width;
	private ArrayList<INameSurferEntry> displayedData;
	private static final long serialVersionUID = -4742942976350763959L;

	public NameSurferGraph() {
		addComponentListener(this);
		displayedData = new ArrayList<INameSurferEntry>();
	}

	public void clear() {
		displayedData.clear();
		update();
	}

	public void addEntry(INameSurferEntry entry) {
		if (entry != null && !displayedData.contains(entry)) {
			displayedData.add(entry);
			update();
		}
	}

	public void update() {
		this.repaint();
	}

	private void drawGraph(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g.setColor(Color.BLACK);

		// Draw border
		g.drawLine(0, GRAPH_MARGIN_SIZE, Width, GRAPH_MARGIN_SIZE);
		g.drawLine(0, Height - GRAPH_MARGIN_SIZE, Width, Height
				- GRAPH_MARGIN_SIZE);

		// draw vertical lines
		for (int j = 0; j <= N_SECTIONS; j++) {
			g.drawString(String.valueOf(START_YEAR - 1 + (j * INTERVAL)), j
					* (Width / N_SECTIONS) + XOffset, Height);
			g2d.draw(new Line2D.Double(j * (Width * 1.0 / N_SECTIONS), 0, j
					* (Width * 1.0 / N_SECTIONS), Height));
		}
	}

	private void plotGraph(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		if (!displayedData.isEmpty()) {
			for (INameSurferEntry entry : displayedData) {
				Color color = (entry.getGender() == GENDER_FEMALE) ? Color.BLUE
						: Color.BLACK;
				for (int i = 1; i < N_YEARS; i++) {
					int j = i - 1;
					int rank1 = entry.getRank(j + START_YEAR);
					int rank2 = entry.getRank(i + START_YEAR);
					int y1 = getYfromRank(rank1);
					int y2 = getYfromRank(rank2);

					if ((j + START_YEAR) % 10 == 1) {
						g.setColor(color);
						g.drawString(entry.getName() + " " + rank1, i
								* (Width / N_YEARS) + XOffset, y1);
					}
					g.setColor(Color.BLACK);
					g2d.draw(new Line2D.Double(j * (Width * 1.0 / N_YEARS), y1,
							i * (Width * 1.0 / N_YEARS), y2));
				}
			}
		}
	}

	public void paint(Graphics g) {
		super.paint(g);
		Height = this.getHeight();
		Width = this.getWidth();
		drawGraph(g);
		plotGraph(g);
	}

	private int getYfromRank(int rank) {
		if (rank == 0)
			return GRAPH_MARGIN_SIZE;

		int y = (rank * (this.getHeight() - 2 * GRAPH_MARGIN_SIZE)) / MAX_RANK
				+ GRAPH_MARGIN_SIZE;

		if (y < GRAPH_MARGIN_SIZE)
			return GRAPH_MARGIN_SIZE;

		if (y > Height - GRAPH_MARGIN_SIZE)
			return Height - GRAPH_MARGIN_SIZE;

		return y;
	}

	/* Implementation of the ComponentListener interface */
	public void componentHidden(ComponentEvent e) {
	}

	public void componentMoved(ComponentEvent e) {
	}

	public void componentResized(ComponentEvent e) {
		update();
	}

	public void componentShown(ComponentEvent e) {
	}
}
