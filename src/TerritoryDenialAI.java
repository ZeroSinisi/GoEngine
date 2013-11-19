import java.util.*;

public class TerritoryDenialAI extends Player {
	Random rand;
	public TerritoryDenialAI(String n, Intersection.Piece p) {
		super(n, p);
		// TODO Auto-generated constructor stub
		rand = new Random();
	}
	public void promptMove(GoEngine g){
		List<Coord> moves = new ArrayList<Coord>();
		int max=0;
		int count=0;
		if(g.turnCount==1){
			g.board.placePiece(rand.nextInt(9), rand.nextInt(9), piece);
		}
		for(int r=0;r<9;r++){
			for(int c=0;c<9;c++){
				if(g.board.checkValid(r, c, piece)==false){
					continue;
				}
				if(r-1>=0){
					if(g.board.getContents(r-1, c)!=Intersection.Piece.EMPTY&&g.board.getContents(r-1, c)!=piece){
						count = countBottom(r,c,g);
						if(count==max){
							moves.add(new Coord(r,c));
						}else if(count>max){
							moves.clear();
							moves.add(new Coord(r,c));
							max=count;
						}
					}
				}
				
				if(r+1<9){
					if(g.board.getContents(r+1, c)!=Intersection.Piece.EMPTY&&g.board.getContents(r+1, c)!=piece){
						count = countTop(r,c,g);
						if(count==max){
							moves.add(new Coord(r,c));
						}else if(count>max){
							moves.clear();
							moves.add(new Coord(r,c));
							max=count;
						}
					}
				}
				
				if(c-1>=0){
					if(g.board.getContents(r, c-1)!=Intersection.Piece.EMPTY&&g.board.getContents(r, c-1)!=piece){
						count = countRight(r,c,g);
						if(count==max){
							moves.add(new Coord(r,c));
						}else if(count>max){
							moves.clear();
							moves.add(new Coord(r,c));
							max=count;
						}
					}
				}
				
				if(c+1<9){
					if(g.board.getContents(r, c+1)!=Intersection.Piece.EMPTY&&g.board.getContents(r, c+1)!=piece){
						count = countLeft(r,c,g);
						if(count==max){
							moves.add(new Coord(r,c));
						}else if(count>max){
							moves.clear();
							moves.add(new Coord(r,c));
							max=count;
						}
					}
				}
			}
		}
		if(moves.size()!=0){
			Coord move = moves.get(rand.nextInt(moves.size()));
			g.board.placePiece(move.row, move.col, piece);
		}else{
			g.board.passTurn();
		}
	}
	
	public int countBottom(int r, int c, GoEngine g) {
		int count = 0;
		for (int i = r; i < 9; i++) {
			if (g.board.getContents(i, c) == Intersection.Piece.EMPTY) {
				count++;
			} else {
				break;
			}
		}
		return count;
	}
	
	public int countTop(int r, int c, GoEngine g){
		int count=0;
		for(int i=r;i>=0;i--){
			if(g.board.getContents(i,c)==Intersection.Piece.EMPTY){
				count++;
			} else{
				break;
			}
		}
		return count;
	}
	
	public int countRight(int r, int c, GoEngine g){
		int count=0;
		for(int i=c;i<9;i++){
			if(g.board.getContents(r,i)==Intersection.Piece.EMPTY){
				count++;
			}else{
				break;
			}
		}
		return count;
	}
	
	public int countLeft(int r, int c, GoEngine g){
		int count=0;
		for(int i=c;i>=0;i--){
			if(g.board.getContents(r,i)==Intersection.Piece.EMPTY){
				count++;
			} else{
				break;
			}
		}
		return count;
	}
	
	/*
	public void promptMove(GoEngine g) {
		
		int row = 0;
		int col = 0;
		boolean placed = false;
		//while(placed != true) {
			int leftSpace = 0;
			int rightSpace = 0;
			int topSpace = 0;
			int bottomSpace = 0;
			for(int i = 0; i < 81; i++){
				if(g.board.getContents(row, col) != Intersection.Piece.EMPTY 
						&& g.board.getContents(row, col) != piece) {
					for(int j = row + 1; j <= 8; j++){
						if(g.board.getContents(j, col) == Intersection.Piece.EMPTY) {
							bottomSpace++;
						} else {
							break;
						}
					}
					for(int j = row - 1; j >= 0; j--){
						if(g.board.getContents(j, col) == Intersection.Piece.EMPTY) {
							topSpace++;
						} else {
							break;
						}
					}
					for(int j = col + 1; j <= 8; j++){
						if(g.board.getContents(row, j) == Intersection.Piece.EMPTY) {
							rightSpace++;
						} else {
							break;
						}
					}
					for(int j = col - 1; j >= 0; j--){
						if(g.board.getContents(row, j) == Intersection.Piece.EMPTY) {
							leftSpace++;
						} else {
							break;
						}
					}
					if(leftSpace >= rightSpace && leftSpace >= topSpace && leftSpace >= bottomSpace){
						placed = g.board.placePiece(row, col - 1, piece);
					} else if(rightSpace >= leftSpace && rightSpace >= topSpace && rightSpace >= bottomSpace){
						placed = g.board.placePiece(row, col + 1, piece);
					} else if(topSpace >= rightSpace && topSpace >= leftSpace && topSpace >= bottomSpace){
						placed = g.board.placePiece(row - 1, col, piece);
					} else if(bottomSpace >= rightSpace && bottomSpace >= topSpace && bottomSpace >= leftSpace){
						placed = g.board.placePiece(row + 1, col, piece);
					}
				}
				if(placed || (col == 8 && row == 8)){
					break;
				}
				if(col >= 8){
					col = 0;
					row++;
				} else {
					col++;
				}
			}
			if(placed==false){
				g.board.passTurn();
			}
		//}
	}*/
}
