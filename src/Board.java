import java.util.*;

public class Board {
	boolean pass;
	boolean gameOver;
	
	private List<Intersection[][]> history;
	//private Intersection koBoard[][];
	//private Intersection previousBoard[][];
	private Intersection board[][];
	public Intersection.Piece territory[][];

	public Board() {
		board = new Intersection[9][9];
		territory = new Intersection.Piece[9][9];
		//koBoard = new Intersection[9][9];
		//previousBoard = new Intersection[9][9];
		
		history = new ArrayList<Intersection[][]>();
		
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {

				board[i][j] = new Intersection();
				territory[i][j]=Intersection.Piece.EMPTY;
			}
		}
		pass=false;
		gameOver=false;
	}

	private void setSpace(int r, int c, Intersection.Piece p) {
		board[r][c].setContents(p);
	}
	
	private void storeBoard(Intersection[][] b, List<Intersection[][]> hist){
		Intersection tempBoard[][] = new Intersection[9][9];
		copyBoard(b,tempBoard);
		hist.add(tempBoard);
	}
	
	// Attempt to place a piece. Return false if space is occupied
	public boolean placePiece(int r, int c, Intersection.Piece p) {
		if (checkValid(r, c, p) == true) {
			setSpace(r, c, p);
			checkRemoval(r, c);
			pass=false;
			storeBoard(board,history);
			//copyBoard(previousBoard,koBoard);
			//copyBoard(previousBoard);
			return true;
		} else
			return false;
	}
	
	public boolean equalBoards(Intersection[][]a,Intersection[][]b){
		for(int i=0;i<9;i++){
			for(int j=0;j<9;j++){
				if(a[i][j].getContents()!=b[i][j].getContents())
					return false;
				
			}
		}
		return true;
	}
	
	public void passTurn(){
		if(pass==true){
			gameOver=true;
		}else{
			pass=true;
		}
	}
	
	public boolean checkValid(int r, int c, Intersection.Piece p) {
		if(r<0||r>=9||c<0||c>=9){
			return false;
		}
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
			for(int i=0;i<history.size();i++){
				if(equalBoards(tempBoard,history.get(i))){
					return false;
				}
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
	
	private void copyBoard(Intersection[][] a, Intersection[][] b){
		for(int i=0;i<9;i++){
			for(int j=0;j<9;j++){
				b[i][j]=new Intersection();
				b[i][j].setContents(a[i][j].getContents());
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
		pB.score=0;
		pW.score=0;
		boolean checked[][] = new boolean[9][9];
		for(int i=0; i<9; i++){
			for (int j=0; j<9; j++){
				if(checked[i][j]==false){
					List<Intersection.Piece> colors = new ArrayList<Intersection.Piece>();
					int score = scoreFill(i, j, colors, checked, 0);
					if(colors.size()>0){
						Intersection.Piece scoreColor = colors.get(0);
						for(int k=0; k<colors.size();k++){
							if(colors.get(k)!=scoreColor){
								scoreColor = Intersection.Piece.EMPTY;
								break;
							}
						}
						if(scoreColor==Intersection.Piece.BLACK){
							pB.score += score;
						}
						else if(scoreColor==Intersection.Piece.WHITE){
							pW.score += score;
						}
					}
				}
			}
		}
	}
	
	public int scoreFill(int r, int c, List<Intersection.Piece> colors, boolean checked[][], int score){
		if (r < 0 || c < 0 || r >= 9 || c >= 9) {
			return 0;
		}
		if(checked[r][c]==true){
			return 0;
		}
		else if(board[r][c].getContents()!=Intersection.Piece.EMPTY){
			colors.add(board[r][c].getContents());
			return 0;
		}
		else{
			checked[r][c]=true;
			score+= scoreFill(r-1, c, colors, checked, score);
			score+= scoreFill(r, c+1, colors, checked, score);
			score+= scoreFill(r+1, c, colors, checked, score);
			score+= scoreFill(r, c-1, colors, checked, score);
			return score+1;
			
		}
	}
	public void calculateTerritory(){
		boolean checked[][]=new boolean[9][9];
		for(int r=0;r<9;r++){
			for(int c=0;c<9;c++){
				if(checked[r][c]==false){
					List<Intersection.Piece> colors = new ArrayList<Intersection.Piece>();
					List<Coord> area = new ArrayList<Coord>();
					territoryFill(r, c, colors, checked, area);
					if(colors.size()>0){
						Intersection.Piece scoreColor = colors.get(0);
						for(int k=0; k<colors.size();k++){
							if(colors.get(k)!=scoreColor){
								scoreColor = Intersection.Piece.EMPTY;
								break;
							}
						}
						if(scoreColor==Intersection.Piece.BLACK){
							for (Coord areaMember : area) {
								territory[areaMember.row][areaMember.col]=Intersection.Piece.BLACK;
							}
						}
						else if(scoreColor==Intersection.Piece.WHITE){
							for (Coord areaMember : area) {
								territory[areaMember.row][areaMember.col]=Intersection.Piece.WHITE;
							}
						}
					}
				}
			}
		}
	}
	
	public void territoryFill(int r, int c, List<Intersection.Piece> colors, boolean checked[][], List<Coord> area){
		if (r < 0 || c < 0 || r >= 9 || c >= 9) {
			return;
		}
		if(checked[r][c]==true){
			return;
		}
		else if(board[r][c].getContents()!=Intersection.Piece.EMPTY){
			colors.add(board[r][c].getContents());
			return;
		}
		else{
			checked[r][c]=true;
			area.add(new Coord(r,c));
			territoryFill(r-1, c, colors, checked, area);
			territoryFill(r, c+1, colors, checked, area);
			territoryFill(r+1, c, colors, checked, area);
			territoryFill(r, c-1, colors, checked, area);
			return;
			
		}
	}
	
	public Intersection.Piece getContents(int r, int c) {
		return board[r][c].getContents();
	}
	
	public Intersection.Piece getTerritory(int r, int c){
		return territory[r][c];
	}
}
