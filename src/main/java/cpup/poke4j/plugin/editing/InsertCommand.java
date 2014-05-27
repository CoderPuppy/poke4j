package cpup.poke4j.plugin.editing;

import cpup.poke4j.Buffer;
import cpup.poke4j.Cursor;
import cpup.poke4j.plugin.CommandRun;
import cpup.poke4j.plugin.ICommand;

public class InsertCommand implements ICommand {
	@Override
	public void invoke(CommandRun run) throws Exception {
		final String text = run.args().get(0, String.class);
		final Buffer buffer = run.getBuffer();
		for(Cursor cursor : buffer.getCursors()) {
			cursor.setSelection(null);
			buffer.insert(cursor.getColumn(), cursor.getLine(), text);
			cursor.move(1);
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