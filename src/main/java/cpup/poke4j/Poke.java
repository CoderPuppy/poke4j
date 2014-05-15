package cpup.poke4j;

import java.util.ArrayList;
import java.util.List;

public class Poke {
	protected List<Buffer> buffers = new ArrayList<Buffer>();
	protected int currentBuffer = 0;

	public Poke() {
		addBuffer(new Buffer(this));
	}

	public Poke addBuffer(Buffer buffer) {
		buffers.add(buffer);
		return this;
	}

	// Getters and Setters
	public List<Buffer> getBuffers() {
		return buffers;
	}

	public Buffer getCurrentBuffer() {
		if(currentBuffer == -1) {
			return null;
		} else {
			return buffers.get(currentBuffer);
		}
	}
}