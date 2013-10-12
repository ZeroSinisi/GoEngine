import java.awt.Color;
//import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
//import java.awt.Insets;
//import java.util.Random;
//import javax.swing.JFrame;
import javax.swing.JPanel;
//import javax.swing.SwingUtilities;
import java.awt.RenderingHints;
//import java.awt.geom.*;
import java.awt.BasicStroke;

public class DisplayArea extends JPanel {

	private Board drawBoard;
	private float[] dash1 = { 2f, 0f, 2f };
	private BasicStroke bs = new BasicStroke(2, BasicStroke.CAP_SQUARE,
			BasicStroke.JOIN_BEVEL, 5.0f, dash1, 5f);

	// Draw the board
	private void render(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;

		clearScreen(g2d);
		drawLines(g2d);
		setRenderingHints(g2d);
		drawPieces(g2d);
	}

	// Turn on anti-aliasing
	private void setRenderingHints(Graphics2D g2d) {
		RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		rh.put(RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_QUALITY);
		g2d.setRenderingHints(rh);
	}

	private void drawPieces(Graphics2D g2d) {
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				if (drawBoard.getContents(i, j) == Intersection.Piece.EMPTY) {
					continue;
				} else if (drawBoard.getContents(i, j) == Intersection.Piece.BLACK)
					g2d.setColor(Color.black);
				else
					g2d.setColor(Color.white);
				g2d.fillOval(40 * j + 50, 40 * i + 50, 26, 26);
			}
		}
	}

	// Draw board grid
	private void drawLines(Graphics2D g2d) {
		g2d.setStroke(bs);
		g2d.setColor(Color.black);
		for (int i = 0; i < 9; i++) {
			g2d.drawLine(40 * i + 63, 63, 40 * i + 63, 383);
		}
		for (int i = 0; i < 9; i++) {
			g2d.drawLine(63, 40 * i + 63, 383, 40 * i + 63);
		}
	}

	private void clearScreen(Graphics2D g2d) {
		g2d.setColor(Color.GRAY);
		g2d.fillRect(0, 0, getWidth(), getHeight());
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		render(g);
	}

	// Set the board that will be rendered
	public void setBoard(Board b) {
		drawBoard = b;
	}
}
