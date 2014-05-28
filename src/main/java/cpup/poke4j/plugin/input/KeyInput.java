package cpup.poke4j.plugin.input;

import cpup.poke4j.Buffer;
import cpup.poke4j.Poke;

import java.awt.event.KeyEvent;

public class KeyInput implements Input {
	protected final Poke poke;
	protected final Buffer buffer;
	protected final Type type;
	protected final KeyEvent e;

	public KeyInput(Poke _poke, Buffer _buffer, Type _type, KeyEvent _e) {
		poke = _poke;
		buffer = _buffer;
		type = _type;
		e = _e;
	}

	public Buffer getBuffer() {
		return buffer;
	}

	// Getters and Setters
	public Poke getPoke() {
		return poke;
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