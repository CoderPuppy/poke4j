package cpup.poke4j.gui;

import cpup.poke4j.ui.SplitUI;
import cpup.poke4j.ui.UI;

public class SplitGUI extends GUI implements SplitUI, ContainerGUI {
	protected Dir direction;
	protected GUI first;
	protected GUI second;

	public SplitGUI(MainWindow _mainWindow, GUI _parent, Dir _direction, GUI _first, GUI _second) {
		super(_mainWindow, _parent);
		direction = _direction;
		first = _first;
		second = _second;
	}

	@Override
	public void replace(GUI sel, GUI repl) {
		if(first == sel) {
			first = repl;
		}

		if(second == sel) {
			second = repl;
		}
	}

	// Getters and Setters
	@Override
	public UI getFirst() {
		return first;
	}

	public void setFirst(GUI first) {
		this.first = first;
	}

	@Override
	public UI getSecond() {
		return second;
	}

	public void setSecond(GUI second) {
		this.second = second;
	}

	@Override
	public Dir getDirection() {
		return direction;
	}

	public void setDirection(Dir direction) {
		this.direction = direction;
	}
}