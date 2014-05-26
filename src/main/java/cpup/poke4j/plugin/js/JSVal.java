package cpup.poke4j.plugin.js;

import sun.org.mozilla.javascript.NativeArray;
import sun.org.mozilla.javascript.NativeObject;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public abstract class JSVal {
	public abstract Object get(String key);

	public <T> T get(String key, Class<T> cla) {
		return cla.cast(get(key));
	}

	public <T> T get(String key, Class<T> cla, T defaul) {
		final Object res = get(key);
		if(cla.isInstance(res)) {
			return cla.cast(res);
		} else {
			return defaul;
		}
	}

	public Object get(int index) {
		return get(Integer.toString(index));
	}

	public <T> T get(int index, Class<T> cla) {
		return get(Integer.toString(index), cla);
	}

	public <T> T get(int index, Class<T> cla, T defaul) {
		return get(Integer.toString(index), cla, defaul);
	}

	public Object get(double key) {
		return get(Double.toString(key));
	}

	public <T> T get(double key, Class<T> cla) {
		return get(Double.toString(key), cla);
	}

	public <T> T get(double key, Class<T> cla, T defaul) {
		return get(Double.toString(key), cla, defaul);
	}

	public Object get(float key) {
		return get(Float.toString(key));
	}

	public <T> T get(float key, Class<T> cla) {
		return get(Float.toString(key), cla);
	}

	public <T> T get(float key, Class<T> cla, T defaul) {
		return get(Float.toString(key), cla, defaul);
	}

	public Object get(Object key) {
		return get(key.toString());
	}

	public <T> T get(Object key, Class<T> cla) {
		return get(key.toString(), cla);
	}

	public <T> T get(Object key, Class<T> cla, T defaul) {
		return get(key.toString(), cla, defaul);
	}

	@SuppressWarnings("unchecked")
	public static Object wrap(Object val) {
		if(val instanceof List) {
			return new JSArray((List) val);
		} else if(val instanceof Map) {
			return new JSObj((Map<String, Object>) val);
		} else {
			System.err.printf("No wrapper for %s%n", val.getClass().getName());
			return val;
		}
	}
}