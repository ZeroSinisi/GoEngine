import javax.swing.*;

import java.awt.*;
//import java.awt.geom.*;
import java.util.Scanner;

public class GoEngine {
	public DisplayArea display;
	public Board board;
	private Player playerBlack;
	private Player playerWhite;
	public static int turnCount;
	public int numTrials;
	public boolean displayMode;
	public int turnTime;
	public int gameTime;
	

	public Scanner in = new Scanner(System.in);

	public static void main(String[] args) {
		GoEngine engine = new GoEngine();
		engine.createWindow();
		engine.playGo();
		turnCount = 1;
	}

	public GoEngine() {
		//playerBlack = new TerritoryDenialAI("Black", Intersection.Piece.BLACK);
		//playerBlack = new TerritoryDenialAI("Black", Intersection.Piece.BLACK);
		//playerWhite = new ScoreMaximizer("White", Intersection.Piece.WHITE);
		//playerWhite = new RandomAI("White", Intersection.Piece.WHITE);
		board = new Board();
		display = new DisplayArea();
		display.setBoard(board);
		chooseSettings();

	}
	
	public void chooseSettings(){	
		selectPlayerBlack();
		selectPlayerWhite();
		selectTestSettings();
	}
	
	public void selectPlayerBlack(){
		System.out.println("1. Random AI");
		System.out.println("2. Territory Denial AI");
		System.out.println("3. Score Maximizing Minimax");
		System.out.println("4. Human Player");
		System.out.print("Select AI for player Black (1-4): ");
		switch(getInt(1,4)){
		case 1:
			playerBlack = new RandomAI("Black", Intersection.Piece.BLACK);
			break;
		case 2:
			playerBlack= new TerritoryDenialAI("Black", Intersection.Piece.BLACK);
			break;
		case 3:
			System.out.print("Select a search depth (1-10): ");
			playerBlack= new ScoreMaximizer("Black", Intersection.Piece.BLACK,getInt(1,10));
			break;
		case 4:
			playerBlack= new Player("Black", Intersection.Piece.BLACK);
			break;
		}
	}
	
	public void selectPlayerWhite(){
		System.out.println("1. Random AI");
		System.out.println("2. Territory Denial AI");
		System.out.println("3. Score Maximizing Minimax");
		System.out.println("4. Human Player");
		System.out.print("Select AI for player White (1-4): ");
		switch(getInt(1,4)){
		case 1:
			playerWhite = new RandomAI("White", Intersection.Piece.WHITE);
			break;
		case 2:
			playerWhite= new TerritoryDenialAI("White", Intersection.Piece.WHITE);
			break;
		case 3:
			System.out.print("Select a search depth (1-10): ");
			playerWhite= new ScoreMaximizer("White", Intersection.Piece.WHITE,getInt(1,10));
			break;
		case 4:
			playerWhite= new Player("White", Intersection.Piece.WHITE);
			break;
		}
	}
	
	public void selectTestSettings(){
		System.out.print("Select number of tests: ");
		numTrials=getInt(1,Integer.MAX_VALUE);
		System.out.println("Display mode? (slows down gameplay for viewing)");
		System.out.println("1.Yes");
		System.out.println("2.No");
		switch(getInt(1,2)){
		case 1:
			System.out.print("Select turn delay (ms): ");
			turnTime=getInt(1,Integer.MAX_VALUE);
			System.out.print("Select game end delay (ms): ");
			gameTime=getInt(1,Integer.MAX_VALUE);
			displayMode=true;
			break;
		case 2:
			displayMode=false;
			break;
		}
		System.out.println("Display territory?");
		System.out.println("1.Yes");
		System.out.println("2.No");
		switch(getInt(1,2)){
		case 1:
			display.displayTerritory=true;
			break;
		case 2:
			display.displayTerritory=false;
			break;
		}
	}
	public int getInt(int min, int max){
		boolean selected=false;
		int input=0;
		while(selected==false){
			try{
				input=in.nextInt();
				if(input>=min&&input<=max)
					selected=true;
				else
					System.out.print("Out of range. Try again: ");
			}catch(Exception e){
				System.out.print("Invalid input. Try again: ");
				
			}
			
		}
		return input;
	}
	
	// Set up the window
	public void createWindow() {
		JFrame frame = new JFrame("GoEngine");
		frame.setBackground(Color.white);
		frame.setSize(500, 500);
		frame.setContentPane(display);
		frame.addWindowListener(new ExitListener());
		frame.setVisible(true);
	}

	// Main game loop
	public void playGo() {
		int whiteWins=0;
		int blackWins=0;
		int draws=0;
		int numGames=0;
		for(int i=0;i<numTrials;i++){
			newGame();
			while (board.gameOver==false) { // TODO This will need termination eventually, but
							// players can't pass yet
				//System.out.println("b");
				playerBlack.promptMove(this);
				turnCount++;
				display.repaint();
				if(displayMode){
					try{
						Thread.sleep(turnTime);
					}catch(Exception e){
						
					}
				}
				//System.out.println("w");
				playerWhite.promptMove(this);
				turnCount++;
				display.repaint();
				if(displayMode){
					try{
						Thread.sleep(turnTime);
					}catch(Exception e){
						
					}
				}
				
					
			}
			//display.repaint();
			board.calculateScores(playerBlack, playerWhite);
			//System.out.println("B:"+playerBlack.score+" W:"+playerWhite.score);
			if(playerBlack.score>playerWhite.score){
				blackWins++;
				System.out.println("B ("+(int)((double)numGames/numTrials*100)+"%)");
			}else if(playerWhite.score>playerBlack.score){
				whiteWins++;
				System.out.println("W ("+(int)((double)numGames/numTrials*100)+"%)");
			}else if(playerWhite.score==playerBlack.score){
				draws++;
				System.out.println("Draw");
			}
			if(displayMode){
				try{
					Thread.sleep(gameTime);
				}catch(Exception e){
					
				}
			}
			numGames++;
			
		}
		System.out.println("####RESULTS####");
		System.out.println("Black: " + playerBlack.getInfo());
		System.out.println("White: " + playerWhite.getInfo());
		System.out.println("Black:" + (int)((double)blackWins/numGames*100)+"% ("+blackWins+"/"+numGames+")");
		System.out.println("White:" + (int)((double)whiteWins/numGames*100)+"% ("+whiteWins+"/"+numGames+")");
		System.out.println("Draws:"+(int)((double)draws/numGames*100)+"% ("+draws+"/"+numGames+")");
		newTest();
	}
	public void newTest(){
		System.out.println("Run another test?");
		System.out.println("1.Yes");
		System.out.println("2.No");
		switch(getInt(1,2)){
		case 1:
			System.out.println("Same settings?");
			System.out.println("1.Yes");
			System.out.println("2.No");
			switch(getInt(1,2)){
			case 1:
				playGo();
				break;
			case 2:
				chooseSettings();
				playGo();
				break;
			}
			break;
		case 2:
			System.exit(0);
			break;
		}
	}
	public void newGame(){
		//board = new Board();
		//display.setBoard(board);
		board.reset();
		turnCount=1;
	}
}
