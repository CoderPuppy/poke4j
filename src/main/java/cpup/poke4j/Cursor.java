package cpup.poke4j;

import cpup.poke4j.events.EventRegister;

import java.util.List;

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
		setPos(pos.move(buffer, dist));
	}

	public void moveWord(int dist) {
		setPos(pos.moveWord(buffer, dist));
	}

	public void moveUD(int dist) {
		setPos(pos.moveUD(buffer, dist));
	}

	public void clearSelection() {
		setSelection(null);
	}

	public void removeSelection() {
		if(selection != null) {
			final List<String> lines = selection.getLines();
			int selLength = lines.size() - 1;
			for(String line : lines) {
				selLength += line.length();
			}
			selection.remove(new BufferPos(0, 0), selLength);
			clearSelection();
		}
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

	public final EventRegister<SelectEvent> selectEv = new EventRegister<SelectEvent>();
	public static class SelectEvent {
		protected final Cursor cursor;

		protected final Selection oldSel;
		protected final Selection newSel;

		public SelectEvent(Cursor _cursor, Selection _oldSel, Selection _newSel) {
			cursor = _cursor;
			oldSel = _oldSel;
			newSel = _newSel;
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

		public Selection getOldSel() {
			return oldSel;
		}
		public Selection getNewSel() {
			return newSel;
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
		final MoveEvent ev = new MoveEvent(this, oldPos, pos);
		moveEv.emit(ev);
		buffer.moveCursorEv.emit(ev);

		if(selection == null)
			setSelection(new Selection(poke, buffer, oldPos, oldPos));

		if(selection.begin.equals(oldPos)) {
			setSelection(new Selection(poke, buffer, pos, selection.end));
		} else if(selection.end.equals(oldPos)) {
			setSelection(new Selection(poke, buffer, selection.begin, pos));
		}
	}

	public void setLine(int line) {
		setPos(new BufferPos(pos.getColumn(), line));
	}

	public void setColumn(int column) {
		setPos(new BufferPos(column, pos.getLine()));
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

	public void setSelection(Selection _selection) {
		final Selection oldSelection = selection;
		this.selection = _selection;
		final SelectEvent ev = new SelectEvent(this, oldSelection, _selection);
		selectEv.emit(ev);
		buffer.selectCursorEv.emit(ev);
	}
}