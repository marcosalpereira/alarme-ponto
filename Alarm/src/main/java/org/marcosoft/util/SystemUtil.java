package org.marcosoft.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class SystemUtil {

	public static String getAppVersion() {
	        final String ret = "?";
	        final InputStream stream = SystemUtil.class.getClassLoader().getResourceAsStream(
	            "META-INF/MANIFEST.MF");
	        if (stream != null) {
	            final Properties prop = new Properties();
	            try {
	                prop.load(stream);
	                return prop.getProperty("version");
	            } catch (final IOException e) {
	            }
	        }
	        return ret;
	    }

    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (final InterruptedException e) {
            return;
        }
    }
}

