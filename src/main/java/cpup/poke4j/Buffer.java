package cpup.poke4j;

import com.google.common.base.Joiner;
import cpup.poke4j.events.EventRegister;
import cpup.poke4j.operations.IOperation;
import cpup.poke4j.operations.InsertOperation;
import cpup.poke4j.operations.OperationData;
import cpup.poke4j.operations.RemoveOperation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class Buffer {
	protected final Poke poke;

	public Buffer(Poke _poke) {
		poke = _poke;
		cursors.add(new Cursor(poke, this, 0, 0));
	}

	// Get the real line to operate on (so if the line doesn't exist it goes to the last one that does)
	public int findLine(int line) {
		final int lineSize = getLineCount();

		if(line > lineSize - 1) {
			line = lineSize - 1;
		}

		if(line < 0) {
			line = Math.max(lineSize + line, 0);
		}

		return line;
	}

	// Get the real column
	public int findColumn(int column, int line) {
		line = findLine(line);

		String existingLine = getLines().get(line);

		if(column > existingLine.length()) {
			column = existingLine.length();
		}

		if(column < 0) {
			column = Math.max(existingLine.length() + column + 1, 0);
		}

		return column;
	}

	public BufferPos offset(BufferPos pos, int offset) {
		if(offset == 0) {
			return pos;
		}

		int dist = Math.abs(offset);
		final int dir = offset / dist;
		int column = pos.getColumn();
		int line = pos.getLine();
		final int lineCount = getLineCount();

		while(dist > 0) {
			if(dir == 1) {
				if(column == getLine(line).length()) {
					if(line < lineCount - 1) {
						line += 1;
						column = 0;
						dist -= 1;
					} else {
						break;
					}
				} else {
					column += 1;
					dist -= 1;
				}
			} else if(dir == -1) {
				if(column > 0) {
					column -= 1;
					dist -= 1;
				} else {
					if(line > 0) {
						line -= 1;
						column = getLine(line).length();
						dist -= 1;
					} else {
						break;
					}
				}
			}
		}

		return new BufferPos(column, line);
	}

	protected final List<Cursor> cursors = new ArrayList<Cursor>();

	// Wrappers for InsertOperation and RemoveOperation for ease of use
	public Buffer insert(int column, int line, String text) {
		return apply(new InsertOperation(column, line, text));
	}
	public Buffer remove(int column, int line, int length) {
		return apply(new RemoveOperation(column, line, length));
	}

	// The buffer handles inserting and deleting so it can have a different data structure
	public abstract int insertImpl(int column, int line, String text);
	public abstract String removeImpl(int column, int line, int length);

	// This should be handled specially for each data structure. parsing stuff for example
	public abstract void load(BufferedReader reader) throws Exception;
	// This is only for making stuff more efficient, symmetry and the rare case that you save stuff in a format that doesn't match the display format (java decompiler for example)
	public abstract void save(BufferedWriter writer) throws Exception;

	protected List<OperationData> history = new ArrayList<OperationData>();
	// Whatever historyPointer points to has already been applied, anything before has also been applied and anything after hasn't
	protected int historyPointer = -1;

	public <OP extends IOperation<OD>, OD extends OperationData<OP>> Buffer apply(OP operation, boolean addToHistory) {
		OD data = operation.apply(this);

		if(addToHistory) {
			// Remove all the entries after this
			history = history.subList(0, historyPointer + 1);
			history.add(historyPointer + 1, data);
			historyPointer += 1;
		}

		applyOperationEv.emit(new ApplyOperationEvent<OP, OD>(poke, this, operation, data));

		return this;
	}
	public <OP extends IOperation<OD>, OD extends OperationData<OP>> Buffer apply(OP operation) {
		return apply(operation, true);
	}

	// Fired when an operation is applied (whether it's added to the history or not)
	public final EventRegister<ApplyOperationEvent> applyOperationEv = new EventRegister<ApplyOperationEvent>();
	public static class ApplyOperationEvent<OP extends IOperation<OD>, OD extends OperationData<OP>> {
		protected final Poke poke;
		protected final Buffer buffer;
		protected final OP operation;
		protected final OD data;

		public ApplyOperationEvent(Poke _poke, Buffer _buffer, OP _operation, OD _data) {
			poke = _poke;
			buffer = _buffer;
			operation = _operation;
			data = _data;
		}

		// Getters and Setters
		public Poke getPoke() {
			return poke;
		}

		public Buffer getBuffer() {
			return buffer;
		}

		public OP getOperation() {
			return operation;
		}

		public OD getData() {
			return data;
		}
	}

	public Buffer undo() {
		// If there's something to undo (historyPointer points to an operation)
		if(historyPointer > -1) {
			history.get(historyPointer).unapply();
			historyPointer -= 1;
		} else {
			throw new IndexOutOfBoundsException("Nothing to undo");
		}

		return this;
	}

	@SuppressWarnings("unchecked")
	public Buffer redo() {
		// If there is something to redo (historyPointer doesn't point to the last operation)
		if(historyPointer < history.size() - 1) {
			historyPointer += 1;
			apply(history.get(historyPointer).getOperation(), false);
		} else {
			throw new IndexOutOfBoundsException("Nothing to redo");
		}

		return this;
	}

	// Getters and Setters
	public Poke getPoke() {
		return poke;
	}

	public abstract List<String> getLines();
	public abstract String getLine(int line);
	public abstract int getLineCount();

	public String getText() {
		return Joiner.on("\n").join(getLines());
	}
	public List<Cursor> getCursors() {
		return cursors;
	}

	public List<OperationData> getHistory() {
		return history;
	}

	public int getHistoryPointer() {
		return historyPointer;
	}
}