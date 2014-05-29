package cpup.poke4j.plugin.movement;

import cpup.poke4j.Buffer;
import cpup.poke4j.BufferPos;
import cpup.poke4j.Cursor;
import cpup.poke4j.Poke;
import cpup.poke4j.plugin.CommandRun;
import cpup.poke4j.plugin.ICommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class LargeMoveLRCommand implements ICommand {
	@Override
	public void invoke(CommandRun run) throws Exception {
		final int dir = run.args().get(0, Integer.class);
		final Poke poke = run.getPoke();
		final Buffer buffer = run.getBuffer();
		final List<Cursor> cursors = run.getBuffer().getCursors();

		switch(dir) {
			case -1:
				for(Cursor cursor : cursors) {
					cursor.setPos(new BufferPos(0, cursor.getPos().getLine()));
				}
				break;
			case -2:
				cursors.clear();
				cursors.add(new Cursor(poke, buffer, new BufferPos(0, 0)));
				break;

			case 1:
				for(Cursor cursor : cursors) {
					final int line = cursor.getPos().getLine();
					cursor.setPos(new BufferPos(buffer.getLine(line).length(), line));
				}
				break;
			case 2:
				cursors.clear();
				final int line = buffer.getLineCount() - 1;
				final int column = buffer.getLine(line).length();
				cursors.add(new Cursor(poke, buffer, new BufferPos(column, line)));
				break;
		}
	}

	private static LargeMoveLRCommand instance;

	public static LargeMoveLRCommand get() {
		if(instance == null) {
			instance = new LargeMoveLRCommand();
		}

		return instance;
	}
}