package org.testobject.appium.example.webapp;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.remote.MobileCapabilityType;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testobject.appium.common.TestObject;
import org.testobject.appium.common.TestObjectCapabilities;
import org.testobject.appium.junit.TestObjectAppiumSuite;
import org.testobject.appium.junit.TestObjectTestResultWatcher;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.concurrent.TimeUnit;

@TestObject(testObjectApiKey = "YOUR_API_KEY_HERE", testObjectSuiteId = 1)
@RunWith(TestObjectAppiumSuite.class)
public class AppiumSevenval {

	private static String IMAGE_PATH = "/Users/hlenke/Documents/sevenval/";

	@Rule
	public TestObjectTestResultWatcher watcher = new TestObjectTestResultWatcher();

	private AppiumDriver driver;

	@Before
	public void setUp() throws MalformedURLException {
		DesiredCapabilities capabilities = new DesiredCapabilities();

		capabilities.setCapability(MobileCapabilityType.BROWSER_NAME, "Chrome");

		capabilities.setCapability(TestObjectCapabilities.TESTOBJECT_API_KEY, watcher.getApiKey());
		capabilities.setCapability(TestObjectCapabilities.TESTOBJECT_TEST_REPORT_ID, watcher.getTestReportId());

		driver = new AppiumDriver(TestObjectCapabilities.TESTOBJECT_APPIUM_ENDPOINT, capabilities);
		watcher.setAppiumDriver(driver);

		System.out.println("Test live view: " + driver.getCapabilities().getCapability("testobject_test_live_view_url"));
		System.out.println("Test report: " + driver.getCapabilities().getCapability("testobject_test_report_url"));
	}

	@After
	public void tearDown() {
		// The watcher will take care of quitting the driver.
		if (watcher == null && driver != null) {
			driver.quit();
		}
	}

	@Test
	public void test() throws IOException {
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		String url = "http://www.sevenval.com/";

		driver.get(url);
		HomePageObject home = new HomePageObject(driver);
		takeScreenshot("Home " + watcher.getTestReportId());

		Assert.assertTrue(
				"Wrong Title",
				home.getTitle()
						.equals("Sevenval » Hochwertige, responsive Webseiten mit RESS."));

		ProductPageObject products = home.gotoProdukte();
		takeScreenshot("Produkte " + watcher.getTestReportId());

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
