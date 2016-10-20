package org.pplm.framework.utils.config.test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.pplm.framework.utils.config.ConfigPlayer;
import org.pplm.framework.utils.config.ConfigPlayer.KeyFormatSupport;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;

@RunWith(DataProviderRunner.class)
public class ConfigPlayerTest {

	@Before
	public void before() {
		ConfigPlayer.getInstance().addKeyFormatSupport(KeyFormatSupport.LPL);
	}
	
	@DataProvider
	public static Object[][] dataProviderValue() {
		return new Object[][] { { "CONFIGPLAYER_TEST_NOTNULL", "configplayer.test.notnull", "10" }, { "CONFIGPLAYER_TEST_NULL", "configplayer.test.null", null } };
	}

	@Test
	@UseDataProvider("dataProviderValue")
	public void configTest(String key, String formatKey, String value) {
		if (value != null) {
			System.setProperty(key, value);
		}
		Assert.assertEquals(ConfigPlayer.getString(key), value);
		Assert.assertEquals(ConfigPlayer.getString(formatKey), value);
	}
	
	@Test
	@UseDataProvider("dataProviderValue")
	public void keySupportFormatTest(String key, String formatKey, String value) {
		Assert.assertEquals(ConfigPlayer.getInstance().getKeyFormatSupport(1).apply(formatKey), key);
	}
	
}
