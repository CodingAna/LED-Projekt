import java.util.Iterator;
import java.util.HashMap;
import java.lang.Math;
import ledControl.BoardController;

public class EineWeitereKlasse {
	
	private BoardController controller;
	private Field[][] mineField = new Field[20][20];
	private HashMap<ColorCodes, Color> colorMap;
	
	public EineWeitereKlasse(BoardController controller) {
		this.controller = controller;
		colorMap = new HashMap<ColorCodes, Color>();
		colorMap.put(ColorCodes.BLACK, new Color(0, 0, 0));
		colorMap.put(ColorCodes.WHITE, new Color(255, 255, 255));
		colorMap.put(ColorCodes.BLUE, new Color(0, 0, 255));
		colorMap.put(ColorCodes.GREEN, new Color(0, 255, 0));
		colorMap.put(ColorCodes.RED, new Color(255, 0, 0));
	}
	
	public void startGame() {
		/*
		 * Visual representation of mineField[y][x]
		 * [2][0]:
		 * 
		 * [
		 *   x value
		 *   |
		 *   |
		 *  \_/
		 *  [# # # # #],
		 *  [# # # # #],
		 *  [o # # # #], <-- y value
		 *  [# # # # #],
		 *  [# # # # #]
		 * ]
		 */
		
		int mineCount = 64;
		
		// Generate empty (no bombs) 20x20 field
		for (int y=0; y<20; y++)
			for (int x=0; x<20; x++)
				mineField[y][x] = new Field(x, y, false);
		
		// Generate 64 bombs in the 20x20 field ---
		int[] randomPositions = new int[mineCount];
		
		for (int i=0; i<mineCount; i++) {
			int rand = getRandomFromTo(0, 399);
			// While 'rand' is in 'randomPositions' generate a new number to avoid duplicates
			while (arrayContains(randomPositions, rand))
				// Will be executed if the 'rand' value is already in 'randomPositions'
				rand = getRandomFromTo(0, 399);
			// Add value to the random array
			randomPositions[i] = rand;
		}
		
		// Add values from 'randomPositions' into the mineField
		for (int i=0; i<mineCount; i++) {
			// Convert position (0-399) to x and y
			int pos = randomPositions[i];
			int x = pos % 20;
			int y = (int)(pos / 20);
			
			// Overwrite field at given x and y with a bomb field
			mineField[y][x] = new Field(x, y, true);
		}
		
		// Draw field
		long start = System.currentTimeMillis();
		int testCount = 100;
		for (int test=0; test<testCount; test++) {
			for (int y=0; y<20; y++) {
				for (int x=0; x<20; x++) {
					// This is just a test, normally they'd all start grey
					Field field = mineField[y][x];
					Color fieldColor = colorMap.get(ColorCodes.BLUE);
					if (field.isBomb()) fieldColor = colorMap.get(ColorCodes.RED);
					controllerSetColor(x, y, fieldColor);
				}
			}
			controller.updateBoard();
		}
		long end = System.currentTimeMillis();
		long diffTotal = end - start;
		double diffMs = diffTotal / testCount;
		System.out.println(diffMs);
		System.out.println(1 / diffMs * 1000);
	}
	
	// TODO: Calculate stuff here
	private int numberOfAdjacentBombs(int x, int y) {
		return 0;
	}
	
	private void controllerSetColor(int x, int y, Color color) {
		controller.setColor(x, y, color.red/2, color.green/2, color.blue/2);
	}
	
	private int getRandomFromTo(int min, int max) {
		return (int) (Math.random() * (max - min + 1) + min);
	}
	
	private boolean arrayContains(int[] array, int value) {
		for (int i : array)
			if (i == value) return true;
		return false;
	}
	
}
