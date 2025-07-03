package jdbc;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AppProperties {

    private final Properties properties = new Properties();

    public AppProperties() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("app.properties")) {
            if (input == null) {
                throw new RuntimeException("Cannot find app.properties in resources folder");
            }
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load app.properties", e);
        }
    }

    public String getDriver() {
        return properties.getProperty("postgres.driver");
    }

    public String getUrl() {
        return properties.getProperty("postgres.url");
    }

    public String getUserName() {
        return properties.getProperty("postgres.user");     // Assuming typo in 'postgres.name' earlier
    }

    public String getPassword() {
        return properties.getProperty("postgres.password");
    }
}
