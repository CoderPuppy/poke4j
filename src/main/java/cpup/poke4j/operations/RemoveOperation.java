package cpup.poke4j.operations;

import cpup.poke4j.Buffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Operation wrapper for buffer.removeImpl
// Allows for undoing and notifying (through a generic event)
public class RemoveOperation implements IOperation<RemoveOperation.Data> {
	public static final Logger logger = LoggerFactory.getLogger(RemoveOperation.class);

	protected final int column;
	protected final int line;
	protected final int length;

	public RemoveOperation(int _column, int _line, int _length) {
		column = _column;
		line = _line;
		length = _length;
	}

	@Override
	public Data apply(Buffer buffer) {
		final int realLine = buffer.findLine(line);
		final int realColumn = buffer.findColumn(column, line);

		String removedText = buffer.removeImpl(realColumn, realLine, length);
		logger.debug("removedText: [{}], realColumn = {}, realLine = {}, line = {}, column = {}, length = {}",
		              removedText,       realColumn,      realLine,      line,      column,      length);
		return new Data(buffer, this, realColumn, realLine, removedText);
	}

	public static class Data extends OperationData<RemoveOperation> {
		// Save the real column and line so stuff like -1 works
		// If I didn't do this that would insert at the new end of line instead of the old end of line
		protected final int column;
		protected final int line;
		protected final String text;

		public Data(Buffer _buffer, RemoveOperation _operation, int _column, int _line, String _text) {
			super(_buffer, _operation);
			column = _column;
			line = _line;
			text = _text;
		}

		@Override
		public void unapply() {
			// Insert the text the was removed
			buffer.apply(new InsertOperation(column, line, text), false);
		}

		// Getters and Setters
		public int getColumn() {
			return column;
		}

		public int getLine() {
			return line;
		}

		public String getText() {
			return text;
		}
	}
}