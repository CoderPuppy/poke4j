package cpup.poke4j.plugin.files;

import cpup.poke4j.plugin.CommandRun;
import cpup.poke4j.plugin.ICommand;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class LoadCommand implements ICommand {
	@Override
	public void invoke(CommandRun run) throws Exception {
		String path = run.getArgs().get(0, String.class);
		File file = new File(path);
		if(file.exists()) {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			run.getBuffer().load(reader);
			reader.close();
		} else {
			throw new FileNotFoundException("No file named: " + path);
		}
	}

	private static LoadCommand instance;

	public static LoadCommand get() {
		if(instance == null) {
			instance = new LoadCommand();
		}
		return instance;
	}
}