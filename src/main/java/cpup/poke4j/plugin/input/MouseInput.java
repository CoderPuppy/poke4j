package cpup.poke4j.plugin.input;

import cpup.poke4j.Buffer;
import cpup.poke4j.BufferPos;
import cpup.poke4j.Poke;
import cpup.poke4j.ui.PokeUI;
import cpup.poke4j.ui.UI;

import java.awt.event.MouseEvent;

public class MouseInput implements Input {
	protected final Poke poke;
	protected final Buffer buffer;
	protected final PokeUI pokeUI;
	protected final UI activeUI;
	protected final Type type;
	protected final MouseEvent e;
	protected final BufferPos pos;

	public MouseInput(Poke _poke, Buffer _buffer, PokeUI _pokeUI, UI _activeUI, Type _type, MouseEvent _e, BufferPos _pos) {
		poke = _poke;
		buffer = _buffer;
		pokeUI = _pokeUI;
		activeUI = _activeUI;
		type = _type;
		e = _e;
		pos = _pos;
	}

	// Getters and Setters
	public Poke getPoke() {
		return poke;
	}

	public Buffer getBuffer() {
		return buffer;
	}

	public PokeUI getPokeUI() {
		return pokeUI;
	}

	public UI getActiveUI() {
		return activeUI;
	}

	public Type getType() {
		return type;
	}

	public MouseEvent getEvent() {
		return e;
	}

	public BufferPos getPos() {
		return pos;
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