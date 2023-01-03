import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import java.lang.Math;
import ledControl.BoardController;

public class GameHandler {

	private BoardController controller;
	public Field[][] mineField = new Field[20][20];
	private Player player;
	private int[] highlight = new int []{10, 10, 10}; //Highlighting where Player is

	public GameHandler(BoardController controller) {
		this.controller = controller;
	}
	
	public void prepareGame() {
		// Generate empty (no bombs) 20x20 field
		for (int y = 0; y < 20; y++)
			for (int x = 0; x < 20; x++) {
				mineField[y][x] = new Field(x, y, false);
				Color fieldColor = ColorCodes.GREY.getColor();
				controllerSetColor(y, x, fieldColor);
			}
		controller.updateBoard();
	}
	
	private void fillBombs(int bombCount, int playerStartPosition) {
		// Generate bombCount bombs in the 20x20 field - usually 64
		int[] randomPositions = new int[bombCount];

		for (int i = 0; i < bombCount; i++) {
			int rand = getRandomFromTo(0, 399);
			while (arrayContains(randomPositions, rand) || rand == playerStartPosition)
				rand = getRandomFromTo(0, 399);
			randomPositions[i] = rand;
		}

		for (int i = 0; i < bombCount; i++) {
			// Convert position (0-399) to x and y
			int pos = randomPositions[i];
			int x = pos % 20;
			int y = (int) (pos / 20);
			// Umgekehrt ist
			// pos = y * 20 + x

			// Overwrite field at given x and y with a bomb field
			mineField[y][x] = new Field(x, y, true);
		}
	}
	
	private void updateAdjacents() {
		for (int y = 0; y < 20; y++) {
			for (int x = 0; x < 20; x++) {
				Field field = mineField[y][x];
				
				int adj = numberOfAdjacentBombs(field);
				field.setAdj(adj);
				
				Color fieldColor = ColorCodes.GREY.getColor();
				controllerSetColor(y, x, fieldColor);
			}
		}
		controller.updateBoard();
	}
	
	public void startGame(int playerX, int playerY) {
		startGame(20 * playerY + playerX);
	}
	
	public void startGame(int playerStartPosition) {
		// This being commented requires Main to call prepareGame()
		// prepareGame();
		fillBombs(64, playerStartPosition);
		updateAdjacents();
	}
	
	public void setPlayer(Player player) {this.player = player;}
	
	/*
	public void startGame() {

		int mineCount = 100;

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
				Color fieldColor = ColorCodes.GREY.getColor();

				//if (field.isBomb()) fieldColor = colorMap.get(ColorCodes.PINK);

				controllerSetColor(y, x, fieldColor);
			}
		}
		controller.updateBoard();

		//System.out.println(numberOfAdjacentBombs(4, 4));
	}
	*/
	
	public long playAudio(String resourceName) {
		// https://stackoverflow.com/questions/26305/how-can-i-play-sound-in-java
		try {
			Clip clip = AudioSystem.getClip();
			AudioInputStream inputStream;
			inputStream = AudioSystem.getAudioInputStream(PhasmaMain.class.getResourceAsStream("/" + resourceName + ".wav"));
			clip.open(inputStream);
			clip.start();
			return clip.getMicrosecondLength();
		} catch (Exception e) { e.printStackTrace(); }
		return 0;
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

	public void controllerSetColor(int x, int y, Color color) {
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
		Color[] adjacentColors = new Color[] { 
				ColorCodes.WHITE.getColor(), // if 0 adj; handle this, this is just a test
				ColorCodes.BLUE.getColor(), 
				ColorCodes.GREEN.getColor(), 
				ColorCodes.RED.getColor(),
				ColorCodes.PURPLE.getColor(), 
				ColorCodes.BROWN.getColor(), 
				ColorCodes.TEAL.getColor(),
				ColorCodes.PINK.getColor(), 
				ColorCodes.GREY.getColor(), // let's just hope we'll never get 8 adjacent fields
			};

		Color fieldColor = adjacentColors[mineField[x][y].getAdj()];
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