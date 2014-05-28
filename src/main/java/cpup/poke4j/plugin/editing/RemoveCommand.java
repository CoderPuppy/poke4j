package cpup.poke4j.plugin.editing;

import cpup.poke4j.Buffer;
import cpup.poke4j.Cursor;
import cpup.poke4j.plugin.CommandRun;
import cpup.poke4j.plugin.ICommand;

public class RemoveCommand implements ICommand {
	@Override
	public void invoke(CommandRun run) throws Exception {
		final int length = run.args().get(0, Integer.class);
		final Buffer buffer = run.getBuffer();
		for(Cursor cursor : buffer.getCursors()) {
			cursor.setSelection(null);
			final int column = cursor.getColumn();
			final int line = cursor.getLine();
			if(length < 0) {
				cursor.move(length);
			}
			buffer.remove(column, line, length);
		}
	}

	private static RemoveCommand instance;

	public static RemoveCommand get() {
		if(instance == null) {
			instance = new RemoveCommand();
		}

		return instance;
	}
}