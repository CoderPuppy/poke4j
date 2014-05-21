package cpup.poke4j;

public class Selection {
	protected final Poke poke;
	protected final Buffer buffer;
	protected final int beginColumn;
	protected final int beginLine;
	protected final int endColumn;
	protected final int endLine;

	public Selection(Poke _poke, Buffer _buffer, int _beginColumn, int _beginLine, int _endColumn, int _endLine) {
		poke = _poke;
		buffer = _buffer;
		beginColumn = _beginColumn;
		beginLine = _beginLine;
		endColumn = _endColumn;
		endLine = _endLine;
	}

	// Getters and Setters
	public Poke getPoke() {
		return poke;
	}

	public Buffer getBuffer() {
		return buffer;
	}

	public int getBeginColumn() {
		return beginColumn;
	}

	public int getBeginLine() {
		return beginLine;
	}

	public int getEndColumn() {
		return endColumn;
	}

	public int getEndLine() {
		return endLine;
	}
}