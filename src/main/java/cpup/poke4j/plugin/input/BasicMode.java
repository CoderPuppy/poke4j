package cpup.poke4j.plugin.input;

import cpup.poke4j.Buffer;
import cpup.poke4j.Cursor;
import cpup.poke4j.Poke;
import cpup.poke4j.plugin.CommandRun;
import cpup.poke4j.plugin.js.JSArray;
import cpup.poke4j.plugin.movement.MoveLRCommand;
import cpup.poke4j.plugin.movement.MoveUDCommand;

import java.awt.event.KeyEvent;
import java.util.List;

public class BasicMode extends Mode {
	@Override
	public void handle(Input input) {
		final Poke poke = input.getPoke();
		final Buffer buffer = poke.getCurrentBuffer();
		final boolean ctrl = input.isControlDown();
		final boolean alt = input.isAltDown();
		if(input instanceof KeyInput) {
			final KeyInput keyInput = (KeyInput) input;
			final int code = keyInput.getKeyCode();

			if(code == KeyEvent.VK_LEFT) {
				new CommandRun(poke, buffer, MoveLRCommand.get(), JSArray.of(-1, ctrl)).invoke();
			} else if(code == KeyEvent.VK_RIGHT) {
				new CommandRun(poke, buffer, MoveLRCommand.get(), JSArray.of(1, ctrl)).invoke();
			} else if(code == KeyEvent.VK_DOWN) {
				new CommandRun(poke, buffer, MoveUDCommand.get(), JSArray.of(1)).invoke();
			} else if(code == KeyEvent.VK_UP) {
				new CommandRun(poke, buffer, MoveUDCommand.get(), JSArray.of(-1)).invoke();
			} else if(code == KeyEvent.VK_BACK_SPACE) {
				// TODO: implement
				// new CommandRun(poke, buffer, DeleteCommand.get(), JSArray.of(-1)).invoke();
			} else if(code == KeyEvent.VK_DELETE) {
				// TODO: implement
				// new CommandRun(poke, buffer, DeleteCommand.get(), JSArray.of(1)).invoke();
			} else if(keyInput.getType() == KeyInput.Type.type) {
				for(Cursor cursor : buffer.getCursors()) {
					cursor.setSelection(null);
					buffer.insert(cursor.getColumn(), cursor.getLine(), Character.toString(keyInput.getKeyChar()));
					if(!ctrl) {
						cursor.move(1);
					}
				}
			}
		} else if(input instanceof MouseInput) {
			final MouseInput mouseInput = (MouseInput) input;

			if(mouseInput.getType() == MouseInput.Type.click) {
				final List<Cursor> cursors = buffer.getCursors();
				final int line = buffer.findLine(mouseInput.getLine());
				final int column = buffer.findColumn(mouseInput.getColumn(), line);
				if(!alt) {
					cursors.clear();
				}
				cursors.add(new Cursor(buffer.getPoke(), buffer, column, line));
			}
		}
	}

	private static BasicMode instance;

	public static BasicMode get() {
		if(instance == null) {
			instance = new BasicMode();
		}

		return instance;
	}
}