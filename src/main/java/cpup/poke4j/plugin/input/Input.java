package cpup.poke4j.plugin.input;

import cpup.poke4j.Poke;

public interface Input {
	// Getters and Setters
	public abstract Poke getPoke();
	public abstract int getModifiers();
	public abstract int getModifiersEx();
	public abstract boolean isShiftDown();
	public abstract boolean isAltDown();
	public abstract boolean isControlDown();
	public abstract boolean isMetaDown();
	public abstract boolean isAltGraphDown();
}