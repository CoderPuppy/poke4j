package cpup.poke4j;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Cursor {
	protected final Poke poke;
	protected final Buffer buffer;
	protected BufferPos pos;
	protected Selection selection;

	public Cursor(Poke _poke, Buffer _buffer, BufferPos _pos, Selection _selection) {
		poke = _poke;
		buffer = _buffer;
		pos = _pos;
		selection = _selection;
	}

	public Cursor(Poke _poke, Buffer _buffer, BufferPos _pos) {
		this(_poke, _buffer, _pos, null);
	}

	public void move(int dist) {
		pos = pos.move(buffer, dist);
	}

	public void moveWord(int dist) {
		pos = pos.moveWord(buffer, dist);
	}

	// Getters and Setters
	public Poke getPoke() {
		return poke;
	}

	public Buffer getBuffer() {
		return buffer;
	}

	public BufferPos getPos() {

		return pos;
	}

	public void setPos(BufferPos pos) {
		this.pos = pos;
	}

	public void setLine(int line) {
		this.pos = new BufferPos(pos.getColumn(), line);
	}

	public void setColumn(int column) {
		this.pos = new BufferPos(column, pos.getLine());
	}

	public Selection getSelection() {
		return selection;
	}

	public Selection getSelectionWithDefault() {
		if(selection == null) {
			return new Selection(poke, buffer, pos, pos);
		} else {
			return selection;
		}
	}

	public void setSelection(Selection selection) {
		this.selection = selection;
	}
}