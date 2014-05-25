package cpup.poke4j.gui;

import cpup.poke4j.Buffer;
import cpup.poke4j.Cursor;
import cpup.poke4j.Poke;
import cpup.poke4j.Selection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.Set;

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
			g.drawRect(
				getTextX(cursor.getColumn()),
				getTextY(cursor.getLine()),
				1,
				metrics.getHeight()
			);
		}

		g.setColor(Color.white);
		for(int i = 0; i < lines.size(); i++) {
			g.drawString(lines.get(i), getTextX(0), getTextY(i + 1));
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		final Set<Cursor> cursors = buffer.getCursors();
		final int line = buffer.findLine(getLine(e.getY()));
		final int column = buffer.findColumn(getColumn(e.getX()), line);
		cursors.clear();
		cursors.add(new Cursor(buffer.getPoke(), buffer, column, line));
		repaint();
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO: select stuff
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent mouseWheelEvent) {
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
		// TODO: up and down
		final Set<Cursor> cursors = buffer.getCursors();
		final boolean ctrl = e.isControlDown();
		int dir = 0;
		if(e.getKeyCode() == KeyEvent.VK_LEFT) {
			dir = -1;
		} else if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
			dir = 1;
		}
		if(dir != 0) {
			for(Cursor cursor : cursors) {
				cursor.setSelection(null);
				if(ctrl) {
					cursor.moveWord(dir);
				} else {
					cursor.move(dir);
				}
			}
		}
		if(e.getKeyCode() == KeyEvent.VK_UP) {
			for(Cursor cursor : cursors) {
				final int line = cursor.getLine();
				final int column = cursor.getColumn();
				if(line > 0) {
					cursor.setSelection(null);
					final int newLine = line - 1;
					cursor.setLine(newLine);
					cursor.setColumn(buffer.findColumn(column, newLine));
				}
			}
		}
		if(e.getKeyCode() == KeyEvent.VK_DOWN) {
			for(Cursor cursor : cursors) {
				final int line = cursor.getLine();
				final int column = cursor.getColumn();
				if(line < buffer.getLineCount() - 1) {
					cursor.setSelection(null);
					final int newLine = line + 1;
					cursor.setLine(newLine);
					cursor.setColumn(buffer.findColumn(column, newLine));
				}
			}
		}
		repaint();
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
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyReleased(KeyEvent e) {}

	protected int getTextX(int column) {
		return column * getCharWidth() + 2;
	}

	protected int getTextY(int line) {
		return line * getLineHeight() - scroll;
	}

	protected int getLine(int y) {
		return (y + scroll) / getLineHeight();
	}

	protected int getColumn(int x) {
		return (x - 2) / getCharWidth();
	}

	protected int getCharWidth() {
		return metrics.charWidth('A');
	}

	protected int getLineHeight() {
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