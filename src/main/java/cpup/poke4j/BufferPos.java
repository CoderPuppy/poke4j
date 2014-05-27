package cpup.poke4j;

public class BufferPos {
	protected final int column;
	protected final int line;

	public BufferPos(int _column, int _line) {
		column = _column;
		line = _line;
	}

	@Override
	public String toString() {
		return column + ", " + line;
	}

	// Getters and Setters
	public int getColumn() {
		return column;
	}

	public int getLine() {
		return line;
	}
}