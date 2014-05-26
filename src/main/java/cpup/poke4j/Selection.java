package cpup.poke4j;

import com.google.common.collect.ImmutableList;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;

public class Selection extends Buffer {
	protected final Buffer buffer;
	protected final int beginColumn;
	protected final int beginLine;
	protected final int endColumn;
	protected final int endLine;

	public Selection(Poke _poke, Buffer _buffer, int _beginColumn, int _beginLine, int _endColumn, int _endLine) {
		super(_poke);

		if(_beginLine > _endLine) {
			int tmp = _endLine;
			_endLine = _beginLine;
			_beginLine = _endLine;
		} else if(_beginLine == _endLine && _beginColumn > _endColumn) {
			int tmp = _endColumn;
			_endColumn = _beginColumn;
			_beginColumn = _endColumn;
		}

		buffer = _buffer;
		beginColumn = _beginColumn;
		beginLine = _beginLine;
		endColumn = _endColumn;
		endLine = _endLine;

		if(beginLine < 0 || beginLine >= buffer.getLineCount()) {
			throw new IndexOutOfBoundsException("beginLine must be a valid line");
		}

		if(endLine < 0 || endLine >= buffer.getLineCount()) {
			throw new IndexOutOfBoundsException("endLine must be a valid line");
		}

		final String lineA = buffer.getLine(beginLine);
		final String lineB = buffer.getLine(endLine);

		if(beginColumn < 0 || beginColumn >= lineA.length()) {
			throw new IndexOutOfBoundsException("beginColumn must be a valid column");
		}

		if(endColumn < 0 || endColumn >= lineB.length()) {
			throw new IndexOutOfBoundsException("endColumn must be a valid column");
		}
	}

	@Override
	public String getLine(int line) {
		if(line == 0) {
			if(beginLine == endLine) {
				return buffer.getLine(beginLine).substring(beginColumn, endColumn);
			} else {
				return buffer.getLine(beginLine).substring(beginColumn);
			}
		} else if(line == endLine - beginLine) {
			return buffer.getLine(endLine).substring(0, endColumn);
		} else {
			return buffer.getLine(beginLine + line);
		}
	}

	@Override
	public int getLineCount() {
		return (endLine - beginLine) + 1;
	}

	@Override
	public List<String> getLines() {
		if(beginLine != endLine) {
			final List<String> lines = new ArrayList<String>();
			final List<String> bufLines = buffer.getLines();
			lines.add(bufLines.get(beginLine).substring(beginColumn));
			lines.addAll(bufLines.subList(beginLine + 1, endLine));
			lines.add(bufLines.get(endLine).substring(0, endColumn));
			return lines;
		} else {
			final List<String> lines = new ArrayList<String>();
			lines.add(buffer.getLine(beginLine).substring(beginColumn, endColumn));
			return lines;
		}
	}

	@Override
	public void save(BufferedWriter writer) throws Exception {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public void load(BufferedReader reader) throws Exception {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public String removeImpl(int column, int line, int length) {
		return buffer.removeImpl(
			beginColumn + column,
			beginLine + line,
			length
		);
	}

	@Override
	public int insertImpl(int column, int line, String text) {
		return buffer.insertImpl(
			beginColumn + column,
			beginLine + line,
			text
		);
	}

	// Getters and Setters
	public Buffer getBuffer() {
		return buffer;
	}

	public int getBeginColumn() {
		return beginColumn;
	}

	public int getBeginLine() {
		return beginLine;
	}

	public int getEndColumn() {
		return endColumn;
	}

	public int getEndLine() {
		return endLine;
	}
}