package cpup.poke4j.plugin.input;

import cpup.poke4j.Poke;

import java.awt.event.MouseEvent;

public class MouseInput implements Input {
	protected final Poke poke;
	protected final Type type;
	protected final MouseEvent e;
	protected final int column;
	protected final int line;

	public MouseInput(Poke _poke, Type _type, MouseEvent _e, int _column, int _line) {
		poke = _poke;
		type = _type;
		e = _e;
		column = _column;
		line = _line;
	}

	// Getters and Setters
	public Poke getPoke() {
		return poke;
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

	public static enum Type {
		click,
		drag
	}
}