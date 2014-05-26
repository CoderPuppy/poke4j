package cpup.poke4j.gui;

import cpup.poke4j.events.EventHandler;
import cpup.poke4j.Poke;

import javax.swing.*;

public class MainWindow extends JFrame {
	protected final Poke poke;
	protected BufferGUI currentBufferGUI;

	public MainWindow(Poke _poke) {
		final MainWindow self = this;

		poke = _poke;

		setTitle("Poke");
		setSize(800, 600);
		setLocationRelativeTo(null);
		setResizable(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		currentBufferGUI = new BufferGUI(poke, poke.getCurrentBuffer());
		getContentPane().add(currentBufferGUI);
		currentBufferGUI.grabFocus();

		poke.switchBufferEv.listen(new EventHandler<Poke.SwitchBufferEvent>() {
			@Override
			public void handle(Poke.SwitchBufferEvent e) {
			self.getContentPane().remove(self.currentBufferGUI);
			self.getContentPane().add(self.currentBufferGUI = new BufferGUI(self.poke, e.getNewBuffer()));
			self.currentBufferGUI.grabFocus();
			}
		});
	}

	// Getters and Setters
	public Poke getPoke() {
		return poke;
	}

	public BufferGUI getCurrentBufferGUI() {
		return currentBufferGUI;
	}
}