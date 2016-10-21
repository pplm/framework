package org.pplm.framework.utils.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.function.Function;
import java.util.Map.Entry;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * reader config from env, properties, or any other things. get va
 * 
 * @author OracleGao
 *
 */
public class ConfigPlayer {

	private static Logger logger = LoggerFactory.getLogger(ConfigPlayer.class);

	public static final String DEFAULT_ENCODING = "UTF-8";

	private final static ConfigPlayer player = new ConfigPlayer();

	private Map<String, String> map = new HashMap<String, String>();

	private List<Function<String, String>> keyFormatSupports = new ArrayList<>();

	private ConfigPlayer() {
		super();
		keyFormatSupports.add(x -> x);
	}

	public static String getString(String key) {
		return getString(key, null);
	}

	public static String getString(String key, String defaultValue) {
		String value = player.getValue(key);
		if (value == null) {
			return defaultValue;
		}
		return value;
	}

	public static boolean getBoolean(String key) {
		return getBoolean(key, false);
	}

	public static boolean getBoolean(String key, boolean defaultValue) {
		try {
			String value = player.getValue(key);
			return value == null ? defaultValue : Boolean.parseBoolean(value);
		} catch (Exception e) {
			logger.warn(key + " get boolean value exception [" + e.getMessage() + "] use default value [" + defaultValue
					+ "] instead");
		}
		return defaultValue;
	}

	public static long getLong(String key, long defaultValue) {
		try {
			String value = player.getValue(key);
			return value == null ? defaultValue : Long.parseLong(value);
		} catch (Exception e) {
			logger.warn(key + " get long value exception [" + e.getMessage() + "] use default value [" + defaultValue
					+ "] instead");
		}
		return defaultValue;
	}

	public static long getLong(String key) {
		return getLong(key, 0L);
	}

	public static int getInt(String key, int defaultValue) {
		try {
			String value = player.getValue(key);
			return value == null ? defaultValue : Integer.parseInt(value);
		} catch (Exception e) {
			logger.warn(key + " get int value exception [" + e.getMessage() + "] use default value [" + defaultValue
					+ "] instead");
		}
		return defaultValue;
	}

	public static int getInt(String key) {
		return getInt(key, 0);
	}

	public static double getDouble(String key, double defaultValue) {
		try {
			String value = player.getValue(key);
			return value == null ? defaultValue : Double.parseDouble(value);
		} catch (Exception e) {
			logger.warn(key + " get double value exception [" + e.getMessage() + "] use default value [" + defaultValue
					+ "] instead");
		}
		return defaultValue;
	}

	public static double getDouble(String key) {
		return getDouble(key, 0.0D);
	}

	private String getValue(String key) {
		Optional<String> value = getValue(key, System::getenv);
		if (value.isPresent()) {
			return value.get();
		}
		value = getValue(key, System::getProperty);
		if (value.isPresent()) {
			return value.get();
		}
		value = getValue(key, map::get);
		if (value.isPresent()) {
			return value.get();
		}
		return null;
	}

	public Optional<String> getValue(String key, Function<String, String> function) {
		return keyFormatSupports.stream().map(x -> x.apply(key)).map(function).filter(x -> x != null).findFirst();
	}

	public void addKeyFormatSupport(KeyFormatSupport keyFormatSupport) {
		switch (keyFormatSupport) {
		case LPL:
			this.keyFormatSupports.add(this::lplSupport);
		}
	}

	public void addKeyFormatSupport(Function<String, String> keyFormatSupport) {
		this.keyFormatSupports.add(keyFormatSupport);
	}

	public Function<String, String> getKeyFormatSupport(int index) {
		return this.keyFormatSupports.get(index);
	}

	public Function<String, String> removeKeyFormatSupport(int index) {
		return this.keyFormatSupports.remove(index);
	}

	public int KeyFormatSupportsSize() {
		return this.keyFormatSupports.size();
	}

	public List<Function<String, String>> getKeyFormatSupports() {
		return new ArrayList<>(keyFormatSupports);
	}

	public boolean addP(Properties properties) {
		for (Entry<Object, Object> entry : properties.entrySet()) {
			map.put(entry.getKey().toString(), entry.getValue().toString());
		}
		return true;
	}

	public static ConfigPlayer getInstance() {
		return player;
	}

	public static boolean addProperties(Properties properties) {
		return player.addP(properties);
	}

	public static boolean addProperties(String fileSource) throws IOException {
		return addProperties(fileSource, DEFAULT_ENCODING);
	}

	public static boolean addProperties(String fileSource, String encoding) throws IOException {
		File file = new File(fileSource);
		if (file.exists()) {
			return addProperties(file, encoding);
		}
		InputStream inputStream = null;
		try {
			inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileSource);
			if (inputStream != null) {
				return addProperties(inputStream, encoding);
			}
		} finally {
			if (inputStream != null) {
				inputStream.close();
			}
		}
		return false;
	}

	public static boolean addProperties(File file, String encoding) throws IOException {
		return addProperties(new FileInputStream(file), encoding);
	}

	public static boolean addProperties(Reader reader) throws IOException {
		Properties properties = new Properties();
		properties.load(reader);
		return addProperties(properties);
	}

	public static boolean addProperties(InputStream inputStream, String encoding) throws IOException {
		return addProperties(new InputStreamReader(inputStream, encoding));
	}

	/**
	 * lower-point-lower key format support function "timed.queue.process.limit"
	 * key gets the same value as "TIMED_QUEUE_PROCESS_LIMIT" key
	 */
	private String lplSupport(String key) {
		return key.replaceAll("\\.", "_").toUpperCase();
	}

	public enum KeyFormatSupport {
		/**
		 * lower-point-lower key format support function
		 * "timed.queue.process.limit" key gets the same value as
		 * "TIMED_QUEUE_PROCESS_LIMIT" key
		 */
		LPL;
	}
}
