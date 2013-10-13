public class Player {
	public String name;
	public Intersection.Piece piece;

	public Player(String n, Intersection.Piece p) {
		name = n;
		piece = p;
	}

	public void promptMove(GoEngine g) {
		boolean valid = false;
		while (valid == false) {
			int row;
			int col;
			System.out.println(name + ", select a space");
			System.out.print("Row: ");
			row = g.in.nextInt();
			System.out.print("Column: ");
			col = g.in.nextInt();

			if (row >= 0 && row < 9 && col >= 0 && col < 9) {
				valid = g.board.placePiece(row, col, piece);
			} else
				valid = false;

			if (valid == false) {
				System.out.println("Move is invalid, try again.");
			}
		}
	}

}
