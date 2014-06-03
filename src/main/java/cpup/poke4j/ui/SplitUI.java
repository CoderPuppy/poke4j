package cpup.poke4j.ui;

public interface SplitUI extends UI {
	// Getters and Setters
	public UI getFirst();
	public UI getSecond();
	public Dir getDirection();
	public void setDirection(Dir direction);
	public int getSplit();
	public void setSplit(int split);

	public static enum Dir {
		VERTICAL,
		HORIZONTAL
	}
}