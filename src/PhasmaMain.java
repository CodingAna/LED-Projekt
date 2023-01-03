import ledControl.BoardController;
import ledControl.LedConfiguration;

public class PhasmaMain {

	public static void main(String[] args) {
		
		//grey = 220
		
		//größeres Board falls gewünscht
		BoardController.getBoardController(LedConfiguration.LED_20x20_EMULATOR);
		BoardController controller = BoardController.getBoardController();
		
		//Das Objekt in der anderen Klasse wird erstellt
		GameHandler handler = new GameHandler(controller);
		Player player = new Player(controller, handler);
		handler.setPlayer(player);

		controller.setBackgroundColor(220/2, 220/2, 220/2);
		controller.resetColors();
		
		handler.prepareGame();
		// handler.startGame();
		player.startListening();
		
	}

}
