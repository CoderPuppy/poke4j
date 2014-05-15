package cpup.poke4j;

import java.util.Vector;

public class Buffer {
	protected final Poke poke;
	protected final Vector<String> lines;

	public Buffer(Poke _poke, Vector<String> _lines) {
		poke = _poke;
		lines = _lines;
	}

	public Buffer(Poke _poke) {
		this(_poke, new Vector<String>());
	}

	// Getters and Setters
	public Poke getPoke() {
		return poke;
	}

	public Vector<String> getLines() {
		return lines;
	}
}