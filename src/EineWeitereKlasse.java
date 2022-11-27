import java.util.Iterator;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.UnsupportedAudioFileException;

import java.util.HashMap;
import java.io.File;
import java.io.IOException;
import java.lang.Math;
import ledControl.BoardController;

public class EineWeitereKlasse {

	private BoardController controller;
	public Field[][] mineField = new Field[20][20];
	private HashMap<ColorCodes, Color> colorMap;
	private Player player;
	private int[] highlight = new int []{10, 10, 10};

	public EineWeitereKlasse(BoardController controller) {
		this.controller = controller;
		colorMap = new HashMap<ColorCodes, Color>();
		colorMap.put(ColorCodes.BLACK, new Color(0, 0, 0));
		colorMap.put(ColorCodes.WHITE, new Color(235, 235, 235));
		colorMap.put(ColorCodes.BLUE, new Color(1, 0, 234));
		colorMap.put(ColorCodes.GREEN, new Color(1, 235, 1));
		colorMap.put(ColorCodes.RED, new Color(234, 0, 0));
		colorMap.put(ColorCodes.PURPLE, new Color(1, 0, 128));
		colorMap.put(ColorCodes.BROWN, new Color(129, 1, 2));
		colorMap.put(ColorCodes.TEAL, new Color(0, 128, 129));
		colorMap.put(ColorCodes.PINK, new Color(235, 85, 160));
		colorMap.put(ColorCodes.GREY, new Color(128, 128, 128));
	}
	
	public void setPlayer(Player player) {this.player = player;}
	
	public void startGame() {
		/*
		 * Visual representation of mineField[y][x] [2][0]:
		 * 
		 * [ x value | | \_/ [# # # # #], [# # # # #], [o # # # #], <-- y value [# # # #
		 * #], [# # # # #] ]
		 */

		int mineCount = 64;

		// Generate empty (no bombs) 20x20 field
		for (int y = 0; y < 20; y++)
			for (int x = 0; x < 20; x++)
				mineField[y][x] = new Field(x, y, false);

		// Generate 64 bombs in the 20x20 field ---
		int[] randomPositions = new int[mineCount];

		for (int i = 0; i < mineCount; i++) {
			int rand = getRandomFromTo(0, 399);
			// While 'rand' is in 'randomPositions' generate a new number to avoid
			// duplicates
			while (arrayContains(randomPositions, rand))
				// Will be executed if the 'rand' value is already in 'randomPositions'
				rand = getRandomFromTo(0, 399);
			// Add value to the random array
			randomPositions[i] = rand;
		}

		// Add values from 'randomPositions' into the mineField
		for (int i = 0; i < mineCount; i++) {
			// Convert position (0-399) to x and y
			int pos = randomPositions[i];
			int x = pos % 20;
			int y = (int) (pos / 20);

			// Overwrite field at given x and y with a bomb field
			mineField[y][x] = new Field(x, y, true);
		}

		// TODO: If clicked on bomb: do a fancy explosion animation

		// Draw field
		for (int y = 0; y < 20; y++) {
			for (int x = 0; x < 20; x++) {
				Field field = mineField[y][x];
				int adj = numberOfAdjacentBombs(field);
				field.setAdj(adj);
				Color fieldColor = colorMap.get(ColorCodes.GREY);

				//if (field.isBomb()) fieldColor = colorMap.get(ColorCodes.PINK);

				controllerSetColor(y, x, fieldColor);
			}
		}
		controller.updateBoard();

		try {
			Thread.sleep(1000);
		} catch (Exception e) {
		}

		/*
		 * try { Clip clip = AudioSystem.getClip(); AudioInputStream inputStream;
		 * inputStream =
		 * AudioSystem.getAudioInputStream(PhasmaMain.class.getResourceAsStream(
		 * "/bomb_explosion.wav")); clip.open(inputStream); clip.start(); } catch
		 * (Exception e) { // TODO Auto-generated catch block e.printStackTrace(); }
		 */

		//System.out.println(numberOfAdjacentBombs(4, 4));
	}

	// Do we even need this method? idk idc
	private Field[] getAdjacentFields(Field field) {
		return getAdjacentFields(field.getX(), field.getY());
	}

	private Field[] getAdjacentFields(int x, int y) {
		Field[] adjacentFields = new Field[8];
		int i = 0;
		for (int dy = -1; dy <= 1; dy++) {
			for (int dx = -1; dx <= 1; dx++) {
				int newX = x + dx;
				int newY = y + dy;

				if (newX == x && newY == y)
					continue;
				if (newX < 0 || newX >= 20)
					continue;
				if (newY < 0 || newY >= 20)
					continue;

				Field currentField = mineField[newY][newX];
				adjacentFields[i] = currentField;
				i++;
			}
		}
		return adjacentFields;
	}

	private int numberOfAdjacentBombs(Field field) {
		return numberOfAdjacentBombs(field.getX(), field.getY());
	}

	public int numberOfAdjacentBombs(int x, int y) {
		int count = 0;
		for (int dy = -1; dy <= 1; dy++) {
			for (int dx = -1; dx <= 1; dx++) {
				int newX = x + dx;
				int newY = y + dy;

				// First check: If position is same as parameter skip because it's technically
				// not adjacent?
				if (newX == x && newY == y)
					continue;
				if (newX < 0 || newX >= 20)
					continue;
				if (newY < 0 || newY >= 20)
					continue;

				Field currentField = mineField[newY][newX];
				if (currentField.isBomb())
					count++;
			}
		}
		return count;
	}

	private void controllerSetColor(int x, int y, Color color) {
		if(player.getX() != x || player.getY() != y)
			controller.setColor(x, y, color.red / 2, color.green / 2, color.blue / 2);
		else
			controller.setColor(x, y, (color.red + 20) / 2, (color.green + 20) / 2, (color.blue + 20) / 2);
			
	}

	private int getRandomFromTo(int min, int max) {
		return (int) (Math.random() * (max - min + 1) + min);
	}

	private boolean arrayContains(int[] array, int value) {
		for (int i : array)
			if (i == value)
				return true;
		return false;
	}

	public void triggerMovement(int x, int y, int oldX, int oldY) {
		controller.addColor(x, y, highlight);
		controller.addColor(oldX, oldY, -highlight[0], -highlight[1], -highlight[2]);
		controller.updateBoard();
		// Playermovement anzeigen aufm Board
	}

	public void coloring(int x, int y, int count) {
		Color fieldColor;

		Color[] adjToColorArray = new Color[] { 
				colorMap.get(ColorCodes.WHITE), // if 0 adj; handle this, this is just a test
				colorMap.get(ColorCodes.BLUE), 
				colorMap.get(ColorCodes.GREEN), 
				colorMap.get(ColorCodes.RED),
				colorMap.get(ColorCodes.PURPLE), 
				colorMap.get(ColorCodes.BROWN), 
				colorMap.get(ColorCodes.TEAL),
				colorMap.get(ColorCodes.PINK), 
				colorMap.get(ColorCodes.GREY), // let's just hope we'll never get 8 adjacent fields
		};

		fieldColor = adjToColorArray[mineField[x][y].getAdj()];
		controllerSetColor(x, y, fieldColor);
		
		if (mineField[x][y].getAdj() == 0 && !mineField[x][y].isRevealed()) {
			mineField[x][y].reveal();
			for (int i = x - 1; i <= x + 1; i++) {
				if (i >= 0 && i <= 19)
					for (int j = y - 1; j <= y + 1; j++) {
						if (j >= 0 && j <= 19) {
							coloring(i, j, count + 1);
						}
					}

			}
		}
		if(count == 0)
			controller.updateBoard();
		mineField[x][y].reveal();
	}
}