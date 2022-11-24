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
	
	public void startListening()
	{
		while (true) {

			event = buffer.pop();
			//event.getID() = 401 für gedrückt
			//event.getID() = 402 für loslassen
			
			
			if (event != null && event.getID() == 401)
			{
				switch(event.getKeyCode())
				{
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
					if (x < 20)
					{
						x++;
						moved = true;
					}
					break;
				case 40:
					if (y < 20)
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
					
				}
				
				System.out.println("x = " + x + ", y = " + y);
				
			}
		}
	}
}