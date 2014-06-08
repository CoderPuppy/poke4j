package cpup.poke4j.plugin.input;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.FluentIterable;
import cpup.poke4j.*;
import cpup.poke4j.Cursor;
import cpup.poke4j.gui.BufferGUI;
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
import cpup.poke4j.ui.BufferUI;
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
		final boolean anyExceptCtrlOrShift = alt || altGr || meta; // if any modifier but ctrl or shift is down this is true
		final boolean anyExceptCtrl = anyExceptCtrlOrShift || shift; // if any modifier but ctrl is down this is true
		final boolean anyMods = anyExceptCtrl || ctrl; // if any modifier is down this is true
		final boolean anyExceptShift = alt || altGr || meta || ctrl; // if any modifier but shift is down this is true
		if(input instanceof KeyInput) {
			final KeyInput keyInput = (KeyInput) input;
			final KeyInput.Type keyType = keyInput.getType();
			final int keyCode = keyInput.getKeyCode();
			final char keyChar = keyInput.getKeyChar();

			if(keyCode == KeyEvent.VK_LEFT && !anyExceptCtrlOrShift) {
				for(Cursor cursor : buffer.getCursors()) {
					if(ctrl)
						cursor.moveWord(-1);
					else
						cursor.move(-1);

					if(!shift)
						cursor.clearSelection();
				}
			} else if(keyCode == KeyEvent.VK_RIGHT && !anyExceptCtrlOrShift) {
				for(Cursor cursor : buffer.getCursors()) {
					if(ctrl)
						cursor.moveWord(1);
					else
						cursor.move(1);

					if(!shift)
						cursor.clearSelection();
				}
			} else if(keyCode == KeyEvent.VK_DOWN && !anyExceptShift) {
				for(Cursor cursor : buffer.getCursors()) {
					cursor.moveUD(1);

					if(!shift)
						cursor.clearSelection();
				}
			} else if(keyCode == KeyEvent.VK_UP && !anyExceptShift) {
				for(Cursor cursor : buffer.getCursors()) {
					cursor.moveUD(-1);

					if(!shift)
						cursor.clearSelection();
				}
			} else if(keyCode == KeyEvent.VK_BACK_SPACE && !anyExceptCtrl) {
				for(Cursor cursor : buffer.getCursors()) {
					if(cursor.getSelection() == null) {
						int len;
						if(ctrl) {
							len = -cursor.getPos().getRightWordLength(buffer);
						} else {
							len = -1;
						}
						buffer.remove(cursor.getPos(), len);
						cursor.move(len);
						cursor.clearSelection();
					} else {
						cursor.removeSelection();
					}
				}
			} else if(keyCode == KeyEvent.VK_DELETE && !anyMods) {
				for(Cursor cursor : buffer.getCursors()) {
					if(cursor.getSelection() == null) {
						int len;
						if(ctrl) {
							len = cursor.getPos().getLeftWordLength(buffer);
						} else {
							len = 1;
						}
						buffer.remove(cursor.getPos(), len);
					} else {
						cursor.removeSelection();
					}
				}
			} else if(keyCode == KeyEvent.VK_S && ctrl && !anyExceptCtrlOrShift) {
				String path = null;
				// try to use an existing path if shift isn't down
				if(!shift) {
					path = buffer.getHints().get("file:path", String.class);
				}
				// if it's still null
				if(path == null) {
					// open a file picker
					JFileChooser chooser = new JFileChooser();
					chooser.setCurrentDirectory(new File("."));
					if(chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
						path = chooser.getSelectedFile().getAbsolutePath();
					}
				}
				logger.debug("saving to {}", path);
				new CommandRun(poke, buffer, SaveCommand.get(), JSArray.of(path)).invoke();
			} else if(keyCode == KeyEvent.VK_O && ctrl && !anyExceptCtrl) {
				JFileChooser chooser = new JFileChooser();
				chooser.setCurrentDirectory(new File("."));
				if(chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					final Buffer newBuffer = new TextBuffer(poke);
					poke.addBuffer(newBuffer);
					input.getActiveUI().replace(input.getActiveUI().getPokeUI().createBufferUI(newBuffer));
					new CommandRun(poke, newBuffer, LoadCommand.get(), JSArray.of(chooser.getSelectedFile().getAbsolutePath())).invoke();
				}
			} else if(keyCode == KeyEvent.VK_C && ctrl && !anyExceptCtrl) {
				// get all the selected text and combine it
				String copied = "";
				int line = -1;
				for(Cursor cursor : buffer.getCursors()) {
					if(cursor.getSelection() != null) {
						final Selection sel = cursor.getSelection();
						if(sel.getBegin().getLine() > line && line != -1) {
							copied += "\n";
						}
						copied += sel.getText();
						line = sel.getEnd().getLine();
					}
				}
				Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(copied), this);
			} else if(keyCode == KeyEvent.VK_V && ctrl && !anyExceptCtrl) {
				// get the contents
				Transferable contents = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
				// if it has a string
				if(contents != null && contents.isDataFlavorSupported(DataFlavor.stringFlavor)) {
					try {
						// then insert it
						new CommandRun(poke, buffer, InsertCommand.get(), JSArray.of((String) contents.getTransferData(DataFlavor.stringFlavor))).invoke();
					} catch (UnsupportedFlavorException e) {
						e.printStackTrace();
					} catch(IOException e) {
						e.printStackTrace();
					}
				}
			} else if(keyCode == KeyEvent.VK_HOME && !anyExceptCtrlOrShift) {

			} else if(keyCode == KeyEvent.VK_END && !anyExceptCtrl) {
				// large movements forwards (if ctrl is down go all the way to the end of the buffer)
				new CommandRun(poke, buffer, LargeMoveLRCommand.get(), JSArray.of(ctrl ? 2 : 1)).invoke();
			} else if(keyCode == KeyEvent.VK_BACK_SLASH && ctrl && shift && !anyExceptCtrlOrShift) {
				input.getActiveUI().splitH();
			// if no modifiers are down and the char isn't backspace or delete
			} else if(keyType == KeyInput.Type.type && !anyExceptShift && keyChar != '\b' && keyChar != '\u007F') {
				// then insert it
				for(Cursor cursor : buffer.getCursors()) {
					cursor.removeSelection();
					buffer.insert(cursor.getPos(), Character.toString(keyChar));
					cursor.move(1);
					cursor.clearSelection();
				}
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