package cpup.poke4j.gui;

import cpup.poke4j.Buffer;
import cpup.poke4j.Cursor;
import cpup.poke4j.Poke;
import cpup.poke4j.Selection;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class BufferGUI extends JComponent {
	protected final Poke poke;
	protected final Buffer buffer;
	protected final Font font = new Font(Font.MONOSPACED, Font.PLAIN, 11);

	public BufferGUI(Poke _poke, Buffer _buffer) {
		final BufferGUI self = this;

		poke = _poke;
		buffer = _buffer;
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		g.setColor(Color.black);
		g.fillRect(0, 0, g.getClipBounds().width, g.getClipBounds().height);

		g.setFont(font);

		final int lineHeight = getLineHeight(g);
		final int baseX = getTextX(g, 0);

		final List<String> lines = buffer.getLines();
		for(Cursor cursor : buffer.getCursors()) {
			final Selection selection = cursor.getSelection();
			if(selection != null) {
				g.setColor(new Color(87, 87, 87));
				final int beginLine = selection.getBeginLine();
				final int beginColumn = selection.getBeginColumn();
				final int endLine = selection.getEndLine();
				final int endColumn = selection.getEndColumn();

				final int startX = getTextX(g, beginColumn);
				final int startY = getTextY(g, beginLine);

				if(beginLine == endLine) {
					g.fillRect(
						startX, startY,
						getTextX(g, endColumn) - startX,
						lineHeight
					);
				} else {
					g.fillRect(
						startX, startY,
						getTextX(g, lines.get(beginLine).length()) - startX,
						lineHeight
					);
					for(int i = beginLine + 1; i < endLine; i++) {
						System.out.println("normal write: " + i);
						g.fillRect(
							baseX, getTextY(g, i),
							getTextX(g, lines.get(i).length()) - baseX,
							lineHeight
						);
					}
					g.fillRect(
						baseX, getTextY(g, endLine),
						getTextX(g, endColumn) - baseX,
						lineHeight
					);
				}
			}

			g.setColor(Color.white);
			g.drawRect(
				getTextX(g, cursor.getColumn()),
				getTextY(g, cursor.getLine()),
				1,
				g.getFontMetrics().getHeight()
			);
		}

		g.setColor(Color.white);
		for(int i = 0; i < lines.size(); i++) {
			g.drawString(lines.get(i), getTextX(g, 0), getTextY(g, i + 1));
		}
	}

	protected int getTextX(Graphics g, int column) {
		return column * getCharWidth(g)  + 2;
	}

	protected int getTextY(Graphics g, int line) {
		return line * getLineHeight(g);
	}

	protected int getCharWidth(Graphics g) {
		return g.getFontMetrics(font).charWidth('A');
	}

	protected int getLineHeight(Graphics g) {
		return g.getFontMetrics(font).getHeight();
	}

	// Getters and Setters
	public Poke getPoke() {
		return poke;
	}

	public Buffer getBuffer() {
		return buffer;
	}
}