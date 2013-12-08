import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;


public class DataManager {

	private BufferedReader rdr;
	private BufferedWriter wrt;
	private int totalGames;
	
	public DataManager() {
		totalGames = 0;
		try {
			wrt = new BufferedWriter(new FileWriter("GoData.txt", true));
			rdr = new BufferedReader(new FileReader("GoData.txt"));
			rdr.mark(0);
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
	public void recordGame(Board game) {
		int r;
		int c;
		Intersection.Piece piece;
		for(r = 0; r < 9; r++) {
			for(c = 0; c < 9; c++) {
				piece = game.getContents(r, c);
				try {
					if(piece == Intersection.Piece.EMPTY) {
						wrt.write('0');
					} else if (piece == Intersection.Piece.WHITE) {
						wrt.write('1');
					} else if (piece == Intersection.Piece.BLACK) {
						wrt.write('2');
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void recordWinner(int winner) {
		try {
		if(winner == 0) {
			wrt.write('D');
		} else if (winner == 1) {
			wrt.write('W');
		} else if (winner == 2) {
			wrt.write('B');
		}
		wrt.write('\n');
		} catch (IOException e) {
			e.printStackTrace();
		}
		totalGames++;
	}
	
	public void closeWriter() {
		try {
			wrt.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String readGame(int gameNum) {
		String gameRequest = null;
		try {
			rdr.reset();
			rdr.skip(gameNum * 83);
			rdr.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return gameRequest;
	}
	
	public int getTotalGames() {
		return totalGames;
	}
}
