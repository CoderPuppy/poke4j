package cpup.poke4j.events;

public interface EventHandler<ET> {
	public void handle(ET e);
}