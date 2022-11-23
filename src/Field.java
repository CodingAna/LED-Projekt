
public class Field {
	
	private int x;
	private int y;
	private boolean isBomb;
	private boolean revealed;
	
	public Field(int x, int y, boolean isBomb) {
		this.x = x;
		this.y = y;
		this.isBomb = isBomb;
		revealed = false;
	}
	
	// Set Field to be revealed and return if the player died to the bomb
	public boolean reveal() {
		revealed = true;
		return !isBomb;
	}
	
	// Getter
	public boolean isRevealed() {return revealed;}
	public int getX() {return x;}
	public int getY() {return y;}
	public boolean isBomb() {return isBomb;}
	
}
