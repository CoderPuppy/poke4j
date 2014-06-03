package cpup.poke4j.plugin.input;

import cpup.poke4j.Buffer;
import cpup.poke4j.Poke;
import cpup.poke4j.ui.PokeUI;
import cpup.poke4j.ui.UI;

import java.awt.event.KeyEvent;

public class KeyInput implements Input {
	protected final Poke poke;
	protected final Buffer buffer;
	protected final PokeUI pokeUI;
	protected final UI activeUI;
	protected final Type type;
	protected final KeyEvent e;

	public KeyInput(Poke _poke, Buffer _buffer, PokeUI _pokeUI, UI _activeUI, Type _type, KeyEvent _e) {
		poke = _poke;
		buffer = _buffer;
		pokeUI = _pokeUI;
		activeUI = _activeUI;
		type = _type;
		e = _e;
	}

	// Getters and Setters
	public Poke getPoke() {
		return poke;
	}

	public PokeUI getPokeUI() {
		return pokeUI;
	}

	public UI getActiveUI() {
		return activeUI;
	}

	public Buffer getBuffer() {
		return buffer;
	}

	public Type getType() {
		return type;
	}

	public KeyEvent getEvent() {
		return e;
	}

	public int getKeyCode() {
		return e.getKeyCode();
	}

	public char getKeyChar() {
		return e.getKeyChar();
	}

	public boolean isShiftDown() {
		return e.isShiftDown();
	}

	public boolean isControlDown() {
		return e.isControlDown();
	}

	public boolean isMetaDown() {
		return e.isMetaDown();
	}

	public boolean isAltDown() {
		return e.isAltDown();
	}

	public boolean isAltGraphDown() {
		return e.isAltGraphDown();
	}

	public int getModifiers() {
		return e.getModifiers();
	}

	public int getModifiersEx() {
		return e.getModifiersEx();
	}

	public static enum Type {
		press,
		type
	}
}