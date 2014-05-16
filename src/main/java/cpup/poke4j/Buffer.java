package cpup.poke4j;

import com.google.common.base.Joiner;
import cpup.poke4j.events.EventRegister;
import cpup.poke4j.operations.IOperation;
import cpup.poke4j.operations.OperationData;

import java.util.ArrayList;
import java.util.List;

public abstract class Buffer {
	protected final Poke poke;

	public Buffer(Poke _poke) {
		poke = _poke;
	}

	// Getters and Setters
	public Poke getPoke() {
		return poke;
	}
	public abstract List<String> getLines();
	public String getText() {
		return Joiner.on("\n").join(getLines());
	}

	public int findLine(int line) {
		final List<String> lines = getLines();

		if(line > lines.size() - 1) {
			line = lines.size() - 1;
		}

		if(line < 0) {
			line = lines.size() + line;
		}

		return line;
	}

	public int findColumn(int column, int line) {
		line = findLine(line);

		String existingLine = getLines().get(line);

		if(column > existingLine.length()) {
			column = existingLine.length();
		}

		if(column < 0) {
			column = existingLine.length() + column;
		}

		return column;
	}

	// The buffer handles inserting and deleting so it can have a different data structure
	public abstract int insertImpl(int column, int line, String text);
	public abstract String removeImpl(int column, int line, int length);

	protected List<OperationData> history = new ArrayList<OperationData>();
	protected int historyPointer = -1;
	public <OP extends IOperation<OD>, OD extends OperationData<OP>> Buffer apply(OP operation, boolean addToHistory) {
		OD data = operation.apply(this);

		if(addToHistory) {
			// Remove all the entries after this
			history = history.subList(0, historyPointer);
			history.add(historyPointer + 1, data);
			historyPointer += 1;
		}

		applyOperationEv.emit(new ApplyOperationEvent<OP, OD>(poke, this, operation, data));

		return this;
	}
	public <OP extends IOperation<OD>, OD extends OperationData<OP>> Buffer apply(OP operation) {
		return apply(operation, true);
	}

	public Buffer undo() {
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
		if(historyPointer < history.size() - 1) {
			historyPointer += 1;
			apply(history.get(historyPointer).getOperation(), false);
		} else {
			throw new IndexOutOfBoundsException("Nothing to redo");
		}

		return this;
	}

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
}