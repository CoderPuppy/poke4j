package cpup.poke4j.plugin;

import cpup.poke4j.Buffer;
import cpup.poke4j.LogMessage;
import cpup.poke4j.Poke;
import cpup.poke4j.events.EventRegister;
import cpup.poke4j.plugin.js.JSArray;

import java.util.ArrayList;
import java.util.List;

public class CommandRun {
	protected final Poke poke;
	protected final Buffer buffer;
	protected final ICommand command;
	protected final JSArray args;
	protected final List<LogMessage> log = new ArrayList<LogMessage>(50);
	protected Status status;

	public CommandRun(Poke _poke, Buffer _buffer, ICommand _command, JSArray _args) {
		poke = _poke;
		buffer = _buffer;
		command = _command;
		args = _args;
		status = Status.PreStart.get();
	}

	public CommandRun(Poke _poke, ICommand _command, JSArray _args) {
		this(_poke, _poke.getCurrentBuffer(), _command, _args);
	}

	public CommandRun invoke() {
		if(status instanceof Status.PreStart) {
			status = Status.Starting.get();
			try {
				command.invoke(this);
				status = Status.Running.get();
			} catch(Exception e) {
				error(e);
			}
		}
		return this;
	}

	public CommandRun done() {
		if(status instanceof Status.Starting || status instanceof Status.Running) {
			status = Status.Success.get();
			SuccessEvent event = new SuccessEvent(this);
			doneEv.emit(event);
			successEv.emit(event);
		}
		return this;
	}

	public CommandRun error(Exception e) {
		if(status instanceof Status.Starting || status instanceof Status.Running) {
			log.add(new LogMessage.Error(e));
			status = new Status.Error(e);
			ErrorEvent event = new ErrorEvent(this, e);
			doneEv.emit(event);
			errorEv.emit(event);
		}
		return this;
	}

	public EventRegister<DoneEvent> doneEv = new EventRegister<DoneEvent>();

	public static abstract class DoneEvent {
		protected final CommandRun run;

		public DoneEvent(CommandRun _run) {
			run = _run;
		}

		// Getters and Setters
		public CommandRun getRun() {
			return run;
		}
	}

	public EventRegister<SuccessEvent> successEv = new EventRegister<SuccessEvent>();
	public static class SuccessEvent extends DoneEvent {
		public SuccessEvent(CommandRun _run) {
			super(_run);
		}
	}

	public EventRegister<ErrorEvent> errorEv = new EventRegister<ErrorEvent>();
	public static class ErrorEvent extends DoneEvent {
		protected final Exception error;

		public ErrorEvent(CommandRun _run, Exception _error) {
			super(_run);
			error = _error;
		}

		// Getters and Setters
		public Exception getError() {
			return error;
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

			public static Success get() {
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

	public JSArray getArgs() {
		return args;
	}
	public JSArray args() {
		return args;
	}

	public Status getStatus() {
		return status;
	}

	public List<LogMessage> getLog() {
		return log;
	}
}