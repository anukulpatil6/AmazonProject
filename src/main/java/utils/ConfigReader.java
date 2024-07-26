package utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {
    private Properties properties;

    public ConfigReader(){
        properties = new Properties();
        {
            try {
                FileInputStream fis = new FileInputStream("C:\\Users\\LENOVO\\IdeaProjects\\untitled\\src\\test\\resources\\config.properties");
                properties.load(fis);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
        public String getProperty(String key){
            return properties.getProperty(key);


    }
}
