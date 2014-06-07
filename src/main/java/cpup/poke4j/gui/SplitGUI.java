package cpup.poke4j.gui;

import cpup.poke4j.ui.SplitUI;
import cpup.poke4j.ui.UI;

import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class SplitGUI extends GUI implements SplitUI, ContainerGUI {
	protected Dir direction;
	protected GUI first;
	protected GUI second;
	protected int split;

	public SplitGUI(MainWindow _mainWindow, GUI _parent, Dir _direction, int _split, GUI _first, GUI _second) {
		super(_mainWindow, _parent);
		direction = _direction;
		split = _split;
		first = _first;
		second = _second;

		setLayout(new SplitGUILayoutManager(this));

		add(first, SplitGUILayoutManager.Side.FIRST);
		add(second, SplitGUILayoutManager.Side.SECOND);
	}

	public SplitGUI(MainWindow _mainWindow, GUI _parent, Dir _direction, GUI _first, GUI _second) {
		this(_mainWindow, _parent, _direction, 50, _first, _second);
	}

	@Override
	public void startup() {
		first.startup();
		second.startup();
	}

	@Override
	public void cleanup() {
		first.cleanup();
		second.cleanup();
	}

	@Override
	public GUI duplicateGUI() {
		return new SplitGUI(mainWindow, parent, direction, first.duplicateGUI(), second.duplicateGUI());
	}

	@Override
	public void replace(GUI sel, GUI repl) {
		if(first == sel) {
			setFirst(repl);
		}

		if(second == sel) {
			setSecond(repl);
		}
	}

	// Getters and Setters
	@Override
	public UI getFirst() {
		return first;
	}

	public GUI getFirstGUI() {
		return first;
	}

	public void setFirst(GUI _first) {
		first.cleanup();
		remove(first);
		first = _first;
		add(_first, SplitGUILayoutManager.Side.FIRST);
		_first.startup();
	}

	@Override
	public UI getSecond() {
		return second;
	}

	public GUI getSecondGUI() {
		return second;
	}

	public void setSecond(GUI _second) {
		second.cleanup();
		remove(second);
		second = _second;
		add(_second, SplitGUILayoutManager.Side.SECOND);
		_second.startup();
	}

	@Override
	public int getSplit() {
		return split;
	}

	@Override
	public void setSplit(int split) {
		this.split = split;
		revalidate();
		repaint();
	}

	@Override
	public Dir getDirection() {
		return direction;
	}

	@Override
	public void setDirection(Dir direction) {
		this.direction = direction;
	}
}