package cpup.poke4j;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Cursor {
	protected final Poke poke;
	protected final Buffer buffer;
	protected int column;
	protected int line;
	protected Selection selection;

	public Cursor(Poke _poke, Buffer _buffer, int _column, int _line, Selection _selection) {
		poke = _poke;
		buffer = _buffer;
		column = _column;
		line = _line;
		selection = _selection;
	}

	public Cursor(Poke _poke, Buffer _buffer, int _column, int _line) {
		this(_poke, _buffer, _column, _line, null);
	}

	public void move(int dist) {
		final int amt = Math.abs(dist);
		if(amt == 0) return;
		final int dir = dist / amt;
		for(int i = 0; i < amt; i++) {
			if(dir == 1 && column == buffer.getLine(line).length()) {
				if(line < buffer.getLineCount() - 1) {
					line += 1;
					column = 0;
				}
			} else if(dir == -1 && column == 0) {
				if(line > 0) {
					line -= 1;
					column = buffer.getLine(line).length();
				}
			} else {
				column += dir;
			}
		}
	}

	protected final Pattern leftWhitespaceRE = Pattern.compile("\\s+$");
	protected final Pattern leftTextRE = Pattern.compile("[a-zA-Z]+$");

	protected final Pattern rightWhitespaceRE = Pattern.compile("^\\s+");
	protected final Pattern rightTextRE = Pattern.compile("^[a-zA-Z]+");

	public void moveWord(int dist) {
		final int amt = Math.abs(dist);
		final int dir = dist / amt;
		for(int i = 0; i < amt; i++) {
			int lamt = 0;
			final String sline = buffer.getLine(line);
			if(dir == -1) {
				if(column == 0) {
					lamt = 1;
				} else {
					String stuff = sline.substring(0, column);
					final Matcher whitespaceMatch = leftWhitespaceRE.matcher(stuff);
					if(whitespaceMatch.find()) {
						stuff = stuff.substring(0, whitespaceMatch.end() - whitespaceMatch.start());
						lamt = whitespaceMatch.end() - whitespaceMatch.start();
					}
					final Matcher textMatch = leftTextRE.matcher(stuff);
					if(textMatch.find()) {
						stuff = stuff.substring(0, textMatch.end() - textMatch.start());
						lamt += textMatch.end() - textMatch.start();
					}
				}
			} else if(dir == 1) {
				if(column == sline.length()) {
					lamt = 1;
				} else {
					String stuff = sline.substring(column);
					final Matcher whitespaceMatch = rightWhitespaceRE.matcher(stuff);
					if(whitespaceMatch.find()) {
						stuff = stuff.substring(0, whitespaceMatch.end());
						lamt = whitespaceMatch.end();
					}
					final Matcher textMatch = rightTextRE.matcher(stuff);
					if(textMatch.find()) {
						stuff = stuff.substring(0, textMatch.end());
						lamt += textMatch.end();
					}
				}
			}
			move(lamt * dir);
		}
	}

	// Getters and Setters
	public Poke getPoke() {
		return poke;
	}

	public Buffer getBuffer() {
		return buffer;
	}

	public int getColumn() {
		return column;
	}

	public int getLine() {
		return line;
	}

	public void setLine(int line) {
		this.line = line;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	public Selection getSelection() {
		return selection;
	}

	public void setSelection(Selection selection) {
		this.selection = selection;
	}
}