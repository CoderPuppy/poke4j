package cpup.poke4j.gui;

import cpup.poke4j.events.EventHandler;
import cpup.poke4j.Poke;
import cpup.poke4j.ui.PokeUI;
import cpup.poke4j.ui.UI;

import javax.swing.*;

public class MainWindow extends JFrame implements PokeUI {
	protected final Poke poke;
	protected GUI mainUI;

	public MainWindow(Poke _poke) {
		final MainWindow self = this;

		poke = _poke;

		setTitle("Poke");
		setSize(800, 600);
		setLocationRelativeTo(null);
		setResizable(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		mainUI = new BufferGUI(this, null, poke.getBuffers().get(0));
		getContentPane().add(mainUI);
		mainUI.grabFocus();
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
		this.mainUI = _mainUI;
		getContentPane().add(_mainUI);
		_mainUI.grabFocus();
	}
}