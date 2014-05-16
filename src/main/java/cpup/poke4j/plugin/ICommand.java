package cpup.poke4j.plugin;

import cpup.poke4j.Poke;

public interface ICommand {
	public void invoke(CommandRun run) throws Exception;
}