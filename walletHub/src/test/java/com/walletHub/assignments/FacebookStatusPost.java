package com.walletHub.assignments;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;


public class FacebookStatusPost {
	static String userName = "";
	static String password = "";
	static WebDriver driver = null;
	
	@Test
	public void facebookStatusPosting() {
	
			System.setProperty("webdriver.chrome.driver", "./src/test/resources/jars/chromedriver.exe");

			Map<String, Object> preference = new HashMap<String, Object>();
			preference.put("profile.default_content_setting_values.notifications", 2);
			ChromeOptions options = new ChromeOptions();
			options.setExperimentalOption("prefs", preference);
			
			driver = new ChromeDriver(options);
			driver.manage().window().maximize();
			driver.get("https://www.facebook.com/");

			// user login
				driver.findElement(By.id("email")).sendKeys(userName);
				driver.findElement(By.id("pass")).sendKeys(password);
				driver.findElement(By.name("login")).click();
			
			WebDriverWait wait = new WebDriverWait(driver, 1000);
			

			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[1]/div/div[1]/div/div[3]/div/div/div[1]/div[1]/div/div[2]/div/div/div[3]/div/div[2]/div/div/div/div[1]/div")));
			driver.findElement(By.xpath("/html/body/div[1]/div/div[1]/div/div[3]/div/div/div[1]/div[1]/div/div[2]/div/div/div[3]/div/div[2]/div/div/div/div[1]/div")).click();

			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[4]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[1]/div[1]/form[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]")));
			driver.findElement(By.xpath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[4]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[1]/div[1]/form[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]")).sendKeys("Hello World");

			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[4]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[1]/div[1]/form[1]/div[1]/div[1]/div[1]/div[1]/div[3]/div[2]/div[1]")));
			driver.findElement(By.xpath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[4]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[1]/div[1]/form[1]/div[1]/div[1]/div[1]/div[1]/div[3]/div[2]/div[1]")).click();
	}

	@AfterMethod
	public void takeScreenShot(ITestResult result) {
		
		if(ITestResult.FAILURE==result.getStatus()) {
			try {
				
				TakesScreenshot screenShot = (TakesScreenshot)driver;
				File sourceFile = screenShot.getScreenshotAs(OutputType.FILE);
				
				FileUtils.copyFile(sourceFile, new File("screenshots\\"+result.getName()+".png"));
				
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
	driver.quit();
	}

}
