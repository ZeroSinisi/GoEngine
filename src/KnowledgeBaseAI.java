import java.util.ArrayList;
import java.util.HashMap;

public class KnowledgeBaseAI extends Player {
	
	ArrayList<String> games;
	HashMap<Integer, Integer> stats;	//<Tile number, Occurrences for win>

	public KnowledgeBaseAI(String n, Intersection.Piece p, DataManager dm) {
		super(n, p);
		games = new ArrayList<String>();
		stats = new HashMap<Integer, Integer>();
		for(int i = 0; i < dm.getTotalGames(); i++) {
			games.add(dm.readGame(i));
		}
		for(int i = 0; i < 81; i++) {
			stats.put(i, 0);
		}
		String myColor;
		if(this.piece == Intersection.Piece.BLACK) {
			myColor = "B";
			for(String game: games) {
				if(game.contains(myColor)) {
					for(int i = 0; i < 81; i++) {
						if(game.charAt(i) == '2') {
							int temp = stats.get(i);
							stats.put(i, temp++);
						}
					}
				}
			}
		} else {
			myColor = "W";
			for(String game: games) {
				if(game.contains(myColor)) {
					for(int i = 0; i < 81; i++) {
						if(game.charAt(i) == '1') {
							int temp = stats.get(i);
							stats.put(i, temp++);
						}
					}
				}
			}
		}

	}

	public void promptMove(GoEngine g) {
		int r;
		int c;
		int highestWin = 0;
		int highestPlace = -1;
		
		for(r = 0; r < 9; r++) {
			for(c = 0; c < 9; c++) {
				int temp = r*9 + c;
				if(g.board.getContents(r, c) == Intersection.Piece.EMPTY) {
					int tempValue = stats.get(temp);
					if(tempValue > highestWin) {
						highestWin = tempValue;
						highestPlace = temp;
					}
				}
			}
		}
		
		if(highestPlace != -1) {
			r = highestPlace / 9;
			c = highestPlace % 9;
			g.board.placePiece(r, c, this.piece);
		} else {
			g.board.passTurn();
		}
	}
	
}
