package cpup.poke4j.plugin.movement;

import cpup.poke4j.BufferPos;
import cpup.poke4j.Cursor;
import cpup.poke4j.plugin.CommandRun;
import cpup.poke4j.plugin.ICommand;

public class MoveUDCommand implements ICommand {
	@Override
	public void invoke(CommandRun run) throws Exception {
		final int dist = run.args().get(0, Integer.class);
		if(dist == 0) {
			return;
		}
		final int amt = Math.abs(dist);
		final boolean up = (dist / amt) == -1;
		for(Cursor cursor : run.getBuffer().getCursors()) {
			for(int i = 0; i < amt; i++) {
				cursor.clearSelection();
				final int line = cursor.getPos().getLine();
				final int column = cursor.getPos().getColumn();
				if(up) {
					if(line > 0) {
						cursor.setPos(run.getBuffer().find(new BufferPos(column, line - 1)));
					}
				} else {
					if(line < run.getBuffer().getLineCount() - 1) {
						cursor.setPos(run.getBuffer().find(new BufferPos(column, line + 1)));
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