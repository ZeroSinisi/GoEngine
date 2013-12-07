import java.util.*;

public class RandomAI extends Player {
	
	Random rand;
	public RandomAI(String n, Intersection.Piece p) {
		super(n, p);
		rand = new Random();
	}
	
	public void promptMove(GoEngine g) {
		List<Coord> moves = new ArrayList<Coord>();
		g.board.calculateTerritory();
		for(int i=0;i<9;i++){
			for(int j=0;j<9;j++){
				if(g.board.checkValid(i,j,piece)&&g.board.getTerritory(i, j)!=piece){
					moves.add(new Coord(i,j));
				}
			}
		}
		if(moves.size()!=0){
			Coord move = moves.get(rand.nextInt(moves.size()));
			if(g.board.placePiece(move.row, move.col, piece)==false){
				
			}
		}else{
			g.board.passTurn();
		}
	}
	
	public String getInfo(){
		String output = "";
		output+="Random";
		return output;
	}
}
