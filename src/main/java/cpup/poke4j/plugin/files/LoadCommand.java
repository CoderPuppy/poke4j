package cpup.poke4j.plugin.files;

import cpup.poke4j.Buffer;
import cpup.poke4j.Cursor;
import cpup.poke4j.plugin.CommandRun;
import cpup.poke4j.plugin.ICommand;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class LoadCommand implements ICommand {
	@Override
	public void invoke(CommandRun run) throws Exception {
		final String path = run.getArgs().get(0, String.class);
		final File file = new File(path);
		final Buffer buffer = run.getBuffer();
		if(file.exists()) {
			buffer.getHints().put("file:path", path);
			final BufferedReader reader = new BufferedReader(new FileReader(file));
			buffer.load(reader);
			reader.close();
			buffer.getCursors().clear();
			buffer.getCursors().add(new Cursor(run.getPoke(), buffer, 0, 0));
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