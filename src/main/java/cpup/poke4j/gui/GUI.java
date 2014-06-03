package cpup.poke4j.gui;

import cpup.poke4j.ui.PokeUI;
import cpup.poke4j.ui.SplitUI;
import cpup.poke4j.ui.UI;

import javax.swing.*;
import java.awt.*;

public abstract class GUI extends JComponent implements UI {
	protected GUI parent;
	protected final MainWindow mainWindow;
	protected final Font font;
	protected final FontMetrics metrics;
	protected final int lineHeight;
	protected final int charWidth;

	public GUI(MainWindow _mainWindow, GUI _parent) {
		mainWindow = _mainWindow;
		parent = _parent;
		font = mainWindow.getFont();
		metrics = mainWindow.getMetrics();
		lineHeight = mainWindow.getLineHeight();
		charWidth = mainWindow.getCharWidth();
	}

	public abstract GUI duplicateGUI();

	@Override
	public void startup() {}

	@Override
	public void cleanup() {}

	@Override
	public UI duplicate() {
		return duplicateGUI();
	}

	@Override
	public SplitUI splitH() {
		SplitGUI split = new SplitGUI(mainWindow, parent, SplitUI.Dir.HORIZONTAL, this, this.duplicateGUI());
		if(parent == null) {
			mainWindow.setMainUI(split);
		} else if(parent instanceof ContainerGUI) {
			((ContainerGUI) parent).replace(this, split);
		}
		parent = split;
		split.getSecondGUI().parent = split;
		return split;
	}

	@Override
	public SplitUI splitV() {
		SplitGUI split = new SplitGUI(mainWindow, parent, SplitUI.Dir.VERTICAL, this, this.duplicateGUI());
		if(parent == null) {
			mainWindow.setMainUI(split);
		} else if(parent instanceof ContainerGUI) {
			((ContainerGUI) parent).replace(this, split);
		}
		parent = split;
		split.getSecondGUI().parent = split;
		return split;
	}

	// Getters and Setters
	@Override
	public PokeUI getPokeUI() {
		return mainWindow;
	}

	public MainWindow getMainWindow() {
		return mainWindow;
	}

	@Override
	public UI getParentUI() {
		return parent;
	}
}