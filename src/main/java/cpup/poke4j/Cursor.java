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

	protected void moveImpl(int dir) {
		if(dir == 1 && column == buffer.getLine(line).length()) {
			if(line < buffer.getLineCount() - 1) {
				line += 1;
				column = 0;
			}
		} else if(dir == -1 && column == 0) {
			if(line > 0) {
				line -= 1;
				column = buffer.getLine(line).length();
			}
		} else {
			column += dir;
		}
	}

	public void move(int dist) {
		final int amt = Math.abs(dist);
		final int dir = dist / amt;
		for(int i = 0; i < amt; i++) {
			moveImpl(dir);
		}
	}

	public void moveWord(int dist) {
		final int amt = Math.abs(dist);
		final int dir = dist / amt;
		for(int i = 0; i < amt; i++) {
			// TODO: implement
		}
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

	public void setLine(int line) {
		this.line = line;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	public Selection getSelection() {
		return selection;
	}

	public void setSelection(Selection selection) {
		this.selection = selection;
	}
}