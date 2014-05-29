package cpup.poke4j.plugin.movement;

import cpup.poke4j.Cursor;
import cpup.poke4j.plugin.CommandRun;
import cpup.poke4j.plugin.ICommand;

public class MoveLRCommand implements ICommand {
	@Override
	public void invoke(CommandRun run) throws Exception {
		final int dist = run.args().get(0, Integer.class);
		// whether to move by words
		final boolean word = run.args().get(1, Boolean.class, Boolean.FALSE);
		for(Cursor cursor : run.getBuffer().getCursors()) {
			// clear the selection
			cursor.setSelection(null);
			if(word) {
				cursor.moveWord(dist);
			} else {
				cursor.move(dist);
			}
		}
	}

	private static MoveLRCommand instance;

	public static MoveLRCommand get() {
		if(instance == null) {
			instance = new MoveLRCommand();
		}

		return instance;
	}
}