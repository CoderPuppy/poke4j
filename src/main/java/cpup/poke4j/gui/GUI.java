package cpup.poke4j.gui;

import cpup.poke4j.ui.PokeUI;
import cpup.poke4j.ui.SplitUI;
import cpup.poke4j.ui.UI;

import javax.swing.*;

public abstract class GUI extends JComponent implements UI {
	protected GUI parent;
	protected final MainWindow mainWindow;

	public GUI(MainWindow _mainWindow, GUI _parent) {
		mainWindow = _mainWindow;
		parent = _parent;
	}

	@Override
	public SplitUI splitH() {
		SplitGUI split = new SplitGUI(mainWindow, parent, SplitUI.Dir.HORIZONTAL, this, this);
		if(parent == null) {
			mainWindow.setMainUI(split);
		} else if(parent instanceof ContainerGUI) {
			((ContainerGUI) parent).replace(this, split);
		}
		parent = split;
		return split;
	}

	@Override
	public SplitUI splitV() {
		SplitGUI split = new SplitGUI(mainWindow, parent, SplitUI.Dir.VERTICAL, this, this);
		if(parent == null) {
			mainWindow.setMainUI(split);
		} else if(parent instanceof ContainerGUI) {
			((ContainerGUI) parent).replace(this, split);
		}
		parent = split;
		return split;
	}

	// Getters and Setters
	@Override
	public PokeUI getPokeUI() {
		return mainWindow;
	}

	@Override
	public UI getParentUI() {
		return parent;
	}
}