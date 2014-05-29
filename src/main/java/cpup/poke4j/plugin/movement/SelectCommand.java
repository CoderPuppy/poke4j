package cpup.poke4j.plugin.movement;

import cpup.poke4j.Buffer;
import cpup.poke4j.BufferPos;
import cpup.poke4j.Cursor;
import cpup.poke4j.Selection;
import cpup.poke4j.plugin.CommandRun;
import cpup.poke4j.plugin.ICommand;

public class SelectCommand implements ICommand {
	@Override
	public void invoke(CommandRun run) throws Exception {
		final int cursorI = run.args().get(0, Integer.class);
		final int dist = run.args().get(1, Integer.class);
		final boolean word = run.args().get(2, Boolean.class, Boolean.FALSE);
		if(cursorI == -1) {
			for(Cursor cursor : run.getBuffer().getCursors()) {
				modifySelection(cursor, dist, word);
			}
		} else {
			final Cursor cursor = run.getBuffer().getCursors().get(cursorI);
			modifySelection(cursor, dist, word);
		}
	}

	protected void modifySelection(Cursor cursor, int dist, boolean word) {
		Selection sel = cursor.getSelectionWithDefault();
		final Buffer buffer = cursor.getBuffer();
		final BufferPos begin = sel.getBegin();
		final BufferPos end = sel.getEnd();
		if(sel.getBegin().equals(cursor.getPos())) {
			sel = new Selection(sel.getPoke(), sel.getBuffer(), (word ? begin.moveWord(buffer, dist) : begin.move(buffer, dist)), end);
		} else if(sel.getEnd().equals(cursor.getPos())) {
			sel = new Selection(sel.getPoke(), sel.getBuffer(), begin, (word ? end.moveWord(buffer, dist) : end.move(buffer, dist)));
		}
		if(word) {
			cursor.moveWord(dist);
		} else {
			cursor.move(dist);
		}
		cursor.setSelection(sel);
	}

	private static SelectCommand instance;

	public static SelectCommand get() {
		if(instance == null) {
			instance = new SelectCommand();
		}

		return instance;
	}
}