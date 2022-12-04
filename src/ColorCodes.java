
public enum ColorCodes {
	BLACK(new Color(0, 0, 0)),
	WHITE(new Color(235, 235, 235)),
	YELLOW(new Color(235, 235, 31)), //Flag
	BLUE(new Color(1, 0, 234)), // One
	GREEN(new Color(1, 235, 1)), // Two
	RED(new Color(234, 0, 0)), // Three
	PURPLE(new Color(200, 0, 200)), // Four
	BROWN(new Color(186, 98, 31)), // Five
	TEAL(new Color(0, 128, 129)), // Six
	PINK(new Color(235, 85, 160)), // Seven
	GREY(new Color(128, 128, 128)); // Eight
	
	private Color color;
	
	private ColorCodes(Color color) {
		this.color = color;
	}
	
	public Color getColor() {
		return color;
	}
}
