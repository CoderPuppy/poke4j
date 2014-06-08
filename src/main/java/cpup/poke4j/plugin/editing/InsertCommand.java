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
			cursor.removeSelection();
			buffer.insert(cursor.getPos(), text);
			cursor.move(text.length());
			// and deselect whatever might be selected
			cursor.clearSelection();
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