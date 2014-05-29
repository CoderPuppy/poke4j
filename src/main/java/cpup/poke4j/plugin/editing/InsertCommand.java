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
			if(cursor.getSelection() != null) {
				final List<String> lines = cursor.getSelection().getLines();
				int length = lines.size() - 1;
				for(String line : lines) {
					length += line.length();
				}
				cursor.getSelection().remove(new BufferPos(0, 0), length);
			}
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