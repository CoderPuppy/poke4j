package cpup.poke4j.ui;

public interface SplitUI extends UI {
	// Getters and Setters
	public UI getFirst();
	public UI getSecond();
	public Dir getDirection();

	public static enum Dir {
		VERTICAL,
		HORIZONTAL
	}
}