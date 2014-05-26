package cpup.poke4j.plugin.files;

import cpup.poke4j.plugin.CommandRun;
import cpup.poke4j.plugin.ICommand;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class SaveCommand implements ICommand {
	@Override
	public void invoke(CommandRun run) throws Exception {
		String path = run.getArgs().get(0, String.class);
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(path)));
		run.getBuffer().save(writer);
		writer.close();
	}

	private static SaveCommand instance;

	public static SaveCommand get() {
		if(instance == null) {
			instance = new SaveCommand();
		}
		return instance;
	}
}