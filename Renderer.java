import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.BasicStroke;
import java.awt.geom.Line2D;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.ArrayList;
import java.util.Iterator;

/** 
* <h1>Renderer</h1>
* Class to render data to image
* during simulation.
*
* @author Fredrik Skoglind (group 11/root)
* @version 1.0 MVP
* @since 2018-04-16
*/
public class Renderer {
	private BufferedImage bi;
	private Graphics2D g;
	private int width;
	private int height;
	private String name;
	Double scale = 1.0;
	Long imageSize = new Long(0);
	Long maxImageSize = new Long(6000*6000);

	public Renderer(String name, int width, int height) {
		this.name = name;
		this.width = width;
		this.height = height;

		this.calculateScale();

		bi = new BufferedImage(scaleValue(this.width), scaleValue(this.height), 
							   BufferedImage.TYPE_INT_RGB);
		g = bi.createGraphics();

		// Set background color
		g.setPaint(Color.black);
		g.fillRect(0, 0, scaleValue(width), scaleValue(height));


	}

	// Calculate scale of.
	private void calculateScale() {
		imageSize = new Long(scaleValue(this.width)) * new Long(scaleValue(this.width)) * 4;
		while(imageSize > maxImageSize) {
			scale += new Double(0.1);
			imageSize = new Long(scaleValue(this.width)) * new Long(scaleValue(this.width)) * 4;
		}
	}

	// Scale pixels.
	private int scaleValue(int value) {
		Double calcValue = new Double(value) / scale;
		return (int)Math.round(calcValue);
	}

	/**
	  * Draw human on canvas
	  * @param x
	  * @param y
	  * @param name
	  * @param human
	  * @param ts
	  */  
	public void drawHuman(int ts, int x, int y, String name, L_Human human) {
		int humanWidth = 30;
		int humanHeight = 30;
		int borderWidth1 = 8;
		int borderWidth2 = 4;

		int humanCenterX = scaleValue(x) - (humanWidth / 2);
		int humanCenterY = scaleValue(y) - (humanHeight / 2);

		// Vitaldata
		TriageState realTriageState = human.getTriage();
		TriageState lastSetTriage = human.getLastTriageState(ts);
		int pulse = human.getPulse();
		int breath = human.getBreathing();
		boolean throat = human.getThroat();

		// Color for real triage (Facit)
		Color realTriageColor = getTriageColor(realTriageState);

		// Color for active triage set
		Color activeTriageColor = getTriageColor(lastSetTriage);

		// Human Border
		g.setPaint(Color.white);
		g.fillOval(humanCenterX-borderWidth1, humanCenterY-borderWidth1, 
				   humanWidth+(borderWidth1*2), humanHeight+(borderWidth1*2));

		// Human (Real Triage)
		g.setPaint(realTriageColor);
		g.fillOval(humanCenterX-borderWidth2, humanCenterY-borderWidth2, 
				   humanWidth+(borderWidth2*2), humanHeight+(borderWidth2*2));

		// Human (Active Triage)
		g.setPaint(activeTriageColor);
		g.fillOval(humanCenterX, humanCenterY, 
				   humanWidth, humanHeight);
	}

	// Get triagecolor from state
	private Color getTriageColor(TriageState triage) {
		Color retColor = Color.white;
		switch(triage) {
			case PRIO1: retColor = Color.red; break;
			case PRIO2: retColor = Color.yellow; break;
			case PRIO3: retColor = Color.green; break;
			case DEAD:  retColor = Color.black; break;
		}

		return retColor;
	}

	/**
	  * Draw trace between points on canvas
	  * @param x
	  * @param y
	  */
	public void drawTrace(int ts, int x1, int y1, int x2, int y2) {
		Color traceColor = Color.orange;
		int lineWidth = 4;

		x1 = scaleValue(x1);
		y1 = scaleValue(y1);
		x2 = scaleValue(x2);
		y2 = scaleValue(y2);

		g.setPaint(traceColor);
		g.setStroke(new BasicStroke(lineWidth));
        g.draw(new Line2D.Float(x1, y1, x2, y2));
	}

	/**
	  * Draw point on canvas
	  * @param x
	  * @param y
	  */
	public void drawPoint(int x, int y) {
		Color borderColor = Color.orange;
		Color fillColor = Color.black;

		int pointWidth = 20;
		int pointHeight = 20;
		int borderWidth = 4;

		int pointCenterX = scaleValue(x) - (pointWidth / 2);
		int pointCenterY = scaleValue(y) - (pointHeight / 2);

		// Border
		g.setPaint(borderColor);
		g.fillOval(pointCenterX-borderWidth, pointCenterY-borderWidth, 
				   pointWidth+(borderWidth*2), pointHeight+(borderWidth*2));

		// Circle
		g.setPaint(fillColor);
		g.fillOval(pointCenterX, pointCenterY, 
				   pointWidth, pointHeight);
	}

	/**
	  * Get image.
	  */
	public BufferedImage getImage() {
		return bi;
	}

	/**
	  * Save image to disk.
	  * @param filename
	  */
	public void saveImage(String filename) {
		try {
			ImageIO.write(bi, "PNG", new File(filename));
		} catch (IOException e) { System.out.println(e.getMessage()); }
	}

	private BufferedImage getHumanImage(Color bodyColor) {
		BufferedImage humanImage = new BufferedImage(30, 80, BufferedImage.TYPE_INT_ARGB);
		return humanImage;
	}
}