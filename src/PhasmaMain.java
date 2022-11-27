import java.awt.event.KeyEvent;
import ledControl.BoardController;
import ledControl.LedConfiguration;
import ledControl.gui.KeyBuffer;
import ledControl.gui.BoardKeyListener;

public class PhasmaMain {

	public static void main(String[] args) {
		
		//grey = 220
		
		//größeres Board falls gewünscht
		BoardController.getBoardController(LedConfiguration.LED_20x20_EMULATOR);
		BoardController controller = BoardController.getBoardController();
		
		//Das Objekt in der anderen Klasse wird erstellt
		EineWeitereKlasse ewk = new EineWeitereKlasse(controller);
		Player player = new Player(controller, ewk);
		ewk.setPlayer(player);

		controller.setBackgroundColor(220/2, 220/2, 220/2);
		controller.resetColors();
		
		ewk.startGame();
		player.startListening();
		
	}

}
