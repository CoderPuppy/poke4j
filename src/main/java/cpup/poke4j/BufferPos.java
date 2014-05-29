package cpup.poke4j;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BufferPos {
	protected final int column;
	protected final int line;

	public BufferPos(int _column, int _line) {
		column = _column;
		line = _line;
	}

	@Override
	public String toString() {
		return column + ", " + line;
	}

	public boolean equals(BufferPos other) {
		return getColumn() == other.getColumn() && getLine() == other.getLine();
	}

	public boolean greater(BufferPos other) {
		if(getLine() == other.getLine()) {
			return getColumn() > other.getColumn();
		} else {
			return getLine() > other.getLine();
		}
	}

	public BufferPos find(Buffer buffer) {
		return buffer.find(this);
	}

	public BufferPos move(Buffer buffer, int dist) {
		int column = getColumn();
		int line = getLine();
		final int amt = Math.abs(dist);
		if(amt == 0)
			return this;
		final int dir = dist / amt;
		for(int i = 0; i < amt; i++) {
			if(dir == 1 && column == buffer.getLine(line).length()) {
				if(line < buffer.getLineCount() - 1) {
					line += 1;
					column = 0;
				} else {
					break;
				}
			} else if(dir == -1 && column == 0) {
				if(line > 0) {
					line -= 1;
					column = buffer.getLine(line).length();
				} else {
					break;
				}
			} else {
				column += dir;
			}
		}
		return new BufferPos(column, line);
	}

	protected final Pattern leftWhitespaceRE = Pattern.compile("\\s+$");
	protected final Pattern leftTextRE = Pattern.compile("(?:[a-zA-Z]+\\s*|[^a-zA-Z])$");

	protected final Pattern rightWhitespaceRE = Pattern.compile("^\\s+");
	protected final Pattern rightTextRE = Pattern.compile("^(?:\\s*[a-zA-Z]+|[^a-zA-Z])");

	public BufferPos moveWord(Buffer buffer, int dist) {
		BufferPos pos = this;
		final int amt = Math.abs(dist);
		final int dir = dist / amt;
		for(int i = 0; i < amt; i++) {
			int moveAmt = 0;
			final String sline = buffer.getLine(line);
			if(dir == -1) {
				if(column == 0) {
					moveAmt = 1;
				} else {
					String stuff = sline.substring(0, column);
//					final Matcher whitespaceMatch = leftWhitespaceRE.matcher(stuff);
//					if(whitespaceMatch.find()) {
//						stuff = stuff.substring(0, whitespaceMatch.end() - whitespaceMatch.start());
//						moveAmt = whitespaceMatch.end() - whitespaceMatch.start();
//					}
					final Matcher textMatch = leftTextRE.matcher(stuff);
					if(textMatch.find()) {
						stuff = stuff.substring(0, textMatch.end() - textMatch.start());
						moveAmt += textMatch.end() - textMatch.start();
					}
				}
			} else if(dir == 1) {
				if(column == sline.length()) {
					moveAmt = 1;
				} else {
					String stuff = sline.substring(column);
//					final Matcher whitespaceMatch = rightWhitespaceRE.matcher(stuff);
//					if(whitespaceMatch.find()) {
//						stuff = stuff.substring(0, whitespaceMatch.end());
//						moveAmt = whitespaceMatch.end();
//					}
					final Matcher textMatch = rightTextRE.matcher(stuff);
					if(textMatch.find()) {
						stuff = stuff.substring(0, textMatch.end());
						moveAmt += textMatch.end();
					}
				}
			}
			pos = move(buffer, moveAmt * dir);
		}
		return pos;
	}

	// Getters and Setters
	public int getColumn() {
		return column;
	}

	public int getLine() {
		return line;
	}
}