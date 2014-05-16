package cpup.poke4j.operations;

import cpup.poke4j.Buffer;

public abstract class OperationData<OP extends IOperation> {
	protected final Buffer buffer;
	protected final OP operation;

	public OperationData(Buffer _buffer, OP _operation) {
		buffer = _buffer;
		operation = _operation;
	}

	public abstract void unapply();

	// Getters and Setters
	public Buffer getBuffer() {
		return buffer;
	}

	public OP getOperation() {
		return operation;
	}
}