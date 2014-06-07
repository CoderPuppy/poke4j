package cpup.poke4j.gui;

import cpup.poke4j.Buffer;
import cpup.poke4j.Poke;
import cpup.poke4j.ui.BufferUI;
import cpup.poke4j.ui.PokeUI;
import cpup.poke4j.ui.UI;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame implements PokeUI {
	protected final Poke poke;
	protected GUI mainUI;
	protected final Font font = new Font(Font.MONOSPACED, Font.PLAIN, 11);
	protected final FontMetrics metrics = getFontMetrics(font);
	protected final int lineHeight = metrics.getHeight();
	protected final int charWidth = metrics.getWidths()[0];

	public MainWindow(Poke _poke) {
		final MainWindow self = this;

		poke = _poke;

		setTitle("Poke");
		setSize(800, 600);
		setLocationRelativeTo(null);
		setResizable(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		mainUI = createBufferGUI(poke.getBuffers().get(0));
		mainUI.startup();
		getContentPane().add(mainUI);
		mainUI.grabFocus();
	}

	public BufferGUI createBufferGUI(Buffer buffer) {
		return new BufferGUI(this, null, buffer);
	}

	@Override
	public BufferUI createBufferUI(Buffer buffer) {
		return createBufferGUI(buffer);
	}

	// Getters and Setters
	public Poke getPoke() {
		return poke;
	}

	public UI getMainUI() {
		return mainUI;
	}

	public void setMainUI(GUI _mainUI) {
		getContentPane().remove(mainUI);
		mainUI.cleanup();
		mainUI = _mainUI;
		_mainUI.startup();
		getContentPane().add(_mainUI);
		_mainUI.grabFocus();
		revalidate();
		repaint();
	}

	public Font getFont() {
		return font;
	}

	public FontMetrics getMetrics() {
		return metrics;
	}

	public int getLineHeight() {
		return lineHeight;
	}

	public int getCharWidth() {
		return charWidth;
	}
}