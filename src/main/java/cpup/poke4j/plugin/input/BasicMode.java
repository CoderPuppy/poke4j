package cpup.poke4j.plugin.input;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.FluentIterable;
import cpup.poke4j.Buffer;
import cpup.poke4j.BufferPos;
import cpup.poke4j.Cursor;
import cpup.poke4j.Poke;
import cpup.poke4j.plugin.CommandRun;
import cpup.poke4j.plugin.editing.InsertCommand;
import cpup.poke4j.plugin.editing.RemoveCommand;
import cpup.poke4j.plugin.files.LoadCommand;
import cpup.poke4j.plugin.files.SaveCommand;
import cpup.poke4j.plugin.js.JSArray;
import cpup.poke4j.plugin.movement.LargeMoveLRCommand;
import cpup.poke4j.plugin.movement.MoveLRCommand;
import cpup.poke4j.plugin.movement.MoveUDCommand;
import cpup.poke4j.plugin.movement.SelectCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class BasicMode extends Mode implements ClipboardOwner {
	public static final Logger logger = LoggerFactory.getLogger(BasicMode.class);

	@Override
	public void handle(Input input) {
		final Poke poke = input.getPoke();
		final Buffer buffer = input.getBuffer();
		final boolean ctrl = input.isControlDown();
		final boolean alt = input.isAltDown();
		final boolean altGr = input.isAltGraphDown();
		final boolean shift = input.isShiftDown();
		final boolean meta = input.isMetaDown();
		final boolean allButCtrlAndShift = alt || altGr || meta;
		final boolean allButCtrl = allButCtrlAndShift || shift;
		final boolean allMods = allButCtrl || ctrl;
		if(input instanceof KeyInput) {
			final KeyInput keyInput = (KeyInput) input;
			final KeyInput.Type keyType = keyInput.getType();
			final int keyCode = keyInput.getKeyCode();
			final char keyChar = keyInput.getKeyChar();

			if(keyCode == KeyEvent.VK_LEFT && !allButCtrlAndShift) {
				if(shift) {
					new CommandRun(poke, buffer, SelectCommand.get(), JSArray.of(-1, -1, ctrl)).invoke();
				} else {
					new CommandRun(poke, buffer, MoveLRCommand.get(), JSArray.of(-1, ctrl)).invoke();
				}
			} else if(keyCode == KeyEvent.VK_RIGHT && !allButCtrlAndShift) {
				if(shift) {
					new CommandRun(poke, buffer, SelectCommand.get(), JSArray.of(-1, 1, ctrl)).invoke();
				} else {
					new CommandRun(poke, buffer, MoveLRCommand.get(), JSArray.of(1, ctrl)).invoke();
				}
			} else if(keyCode == KeyEvent.VK_DOWN && !allMods) {
				new CommandRun(poke, buffer, MoveUDCommand.get(), JSArray.of(1)).invoke();
			} else if(keyCode == KeyEvent.VK_UP && !allMods) {
				new CommandRun(poke, buffer, MoveUDCommand.get(), JSArray.of(-1)).invoke();
			} else if(keyCode == KeyEvent.VK_BACK_SPACE && !allMods) {
				new CommandRun(poke, buffer, RemoveCommand.get(), JSArray.of(-1)).invoke();
			} else if(keyCode == KeyEvent.VK_DELETE && !allMods) {
				new CommandRun(poke, buffer, RemoveCommand.get(), JSArray.of(1)).invoke();
			} else if(keyCode == KeyEvent.VK_S && ctrl && !allButCtrl) {
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
			} else if(keyCode == KeyEvent.VK_O && ctrl && !allButCtrl) {
				JFileChooser chooser = new JFileChooser();
				chooser.setCurrentDirectory(new File("."));
				if(chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					new CommandRun(poke, buffer, LoadCommand.get(), JSArray.of(chooser.getSelectedFile().getAbsolutePath())).invoke();
				}
			} else if(keyCode == KeyEvent.VK_C && ctrl && !allButCtrl) {
				Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(
					Joiner.on("\n").join(
						FluentIterable.from(buffer.getCursors())
							.transform(new Function<Cursor, String>() {
								@Override
								public String apply(Cursor input) {
									return input.getSelectionWithDefault().getText();
								}
							})
					)
				), this);
			} else if(keyCode == KeyEvent.VK_V && ctrl && !allButCtrl) {
				Transferable contents = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
				if(contents != null && contents.isDataFlavorSupported(DataFlavor.stringFlavor)) {
					try {
						new CommandRun(poke, buffer, InsertCommand.get(), JSArray.of((String) contents.getTransferData(DataFlavor.stringFlavor))).invoke();
					} catch (UnsupportedFlavorException | IOException e) {
						e.printStackTrace();
					}
				}
			} else if(keyCode == KeyEvent.VK_HOME && !allButCtrl) {
				new CommandRun(poke, buffer, LargeMoveLRCommand.get(), JSArray.of(ctrl ? -2 : -1)).invoke();
			} else if(keyCode == KeyEvent.VK_END && !allButCtrl) {
				new CommandRun(poke, buffer, LargeMoveLRCommand.get(), JSArray.of(ctrl ? 2 : 1)).invoke();
			} else if(keyType == KeyInput.Type.type && !ctrl && !alt && !altGr && !meta && keyChar != '\b' && keyChar != '\u007F') {
				new CommandRun(poke, buffer, InsertCommand.get(), JSArray.of(Character.toString(keyInput.getKeyChar()))).invoke();
			} else {
				logger.debug("Unhandled KeyInput: {} {} ({}), char = {}", keyType.toString(), KeyEvent.getKeyText(keyCode), keyCode, keyChar);
			}
		} else if(input instanceof MouseInput) {
			final MouseInput mouseInput = (MouseInput) input;
			final MouseInput.Type mouseType = mouseInput.getType();

			if(mouseType == MouseInput.Type.click) {
				final List<Cursor> cursors = buffer.getCursors();
				final BufferPos pos = buffer.find(mouseInput.getPos());
				if(!alt) {
					cursors.clear();
				}
				cursors.add(new Cursor(buffer.getPoke(), buffer, pos));
			}
		}
	}

	@Override
	public void lostOwnership(Clipboard clipboard, Transferable transferable) {}

	private static BasicMode instance;

	public static BasicMode get() {
		if(instance == null) {
			instance = new BasicMode();
		}

		return instance;
	}
}