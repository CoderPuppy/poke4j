package cpup.poke4j.plugin.js;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.org.mozilla.javascript.NativeArray;
import sun.org.mozilla.javascript.NativeObject;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public abstract class JSVal {
	public static final Logger logger = LoggerFactory.getLogger(JSVal.class);

	public abstract Object get(String key);
	public abstract void put(String key, Object val);

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
	public void put(int index, Object val) {
		put(Integer.toString(index), val);
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
	public void put(double key, Object val) {
		put(Double.toString(key), val);
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
	public void put(float key, Object val) {
		put(Float.toString(key), val);
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
	public void put(Object key, Object val) {
		put(key.toString(), val);
	}

	public <T> T get(Object key, Class<T> cla) {
		return get(key.toString(), cla);
	}

	public <T> T get(Object key, Class<T> cla, T defaul) {
		return get(key.toString(), cla, defaul);
	}

	@SuppressWarnings("unchecked")
	public static Object wrap(Object val) {
		if(val == null) {
			return null;
		} else if(val instanceof List) {
			return new JSArray((List<Object>) val);
		} else if(val instanceof Map) {
			return new JSObj((Map<String, Object>) val);
		} else {
			logger.debug("No wrapper for {}", val.getClass().getName());
			return val;
		}
	}
}