package cpup.poke4j;

import cpup.poke4j.gui.MainWindow;
import cpup.poke4j.operations.InsertOperation;
import cpup.poke4j.plugin.CommandRun;
import cpup.poke4j.plugin.files.LoadCommand;
import cpup.poke4j.plugin.files.SaveCommand;

import javax.swing.*;
import java.util.Arrays;

public class Start {
	public static void main(String[] args) {
		final Poke poke = new Poke();
		final InsertOperation.Data data = new InsertOperation(0, 0, "HI!").apply(poke.getCurrentBuffer());
		//poke.getCurrentBuffer().insertImpl(0, 0, "HI!");
		poke.getCurrentBuffer().insertImpl(2, 0, " World");
		System.out.println("[" + poke.getCurrentBuffer().removeImpl(2, 0, 1) + "]");
		poke.getCurrentBuffer().insertImpl(2, 0, "\n");
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