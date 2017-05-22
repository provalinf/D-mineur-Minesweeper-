package demineur.model;

/**
 * Created by Valentin.
 */
public class PlateauSize {
	private int x;
	private int y;

	public PlateauSize(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public boolean equals(PlateauSize dimension) {
		return getX() == dimension.getX() && getY() == dimension.getY();
	}

	@Override
	public String toString() {
		return "PlateauSize{" +
				"x=" + x +
				", y=" + y +
				'}';
	}
}
