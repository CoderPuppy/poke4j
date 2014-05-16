package cpup.poke4j;

import cpup.poke4j.events.EventRegister;

import java.util.ArrayList;
import java.util.List;

public class Poke {
	protected List<Buffer> buffers = new ArrayList<Buffer>();
	protected int currentBuffer = 0;

	public Poke() {
		addBuffer(new TextBuffer(this));
	}

	public Poke addBuffer(Buffer buffer) {
		buffers.add(buffer);
		return this;
	}

	public Poke switchBuffer(Buffer buffer) {
		int index = buffers.indexOf(buffer);
		if(index == -1 || buffer.getPoke() != this) {
			throw new NullPointerException("Attempt to switch to buffer that isn't associated with this");
		} else {
			Buffer oldBuffer = getCurrentBuffer();
			currentBuffer = index;
			switchBufferEv.emit(new SwitchBufferEvent(this, oldBuffer, getCurrentBuffer()));
			return this;
		}
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

	public final EventRegister<SwitchBufferEvent> switchBufferEv = new EventRegister<SwitchBufferEvent>();
	public static class SwitchBufferEvent {
		protected final Poke poke;
		protected final Buffer oldBuffer;
		protected final Buffer newBuffer;

		public SwitchBufferEvent(Poke _poke, Buffer _oldBuffer, Buffer _newBuffer) {
			poke = _poke;
			oldBuffer = _oldBuffer;
			newBuffer = _newBuffer;
		}

		// Getters and Setters
		public Poke getPoke() {
			return poke;
		}

		public Buffer getOldBuffer() {
			return oldBuffer;
		}

		public Buffer getNewBuffer() {
			return newBuffer;
		}
	}
}