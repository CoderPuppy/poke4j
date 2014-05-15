package cpup.poke4j.plugin.files;

import cpup.poke4j.plugin.CommandRun;
import cpup.poke4j.plugin.ICommand;

public class SaveCommand implements ICommand {
	private static SaveCommand instance;

	public static SaveCommand get() {
		if(instance == null) {
			instance = new SaveCommand();
		}
		return instance;
	}

	@Override
	public void invoke(CommandRun run) {

	}
}