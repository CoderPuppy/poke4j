package cpup.poke4j.ui;

public interface UI {
	public abstract SplitUI splitH();
	public abstract SplitUI splitV();

	// Getters and Setters
	public abstract PokeUI getPokeUI();
	public abstract UI getParentUI();
}