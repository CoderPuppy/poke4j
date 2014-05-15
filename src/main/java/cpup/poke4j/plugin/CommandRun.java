package cpup.poke4j.plugin;

import cpup.poke4j.Buffer;
import cpup.poke4j.Poke;

import java.util.List;

public class CommandRun {
	protected final Poke poke;
	protected final Buffer buffer;
	protected final ICommand command;
	protected final List<Object> args;
	protected Status status;

	public CommandRun(Poke _poke, Buffer _buffer, ICommand _command, List<Object> _args) {
		poke = _poke;
		buffer = _buffer;
		command = _command;
		args = _args;
		status = Status.PreStart.get();
	}

	public void invoke() {
		if(status instanceof Status.PreStart) {
			status = Status.Starting.get();
			command.invoke(this);
			status = Status.Running.get();
		}
	}

	public static interface Status {
		public static class PreStart implements Status {
			protected PreStart() {}

			private static PreStart instance;

			public static PreStart get() {
				if(instance == null) {
					instance = new PreStart();
				}
				return instance;
			}

			@Override
			public String toString() {
				return "Status.PreStart";
			}
		}

		public static class Starting implements Status {
			protected Starting() {}

			private static Starting instance;

			public static Starting get() {
				if(instance == null) {
					instance = new Starting();
				}
				return instance;
			}

			@Override
			public String toString() {
				return "Status.Starting";
			}
		}

		public static class Running implements Status {
			protected Running() {}

			private static Running instance;

			public static Running get() {
				if(instance == null) {
					instance = new Running();
				}
				return instance;
			}

			@Override
			public String toString() {
				return "Status.Running";
			}
		}

		public static class Success implements Status {
			protected Success() {}

			private static Success instance;

			public Success get() {
				if(instance == null) {
					instance = new Success();
				}
				return instance;
			}

			@Override
			public String toString() {
				return "Status.Success";
			}
		}

		public static class Error implements Status {
			protected final Exception error;

			public Error(Exception _error) {
				error = _error;
			}

			@Override
			public String toString() {
				return "Status.Error[" + error.getLocalizedMessage() + "]";
			}

			// Getters and Setters
			public Exception getError() {
				return error;
			}
		}
	}

	// Getters and Setters
	public Poke getPoke() {
		return poke;
	}

	public Buffer getBuffer() {
		return buffer;
	}

	public ICommand getCommand() {
		return command;
	}

	public List<Object> getArgs() {
		return args;
	}

	public Status getStatus() {
		return status;
	}
}