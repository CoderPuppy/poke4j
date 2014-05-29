package cpup.poke4j;

import cpup.poke4j.gui.MainWindow;
import cpup.poke4j.plugin.CommandRun;
import cpup.poke4j.plugin.files.LoadCommand;
import cpup.poke4j.plugin.files.SaveCommand;
import cpup.poke4j.plugin.input.BasicMode;
import cpup.poke4j.plugin.input.Input;
import cpup.poke4j.plugin.input.Mode;
import cpup.poke4j.plugin.js.JSArray;
import cpup.poke4j.plugin.movement.SelectCommand;

import javax.swing.*;

public class Start {
	public static void main(String[] args) {
		final Poke poke = new Poke();

		poke.setMode(BasicMode.get());

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				MainWindow window = new MainWindow(poke);
				window.setVisible(true);
			}
		});
	}
}