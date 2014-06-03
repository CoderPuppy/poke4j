package cpup.poke4j;

import cpup.poke4j.events.EventRegister;

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

	protected void fireMoveEvent(MoveEvent ev) {
		moveEv.emit(ev);
		buffer.moveCursorEv.emit(ev);
	}

	public void move(int dist) {
		final BufferPos oldPos = this.pos;
		pos = pos.move(buffer, dist);
		fireMoveEvent(new MoveEvent(this, oldPos, pos));
	}

	public void moveWord(int dist) {
		final BufferPos oldPos = this.pos;
		pos = pos.moveWord(buffer, dist);
		fireMoveEvent(new MoveEvent(this, oldPos, pos));
	}


	public final EventRegister<MoveEvent> moveEv = new EventRegister<MoveEvent>();
	public static class MoveEvent {
		protected final Cursor cursor;
		protected final BufferPos oldPos;

		protected final BufferPos newPos;

		public MoveEvent(Cursor _cursor, BufferPos _oldPos, BufferPos _newPos) {
			cursor = _cursor;
			oldPos = _oldPos;
			newPos = _newPos;
		}

		// Getters and Setters
		public Poke getPoke() {
			return cursor.getPoke();
		}

		public Buffer getBuffer() {
			return cursor.getBuffer();
		}

		public Cursor getCursor() {
			return cursor;
		}

		public BufferPos getOldPos() {
			return oldPos;
		}
		public BufferPos getNewPos() {
			return newPos;
		}
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
		final BufferPos oldPos = this.pos;
		this.pos = pos;
		fireMoveEvent(new MoveEvent(this, oldPos, pos));
	}

	public void setLine(int line) {
		final BufferPos oldPos = this.pos;
		this.pos = new BufferPos(pos.getColumn(), line);
		fireMoveEvent(new MoveEvent(this, oldPos, pos));
	}

	public void setColumn(int column) {
		final BufferPos oldPos = this.pos;
		this.pos = new BufferPos(column, pos.getLine());
		fireMoveEvent(new MoveEvent(this, oldPos, pos));
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