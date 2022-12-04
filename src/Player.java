import java.awt.event.KeyEvent;
import ledControl.BoardController;
import ledControl.gui.BoardKeyListener;
import ledControl.gui.KeyBuffer;


public class Player {
	
	private BoardController controller;
	private KeyBuffer buffer;
	private KeyEvent event;
	private BoardKeyListener listener;
	private EineWeitereKlasse ewk;

	private boolean moved;
	private boolean died;
	
	private int x = 0;
	private int y = 0;
	private int oldX = 0;
	private int oldY = 0;
	
	public Player(BoardController controller, EineWeitereKlasse ewk) 
	{
		this.ewk = ewk;
		this.controller = controller;
		this.buffer = KeyBuffer.getKeyBuffer();
		this.listener = new BoardKeyListener(buffer);
		this.moved = false;
		this.died = false;
	}
	
	public int getX() {return x;}
	public int getY() {return y;}
	
	public void startListening()
	{
		while (!died) {
			event = buffer.pop();
			if (event != null && event.getID() == 401) //event.getID() = 401 for button presses
			{
				switch(event.getKeyCode())
				{
				case 10: // revealing current field
					if (ewk.mineField[x][y].isBomb()) {
						this.died = true;
					}
					if(!ewk.mineField[x][y].isRevealed() && !ewk.mineField[x][y].isFlagged())
					{
						ewk.coloring(x, y, 0);
					}
					break;
				case 32: //setting flag on current field
					if(!ewk.mineField[x][y].isFlagged() && !ewk.mineField[x][y].isRevealed())
					{
						ewk.mineField[x][y].setFlag(true);
						ewk.controllerSetColor(x, y, ColorCodes.YELLOW.getColor());
						controller.updateBoard();
					}
					else
					{
						if (ewk.mineField[x][y].isFlagged() && !ewk.mineField[x][y].isRevealed())
						{
							ewk.mineField[x][y].setFlag(false);
							ewk.controllerSetColor(x, y, ColorCodes.GREY.getColor());
							controller.updateBoard();
						}
					}
					break;
				case 37:
					if (x > 0)
					{
						x--;
						moved = true;
					}
					break;
				case 38:
					if (y > 0)
					{
						y--;
						moved = true;
					}
					break;
				case 39:
					if (x < 19)
					{
						x++;
						moved = true;
					}
					break;
				case 40:
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
					ewk.triggerMovement(x, y, oldX, oldY);
					moved = false;
					oldX = x;
					oldY = y;
					System.out.println("Flagge: " + ewk.mineField[x][y].isFlagged() + ", Revealed: " + ewk.mineField[x][y].isRevealed() + ", Bombs: " + ewk.mineField[x][y].getAdj() + ", isBomb: " + ewk.mineField[x][y].isBomb());
				}				
			}
		}
		// The player died and while loop ended
		long length;
		length = ewk.playAudio("bomb_explosion");
		
		// Make a RED / WHITE flashing animation
		for (int switching=0; switching<6; switching++) {
			for (int y=0; y<20; y++) {
				for (int x=0; x<20; x++) {
					ewk.controllerSetColor(x, y, ((switching % 2 == 0) ? ColorCodes.RED : ColorCodes.WHITE).getColor());
				}				
			}
			controller.updateBoard();
			controller.sleep(500);
		}
		
		length = (int)(length / 1000) - (500 * 6);
		if (length > 0) controller.sleep((int)length);
		length = ewk.playAudio("sad_trombone");
		
		// Reset board to all BLACK
		for (int y=0; y<20; y++) {
			for (int x=0; x<20; x++) {
				ewk.controllerSetColor(x, y, ColorCodes.BLACK.getColor());
			}				
		}
		controller.updateBoard();
		
		controller.sleep((int)(length / 1000));
		ewk.playAudio("sad_violin_airhorn");
		
		boolean flashing = true;
		int count = 0;
		while (true) {
			// Left Eye
			for (int y=0; y<2; y++)
				for (int x=0; x<2; x++)
					ewk.controllerSetColor(x+5, y+4, (flashing ? ColorCodes.WHITE : ColorCodes.BLACK).getColor());
		
			// Right Eye
			for (int y=0; y<2; y++)
				for (int x=0; x<2; x++)
					ewk.controllerSetColor(x+12, y+4, (flashing ? ColorCodes.WHITE : ColorCodes.BLACK).getColor());
			
			// Mouth Left Lower
			for (int y=0; y<4; y++)
				for (int x=0; x<2; x++)
					ewk.controllerSetColor(x+3, y+13, (flashing ? ColorCodes.WHITE : ColorCodes.BLACK).getColor());
			
			// Mouth Left Middle
			for (int y=0; y<2; y++)
				for (int x=0; x<2; x++)
					ewk.controllerSetColor(x+4, y+12, (flashing ? ColorCodes.WHITE : ColorCodes.BLACK).getColor());
			
			// Mouth Middle
			for (int y=0; y<2; y++)
				for (int x=0; x<7; x++)
					ewk.controllerSetColor(x+6, y+11, (flashing ? ColorCodes.WHITE : ColorCodes.BLACK).getColor());
			
			// Mouth Right Middle
			for (int y=0; y<2; y++)
				for (int x=0; x<2; x++)
					ewk.controllerSetColor(x+13, y+12, (flashing ? ColorCodes.WHITE : ColorCodes.BLACK).getColor());
			
			// Mouth Right Lower
			for (int y=0; y<4; y++)
				for (int x=0; x<2; x++)
					ewk.controllerSetColor(x+14, y+13, (flashing ? ColorCodes.WHITE : ColorCodes.BLACK).getColor());
			
			controller.updateBoard();
			controller.sleep(840);
			
			flashing = !flashing;
			count++;
			if (count > 24) break;
		}
		
		flashing = true;
		
		ewk.playAudio("mlg_airhorn");
		for (int i=0; i<2; i++) {
			laughingSmiley(flashing);
			flashing = !flashing;
			controller.sleep(700);
		}
		
		ewk.playAudio("mlg_triple");
		for (int i=0; i<7; i++) {
			laughingSmiley(flashing);
			flashing = !flashing;
			controller.sleep(400);
		}
		
		ewk.playAudio("mlg_cancan");
		while (true) {
			laughingSmiley(flashing);
			flashing = !flashing;
			controller.sleep(150);
		}
	}
	
	private void laughingSmiley(boolean flashing) {
		// Whole background
		for (int y=0; y<20; y++)
			for (int x=0; x<20; x++)
				ewk.controllerSetColor(x, y, (flashing ? ColorCodes.BLACK : ColorCodes.WHITE).getColor());
		
		// Left Eye
		for (int y=0; y<2; y++)
			for (int x=0; x<2; x++)
				ewk.controllerSetColor(x+5, y+4, (flashing ? ColorCodes.WHITE : ColorCodes.BLACK).getColor());
	
		// Right Eye
		for (int y=0; y<2; y++)
			for (int x=0; x<2; x++)
				ewk.controllerSetColor(x+12, y+4, (flashing ? ColorCodes.WHITE : ColorCodes.BLACK).getColor());
		
		// Mouth Left Lower
		for (int y=0; y<4; y++)
			for (int x=0; x<2; x++)
				ewk.controllerSetColor(x+3, y+11, (flashing ? ColorCodes.WHITE : ColorCodes.BLACK).getColor());
		
		// Mouth Left Middle
		for (int y=0; y<2; y++)
			for (int x=0; x<2; x++)
				ewk.controllerSetColor(x+4, y+14, (flashing ? ColorCodes.WHITE : ColorCodes.BLACK).getColor());
		
		// Mouth Middle
		for (int y=0; y<2; y++)
			for (int x=0; x<7; x++)
				ewk.controllerSetColor(x+6, y+15, (flashing ? ColorCodes.WHITE : ColorCodes.BLACK).getColor());
		
		// Mouth Right Middle
		for (int y=0; y<2; y++)
			for (int x=0; x<2; x++)
				ewk.controllerSetColor(x+13, y+14, (flashing ? ColorCodes.WHITE : ColorCodes.BLACK).getColor());
		
		// Mouth Right Lower
		for (int y=0; y<4; y++)
			for (int x=0; x<2; x++)
				ewk.controllerSetColor(x+14, y+11, (flashing ? ColorCodes.WHITE : ColorCodes.BLACK).getColor());
		
		controller.updateBoard();
	}
}