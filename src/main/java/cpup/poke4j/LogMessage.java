package cpup.poke4j;

import com.google.common.base.Joiner;

// Store log messages as objects so they retain their data
// If you want a plain text message write your own write one for your specific use case (e.g. write one for shell output and don't use it for anything else)
public interface LogMessage {
	// Generic exception message
	public static class Error implements LogMessage {
		protected final Exception error;

		public Error(Exception _error) {
			error = _error;
		}

		@Override
		public String toString() {
			return "Exception: " + error.getLocalizedMessage() + "\n" + Joiner.on("\n").join(error.getStackTrace());
		}

		// Getters and Setters
		public Exception getError() {
			return error;
		}
	}
}