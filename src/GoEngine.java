import javax.swing.*;
import java.awt.*;
//import java.awt.geom.*;
import java.util.Scanner;

public class GoEngine {
	public DisplayArea display;
	private Board board;
	private Player playerBlack;
	private Player playerWhite;

	Scanner in = new Scanner(System.in);

	public static void main(String[] args) {
		GoEngine engine = new GoEngine();
		engine.createWindow();
		engine.playGo();
	}

	public GoEngine() {
		playerBlack = new Player("Black", Intersection.Piece.BLACK);
		playerWhite = new Player("White", Intersection.Piece.WHITE);
		board = new Board();
		display = new DisplayArea();
		display.setBoard(board);

	}

	// Set up the window
	public void createWindow() {
		JFrame frame = new JFrame("GoEngine");
		frame.setBackground(Color.white);
		frame.setSize(500, 500);
		frame.setContentPane(display);
		frame.addWindowListener(new ExitListener());
		frame.setVisible(true);
	}

	// Main game loop
	public void playGo() {
		while (true) { // TODO This will need termination eventually, but
						// players can't pass yet
			playerMove(playerBlack);
			display.setBoard(board);
			display.paintComponent(display.getGraphics());
			playerMove(playerWhite);
			display.setBoard(board);
			display.paintComponent(display.getGraphics());
		}
	}

	// Prompt a player to make a move
	// TODO Possibly implement some better input, rather than console
	// TODO Implement in Player rather than here, so it can be overridden to
	// allow for AI
	public void playerMove(Player p) {
		boolean valid = false;
		while (valid == false) {
			int row;
			int col;
			System.out.println(p.name + ", select a space");
			System.out.print("Row: ");
			row = in.nextInt();
			System.out.print("Column: ");
			col = in.nextInt();

			if (row >= 0 && row < 9 && col >= 0 && col < 9) {
				valid = board.placePiece(row, col, p.piece);
			} else
				valid = false;

			if (valid == false) {
				System.out.println("Move is invalid, try again.");
			}
		}
	}

}
