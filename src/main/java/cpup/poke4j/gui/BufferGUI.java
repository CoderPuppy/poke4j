package cpup.poke4j.gui;

import cpup.poke4j.*;
import cpup.poke4j.Cursor;
import cpup.poke4j.events.EventHandler;
import cpup.poke4j.plugin.CommandRun;
import cpup.poke4j.plugin.input.KeyInput;
import cpup.poke4j.plugin.input.MouseInput;
import cpup.poke4j.plugin.js.JSArray;
import cpup.poke4j.plugin.movement.MoveLRCommand;
import cpup.poke4j.plugin.movement.MoveUDCommand;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class BufferGUI extends GUI implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener {
	protected final Poke poke;
	protected final Buffer buffer;
	protected int scroll = 0;

	protected EventHandler<Buffer.ApplyOperationEvent> applyOperationHandler;
	protected EventHandler<Cursor.MoveEvent> moveCursorHandler;

	public BufferGUI(MainWindow _mainWindow, GUI _parent, Buffer _buffer) {
		super(_mainWindow, _parent);

		final BufferGUI self = this;

		poke = mainWindow.getPoke();
		buffer = _buffer;

		addKeyListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);
		addMouseWheelListener(this);
		setFocusable(true);
	}

	@Override
	public void startup() {
		buffer.applyOperationEv.listen(applyOperationHandler = new EventHandler<Buffer.ApplyOperationEvent>() {
			@Override
			public void handle(Buffer.ApplyOperationEvent e) {
				repaint();
			}
		});

		buffer.moveCursorEv.listen(moveCursorHandler = new EventHandler<Cursor.MoveEvent>() {
			@Override
			public void handle(Cursor.MoveEvent e) {
				repaint();
			}
		});
	}

	@Override
	public void cleanup() {
		buffer.applyOperationEv.unlisten(applyOperationHandler);
		buffer.moveCursorEv.unlisten(moveCursorHandler);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		g.setColor(Color.black);
		g.fillRect(0, 0, g.getClipBounds().width, g.getClipBounds().height);

		g.setFont(font);

		final int baseX = getTextX(0);

		final List<String> lines = buffer.getLines();
		for(Cursor cursor : buffer.getCursors()) {
			final Selection selection = cursor.getSelection();
			if(selection != null) {
				g.setColor(new Color(87, 87, 87));
				final int beginLine = selection.getBegin().getLine();
				final int beginColumn = selection.getBegin().getColumn();
				final int endLine = selection.getEnd().getLine();
				final int endColumn = selection.getEnd().getColumn();

				final int startX = getTextX(beginColumn);
				final int startY = getTextY(beginLine) + 2;

				if(beginLine == endLine) {
					g.fillRect(
						startX, startY,
						getTextX(endColumn) - startX,
						lineHeight
					);
				} else {
					g.fillRect(
						startX, startY,
						getTextX(lines.get(beginLine).length()) - startX,
						lineHeight
					);
					for(int i = beginLine + 1; i < endLine; i++) {
						g.fillRect(
							baseX, getTextY(i) + 2,
							getTextX(lines.get(i).length()) - baseX,
							lineHeight
						);
					}
					g.fillRect(
						baseX, getTextY(endLine) + 2,
						getTextX(endColumn) - baseX,
						lineHeight
					);
				}
			}

			g.setColor(Color.white);
			final int x = getTextX(cursor.getPos().getColumn());
			final int y = getTextY(cursor.getPos().getLine()) + 2;
			g.drawLine(x, y, x, y + lineHeight - 1);
		}

		g.setColor(Color.white);
		for(int i = 0; i < lines.size(); i++) {
			g.drawString(lines.get(i), getTextX(0), getTextY(i + 1));
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		grabFocus();
		poke.getMode().handle(new MouseInput(poke, buffer, mainWindow, this, MouseInput.Type.click, e, new BufferPos(getColumn(e.getX()), getLine(e.getY()))));
	}

	@Override
	public void mousePressed(MouseEvent e) {
		grabFocus();
		poke.getMode().handle(new MouseInput(poke, buffer, mainWindow, this, MouseInput.Type.press, e, new BufferPos(getColumn(e.getX()), getLine(e.getY()))));
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		poke.getMode().handle(new MouseInput(poke, buffer, mainWindow, this, MouseInput.Type.drag, e, new BufferPos(getColumn(e.getX()), getLine(e.getY()))));
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent mouseWheelEvent) {
		// TODO: move scroll to Buffer?
		scroll += mouseWheelEvent.getWheelRotation() * 20;
		if(scroll < 0) {
			scroll = 0;
		}
		final int lastLine = getTextY(buffer.getLineCount() - 1) + scroll;
		if(scroll > lastLine) {
			scroll = lastLine;
		}
		repaint();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		poke.getMode().handle(new KeyInput(poke, buffer, mainWindow, this, KeyInput.Type.press, e));
	}

	@Override
	public void keyTyped(KeyEvent e) {
		poke.getMode().handle(new KeyInput(poke, buffer, mainWindow, this, KeyInput.Type.type, e));
	}

	@Override
	public GUI duplicateGUI() {
		return new BufferGUI(mainWindow, parent, buffer);
	}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mouseMoved(MouseEvent e) {}

	@Override
	public void keyReleased(KeyEvent e) {}

	public int getTextX(int column) {
		return column * getCharWidth() + 2;
	}

	public int getTextY(int line) {
		return line * getLineHeight() - scroll;
	}

	public int getLine(int y) {
		return (y + scroll) / getLineHeight();
	}

	public int getColumn(int x) {
		return (x - 2) / getCharWidth();
	}

	public int getCharWidth() {
		return metrics.charWidth('A');
	}

	public int getLineHeight() {
		return metrics.getHeight();
	}

	// Getters and Setters
	public Poke getPoke() {
		return poke;
	}

	public Buffer getBuffer() {
		return buffer;
	}
}