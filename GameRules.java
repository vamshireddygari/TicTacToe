import java.util.*;


public class GameRules {

	
	static final int GRID_SIZE = 4;
	static int Level;
	
	//calculates the result for the particular state
	public static Boolean gameRes(Boolean[][] state) {
		Boolean verWin = verticalWin(state);
		Boolean horizonWin = horizontalWin(state);
		Boolean diagWin = diagonalWin(state);
		if(verWin != null){
			return verWin;
		}
		if(horizonWin != null){
			return horizonWin;
		}
		if(diagWin != null){
			return diagWin;
		}
		return null;
	}
	
	//method to check for diagonal win
	private static Boolean diagonalWin(Boolean[][] state) {
		Boolean diagWin45 = primaryDiagonalWin(state);
		
		if(diagWin45 == null){
			Boolean newState = state[0][GRID_SIZE-1];
			int i = 1;
			int j = GRID_SIZE-2;
			boolean terminalFlag = true;
			for(;j >= 0; i++, j--){
				if(newState == null || !newState.equals(state[i][j])){
					terminalFlag = false;
					break;
				}
			}
			if(terminalFlag){
				return newState;
			}
			return null;
		}
		return diagWin45;
	}

  //method for validating 45 degree diagonal win
	private static Boolean primaryDiagonalWin(Boolean[][] gState) {
		Boolean state = gState[0][0];
		int i = 1;
		int j = 1;
		boolean terminalFlag = true;
		for(;i < GRID_SIZE; i++, j++){
			if(state == null || !state.equals(gState[i][j])){
				terminalFlag = false;
				break;
			}
		}
		if(terminalFlag){
			return state;
		}
		return null;
	}
	
	//method for validating horizontal win
	private static Boolean horizontalWin(Boolean[][] gState) {
		for(int i = 0; i < GRID_SIZE; i++){
			Boolean state = gState[i][0];
			boolean terminalFlag = true;
			for(int j = 1; j < GRID_SIZE; j++){
				if(state == null || !state.equals(gState[i][j])){
					terminalFlag = false;
					break;
				}
			}
			if(terminalFlag){
				return state;
			}
		}
		return null;
	}

	//validating the vertical win
	private static Boolean verticalWin(Boolean[][] gState) {
		for(int i = 0; i < GRID_SIZE; i++){
			Boolean state = gState[0][i];
			boolean terminalFlag = true;
			for(int j = 1; j < GRID_SIZE; j++){
				if(state == null || !state.equals(gState[j][i])){
					terminalFlag = false;
					break;
				}
			}
			if(terminalFlag){
				return state;
			}
		}
		return null;
	}
	
	//validating whether the game is draw or not
	public static boolean isDraw(Boolean[][] gState) {
		for(int i = 0; i < GRID_SIZE; i++){
			for(int j = 0; j < GRID_SIZE; j++){
				if(gState[i][j] == null){
					return false;
				}
			}
		}
		return true;
	}

	//obtaining the next states for current state
	public static Set<Boolean[][]> getNextStates(Boolean[][] state, boolean maxPlayerTurn) {
		Set<Boolean[][]> nextStateSet = new HashSet<Boolean[][]>();
		for(int i = 0; i < GRID_SIZE; i++){
			for(int j = 0; j < GRID_SIZE; j++){
				if(state[i][j] == null){
					Boolean[][] newState = copyState(state);
					newState[i][j] = maxPlayerTurn;
					nextStateSet.add(newState);
				}
			}
		}
		return nextStateSet;
	}
	
	//copying the current state for the use of generating next states
	private static Boolean[][] copyState(Boolean[][] state) {
		Boolean[][] newState = new Boolean[GRID_SIZE][GRID_SIZE];
		for(int i = 0; i < GRID_SIZE; i++){
			for(int j = 0; j < GRID_SIZE; j++){
				newState[i][j] = state[i][j];
			}
		}
		return newState;
	}
	
	//Obtaining the result for Evaluation Function
	public static int eval(Boolean[][] state){
		//Xn is the number of rows, columns, or diagonals with exactly n X's and no O’s. Similarly, On is the number of rows, columns, or diagonals with just n O’s
		int x1 = 0;
		int x2 = 0;
		int x3 = 0;
		int o3 = 0;
		int o2 = 0;
		int o1 = 0;
		for(int i = 0; i < GRID_SIZE; i++){
			int countX = 0;
			int countO = 0;
			int countN = 0;
			for(int j = 0; j < GRID_SIZE; j++){
				if(state[i][j] != null){
					if(state[i][j]){
						countX++;
					}else{
						countO++;
					}
				}else{
					countN++;
				}
			}
			
			x3 = (countX == 3 && countN == 1)? x3+1 : x3;
			x2 = (countX == 2 && countN == 2)? x2+1 : x2;
			x1 = (countX == 1 && countN == 3)? x1+1 : x1;
			
			o3 = (countO == 3 && countN == 1)? o3+1 : o3;
			o2 = (countO == 2 && countN == 2)? o2+1 : o2;
			o1 = (countO == 1 && countN == 3)? o1+1 : o1;
			
		}
		
		for(int j = 0; j < GRID_SIZE; j++){
			int countX = 0;
			int countO = 0;
			int countN = 0;
			for(int i = 0; i < GRID_SIZE; i++){
				if(state[i][j] != null){
					if(state[i][j]){
						countX++;
					}else{
						countO++;
					}
				}else{
					countN++;
				}
			}
			
			x3 = (countX == 3 && countN == 1)? x3+1 : x3;
			x2 = (countX == 2 && countN == 2)? x2+1 : x2;
			x1 = (countX == 1 && countN == 3)? x1+1 : x1;
			
			o3 = (countO == 3 && countN == 1)? o3+1 : o3;
			o2 = (countO == 2 && countN == 2)? o2+1 : o2;
			o1 = (countO == 1 && countN == 3)? o1+1 : o1;
			
		}
		
		int i = 1;
		int j = 1;
		int countX = 0;
		int countO = 0;
		int countN = 0;
		for(;i < GRID_SIZE; i++, j++){
			if(state[i][j] != null){
				if(state[i][j]){
					countX++;
				}else{
					countO++;
				}
			}else{
				countN++;
			}
		}
		
		x3 = (countX == 3 && countN == 1)? x3+1 : x3;
		x2 = (countX == 2 && countN == 2)? x2+1 : x2;
		x1 = (countX == 1 && countN == 3)? x1+1 : x1;
		
		o3 = (countO == 3 && countN == 1)? o3+1 : o3;
		o2 = (countO == 2 && countN == 2)? o2+1 : o2;
		o1 = (countO == 1 && countN == 3)? o1+1 : o1;
		
		
		countX = 0;
		countO = 0;
		countN = 0;
		i = 1;
		j = GRID_SIZE-2;
		for(;j >= 0; i++, j--){
			if(state[i][j] != null){
				if(state[i][j]){
					countX++;
				}else{
					countO++;
				}
			}else{
				countN++;
			}
		}
		
		x3 = (countX == 3 && countN == 1)? x3+1 : x3;
		x2 = (countX == 2 && countN == 2)? x2+1 : x2;
		x1 = (countX == 1 && countN == 3)? x1+1 : x1;
		
		o3 = (countO == 3 && countN == 1)? o3+1 : o3;
		o2 = (countO == 2 && countN == 2)? o2+1 : o2;
		o1 = (countO == 1 && countN == 3)? o1+1 : o1;
		
		int evalRes;
		if(Level == 1){
			//Evaluation function for easy level
			evalRes = x3 + x2 + x1 ;
		}else if(Level == 2){
			//Evaluation function for medium level
			evalRes = x3 - o3;
		}else{
			//Evaluation function for hard level
			evalRes = 6 * x3 + 3 * x2 + x1 - (6 * o3 + 3 * o2 + o1);
		}
		
		return evalRes;
	}

}
	
