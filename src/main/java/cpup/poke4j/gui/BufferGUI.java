package cpup.poke4j.gui;

import cpup.poke4j.Buffer;
import cpup.poke4j.Cursor;
import cpup.poke4j.Poke;
import cpup.poke4j.Selection;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class BufferGUI extends JPanel {
	protected final Poke poke;
	protected final Buffer buffer;

	public BufferGUI(Poke _poke, Buffer _buffer) {
		final BufferGUI self = this;

		poke = _poke;
		buffer = _buffer;

		setFont(new Font(Font.MONOSPACED, Font.PLAIN, 11));
		setBackground(Color.black);
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		final List<String> lines = buffer.getLines();
		for(Cursor cursor : buffer.getCursors()) {
			g.setColor(Color.white);
			g.drawRect(
				getTextX(g, cursor.getColumn()),
				getTextY(g, cursor.getLine()),
				1,
				g.getFontMetrics().getHeight()
			);

			final Selection selection = cursor.getSelection();
			if(selection != null) {
				final int x = getTextX(g, selection.getBeginColumn());
				final int y = getTextY(g, selection.getBeginLine());
				g.setColor(new Color(87, 87, 87));
				g.drawRect(
					x, y,
					x - getTextX(g, selection.getEndColumn()),
					y - getTextY(g, selection.getEndLine())
				);
			}
		}

		g.setColor(Color.white);
		for(int i = 0; i < lines.size(); i++) {
			g.drawString(lines.get(i), getTextX(g, 0), getTextY(g, i + 1));
		}
	}

	protected int getTextX(Graphics g, int column) {
		return column * g.getFontMetrics().getWidths()[0] + 2;
	}

	protected int getTextY(Graphics g, int line) {
		return line * g.getFontMetrics().getHeight();
	}

	// Getters and Setters
	public Poke getPoke() {
		return poke;
	}

	public Buffer getBuffer() {
		return buffer;
	}
}