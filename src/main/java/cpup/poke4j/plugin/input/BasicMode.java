package cpup.poke4j.plugin.input;

import cpup.poke4j.Buffer;
import cpup.poke4j.Cursor;
import cpup.poke4j.Poke;
import cpup.poke4j.plugin.CommandRun;
import cpup.poke4j.plugin.editing.InsertCommand;
import cpup.poke4j.plugin.editing.RemoveCommand;
import cpup.poke4j.plugin.files.LoadCommand;
import cpup.poke4j.plugin.files.SaveCommand;
import cpup.poke4j.plugin.js.JSArray;
import cpup.poke4j.plugin.movement.MoveLRCommand;
import cpup.poke4j.plugin.movement.MoveUDCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.List;

public class BasicMode extends Mode {
	public static final Logger logger = LoggerFactory.getLogger(BasicMode.class);

	@Override
	public void handle(Input input) {
		final Poke poke = input.getPoke();
		final Buffer buffer = input.getBuffer();
		final boolean ctrl = input.isControlDown();
		final boolean alt = input.isAltDown();
		final boolean shift = input.isShiftDown();
		if(input instanceof KeyInput) {
			final KeyInput keyInput = (KeyInput) input;
			final KeyInput.Type keyType = keyInput.getType();
			final int keyCode = keyInput.getKeyCode();
			final char keyChar = keyInput.getKeyChar();

			if(keyCode == KeyEvent.VK_LEFT) {
				new CommandRun(poke, buffer, MoveLRCommand.get(), JSArray.of(-1, ctrl)).invoke();
			} else if(keyCode == KeyEvent.VK_RIGHT) {
				new CommandRun(poke, buffer, MoveLRCommand.get(), JSArray.of(1, ctrl)).invoke();
			} else if(keyCode == KeyEvent.VK_DOWN) {
				new CommandRun(poke, buffer, MoveUDCommand.get(), JSArray.of(1)).invoke();
			} else if(keyCode == KeyEvent.VK_UP) {
				new CommandRun(poke, buffer, MoveUDCommand.get(), JSArray.of(-1)).invoke();
			} else if(keyCode == KeyEvent.VK_BACK_SPACE) {
				new CommandRun(poke, buffer, RemoveCommand.get(), JSArray.of(-1)).invoke();
			} else if(keyCode == KeyEvent.VK_DELETE) {
				new CommandRun(poke, buffer, RemoveCommand.get(), JSArray.of(1)).invoke();
			} else if(keyCode == KeyEvent.VK_S && ctrl) {
				String path = null;
				if(!shift) {
					path = buffer.getHints().get("file:path", String.class);
				}
				if(path == null) {
					JFileChooser chooser = new JFileChooser();
					chooser.setCurrentDirectory(new File("."));
					if(chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
						path = chooser.getSelectedFile().getAbsolutePath();
					}
				}
				logger.debug("saving to {}", path);
				new CommandRun(poke, buffer, SaveCommand.get(), JSArray.of(path)).invoke();
			} else if(keyCode == KeyEvent.VK_O && input.isControlDown()) {
				JFileChooser chooser = new JFileChooser();
				chooser.setCurrentDirectory(new File("."));
				if(chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					new CommandRun(poke, buffer, LoadCommand.get(), JSArray.of(chooser.getSelectedFile().getAbsolutePath())).invoke();
				}
			} else if(keyType == KeyInput.Type.type && !ctrl && keyChar != '\b' && keyChar != '\u007F') {
				new CommandRun(poke, buffer, InsertCommand.get(), JSArray.of(Character.toString(keyInput.getKeyChar()))).invoke();
			} else {
				logger.debug("Unhandled KeyInput: {} {} ({}), char = {}", keyType.toString(), KeyEvent.getKeyText(keyCode), keyCode, keyChar);
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