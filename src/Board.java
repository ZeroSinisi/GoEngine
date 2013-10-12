public class Board {
	Intersection board[][];

	public Board() {
		board = new Intersection[9][9];
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {

				board[i][j] = new Intersection();
			}
		}
	}

	private void setSpace(int r, int c, Intersection.Piece p) {
		board[r][c].setContents(p);
	}

	// Attempt to place a piece. Return false if space is occupied
	public boolean placePiece(int r, int c, Intersection.Piece p) {
		if (board[r][c].getContents() == Intersection.Piece.EMPTY) {
			setSpace(r, c, p);
			return true;
		} else
			return false;
	}

	public void checkRemoval() {
		// TODO Check the board for chains with no liberties. Remove them, and
		// update captured values for players
	}

	public void calculateScores(Player pB, Player pW) {
		// TODO Calculate territory, and add to captured pieces to update scores
	}

	public Intersection.Piece getContents(int r, int c) {
		return board[r][c].getContents();
	}
}
