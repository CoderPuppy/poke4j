package cpup.poke4j.plugin.input;

import cpup.poke4j.Buffer;
import cpup.poke4j.Poke;

import java.awt.event.MouseEvent;

public class MouseInput implements Input {
	protected final Poke poke;
	protected final Buffer buffer;
	protected final Type type;
	protected final MouseEvent e;
	protected final int column;
	protected final int line;

	public MouseInput(Poke _poke, Buffer _buffer, Type _type, MouseEvent _e, int _column, int _line) {
		poke = _poke;
		buffer = _buffer;
		type = _type;
		e = _e;
		column = _column;
		line = _line;
	}

	// Getters and Setters
	public Poke getPoke() {
		return poke;
	}

	public Buffer getBuffer() {
		return buffer;
	}

	public Type getType() {
		return type;
	}

	public MouseEvent getEvent() {
		return e;
	}

	public int getColumn() {
		return column;
	}

	public int getLine() {
		return line;
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
		click,
		drag,
		press
	}
}