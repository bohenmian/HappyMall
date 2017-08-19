package cn.edu.swpu.cins.config;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

public class PropertiesConfig {

    private static Logger logger = LoggerFactory.getLogger(PropertiesConfig.class);

    private static Properties properties;

    static {
        String fileName = "happymall.properties";
        properties = new Properties();
        try {
            properties.load(new InputStreamReader(PropertiesConfig.class.getClassLoader().getResourceAsStream(fileName), "utf-8"));
        } catch (IOException e) {
            logger.error("config exception", e);
        }
    }

    public static String getProperties(String key) {
        String value = properties.getProperty(key.trim());
        if (StringUtils.isBlank(value)) {
            return null;
        }
        return value.trim();
    }

    public static String getProperties(String key, String defaultValue) {
        String value = properties.getProperty(key.trim());
        if (StringUtils.isBlank(value)) {
            value = defaultValue;
        }
        return value.trim();
    }
}
