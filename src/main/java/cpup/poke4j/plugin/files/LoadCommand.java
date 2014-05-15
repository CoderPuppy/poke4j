package cpup.poke4j.plugin.files;

import cpup.poke4j.plugin.CommandRun;
import cpup.poke4j.plugin.ICommand;

public class LoadCommand implements ICommand {
	private static LoadCommand instance;

	public static LoadCommand get() {
		if(instance == null) {
			instance = new LoadCommand();
		}
		return instance;
	}

	@Override
	public void invoke(CommandRun run) {

	}
}