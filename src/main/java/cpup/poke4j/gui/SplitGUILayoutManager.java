package cpup.poke4j.gui;

import cpup.poke4j.ui.SplitUI;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class SplitGUILayoutManager implements LayoutManager {
	protected final SplitGUI split;

	public SplitGUILayoutManager(SplitGUI _split) {
		split = _split;
	}

	@Override
	public void addLayoutComponent(String s, Component component) {}

	@Override
	public void removeLayoutComponent(Component component) {}

	@Override
	public Dimension preferredLayoutSize(Container container) {
		return null;
	}

	@Override
	public Dimension minimumLayoutSize(Container container) {
		return null;
	}

	@Override
	public void layoutContainer(Container container) {
		final SplitUI.Dir dir = split.getDirection();
		final GUI first = split.getFirstGUI();
		final GUI second = split.getSecondGUI();
		if(dir == SplitUI.Dir.HORIZONTAL) {
			first.setLocation(0, 0);
			int splitX = Math.round((((float) split.getSplit()) / 100) * this.split.getWidth());
			first.setSize(splitX, split.getHeight());
			second.setLocation(splitX, 0);
			second.setSize(split.getWidth() - splitX, split.getHeight());
		} else if(dir == SplitUI.Dir.VERTICAL) {
			first.setLocation(0, 0);
			int splitY = Math.round((((float) split.getSplit()) / 100) * this.split.getHeight());
			first.setSize(split.getWidth(), splitY);
			second.setLocation(0, splitY);
			second.setSize(split.getWidth(), split.getHeight() - splitY);
		}
	}

	// Getters and Setters
	public SplitGUI getSplit() {
		return split;
	}

	public static enum Side {
		FIRST,
		SECOND
	}
}