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

		final Buffer buffer = poke.getCurrentBuffer();

//		buffer.insert(-1, -1, "Hello");
//		buffer.insert(-1, -1, "\nWorld");
//		buffer.insert(-1, -2, "\nfoo\nbar\nbaz");

		new CommandRun(poke, LoadCommand.get(), JSArray.of("test.txt")).invoke();

		new CommandRun(poke, SelectCommand.get(), JSArray.of(0, 1, 3)).invoke();

		new CommandRun(poke, SaveCommand.get(), JSArray.of("test.txt")).invoke();

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				MainWindow window = new MainWindow(poke);
				window.setVisible(true);
			}
		});
	}
}