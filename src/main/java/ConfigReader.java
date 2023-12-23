import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {
    private static final String CONFIG_FILE = "config.properties";

    public static String getWebDriverPath() {
        return getProperty("webdriver.chrome.driver");
    }

    private static String getProperty(String key) {
        Properties properties = new Properties();
        try (InputStream input = ConfigReader.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (input == null) {
                System.out.println("죄송합니다. " + CONFIG_FILE + " 파일을 찾을 수 없습니다.");
                return null;
            }
            properties.load(input);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return properties.getProperty(key);
    }
}