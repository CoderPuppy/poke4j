package cpup.poke4j;

import com.google.common.collect.ImmutableList;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;

public class Selection extends Buffer {
	protected final Buffer buffer;
	protected final BufferPos begin;
	protected final BufferPos end;

	public Selection(Poke _poke, Buffer _buffer, BufferPos _begin, BufferPos _end) {
		super(_poke);

		_begin = _begin.find(_buffer);
		_end = _end.find(_buffer);

		if(_begin.greater(_end)) {
			BufferPos tmp = _end;
			_end = _begin;
			_begin = tmp;
		}

		buffer = _buffer;
		begin = _begin;
		end = _end;
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
	public String removeImpl(BufferPos pos, int length) {
		return buffer.removeImpl(
			new BufferPos(
				(pos.getLine() == 0 ? begin.getColumn() : 0) + pos.getColumn(),
				begin.getLine() + pos.getLine()
			),
			length
		);
	}

	@Override
	public int insertImpl(BufferPos pos, String text) {
		return buffer.insertImpl(
			new BufferPos(
				(pos.getLine() == 0 ? begin.getColumn() : 0) + pos.getColumn(),
				begin.getLine() + pos.getLine()
			),
			text
		);
	}

	// Getters and Setters
	@Override
	public String getLine(int line) {
		if(line == 0) {
			if(begin.getLine() == end.getLine()) {
				return buffer.getLine(begin.getLine()).substring(begin.getColumn(), end.getColumn());
			} else {
				return buffer.getLine(begin.getLine()).substring(begin.getColumn());
			}
		} else if(line == end.getLine() - begin.getLine()) {
			return buffer.getLine(end.getLine()).substring(0, end.getColumn());
		} else {
			return buffer.getLine(begin.getLine() + line);
		}
	}

	@Override
	public int getLineCount() {
		return (end.getLine() - begin.getLine()) + 1;
	}

	@Override
	public List<String> getLines() {
		if(begin.getLine() != end.getLine()) {
			final List<String> lines = new ArrayList<String>();
			final List<String> bufLines = buffer.getLines();
			lines.add(bufLines.get(begin.getLine()).substring(begin.getColumn()));
			lines.addAll(bufLines.subList(begin.getLine() + 1, end.getLine()));
			lines.add(bufLines.get(end.getLine()).substring(0, end.getColumn()));
			return lines;
		} else {
			final List<String> lines = new ArrayList<String>();
			lines.add(buffer.getLine(begin.getLine()).substring(begin.getColumn(), end.getColumn()));
			return lines;
		}
	}

	public Buffer getBuffer() {
		return buffer;
	}

	public BufferPos getBegin() {
		return begin;
	}

	public BufferPos getEnd() {
		return end;
	}
}