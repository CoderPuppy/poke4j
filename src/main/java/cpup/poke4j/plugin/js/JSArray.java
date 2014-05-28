package cpup.poke4j.plugin.js;

import java.util.Arrays;
import java.util.List;

public class JSArray extends JSVal {
	protected final List<Object> jsData;

	public JSArray(List<Object> _jsData) {
		jsData = _jsData;
	}

	@Override
	public void put(String key, Object val) {
		jsData.set(Integer.parseInt(key), val);
	}

	@Override
	public Object get(String key) {
		return JSVal.wrap(jsData.get(Integer.parseInt(key)));
	}

	public static JSArray of(Object... vals) {
		return new JSArray(Arrays.asList(vals));
	}

	// Getters and Setters
	public List<Object> getJsData() {
		return jsData;
	}
}