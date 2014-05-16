package cpup.poke4j.operations;

import cpup.poke4j.Buffer;

public interface IOperation<D extends OperationData> {
	public D apply(Buffer buffer);
}