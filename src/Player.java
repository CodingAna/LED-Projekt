import java.awt.event.KeyEvent;
import ledControl.BoardController;
import ledControl.gui.KeyBuffer;


public class Player {
	
	private BoardController controller;
	private KeyBuffer buffer = KeyBuffer.getKeyBuffer();
	private KeyEvent event;
	private Listener listener = new Listener(buffer);

	private int x = 0;
	private int y = 0;
	
	public Player(BoardController controller) 
	{
		this.controller = controller;
	}
	
	public void startListening()
	{
		while(true)
		{
			
			event = buffer.pop();
			if(event != null && event.getKeyCode() != 0 && event.getModifiersEx() == KeyEvent.KEY_TYPED)
				System.out.println("Typed");

		}
	}
}
