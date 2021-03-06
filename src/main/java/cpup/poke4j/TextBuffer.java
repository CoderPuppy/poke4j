package cpup.poke4j;

import com.google.common.base.CharMatcher;
import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.FluentIterable;

import java.io.BufferedReader;
import java.io.BufferedWriter;
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
	public int insertImpl(BufferPos pos, String text) {
		List<String> newLines = Splitter.on(CharMatcher.anyOf("\n")).splitToList(text);
		// The first line of text
		String firstLine = newLines.get(0);
		// The last line of text (null if it only one line)
		String lastLine = newLines.size() > 1 ? newLines.get(newLines.size() - 1) : null;
		// The rest of the lines to insert between
		newLines = newLines.subList(1, Math.max(newLines.size() - 2, 1));

		String existingLine = lines.get(pos.getLine());

		// The part of the original line that should stay (before column)
		String stay = existingLine.substring(0, pos.getColumn());
		// The part of the original line that should be moved (after column)
		String move = existingLine.substring(pos.getColumn());

		// If text spans multiple lines
		if(lastLine != null) {
			// Add the first line to the line it's inserting at
			lines.set(pos.getLine(), stay + firstLine);
			// And add the last line after it (with the rest of the original line)
			lines.add(pos.getLine() + 1, lastLine + move);
		} else {
			// Insert it in the middle of the original line
			lines.set(pos.getLine(), stay + firstLine + move);
		}
		// Add the rest of the lines between
		lines.addAll(pos.getLine() + 1, newLines);

		return text.length();
	}

	@Override
	public String removeImpl(BufferPos pos, int length) {
		String removedContent = "";

		while(length > 0) {
			String tLine = lines.get(pos.getLine());
			String remainingLine = tLine.substring(pos.getColumn());

			if(remainingLine.length() == 0) {
				if(pos.getLine() == lines.size() - 1) {
					length = 0;
				} else {
					String nextLine = lines.remove(pos.getLine() + 1);
					lines.set(pos.getLine(), tLine + nextLine);
					length -= 1;
					removedContent += "\n";
				}
			} else if(length >= remainingLine.length()) {
				removedContent += remainingLine;
				length -= remainingLine.length();
				lines.set(pos.getLine(), tLine.substring(0, pos.getColumn()));
			} else {
				removedContent += remainingLine.substring(0, length);
				length = 0;
				lines.set(pos.getLine(), tLine.substring(0, pos.getColumn()) + remainingLine.substring(length + 1));
			}
		}

		return removedContent;
	}

	@Override
	public void load(BufferedReader reader) throws Exception {
		lines.clear();
		String line;
		while((line = reader.readLine()) != null) {
			lines.add(line);
		}
		if(lines.size() == 0) {
			lines.add("");
		}
	}

	@Override
	public void save(BufferedWriter writer) throws Exception {
		writer.write(Joiner.on('\n').join(lines));
	}

	// Getters and Setters
	public List<String> getLines() {
		return lines;
	}

	@Override
	public int getLineCount() {
		return lines.size();
	}

	@Override
	public String getLine(int line) {
		if(line < lines.size()) {
			return lines.get(line);
		} else {
			return null;
		}
	}
}