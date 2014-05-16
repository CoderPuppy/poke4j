package cpup.poke4j.events;

import java.util.HashSet;
import java.util.Set;

public class EventRegister<ET> {
	protected Set<EventHandler<ET>> handlers = new HashSet<EventHandler<ET>>();

	public EventRegister() {}

	public EventRegister<ET> listen(EventHandler<ET> handler) {
		handlers.add(handler);
		return this;
	}

	public EventRegister<ET> emit(ET event) {
		for(EventHandler<ET> handler : handlers) {
			handler.handle(event);
		}
		return this;
	}
}