package cpup.poke4j.operations;

import cpup.poke4j.Buffer;
import cpup.poke4j.BufferPos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Operation wrapper for buffer.removeImpl
// Allows for undoing and notifying (through a generic event)
public class RemoveOperation implements IOperation<RemoveOperation.Data> {
	public static final Logger logger = LoggerFactory.getLogger(RemoveOperation.class);

	protected final BufferPos pos;
	protected final int length;

	public RemoveOperation(BufferPos _pos, int _length) {
		pos = _pos;
		length = _length;
	}

	@Override
	public Data apply(Buffer buffer) {
		BufferPos realPos = buffer.find(pos);
		int realLength = length;
		if(length < 0) {
			realPos = buffer.offset(realPos, length);
			realLength = Math.abs(realLength);
		}

		String removedText = buffer.removeImpl(realPos, realLength);
		logger.debug("removedText: [{}], realPos = {}, pos = {}, length = {}",
		              removedText,       realPos,      pos,      length);
		return new Data(buffer, this, realPos, removedText);
	}

	// Getters and Setters
	public BufferPos getPos() {
		return pos;
	}

	public int getLength() {
		return length;
	}

	public static class Data extends OperationData<RemoveOperation> {
		// Save the real column and line so stuff like -1 works
		// If I didn't do this that would insert at the new end of line instead of the old end of line
		protected final BufferPos pos;
		protected final String text;

		public Data(Buffer _buffer, RemoveOperation _operation, BufferPos _pos, String _text) {
			super(_buffer, _operation);
			pos = _pos;
			text = _text;
		}

		@Override
		public void unapply() {
			// Insert the text the was removed
			buffer.apply(new InsertOperation(pos, text), false);
		}

		// Getters and Setters
		public BufferPos getPos() {
			return pos;
		}

		public String getText() {
			return text;
		}
	}
}