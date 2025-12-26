package testBase;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Platform;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;

public class BaseClass                   // for reusable methods
{
public static WebDriver driver;
public Logger logger;
public Properties p;
	
	@SuppressWarnings("deprecation")
	@BeforeClass(groups = {"Regression" ,"Master","Sanity","DataDriven"})
	@Parameters({"os","browser"})
	public void setup(String os, String br) throws IOException
	{
		//Loading config.properties file
		
		FileReader file = new FileReader("./src//test//resources//config.properties");
		logger = LogManager.getLogger(this.getClass());
		p = new Properties();
		p.load(file);
		
		if(p.getProperty("execution_env").equalsIgnoreCase("remote"))
		{
			DesiredCapabilities capabilities=new DesiredCapabilities();
			
			//os
			if(os.equalsIgnoreCase("windows"))
			{
				capabilities.setPlatform(Platform.WIN11);
			}
			else if(os.equalsIgnoreCase("linux"))
			{
				capabilities.setPlatform(Platform.LINUX);
				
			}
			else if (os.equalsIgnoreCase("mac"))
			{
				capabilities.setPlatform(Platform.MAC);
			}
			else
			{
				System.out.println("No matching os");
				return;
			}
			
			//browser
			switch(br.toLowerCase())
			{
			case "chrome": capabilities.setBrowserName("chrome"); break;
			case "edge": capabilities.setBrowserName("MicrosoftEdge"); break;
			case "firefox": capabilities.setBrowserName("firefox"); break;
			default: System.out.println("No matching browser"); return;
			}
			
			driver=new RemoteWebDriver(new URL("http://192.168.1.2:4444/wd/hub"),capabilities);
		}
		
		if(p.getProperty("execution_env").equalsIgnoreCase("local"))
		{	
		  switch(br.toLowerCase())
		  {
		     case "chrome" : driver =new ChromeDriver();
		     break;
		     case "edge" : driver =new EdgeDriver();
		     break;
		     case "firefox" : driver =new FirefoxDriver();
		     break;
		     default :System.out.println("Invalid Browser name..");
		     return; // It will exit from the whole execution
		
		  }
		}
		
		driver.manage().deleteAllCookies();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		// driver.get("https://tutorialsninja.com/demo/");
		driver.get(p.getProperty("appURL"));  // reading URL from properties file
		
		driver.manage().window().maximize();
		
	}
	
	@AfterClass(groups = {"Regression" ,"Master","Sanity","DataDriven"})
	public void tearDown()
	{
		driver.quit();
	}
	
	
	public String randomString() {
	    String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	    SecureRandom random = new SecureRandom();

	    StringBuilder sb = new StringBuilder(5);
	    for (int i = 0; i < 5; i++) {
	        sb.append(alphabet.charAt(random.nextInt(alphabet.length())));
	    }
	    return sb.toString();
	}
	
	public String randomNumber() {
	    String number = "1234567890";
	    SecureRandom random = new SecureRandom();

	    StringBuilder sb = new StringBuilder(10);
	    for (int i = 0; i < 10; i++) {
	        sb.append(number.charAt(random.nextInt(number.length())));
	    }
	    return sb.toString();
	}
	
	public String randomAlphaNumeric() {
	    String password = "1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	    SecureRandom random = new SecureRandom();

	    StringBuilder sb = new StringBuilder(10);
	    for (int i = 0; i < 10; i++) {
	        sb.append(password.charAt(random.nextInt(password.length())));
	    }
	    return sb.toString();
	}
	
	public String captureScreen(String tname) throws IOException {

		String timeStamp = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
				
		TakesScreenshot takesScreenshot = (TakesScreenshot) driver;
		File sourceFile = takesScreenshot.getScreenshotAs(OutputType.FILE);
		
		String targetFilePath=System.getProperty("user.dir")+"\\screenshots\\" + tname + "_" + timeStamp + ".png";
		File targetFile=new File(targetFilePath);
		
		sourceFile.renameTo(targetFile);
			
		return targetFilePath;

	}
}
