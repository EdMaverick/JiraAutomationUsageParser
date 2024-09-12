package org.example;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesLoader {

    private final Properties properties;

    public PropertiesLoader() {
        properties = new Properties();
        try {
            // Use ClassLoader to load the properties file from the JAR
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("credentials.properties");

            if (inputStream == null) {
                throw new FileNotFoundException("Property file 'credentials.properties' not found in the classpath");
            }

            // Load properties from the input stream
            properties.load(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getLogin() {
        return properties.getProperty("login");
    }

    public String getPassword() {
        return properties.getProperty("password");
    }

}
