package cpup.poke4j;

import cpup.poke4j.events.EventRegister;
import cpup.poke4j.plugin.input.Mode;
import cpup.poke4j.plugin.input.NOPMode;

import java.util.ArrayList;
import java.util.List;

public class Poke {
	protected List<Buffer> buffers = new ArrayList<Buffer>();
	protected Mode mode = NOPMode.get();

	public Poke() {
		addBuffer(new TextBuffer(this));
	}

	public Poke addBuffer(Buffer buffer) {
		buffers.add(buffer);
		return this;
	}

	// Getters and Setters
	public List<Buffer> getBuffers() {
		return buffers;
	}

	public Mode getMode() {
		return mode;
	}

	public void setMode(Mode mode) {
		if(mode == null) {
			throw new NullPointerException("Mode can't be null");
		}
		this.mode = mode;
	}
}