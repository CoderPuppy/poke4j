package cpup.poke4j.gui;

import cpup.poke4j.Buffer;
import cpup.poke4j.Poke;

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
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		final FontMetrics fontMetrics = g.getFontMetrics();
		final List<String> lines = buffer.getLines();
		for(int i = 0; i < lines.size(); i++) {
			g.drawString(lines.get(i), 0, (i + 1) * fontMetrics.getHeight());
		}
	}

	// Getters and Setters
	public Poke getPoke() {
		return poke;
	}

	public Buffer getBuffer() {
		return buffer;
	}
}