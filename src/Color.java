
public class Color {
	public int red;
	public int green;
	public int blue;
	
	public Color(int red, int green, int blue) {
		this.red = red;
		this.green = green;
		this.blue = blue;
	}

	public int[] toArray() {
		return new int[] {red, green, blue};
	}
}
