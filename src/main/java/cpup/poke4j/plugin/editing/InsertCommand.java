package cpup.poke4j.plugin.editing;

import cpup.poke4j.Buffer;
import cpup.poke4j.BufferPos;
import cpup.poke4j.Cursor;
import cpup.poke4j.plugin.CommandRun;
import cpup.poke4j.plugin.ICommand;

import java.util.List;

public class InsertCommand implements ICommand {
	@Override
	public void invoke(CommandRun run) throws Exception {
		final String text = run.args().get(0, String.class);
		final Buffer buffer = run.getBuffer();
		for(Cursor cursor : buffer.getCursors()) {
			// if something is selected
			if(cursor.getSelection() != null) {
				// remove it
				final List<String> lines = cursor.getSelection().getLines();
				int length = lines.size() - 1; // number of new lines
				for(String line : lines) {
					// + the lengths of all the lines
					length += line.length();
				}
				// remove all that
				cursor.getSelection().remove(new BufferPos(0, 0), length);
			}
			// and deselect whatever might be selected
			cursor.setSelection(null);
			buffer.insert(cursor.getPos(), text);
			cursor.move(text.length());
		}
	}

	private static InsertCommand instance;

	public static InsertCommand get() {
		if(instance == null) {
			instance = new InsertCommand();
		}

		return instance;
	}
}