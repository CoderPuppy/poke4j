package cpup.poke4j.plugin.input;

import cpup.poke4j.Buffer;
import cpup.poke4j.Cursor;
import cpup.poke4j.Poke;
import cpup.poke4j.plugin.CommandRun;
import cpup.poke4j.plugin.editing.InsertCommand;
import cpup.poke4j.plugin.editing.RemoveCommand;
import cpup.poke4j.plugin.js.JSArray;
import cpup.poke4j.plugin.movement.MoveLRCommand;
import cpup.poke4j.plugin.movement.MoveUDCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.event.KeyEvent;
import java.util.List;

public class BasicMode extends Mode {
	public static final Logger logger = LoggerFactory.getLogger(BasicMode.class);

	@Override
	public void handle(Input input) {
		final Poke poke = input.getPoke();
		final Buffer buffer = poke.getCurrentBuffer();
		final boolean ctrl = input.isControlDown();
		final boolean alt = input.isAltDown();
		if(input instanceof KeyInput) {
			final KeyInput keyInput = (KeyInput) input;
			final KeyInput.Type keyType = keyInput.getType();
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
				new CommandRun(poke, buffer, RemoveCommand.get(), JSArray.of(-1)).invoke();
			} else if(code == KeyEvent.VK_DELETE) {
				new CommandRun(poke, buffer, RemoveCommand.get(), JSArray.of(1)).invoke();
			} else if(keyType == KeyInput.Type.type && !ctrl && keyInput.getKeyCode() != 0) {
				new CommandRun(poke, buffer, InsertCommand.get(), JSArray.of(Character.toString(keyInput.getKeyChar()))).invoke();
			}
		} else if(input instanceof MouseInput) {
			final MouseInput mouseInput = (MouseInput) input;
			final MouseInput.Type mouseType = mouseInput.getType();

			if(mouseType == MouseInput.Type.click) {
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