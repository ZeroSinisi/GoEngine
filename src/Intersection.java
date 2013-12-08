public class Intersection {
	public enum Piece {
		EMPTY, BLACK, WHITE
	}

	private Piece contents;

	public Intersection() {
		contents = Piece.EMPTY;
	}

	public Intersection(Piece p) {
		contents = p;
	}

	public Piece getContents() {
		return contents;
	}

	public void setContents(Piece p) {
		contents = p;
	}
	
}
