package utils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

/*
 * Created by demidovskiy-r on 01.06.2015.
 */
public class IO {
    public static Properties getProperties(File propertiesFile) {
        Properties properties = new Properties();

        try {
            properties.load(new FileReader(propertiesFile));
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return properties;
    }
}