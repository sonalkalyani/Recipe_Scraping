package utils;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public class ConfigReader {
	public static Properties initializeProperties()
	{
		Properties prop = new Properties();
		File file = new File(System.getProperty("user.dir") + "//src//test//resources//config//config.properties");
		
		try {
			
			FileInputStream fis = new FileInputStream(file);
			prop.load(fis);
		}
		catch(Throwable e)
		{
			e.printStackTrace();
		}
		return prop;
	}
}