package cpup.poke4j.plugin.files;

import cpup.poke4j.Buffer;
import cpup.poke4j.plugin.CommandRun;
import cpup.poke4j.plugin.ICommand;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

public class LoadCommand implements ICommand {
	private static LoadCommand instance;

	public static LoadCommand get() {
		if(instance == null) {
			instance = new LoadCommand();
		}
		return instance;
	}

	@Override
	public void invoke(CommandRun run) throws Exception {
		// TODO: i think this is broken
		String path = (String) run.getArgs().get(0);
		File file = new File(path);
		if(file.exists()) {
			Buffer buffer = run.getBuffer();
			buffer.removeImpl(0, 0, buffer.getText().length());

			BufferedReader reader = new BufferedReader(new FileReader(file));

			String line;
			int i = 0;
			while((line = reader.readLine()) != null) {
				buffer.insert(0, i, line + "\n");
				i++;
			}
			reader.close();
			buffer.remove(-1, -2, 1);
		} else {
			throw new FileNotFoundException("No file named: " + path);
		}
	}
}