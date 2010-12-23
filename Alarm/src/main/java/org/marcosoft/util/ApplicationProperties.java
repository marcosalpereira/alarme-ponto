package org.marcosoft.util;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class ApplicationProperties {
    private final Properties properties = new Properties();
    private File fileProperties;

    public ApplicationProperties(String applicationName, String propertyFileName) {
        String appHome = System.getProperty("user.home") + File.separator + "." + applicationName;
        File appHomeFile = new File(appHome);
        if (!appHomeFile.exists()) {
            appHomeFile.mkdirs();
        }

        String fileName =  appHome + File.separator + propertyFileName + ".properties";
        fileProperties = new File(fileName);

        try {
            if (fileProperties.exists()) {
                properties.load(new FileInputStream(fileProperties));
            } else {
                properties.store(new FileOutputStream(fileProperties), null);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }
    
    public String getProperty(String key, String defaultValue) {
    	String property = properties.getProperty(key);
    	if (property != null) return property;
    	return defaultValue;
    }
    
    public int getProperty(String key, int defaultValue) {
    	String property = properties.getProperty(key);
    	if (property != null) return Integer.parseInt(property);
    	return defaultValue;
    }

    public void setProperty(String key, String value) {
        properties.setProperty(key, value);
        try {
            properties.store(new FileOutputStream(fileProperties), null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}