public class ScoreMaximizer extends MinimaxAI {

	public ScoreMaximizer(String n, Intersection.Piece p, int depth) {
		super(n, p, depth);
		// TODO Auto-generated constructor stub
	}

	@Override
	int calculateHeuristic(Board b, Intersection.Piece color) {
		// TODO Auto-generated method stub
		ScorePair scores = b.calculateScores();
		int score;
		if(color==Intersection.Piece.BLACK){
			score = scores.black-scores.white;
		}else{
			score = scores.white-scores.black;
		}
		//System.out.println(score);
		return score;
	}
	
	public String getInfo(){
		String output = "";
		output+="Score Maximizer D"+maxDepth;
		return output;
	}

}
