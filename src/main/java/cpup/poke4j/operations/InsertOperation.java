package cpup.poke4j.operations;

import cpup.poke4j.Buffer;

// Operation wrapper for buffer.insertImpl
// Allows for undoing and notifying (through a generic event)
public class InsertOperation implements IOperation<InsertOperation.Data> {
	protected final int column;
	protected final int line;
	protected final String text;

	public InsertOperation(int _column, int _line, String _text) {
		column = _column;
		line = _line;
		text = _text;
	}

	@Override
	public Data apply(Buffer buffer) {
		final int realLine = buffer.findLine(line);
		final int realColumn = buffer.findColumn(column, line);

		buffer.insertImpl(realColumn, realLine, text);

		return new Data(buffer, this, realColumn, realLine);
	}

	public static class Data extends OperationData<InsertOperation> {
		// Save the real column and line so stuff like -1 works
		// If I didn't do this that would remove from the new end of line instead of the old end of line
		protected final int column;
		protected final int line;

		public Data(Buffer _buffer, InsertOperation _operation, int _column, int _line) {
			super(_buffer, _operation);
			column = _column;
			line = _line;
		}

		@Override
		public void unapply() {
			// Remove the text it added
			buffer.apply(new RemoveOperation(column, line, operation.text.length()), false);
		}

		// Getters and Setters
		public int getColumn() {
			return column;
		}

		public int getLine() {
			return line;
		}
	}
}