
public class Field {
	
	private int x;
	private int y;
	private int adj;
	private boolean isBomb;
	private boolean isFlagged;
	private boolean revealed;
	private GameHandler handler;
	
	public Field(int x, int y, boolean isBomb, GameHandler handler) {
		this.x = x;
		this.y = y;
		this.isBomb = isBomb;
		revealed = false;
		this.handler = handler;
	}
	
	// Set Field to be revealed and return if the player died to the bomb
	public void reveal() {
		// Return true if isFlagged to prevent missclicks?
		if(!isFlagged && !revealed) {
			revealed = true;
			handler.uncoveredFields++;
		}
	}
	
	// Getter
	public boolean isRevealed() {return revealed;}
	public int getX() {return x;}
	public int getY() {return y;}
	public int getAdj() {return adj;}
	public boolean isFlagged() {return isFlagged;}
	public boolean isBomb() {return isBomb;}
	
	// Setter
	public void setFlag(boolean isFlagged) {
		if(!revealed)
			this.isFlagged = isFlagged;
	}
	
	public void setAdj(int adj)
	{
		this.adj = adj;
	}
}
