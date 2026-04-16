// Path: src/test/java/config/Config.java
package config;

import java.io.FileInputStream;
import java.util.Properties;

public class Config {
    private static final Properties p = new Properties();
    static {
        try {
            p.load(new FileInputStream("src/test/resources/config.properties"));
        } catch (Exception e) {
            throw new RuntimeException("Cannot load config.properties", e);
        }
    }
    private static String orSys(String key) {
        return System.getProperty(key, p.getProperty(key));
    }

    public static String uiBaseUrl() { return orSys("ui.baseUrl"); }   // can be overridden by -Dui.baseUrl
    public static String apiBaseUrl() { return orSys("api.baseUrl"); } // can be overridden by -Dapi.baseUrl
}
