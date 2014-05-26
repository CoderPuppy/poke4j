package cpup.poke4j.plugin.movement;

import cpup.poke4j.Cursor;
import cpup.poke4j.Selection;
import cpup.poke4j.plugin.CommandRun;
import cpup.poke4j.plugin.ICommand;

public class SelectCommand implements ICommand {
	@Override
	public void invoke(CommandRun run) throws Exception {
		final int cursorI = run.args().get(0, Integer.class);
		final int column = run.args().get(1, Integer.class);
		final int line = run.args().get(2, Integer.class);
		final Cursor cursor = run.getBuffer().getCursors().get(cursorI);
		cursor.setSelection(new Selection(
			run.getPoke(),
			run.getBuffer(),
			cursor.getColumn(),
			cursor.getLine(),
			column,
			line
		));
	}

	private static SelectCommand instance;

	public static SelectCommand get() {
		if(instance == null) {
			instance = new SelectCommand();
		}

		return instance;
	}
}