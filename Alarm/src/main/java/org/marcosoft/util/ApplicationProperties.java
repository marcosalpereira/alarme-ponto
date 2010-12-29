package org.marcosoft.util;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ApplicationProperties {
    private final Properties properties = new Properties();
    private File fileProperties;
    private Map<String, Object> defaults = new HashMap<String, Object>();

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
    
    public void setDefault(String property, String value) {
    	defaults.put(property, value);
    }

    public String getProperty(String key) {
    	String property = properties.getProperty(key);
    	if (property != null) return property;
    	return (String) defaults.get(key);
    }
    
    public String getProperty(String key, String notFoundValue) {
    	String property = getProperty(key);
    	if (property != null) return property;
    	return notFoundValue;
    }
    
    public int getIntProperty(String key) {
    	return Integer.parseInt(getProperty(key));
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