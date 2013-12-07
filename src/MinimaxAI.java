import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class MinimaxAI extends Player {
	
	Random rand;
	int maxDepth;
	public MinimaxAI(String n, Intersection.Piece p, int depth) {
		super(n, p);
		rand = new Random();
		maxDepth=depth;
	}
	
	public void promptMove(GoEngine g){
		int bestValue=Integer.MIN_VALUE;
		List<Coord> bestMoves = new ArrayList<Coord>();
		Board testBoard = new Board();
		int alpha=Integer.MIN_VALUE;
		int beta=Integer.MAX_VALUE;
		for(int r=0;r<9;r++){
			for(int c=0;c<9;c++){
				g.board.calculateTerritory();
				if(g.board.checkValid(r, c, piece)&&g.board.getTerritory(r, c)!=piece){
					testBoard.CopyBoard(g.board);
					testBoard.placePiece(r, c, piece);
					int testValue = minimax(testBoard,maxDepth-1,1,alpha,beta);
					//System.out.println(testValue);
					if(testValue>bestValue){
						bestMoves.clear();
						bestMoves.add(new Coord(r,c));
						bestValue=testValue;
					}else if(testValue==bestValue){
						bestMoves.add(new Coord(r,c));
					}
				}
			}
		}
		if(bestMoves.size()>0){
			Coord move = bestMoves.get(rand.nextInt(bestMoves.size()));
			g.board.placePiece(move.row, move.col, piece);
		}else{
			g.board.passTurn();
		}
	}
	
	private int minimax(Board b, int depth, int color, int alpha, int beta){
		
		Intersection.Piece cPiece;
		if(color==1){
			cPiece = piece;
		}else{
			if(piece==Intersection.Piece.BLACK)
				cPiece=Intersection.Piece.WHITE;
			else
				cPiece=Intersection.Piece.BLACK;
		}
		
		List<Coord> validMoves = new ArrayList<Coord>();
		
		b.calculateTerritory();
		for(int r=0;r<9;r++){
			for(int c=0;c<9;c++){
				if(b.checkValid(r,c,cPiece)&&b.getTerritory(r, c)!=piece){
					validMoves.add(new Coord(r,c));
				}
			}
		}
		if(depth==0||validMoves.size()==0){
			return calculateHeuristic(b,piece)*color;
		}
		
		int bestValue = Integer.MIN_VALUE;
		Board testBoard = new Board();
		for(Coord move : validMoves){
			testBoard.CopyBoard(b);
			testBoard.placePiece(move.row, move.col, cPiece);
			int testValue = -minimax(testBoard,depth-1,-color, -beta, -alpha);
			if(testValue>bestValue){
				bestValue=testValue;
			}
			alpha = Math.max(testValue, alpha);
			if(alpha>=beta){
				//System.out.println("Pruned");
				break;
			}
		}
		
		return bestValue;
		
	}
	
	abstract int calculateHeuristic(Board b, Intersection.Piece color);

}
