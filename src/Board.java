import java.util.*;

public class Board {
	final class Coord {
		int row;
		int col;

		public Coord(int r, int c) {
			row = r;
			col = c;
		}
	}

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
			checkRemoval(r, c);
			return true;
		} else
			return false;
	}

	public void checkRemoval() {
		// TODO Check the board for chains with no liberties. Remove them, and
		// update captured values for players
		boolean checked[][] = new boolean[9][9];
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				if (checked[i][j] == false
						&& board[i][j].getContents() != Intersection.Piece.EMPTY) {
					List<Coord> group = new ArrayList<Coord>();
					if (checkGroup(i, j, board[i][j].getContents(), checked,
							group, 0) == 0) {
						removeGroup(group);
					}
				}
			}
		}
	}

	public void checkRemoval(int r, int c) {
		boolean checked[][] = new boolean[9][9];
		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				if (r + i >= 0 && r + i < 9 && c + j >= 0 && c + j < 9) {
					if (checked[r + i][c + j] == false
							&& board[r + i][c + j].getContents() != Intersection.Piece.EMPTY) {
						List<Coord> group = new ArrayList<Coord>();
						if (checkGroup(r + i, c + j,
								board[r + i][c + j].getContents(), checked,
								group, 0) == 0) {
							removeGroup(group);
						}
					}
				}

			}
		}
	}

	private void removeGroup(List<Coord> group) {
		for (Coord groupMember : group) {
			setSpace(groupMember.row, groupMember.col, Intersection.Piece.EMPTY);
		}
	}

	public int checkGroup(int r, int c, Intersection.Piece p,
			boolean checked[][], List<Coord> group, int liberties) {
		if (r >= 0 && c >= 0 && r < 9 && c < 9) {
			if (board[r][c].getContents() == p && checked[r][c] == false) {
				group.add(new Coord(r, c));
				checked[r][c] = true;

				liberties += checkGroup(r - 1, c, p, checked, group, liberties);
				liberties += checkGroup(r, c + 1, p, checked, group, liberties);
				liberties += checkGroup(r + 1, c, p, checked, group, liberties);
				liberties += checkGroup(r, c - 1, p, checked, group, liberties);

			} else if (board[r][c].getContents() == Intersection.Piece.EMPTY) {
				return liberties + 1;
			}
		}
		return liberties;
	}

	public void calculateScores(Player pB, Player pW) {
		// TODO Calculate territory, and add to captured pieces to update scores
	}

	public Intersection.Piece getContents(int r, int c) {
		return board[r][c].getContents();
	}
}
