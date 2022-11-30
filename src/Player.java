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
	}
	
	public int getX() {return x;}
	public int getY() {return y;}
	
	public void startListening()
	{
		while (true) {
			event = buffer.pop();
			if (event != null && event.getID() == 401) //event.getID() = 401 for button presses
			{
				switch(event.getKeyCode())
				{
				case 10: // revealing current field
					if(!ewk.mineField[x][y].isRevealed() && !ewk.mineField[x][y].isFlagged())
					{
						ewk.coloring(x, y, 0);
					}
					break;
				case 32: //setting flag on current field
					if(!ewk.mineField[x][y].isFlagged() && !ewk.mineField[x][y].isRevealed())
					{
						ewk.mineField[x][y].setFlag(true);
						ewk.controllerSetColor(x, y, ewk.getColor(ColorCodes.YELLOW));
						controller.updateBoard();
					}
					else
					{
						if (ewk.mineField[x][y].isFlagged() && !ewk.mineField[x][y].isRevealed())
						{
							ewk.mineField[x][y].setFlag(false);
							ewk.controllerSetColor(x, y, ewk.getColor(ColorCodes.GREY));
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
	}
}