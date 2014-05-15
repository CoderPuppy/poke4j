package cpup.poke4j;

import cpup.poke4j.gui.MainWindow;
import cpup.poke4j.plugin.CommandRun;
import cpup.poke4j.plugin.files.LoadCommand;
import cpup.poke4j.plugin.files.SaveCommand;

import javax.swing.*;
import java.util.Arrays;

public class Start {
	public static void main(String[] args) {
		final Poke poke = new Poke();
		new CommandRun(poke, LoadCommand.get(), Arrays.asList(new Object[] { "test.txt" })).invoke();
		new CommandRun(poke, SaveCommand.get(), Arrays.asList(new Object[] { "test.txt" })).invoke();
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				MainWindow window = new MainWindow(poke);
				window.setVisible(true);
			}
		});
	}
}