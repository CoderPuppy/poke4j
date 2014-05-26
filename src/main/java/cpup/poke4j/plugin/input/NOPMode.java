package cpup.poke4j.plugin.input;

public class NOPMode extends Mode {
	@Override
	public void handle(Input input) {}

	private static NOPMode instance;

	public static NOPMode get() {
		if(instance == null) {
			instance = new NOPMode();
		}

		return instance;
	}
}