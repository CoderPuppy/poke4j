package cpup.poke4j.plugin.js;

import java.util.Map;

public class JSObj extends JSVal {
	protected final Map<String, Object> jsData;

	public JSObj(Map<String, Object> _jsData) {
		jsData = _jsData;
	}

	@Override
	public Object get(String key) {
		return JSVal.wrap(jsData.get(key));
	}

	// Getters and Setters
	public Map<String, Object> getJsData() {
		return jsData;
	}
}