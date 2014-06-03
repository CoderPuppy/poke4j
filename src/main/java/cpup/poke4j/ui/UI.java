package cpup.poke4j.ui;

public interface UI {
	public abstract SplitUI splitH();
	public abstract SplitUI splitV();
	public abstract UI duplicate();
	public abstract void startup();
	public abstract void cleanup();

	// Getters and Setters
	public abstract PokeUI getPokeUI();
	public abstract UI getParentUI();
}