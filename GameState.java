public class GameState {
	
	//Initializing Grid Size as 4
	static final int GRID_SIZE = 4;
	
	Boolean[][] state;
	
	int reward;
	
	GameState(Boolean[][] s, int r){
		state = new Boolean[GRID_SIZE][GRID_SIZE];
		for(int i = 0; i < GRID_SIZE; i++){
			for(int j = 0; j < GRID_SIZE; j++){
				state[i][j] = s[i][j];
			}
		}
		reward = r;
	}
	
	GameState(){
		state = new Boolean[GRID_SIZE][GRID_SIZE];
	}
	
	//Printing the board values
	public void printGrid(){
		for(int i = 0; i < GRID_SIZE; i++){
			for(int j = 0; j < GRID_SIZE; j++){
				if(state[i][j] == null){
					System.out.print("-  ");
				}else if(state[i][j]){
					System.out.print("X  ");
				}else{
					System.out.print("O  ");
				}
			}
			System.out.println("");
		}
	}

	//function to get state
	public Boolean[][] getState() {
		return state;
	}

	//function to set state
	public void setState(Boolean[][] state) {
		this.state = state;
	}

	//function to get reward
	public int getReward() {
		return reward;
	}

	//function to set reward
	public void setReward(int reward) {
		this.reward = reward;
	}
	
}
