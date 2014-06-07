package cpup.poke4j.ui;

import cpup.poke4j.Buffer;

public interface PokeUI {
	public abstract BufferUI createBufferUI(Buffer buffer);

	// Getters and Setters
	public abstract UI getMainUI();
}