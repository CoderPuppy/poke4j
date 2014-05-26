package cpup.poke4j.plugin.movement;

import cpup.poke4j.Cursor;
import cpup.poke4j.plugin.CommandRun;
import cpup.poke4j.plugin.ICommand;

public class MoveUDCommand implements ICommand {
	@Override
	public void invoke(CommandRun run) throws Exception {
		final int dist = run.args().get(0, Integer.class);
		final int amt = Math.abs(dist);
		final boolean up = (dist / amt) == -1;
		for(Cursor cursor : run.getBuffer().getCursors()) {
			for(int i = 0; i < amt; i++) {
				cursor.setSelection(null);
				final int line = cursor.getLine();
				final int column = cursor.getColumn();
				if(up) {
					if(line > 0) {
						final int newLine = line - 1;
						cursor.setLine(newLine);
						cursor.setColumn(run.getBuffer().findColumn(column, newLine));
					}
				} else {
					if(line < run.getBuffer().getLineCount() - 1) {
						final int newLine = line + 1;
						cursor.setLine(newLine);
						cursor.setColumn(run.getBuffer().findColumn(column, newLine));
					}
				}
			}
		}
	}

	private static MoveUDCommand instance;

	public static MoveUDCommand get() {
		if(instance == null) {
			instance = new MoveUDCommand();
		}

		return instance;
	}
}