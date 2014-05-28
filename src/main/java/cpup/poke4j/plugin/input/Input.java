package cpup.poke4j.plugin.input;

import cpup.poke4j.Buffer;
import cpup.poke4j.Poke;
import cpup.poke4j.ui.PokeUI;
import cpup.poke4j.ui.UI;

public interface Input {
	// Getters and Setters
	public abstract Poke getPoke();
	public abstract Buffer getBuffer();
//	public abstract PokeUI getPokeUI();
//	public abstract UI getActiveUI();
	public abstract int getModifiers();
	public abstract int getModifiersEx();
	public abstract boolean isShiftDown();
	public abstract boolean isAltDown();
	public abstract boolean isControlDown();
	public abstract boolean isMetaDown();
	public abstract boolean isAltGraphDown();
}