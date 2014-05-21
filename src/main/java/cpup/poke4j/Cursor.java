package cpup.poke4j;

public class Cursor {
	protected final Poke poke;
	protected final Buffer buffer;
	protected int column;
	protected int line;
	protected Selection selection;

	public Cursor(Poke _poke, Buffer _buffer, int _column, int _line) {
		poke = _poke;
		buffer = _buffer;
		column = _column;
		line = _line;
	}

	// Getters and Setters
	public Poke getPoke() {
		return poke;
	}

	public Buffer getBuffer() {
		return buffer;
	}

	public int getColumn() {
		return column;
	}

	public int getLine() {
		return line;
	}

	public Selection getSelection() {
		return selection;
	}
}