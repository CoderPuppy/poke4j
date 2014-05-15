package cpup.poke4j.gui;

import cpup.poke4j.Poke;

import javax.swing.*;

public class MainWindow extends JFrame {
	protected final Poke poke;

	public MainWindow(Poke _poke) {
		poke = _poke;

		setTitle("Poke");
		setSize(800, 600);
		setLocationRelativeTo(null);
		setResizable(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	// Getters and Setters
	public Poke getPoke() {
		return poke;
	}
}