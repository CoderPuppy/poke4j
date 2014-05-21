package cpup.poke4j;

public class Cursor {
	protected final Poke poke;
	protected final Buffer buffer;
	protected int column;
	protected int line;
	protected Selection selection;

	public Cursor(Poke _poke, Buffer _buffer, int _column, int _line, Selection _selection) {
		poke = _poke;
		buffer = _buffer;
		column = _column;
		line = _line;
		selection = _selection;
	}

	public Cursor(Poke _poke, Buffer _buffer, int _column, int _line) {
		this(_poke, _buffer, _column, _line, null);
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

	public void setSelection(Selection selection) {
		this.selection = selection;
	}
}