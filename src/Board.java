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
		if (checkValid(r, c, p) == true) {
			setSpace(r, c, p);
			checkRemoval(r, c);
			return true;
		} else
			return false;
	}

	public boolean checkValid(int r, int c, Intersection.Piece p) {
		boolean valid = false;
		boolean checked[][] = new boolean[9][9];
		Intersection tempBoard[][] = new Intersection[9][9];
		copyBoard(tempBoard);
		if (tempBoard[r][c].getContents() == Intersection.Piece.EMPTY) {
			tempBoard[r][c].setContents(p);
			checkRemovalGroup(r + 1, c, tempBoard, checked, p);
			checkRemovalGroup(r - 1, c, tempBoard, checked, p);
			checkRemovalGroup(r, c + 1, tempBoard, checked, p);
			checkRemovalGroup(r, c - 1, tempBoard, checked, p);
			List<Coord> group = new ArrayList<Coord>();
			if (checkGroup(r, c, tempBoard[r][c].getContents(), checked, group,
					0, tempBoard) != 0) {
				valid = true;
			}
		}
		return valid;
	}
	
	private void copyBoard(Intersection[][] b){
		for(int i=0;i<9;i++){
			for(int j=0;j<9;j++){
				b[i][j]=new Intersection();
				b[i][j].setContents(board[i][j].getContents());
			}
		}
	}
	private void checkRemovalGroup(int r, int c, Intersection[][] b,
			boolean[][] checked, Intersection.Piece p) {
		if (r >= 0 && r < 9 && c >= 0 && c < 9) {
			if (b[r][c].getContents() != p
					&& b[r][c].getContents() != Intersection.Piece.EMPTY
					&& checked[r][c] == false) {
				List<Coord> group = new ArrayList<Coord>();
				if (checkGroup(r, c, b[r][c].getContents(), checked, group, 0, b) == 0) {
					removeGroup(group, b);
				}
			}
		}
	}

	public void checkRemoval() {
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

	/*public void checkRemoval(int r, int c) {
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
	}*/
	
	public void checkRemoval(int r, int c) {
		boolean checked[][] = new boolean[9][9];
		checkRemovalGroup(r+1,c,board,checked,board[r][c].getContents());
		checkRemovalGroup(r-1,c,board,checked,board[r][c].getContents());
		checkRemovalGroup(r,c+1,board,checked,board[r][c].getContents());
		checkRemovalGroup(r,c-1,board,checked,board[r][c].getContents());
	}

	private void removeGroup(List<Coord> group) {
		for (Coord groupMember : group) {
			setSpace(groupMember.row, groupMember.col, Intersection.Piece.EMPTY);
		}
	}

	private void removeGroup(List<Coord> group, Intersection[][] b) {
		for (Coord groupMember : group) {
			b[groupMember.row][groupMember.col].setContents(Intersection.Piece.EMPTY);
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

	public int checkGroup(int r, int c, Intersection.Piece p,
			boolean checked[][], List<Coord> group, int liberties,
			Intersection b[][]) {
		if (r >= 0 && c >= 0 && r < 9 && c < 9) {
			if (b[r][c].getContents() == p && checked[r][c] == false) {
				group.add(new Coord(r, c));
				checked[r][c] = true;

				liberties += checkGroup(r - 1, c, p, checked, group, liberties,b);
				liberties += checkGroup(r, c + 1, p, checked, group, liberties,b);
				liberties += checkGroup(r + 1, c, p, checked, group, liberties,b);
				liberties += checkGroup(r, c - 1, p, checked, group, liberties,b);

			} else if (b[r][c].getContents() == Intersection.Piece.EMPTY) {
				return liberties + 1;
			}
		}
		return liberties;
	}

	public void calculateScores(Player pB, Player pW) {
		// TODO Calculate territory, and add to captured pieces to update scores
	}

	public void calculateHeuristic(Player pB, Player pW) {
		// TODO Calculate the relative score (Positive: black winning; Negative:
		// white winning)
	}

	public Intersection.Piece getContents(int r, int c) {
		return board[r][c].getContents();
	}
}
