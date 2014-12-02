package org.testobject.appium.example.webapp;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.remote.MobileCapabilityType;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AppiumSevenval {

	private static String IMAGE_PATH = "/Users/hlenke/Documents/sevenval/";
	private static String DEVICE = "LG_Nexus_5_real";
	
	public static String TESTOBJECT_SUITE_NAME = "testobject_suite_name";
    public static String TESTOBJECT_TEST_NAME = "testobject_test_name";
    public static String TESTOBJECT_DEVICE = "testobject_device";
    public static String TESTOBJECT_API_KEY = "testobject_api_key";
    
	private AppiumDriver driver;

	@Before
	public void setUpClass() throws MalformedURLException {
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability(MobileCapabilityType.BROWSER_NAME, "Chrome");
		capabilities.setCapability(TESTOBJECT_API_KEY,
				"984FC9C513424B24B68CE3B3C15DB5E5");
		capabilities.setCapability(TESTOBJECT_DEVICE, DEVICE);
		capabilities.setCapability(TESTOBJECT_SUITE_NAME, "SevenvalSuite");
		capabilities.setCapability(TESTOBJECT_TEST_NAME, "ProductTest");
		
		driver = new AppiumDriver(new URL(
				"https://app.testobject.com:443/api/appium/wd/hub"),
				capabilities);
	}

	@After
	public void tearDownClass() {
		if (driver != null) {
			driver.quit();
		}
	}

	@Test
	public void test() throws IOException {
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			
		String url = "http://www.sevenval.com/";
		
		driver.get(url);		
		HomePageObject home = new HomePageObject(driver);
		takeScreenshot("Home " + DEVICE);
		
		Assert.assertTrue(
				"Wrong Title",
				home.getTitle()
						.equals("Sevenval » Hochwertige, responsive Webseiten mit RESS."));

		ProductPageObject products = home.gotoProdukte();
		takeScreenshot("Produkte " + DEVICE);

		Assert.assertTrue(
				"Wrong Title",
				products.getTitle()
						.equals("Sevenval » Technologie zur Optimierung Ihrer Web Performance."));

	}

	public static class HomePageObject {
		private final AppiumDriver driver;

		@FindBy(linkText = "PRODUKTE")
		private WebElement productLink;

		@FindBy(id = "menu-btn-bright")
		private WebElement menuButton;

		public HomePageObject(AppiumDriver driver) {
			this.driver = driver;

			//wait until page loaded
			WebDriverWait wait = new WebDriverWait(driver, 60);
			wait.until(ExpectedConditions.elementToBeClickable(By.id("menu-btn-bright")));
			
			PageFactory.initElements(new AppiumFieldDecorator(driver), this);
		}

		public ProductPageObject gotoProdukte() {
			// FIXME depends on mobile device - resolution
			menuButton.click();
			productLink.click();
			return new ProductPageObject(driver);
		}

		public String getTitle() {
			return driver.getTitle();
		}
	}
	
	public static class ProductPageObject {
		private final AppiumDriver driver;

		@FindBy(linkText = "PRODUKTE")
		private WebElement productLink;

		@FindBy(id = "menu-btn-bright")
		private WebElement menuButton;

		public ProductPageObject(AppiumDriver driver) {
			this.driver = driver;
			
			//wait until page loaded
			WebDriverWait wait = new WebDriverWait(driver, 60);
			wait.until(ExpectedConditions.elementToBeClickable(By.id("menu-btn-bright")));
			
			PageFactory.initElements(new AppiumFieldDecorator(driver), this);
		}

		public void clickProdukte() {
			// FIXME depends on mobile device - resolution
			menuButton.click();
			productLink.click();
		}

		public String getTitle() {
			return driver.getTitle();
		}
	}

	private void takeScreenshot(String name) {
		try {
			File screenshot = driver.getScreenshotAs(OutputType.FILE);
			String filename = IMAGE_PATH + name + ".png";
			FileUtils.copyFile(screenshot, new File(filename));
		} catch (Exception e) {
			System.out.println("Exception while saving the file " + e);
		}
	}
}
