import java.awt.event.KeyEvent;
import ledControl.BoardController;
import ledControl.gui.BoardKeyListener;
import ledControl.gui.KeyBuffer;


public class Player {
	
	private BoardController controller;
	private KeyBuffer buffer;
	private KeyEvent event;
	private BoardKeyListener listener;
	private GameHandler handler;

	private boolean moved;
	private boolean died;
	private boolean won;
	
	private int x = 0;
	private int y = 0;
	private int oldX = 0;
	private int oldY = 0;
	private int turns;
	
	public Player(BoardController controller, GameHandler handler) 
	{
		this.handler = handler;
		this.controller = controller;
		this.buffer = KeyBuffer.getKeyBuffer();
		this.listener = new BoardKeyListener(buffer);
		this.moved = false;
		this.died = false;
		this.won = false;
	}
	
	public int getX() {return x;}
	public int getY() {return y;}
	
	public void startListening()
	{
		while (!died && !won) {
			event = buffer.pop();
			if (event != null && event.getID() == 401) //event.getID() = 401 for button presses
			{
				switch(event.getKeyCode())
				{
				case KeyEvent.VK_ENTER: // revealing current field
					if(!handler.mineField[x][y].isRevealed() && !handler.mineField[x][y].isFlagged())
					{
						if (handler.mineField[x][y].isBomb()) this.died = true;
						turns++;
						if (turns == 1) handler.startGame(x, y);
						handler.coloring(x, y, 0);
						won = handler.checkWin();
					}
					break;
				case KeyEvent.VK_SPACE: //setting flag on current field
					if(!handler.mineField[x][y].isFlagged() && !handler.mineField[x][y].isRevealed())
					{
						handler.mineField[x][y].setFlag(true);
						handler.controllerSetColor(x, y, ColorCodes.YELLOW.getColor());
						controller.updateBoard();
					}
					else
					{
						if (handler.mineField[x][y].isFlagged() && !handler.mineField[x][y].isRevealed())
						{
							handler.mineField[x][y].setFlag(false);
							handler.controllerSetColor(x, y, ColorCodes.GREY.getColor());
							controller.updateBoard();
						}
					}
					break;
				case KeyEvent.VK_LEFT:
					if (x > 0)
					{
						x--;
						moved = true;
					}
					break;
				case KeyEvent.VK_UP:
					if (y > 0)
					{
						y--;
						moved = true;
					}
					break;
				case KeyEvent.VK_RIGHT:
					if (x < 19)
					{
						x++;
						moved = true;
					}
					break;
				case KeyEvent.VK_DOWN:
					if (y < 19)
					{
						y++;
						moved = true;
					}
					break;
				default:
					break;
				}
				
				if (moved)
				{
					handler.triggerMovement(x, y, oldX, oldY);
					moved = false;
					oldX = x;
					oldY = y;
					System.out.println("Flagge: " + handler.mineField[x][y].isFlagged() + ", Revealed: " + handler.mineField[x][y].isRevealed() + ", Bombs: " + handler.mineField[x][y].getAdj() + ", isBomb: " + handler.mineField[x][y].isBomb());
				}				
			}
		}
		if (died) {
			// The player died and while loop ended
			long length;
			length = handler.playAudio("bomb_explosion");
			
			// Make a RED / WHITE flashing animation
			for (int switching=0; switching<3; switching++) {
				for (int y=0; y<20; y++) {
					for (int x=0; x<20; x++) {
						handler.controllerSetColor(x, y, ((switching % 2 == 0) ? ColorCodes.RED : ColorCodes.WHITE).getColor());
					}				
				}
				controller.updateBoard();
				controller.sleep(500);
			}
			
			length = (int)(length / 1000) - (500 * 6);
			if (length > 0) controller.sleep((int)length);
			
			// Reset board to all BLACK
			for (int y=0; y<20; y++) {
				for (int x=0; x<20; x++) {
					handler.controllerSetColor(x, y, ColorCodes.BLACK.getColor());
				}				
			}
			controller.updateBoard();
			handler.playAudio("sad_violin");
			
		} // if (died)
		else if (won) {
			handler.playAudio("win_fanfare");
			boolean flashing = false;
			while (true) {
				laughingSmiley(flashing);
				flashing = !flashing;
				controller.sleep(1000);
			}
		} // if (won)
	}
	
	private void laughingSmiley(boolean flashing) {
		// Whole background
		for (int y=0; y<20; y++)
			for (int x=0; x<20; x++)
				handler.controllerSetColor(x, y, (flashing ? ColorCodes.BLACK : ColorCodes.WHITE).getColor());
		
		// Left Eye
		for (int y=0; y<2; y++)
			for (int x=0; x<2; x++)
				handler.controllerSetColor(x+5, y+4, (flashing ? ColorCodes.WHITE : ColorCodes.BLACK).getColor());
	
		// Right Eye
		for (int y=0; y<2; y++)
			for (int x=0; x<2; x++)
				handler.controllerSetColor(x+12, y+4, (flashing ? ColorCodes.WHITE : ColorCodes.BLACK).getColor());
		
		// Mouth Left Lower
		for (int y=0; y<4; y++)
			for (int x=0; x<2; x++)
				handler.controllerSetColor(x+3, y+11, (flashing ? ColorCodes.WHITE : ColorCodes.BLACK).getColor());
		
		// Mouth Left Middle
		for (int y=0; y<2; y++)
			for (int x=0; x<2; x++)
				handler.controllerSetColor(x+4, y+14, (flashing ? ColorCodes.WHITE : ColorCodes.BLACK).getColor());
		
		// Mouth Middle
		for (int y=0; y<2; y++)
			for (int x=0; x<7; x++)
				handler.controllerSetColor(x+6, y+15, (flashing ? ColorCodes.WHITE : ColorCodes.BLACK).getColor());
		
		// Mouth Right Middle
		for (int y=0; y<2; y++)
			for (int x=0; x<2; x++)
				handler.controllerSetColor(x+13, y+14, (flashing ? ColorCodes.WHITE : ColorCodes.BLACK).getColor());
		
		// Mouth Right Lower
		for (int y=0; y<4; y++)
			for (int x=0; x<2; x++)
				handler.controllerSetColor(x+14, y+11, (flashing ? ColorCodes.WHITE : ColorCodes.BLACK).getColor());
		
		controller.updateBoard();
	}
}