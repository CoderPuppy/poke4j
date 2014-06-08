package cpup.poke4j.plugin.editing;

import cpup.poke4j.Buffer;
import cpup.poke4j.BufferPos;
import cpup.poke4j.Cursor;
import cpup.poke4j.plugin.CommandRun;
import cpup.poke4j.plugin.ICommand;

import java.util.List;

public class RemoveCommand implements ICommand {
	@Override
	public void invoke(CommandRun run) throws Exception {
		final int length = run.args().get(0, Integer.class);
		final Buffer buffer = run.getBuffer();
		for(Cursor cursor : buffer.getCursors()) {
			if(cursor.getSelection() != null) {
				final List<String> lines = cursor.getSelection().getLines();
				int selLength = lines.size() - 1;
				for(String line : lines) {
					selLength += line.length();
				}
				cursor.getSelection().remove(new BufferPos(0, 0), selLength);
			} else {
				final BufferPos pos = cursor.getPos();
				if(length < 0) {
					cursor.move(length);
				}
				buffer.remove(pos, length);
			}
			cursor.clearSelection();
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