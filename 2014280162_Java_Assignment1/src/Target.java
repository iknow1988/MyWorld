import acm.graphics.GOval;
import java.awt.Color;
import acm.program.*;

public class Target extends GraphicsProgram {
	
	public void run() {
		add(Circle(72, Color.RED));
		add(Circle(46.8, Color.WHITE));
		add(Circle(21.6, Color.RED));
	}

	private GOval Circle(double radius, Color color) {
		GOval circle = new GOval(getWidth() / 2 - radius, getHeight() / 2
				- radius, 2 * radius, 2 * radius);
		circle.setFilled(true);
		circle.setFillColor(color);
		circle.setColor(color);

		return circle;
	}
}
