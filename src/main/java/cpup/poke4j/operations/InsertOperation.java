package cpup.poke4j.operations;

import cpup.poke4j.Buffer;
import cpup.poke4j.BufferPos;

// Operation wrapper for buffer.insertImpl
// Allows for undoing and notifying (through a generic event)
public class InsertOperation implements IOperation<InsertOperation.Data> {
	protected final BufferPos pos;
	protected final String text;

	public InsertOperation(BufferPos _pos, String _text) {
		pos = _pos;
		text = _text;
	}

	@Override
	public Data apply(Buffer buffer) {
		final BufferPos realPos = buffer.find(pos);

		buffer.insertImpl(realPos, text);

		return new Data(buffer, this, realPos);
	}

	// Getters and Setters
	public BufferPos getPos() {
		return pos;
	}

	public String getText() {
		return text;
	}

	public static class Data extends OperationData<InsertOperation> {
		// Save the real column and line so stuff like -1 works
		// If I didn't do this that would remove from the new end of line instead of the old end of line
		protected final BufferPos pos;

		public Data(Buffer _buffer, InsertOperation _operation, BufferPos _pos) {
			super(_buffer, _operation);
			pos = _pos;
		}

		@Override
		public void unapply() {
			// Remove the text it added
			buffer.apply(new RemoveOperation(pos, operation.text.length()), false);
		}

		// Getters and Setters
		public BufferPos getPos() {
			return pos;
		}
	}
}