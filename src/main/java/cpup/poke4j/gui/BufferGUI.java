package cpup.poke4j.gui;

import cpup.poke4j.Buffer;
import cpup.poke4j.Cursor;
import cpup.poke4j.Poke;
import cpup.poke4j.Selection;
import cpup.poke4j.plugin.CommandRun;
import cpup.poke4j.plugin.input.MouseInput;
import cpup.poke4j.plugin.js.JSArray;
import cpup.poke4j.plugin.movement.MoveLRCommand;
import cpup.poke4j.plugin.movement.MoveUDCommand;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class BufferGUI extends JComponent implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener {
	protected final Poke poke;
	protected final Buffer buffer;
	protected final Font font = new Font(Font.MONOSPACED, Font.PLAIN, 11);
	protected final FontMetrics metrics = getFontMetrics(font);
	protected final int lineHeight = metrics.getHeight();
	protected final int charWidth = metrics.getWidths()[0];
	protected int scroll = 0;

	public BufferGUI(Poke _poke, Buffer _buffer) {
		super();

		final BufferGUI self = this;

		poke = _poke;
		buffer = _buffer;

		addKeyListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);
		addMouseWheelListener(this);
		setFocusable(true);
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
				final int beginLine = selection.getBeginLine();
				final int beginColumn = selection.getBeginColumn();
				final int endLine = selection.getEndLine();
				final int endColumn = selection.getEndColumn();

				final int startX = getTextX(beginColumn);
				final int startY = getTextY(beginLine);

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
							baseX, getTextY(i),
							getTextX(lines.get(i).length()) - baseX,
							lineHeight
						);
					}
					g.fillRect(
						baseX, getTextY(endLine),
						getTextX(endColumn) - baseX,
						lineHeight
					);
				}
			}

			g.setColor(Color.white);
			final int x = getTextX(cursor.getColumn());
			final int y = getTextY(cursor.getLine());
			g.drawLine(x, y, x, y + metrics.getHeight());
		}

		g.setColor(Color.white);
		for(int i = 0; i < lines.size(); i++) {
			g.drawString(lines.get(i), getTextX(0), getTextY(i + 1));
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		poke.getMode().handle(new MouseInput(poke, MouseInput.Type.click, e, getColumn(e.getX()), getLine(e.getY())));
		final List<Cursor> cursors = buffer.getCursors();
		final int line = buffer.findLine(getLine(e.getY()));
		final int column = buffer.findColumn(getColumn(e.getX()), line);
		if(!e.isAltDown()) {
			cursors.clear();
		}
		cursors.add(new Cursor(buffer.getPoke(), buffer, column, line));
		repaint();
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO: select stuff
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
		final boolean ctrl = e.isControlDown();
		int dir = 0;
		if(e.getKeyCode() == KeyEvent.VK_LEFT) {
			dir = -1;
		} else if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
			dir = 1;
		}
		if(dir != 0) {
			new CommandRun(poke, buffer, MoveLRCommand.get(), JSArray.of(dir, ctrl)).invoke();
		}
		if(e.getKeyCode() == KeyEvent.VK_UP) {
			new CommandRun(poke, buffer, MoveUDCommand.get(), JSArray.of(-1)).invoke();
		}
		if(e.getKeyCode() == KeyEvent.VK_DOWN) {
			new CommandRun(poke, buffer, MoveUDCommand.get(), JSArray.of(1)).invoke();
		}
		repaint();
	}

	@Override
	public void keyTyped(KeyEvent e) {
		for(Cursor cursor : buffer.getCursors()) {
			cursor.setSelection(null);
			buffer.insert(cursor.getColumn(), cursor.getLine(), Character.toString(e.getKeyChar()));
			if(!e.isControlDown()) {
				cursor.move(1);
			}
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {}

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