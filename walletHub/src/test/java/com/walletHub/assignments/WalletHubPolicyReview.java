package com.walletHub.assignments;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;


import junit.framework.Assert;

// class to review wallethub policy
public class WalletHubPolicyReview {
	
	String walletHubUserName = null;
	String walletHubPasswd = null;
	static WebDriver driver = null;
	static String review_url = "https://wallethub.com/profile/test_insurance_company/";
	static String review_verification_url = "https://wallethub.com/profile/username/reviews/";
	
	@Test
	public void postUserReviewOnWalletHub() {
		
		System.setProperty("webdriver.chrome.driver", "./src/test/resources/jars/chromedriver.exe");

		Map<String, Object> preference = new HashMap<String, Object>();
		preference.put("profile.default_content_setting_values.notifications", 2);
		ChromeOptions options = new ChromeOptions();
		options.setExperimentalOption("prefs", preference);
		//initiate webdriver instance 
		driver = new ChromeDriver(options);
		driver.manage().window().maximize();
		
		// click on login text to enter username and password
		WebElement loginClick = driver.findElement(By.xpath("//li/a[contains(text(),'Login')"));
		loginClick.click();
		
		WebDriverWait wait = new WebDriverWait(driver, 1000);
		
		WebElement userName = driver.findElement(By.xpath("//input[@type='text' and contains(@placeholder,'Email')]"));
		userName.sendKeys(walletHubUserName);
		
		WebElement password = driver.findElement(By.xpath("//input[@type='password' and contains(@placeholder,'Password')]"));
		password.sendKeys(walletHubPasswd);
		
		WebElement loginButton = driver.findElement(By.xpath("//button//span[contains(text(),'Login']"));
		loginButton.click();
		
		//post login navigate to review url
		driver.navigate().to(review_url);
		
		//handling free credit score pop up
		WebElement csrPopup = driver.findElement(By.xpath("//*[@id='footer_cta']/span/span/i[2]"));
		wait.until(ExpectedConditions.elementToBeClickable(csrPopup));
		csrPopup.click();
		
		//hovering on review stars and selecting 5th one
		WebElement revStars = driver.findElement(By.xpath("//*[contains(@class,'wh-rating rating_5')]"));
		Actions actionBuilder = new Actions(driver);
		actionBuilder.moveToElement(revStars);
		WebElement reviewFiveStar = driver.findElement(By.xpath("//*[contains(@class,'wh-rating-choices-holder')]/a[5]"));
		actionBuilder.moveToElement(reviewFiveStar).click().perform();
		
		//selecting health insurance from policy dropdown
		WebElement policySelector = driver.findElement(By.xpath("//*[@class='dropdown-list-new']"));
		wait.until(ExpectedConditions.elementToBeClickable(policySelector));
		policySelector.click();
		WebElement healthPolicySelector = driver.findElement(By.xpath("//a[contains(@data-target,'Health')]"));
		healthPolicySelector.click();
		
		//selecting 5th star
		WebElement healthPolicyStar = driver.findElement(By.xpath("//*[@class='bf-icon-star-empty star bstar bf-icon-star'][5]"));
		healthPolicyStar.click();
		
		//posting a review
		WebElement policyReview = driver.findElement(By.xpath("//textarea[@name='review' and @id='review-content']"));
		policyReview.clear();
		
		String reviewMessage = "";
		//for loop to enter review less than 200 chars
		for(int i=0;i<15;i++)
		{
			reviewMessage+= " Five Star ";
			
		}
		policyReview.sendKeys(reviewMessage);
		policyReview.submit();
		
		//wait for confirmation
		wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//h1/span[contains(text(),'Your')]/a[contains(text(),'has been posted')]"))));
		
		//navigating to profile to see if review is posted exists
		driver.navigate().to(review_verification_url);
		String postedReview = driver.findElement(By.tagName("body")).getText();
		Assert.assertTrue("success", postedReview.contains(reviewMessage));
		
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
