package cpup.poke4j;

import com.google.common.base.CharMatcher;
import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.FluentIterable;

import java.util.ArrayList;
import java.util.List;

public class TextBuffer extends Buffer {
	protected final List<String> lines;

	public TextBuffer(Poke _poke, List<String> _lines) {
		super(_poke);
		lines = _lines;
		if(lines.size() <= 0) {
			lines.add("");
		}
	}

	public TextBuffer(Poke _poke) {
		this(_poke, new ArrayList<String>());
	}

	@Override
	public int insertImpl(int column, int line, String text) {
		List<String> newLines = Splitter.on(CharMatcher.anyOf("\n")).splitToList(text);
		String firstLine = newLines.get(0);
		String lastLine = newLines.size() > 1 ? newLines.get(newLines.size() - 1) : null;
		newLines = newLines.subList(1, Math.max(newLines.size() - 2, 1));

		String existingLine = lines.get(line);

		String stay = existingLine.substring(0, column);
		String move = existingLine.substring(column);

		if(lastLine != null) {
			lines.set(line, stay + firstLine);
			lines.add(line + 1, move + lastLine);
		} else {
			lines.set(line, stay + firstLine + move);
		}
		lines.addAll(line + 1, newLines);

//		System.out.println("[ " + Joiner.on(", ").join(new String[] {
//			"insertImpl",
//			"column = [" + Integer.toString(column) + "]",
//			"line = [" + Integer.toString(line) + "]",
//			"first = [" + firstLine + "]",
//			"last = [" + lastLine + "]",
//			"newLines = [ " + Joiner.on(", ").join(FluentIterable.from(newLines).transform(new Function<String, String>() {
//				@Override
//				public String apply(String input) {
//					return "[" + input + "]";
//				}
//			})) + " ]",
//			"existing = [" + existingLine + "]",
//			"stay = [" + stay + "]",
//			"move = [" + move + "]",
//			"lines = [ " + Joiner.on(", ").join(lines) + " ]"
//		}) + " ]");

		return text.length();
	}

	@Override
	public String removeImpl(int column, int line, int length) {
		String removedContent = "";

		while(length > 0) {
			String tLine = lines.get(line);
			String remainingLine = tLine.substring(column);

			if(remainingLine.length() == 0) {
				if(line == lines.size() - 1) {
					length = 0;
				} else {
					String nextLine = lines.remove(line + 1);
					lines.set(line, tLine + nextLine);
					length -= 1;
					removedContent += "\n";
				}
			} else if(length >= remainingLine.length()) {
				removedContent += remainingLine;
				length -= remainingLine.length();
				lines.set(line, tLine.substring(0, column));
			} else {
				removedContent += remainingLine.substring(0, length);
				length = 0;
				lines.set(line, tLine.substring(0, column) + remainingLine.substring(length + 1));
			}
		}

		return removedContent;
	}

	// Getters and Setters
	public List<String> getLines() {
		return lines;
	}
}