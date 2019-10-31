package application;

public class Player {

	private String name;
	//Merlin(0), Percival(1), Assassin(2), Mordred(3), Morgana(4), Oberon(5), NormalBlue(6), NormalSpy(7)
	private int[] characters = {0, 0, 0, 0, 0, 0, 0, 0};
	private float win, loss, merlin_loss, merlin_win;
	private float win_percentage, merlin_kill;
	private String best_char;
	
	Player (String name) {
		this.name = name;
		this.win = 0;
		this.loss = 0;
		this.merlin_loss = 0;
		this.merlin_win = 0;
		this.best_char = "N/A";
	}
	
	Player (String name, Float wins, Float loss, Float mer_loss, Float mer_win, Float win_per, Float mer_per, String b_char, int[] track) {
		this.name = name;
		this.win = wins;
		this.loss = loss;
		this.merlin_loss = mer_loss;
		this.merlin_win = mer_win;
		this.win_percentage = win_per;
		this.merlin_kill = mer_per;
		this.best_char = b_char;
		for (int i = 0; i < 8; i++) {
			this.setCharacters(i, track[i]);
		}
		
	}
	
	
	public float getWin_percentage() {
		return win_percentage;
	}

	public void setWin_percentage(float win_percentage) {
		this.win_percentage = win_percentage;
	}

	public float getMerlin_kill() {
		return merlin_kill;
	}

	public void setMerlin_kill(float merlin_kill) {
		this.merlin_kill = merlin_kill;
	}

	public String getBest_char() {
		return best_char;
	}

	public void setBest_char(String best_char) {
		this.best_char = best_char;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCharacters(int index) {
		return characters[index];
	}

	public void setCharacters(int index, int value) {
		characters[index] = value;
	}

	public float getWin() {
		return win;
	}

	public void setWin(float win) {
		this.win = win;
	}

	public float getLoss() {
		return loss;
	}

	public void setLoss(float loss) {
		this.loss = loss;
	}

	public float getMerlin_loss() {
		return merlin_loss;
	}

	public void setMerlin_loss(float merlin_loss) {
		this.merlin_loss = merlin_loss;
	}

	public float getMerlin_win() {
		return merlin_win;
	}

	public void setMerlin_win(float merlin_win) {
		this.merlin_win = merlin_win;
	}
	
}
