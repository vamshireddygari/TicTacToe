import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.*;

/*
 * Easy GameRules.Level : if you play randomly and give chance for the AI to win it wins, but if you try to win you may win
 * Medium GameRules.Level : you have to plan and play, if you try to make straight forward moves.. the AI will catch you
 * Hard GameRules.Level : you can't win!!
 */
public class GamePlay {

	//variable to track total nodes visited
	int countNodesVisited; 
	//variable to set cutOffDepth based on level of Difficulty
	static int cutOffDepth;
	//variable to track the number of times the terminal state is reached in max Function
	int terminalMax;
	//variable to track the number of times the terminal state is reached in min Function
	int terminalMin;
	//variable to track the number of times cutoff occured in max Function
	int cutOffMax;
	//variable to track the number of times cutoff occured in min Function
	int cutOffMin;
	
	//Starting the Game
	void startGame(boolean playerStart){
		//Initializing initial gamestate
		GameState gameState = new GameState();
		//printing initial gameboard
		gameState.printGrid();
		boolean playerTurn = playerStart;
		Boolean result = null;
		while(result == null){
			//if playerTurn is true then player turn, else it's AI turn
			gameState = playGame(gameState, playerTurn);
			//altering player turn
			playerTurn = !playerTurn;
			//calculating the result of game
			result = GameRules.gameRes(gameState.getState());
			//checking for draw
			if(GameRules.isDraw(gameState.getState())){
				break;
			}
		}
		if(result != null){
			if(result){
				System.out.println("Computer WINS!!! \n");
			}else{
				System.out.println("Congratulations!!! You won against Computer \n");
			}
		}else{
			System.out.println("The game result is draw \n");
		}
	}
	
	//returns the gamestate after player pr computer had maken a choice
	private GameState playGame(GameState gameState, boolean playerTurn) {
		//Getting Player turn input
		if(playerTurn){
			System.out.println("***** PLAYER TURN ***** \n");
			System.out.println("Enter the location co-ordinates where you want to play \n");
			System.out.println("For example enter 11 for playing at (1,1) \n");
			Scanner scanner = new Scanner(System.in);
			int xPos = -1;
			int yPos = -1;
			int pos = 0;
			try{
				pos = Integer.parseInt(scanner.next());
				xPos = (pos/10) - 1;
				yPos = (pos % 10) - 1;
			}catch(NumberFormatException ne){
				
			}
			
			while(xPos < 0 || yPos < 0 || xPos >= gameState.GRID_SIZE || yPos >= gameState.GRID_SIZE || gameState.getState()[xPos][yPos] != null){
				System.out.println("Invalid co-ordinates!!!");
				System.out.println("Enter the location co-ordinates where you want to play");
				System.out.println("For example enter 11 for playing at (1,1)");
				try{
					pos = Integer.parseInt(scanner.next());
					xPos = (pos/10) - 1;
					yPos = (pos % 10) - 1;
				}catch(NumberFormatException ne){
					
				}
			}
			
			//Marking player's choice in board
			gameState.getState()[xPos][yPos] = false;	
			
		}else{
			//AI Turn
			gameState = alphaBetaSearch(gameState);	
		}
		System.out.println("\n");
		System.out.println("***** COMPUTER TURN ***** \n");
		gameState.printGrid();
		return gameState;
	}

	//returns game state for Computer i.e AI
	private GameState alphaBetaSearch(GameState gameState) {
		List<GameState> GameStateList = new ArrayList<GameState>();
		terminalMax = 0;
		terminalMin = 0;
		countNodesVisited = 0;
		cutOffMax = 0;
		cutOffMin = 0;
		
		//obtaining the state with max reward
		int v = maxVal(gameState,Integer.MIN_VALUE,Integer.MAX_VALUE, GameStateList, 0);
		
		for(GameState state : GameStateList){
			if(state.reward == v){
				gameState = state;
				break;
			}
		}
		
		System.out.println("Number of times cutOff occured in max function : "+ cutOffMax);
		System.out.println("Number of times cutOff occured in min function : "+ cutOffMin);
		System.out.println("Number of times terminal state reached in max function : "+ terminalMax);
		System.out.println("Number of times terminal state reached in min function : "+ terminalMin);
		System.out.println("Total nodes visited : "+ countNodesVisited);
		
		//returning the next game state
		return gameState;
	}
	

	//used to calculate the state with max reward. This algorithm follows same procedure as given in the instructions
	private int maxVal(GameState gState, int alpha, int beta, List<GameState> GameStateList, int depth) {
		countNodesVisited++;
		//Base case:start
		Boolean res = GameRules.gameRes(gState.getState());
		boolean drawGame = GameRules.isDraw(gState.getState());
		if(res != null){
			terminalMax++;
			if(res){
				GameStateList.add(new GameState(gState.getState(), 1000));
				return 1000;
			}else{
				GameStateList.add(new GameState(gState.getState(), -1000));
				return -1000;
			}
		}else if(drawGame){
			terminalMax++;
			GameStateList.add(new GameState(gState.getState(), 0));
			return 0;
		}
		
		if(depth >= cutOffDepth){
			cutOffMax++;
			int reward = GameRules.eval(gState.getState());
			GameStateList.add(new GameState(gState.getState(), reward));
			return reward;
		}
		
		int v = Integer.MIN_VALUE;
		
		List<GameState> nextGameStateList = new ArrayList<GameState>();
		
		for(Boolean[][] state : GameRules.getNextStates(gState.getState(), true)){
			v = Math.max(v, minVal(new GameState(state, 0),alpha,beta,nextGameStateList, depth+1));
			GameState tempState = new GameState(state, v);
			GameStateList.add(tempState);
			if(v >= beta){
				return v;
			}
			alpha = Math.max(alpha, v);
		}
		return v;
	}
	
//used to calculate the state with min reward. This algorithm follows same procedure as given in the instruction
	private int minVal(GameState gState, int alpha, int beta, List<GameState> GameStateList, int depth) {
		countNodesVisited++;
		//Base case:start
		Boolean res = GameRules.gameRes(gState.getState());
		boolean drawGame = GameRules.isDraw(gState.getState());
		
		if(res != null){
			terminalMin++;
			if(res){
				GameStateList.add(new GameState(gState.getState(), 1000));
				return 1000;
			}else{
				GameStateList.add(new GameState(gState.getState(), -1000));
				return -1000;
			}
		}else if(drawGame){
			terminalMin++;
			GameStateList.add(new GameState(gState.getState(), 0));
			return 0;
		}
		
		if(depth >= cutOffDepth){
			cutOffMin++;
			int reward = GameRules.eval(gState.getState());
			GameStateList.add(new GameState(gState.getState(), reward));
			return reward;
		}
		
		//Base case: end
		
		int v = Integer.MAX_VALUE;
		List<GameState> nextGameStateList = new ArrayList<GameState>();
		for(Boolean[][] state : GameRules.getNextStates(gState.getState(), false)){
			v = Math.min(v, maxVal(new GameState(state,0),alpha,beta,nextGameStateList,depth+1));
			GameState tempState = new GameState(state, v);
			GameStateList.add(tempState);
			if(v <= alpha){
				return v;
			}
			beta = Math.min(beta, v);
		}
		return v;
	}

	
	
	//Main Function
	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
		
		
		//Starting the game
		GamePlay gamePlay = new GamePlay();
		
		while(true){
			//Taking the inputs from user
			System.out.println("Enter any key and then Enter to play, Enter 0 to exit \n");
			Scanner scanner = new Scanner(System.in);
			try{
				if(Integer.parseInt(scanner.next()) == 0){
					System.exit(0);
				}
			}catch(NumberFormatException ne){
				
			}
			//Asking the user to choose level of difficulty
			System.out.println("Choose Level \n");
			System.out.println(" 1 : Easy \n");
			System.out.println(" 2 : Medium \n");
			System.out.println(" 3 : Hard \n");
			
			
			try{
				GameRules.Level = Integer.parseInt(scanner.next());
			}catch(NumberFormatException ne){
				
			}
			while(GameRules.Level < 1 || GameRules.Level > 3){
				System.out.println("Invalid Level!!!");
				System.out.println("Choose GameRules.Level \n");
				System.out.println(" 1 : Easy \n");
				System.out.println(" 2 : Medium \n");
				System.out.println(" 3 : Hard \n");
				try{
					GameRules.Level = Integer.parseInt(scanner.next());
				}catch(NumberFormatException ne){
					
				}
			}
			
			switch(GameRules.Level){
			case 1 : cutOffDepth = 1;
					 break;
			case 2 : cutOffDepth = 3;
				     break;
			case 3 : cutOffDepth = 6;
			}
			
			//asking the player whether he wants to play first
			System.out.println("Enter 0 if you want to play first \n");
			try{
				int playerStart = Integer.parseInt(scanner.next());
				gamePlay.startGame(playerStart == 0);
			}catch(NumberFormatException ne){
				gamePlay.startGame(false);
			}
			
		}
	}
	
}

