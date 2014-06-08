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
		final int amt = Math.abs(dist); // pull off the sign so i only have to loop one way
		if(amt == 0) {
			return this;
		}
		final int dir = dist / amt; // the direction to move in (-1 for back, 1 for forwards)
		for(int i = 0; i < amt; i++) {
			// if this is at the end of the line (and we're going forwards)
			if(dir == 1 && column == buffer.getLine(line).length()) {
				// try to go down a line
				if(line < buffer.getLineCount() - 1) {
					line += 1;
					column = 0;
				} else {
					// otherwise we're done
					break;
				}
			// if this is at the start of the line (and we're going backwards)
			} else if(dir == -1 && column == 0) {
				// try to go up a line
				if(line > 0) {
					line -= 1;
					column = buffer.getLine(line).length();
				} else {
					// otherwise we're done
					break;
				}
			} else {
				column += dir;
			}
		}
		return new BufferPos(column, line);
	}

	protected final Pattern rightWordRE = Pattern.compile("(?:[a-zA-Z]+\\s*|[^a-zA-Z])$");
	protected final Pattern leftWordRE = Pattern.compile("^(?:\\s*[a-zA-Z]+|[^a-zA-Z])");

	public int getRightWordLength(Buffer buffer) {
		final Matcher textMatch = rightWordRE.matcher(buffer.getLine(line).substring(0, column));
		if(textMatch.find()) {
			return textMatch.end() - textMatch.start();
		} else {
			return 0;
		}
	}

	public int getLeftWordLength(Buffer buffer) {
		final Matcher textMatch = leftWordRE.matcher(buffer.getLine(line).substring(column));
		if(textMatch.find()) {
			return textMatch.end();
		} else {
			return 0;
		}
	}

	public BufferPos moveWord(Buffer buffer, int dist) {
		BufferPos pos = this;
		final int amt = Math.abs(dist); // how much needs to be moved (without a direction)
		if(amt == 0) {
			return this;
		}
		final int dir = dist / amt; // the direction to move in (-1 for back, 1 for forwards)
		for(int i = 0; i < amt; i++) {
			int moveAmt = 0;
			final String sline = buffer.getLine(line);
			if(dir == -1) {
				if(column == 0) {
					// if this is the first column then try to move up a line (one character)
					moveAmt = 1;
				} else {
					final Matcher textMatch = rightWordRE.matcher(sline.substring(0, column));
					if(textMatch.find()) {
						// move back however much the pattern matched (end - start should be positive)
						moveAmt += textMatch.end() - textMatch.start();
					}
				}
			} else if(dir == 1) {
				if(column == sline.length()) {
					// move down a line (one character)
					moveAmt = 1;
				} else {
					final Matcher textMatch = leftWordRE.matcher(sline.substring(column));
					if(textMatch.find()) {
						// move forward however much the pattern matched
						moveAmt += textMatch.end();
					}
				}
			}
			// do the move (multiple the amt by the direction (makes stuff simpler while calculating this))
			pos = move(buffer, moveAmt * dir);
		}
		return pos;
	}

	public BufferPos moveUD(Buffer buffer, int dist) {
		int column = getColumn();
		int line = getLine();
		line += dist;
		return new BufferPos(column, line).find(buffer);
	}

	// Getters and Setters
	public int getColumn() {
		return column;
	}

	public int getLine() {
		return line;
	}
}