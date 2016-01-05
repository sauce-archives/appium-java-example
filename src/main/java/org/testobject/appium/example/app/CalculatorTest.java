package org.testobject.appium.example.app;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileBy;
import io.appium.java_client.TouchAction;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.remote.MobileCapabilityType;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.PageFactory;
import org.testobject.appium.common.TestObject;
import org.testobject.appium.common.TestObjectCapabilities;
import org.testobject.appium.junit.TestObjectAppiumSuite;
import org.testobject.appium.junit.TestObjectTestResultWatcher;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

@TestObject(testObjectApiKey = "YOUR_API_KEY_HERE", testObjectSuiteId = 1)
@RunWith(TestObjectAppiumSuite.class)
public class CalculatorTest {

	@Rule
	public TestObjectTestResultWatcher watcher = new TestObjectTestResultWatcher();

	private AppiumDriver driver;

	@Before
	public void setUp() throws MalformedURLException {
		DesiredCapabilities capabilities = new DesiredCapabilities();

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
	public void testPlusOperation() throws IOException {
		CalculatorActivityPageObject calculator = new CalculatorActivityPageObject(driver);
		calculator.reset();

		calculator.tap2();
		calculator.tapPlus();
		calculator.tap5();
		calculator.tapEquals();

		Assert.assertEquals("7", calculator.getResult());

		byte[] image = driver.getScreenshotAs(OutputType.BYTES);
		// Files.write(Paths.get("/home/aluedeke/Desktop/" + device + ".png"), image);
	}

	public static class CalculatorActivityPageObject {

		private final AppiumDriver driver;

		@AndroidFindBy(name = "2")
		private WebElement two;

		@AndroidFindBy(name = "5")
		private WebElement five;

		@AndroidFindBy(accessibility = "plus")
		private WebElement plus;

		@AndroidFindBy(accessibility = "equals")
		private WebElement equals;

		@AndroidFindBy(className = "android.widget.EditText")
		private WebElement result;

		public CalculatorActivityPageObject(AppiumDriver driver) {
			this.driver = driver;
			PageFactory.initElements(new AppiumFieldDecorator(driver), this);
		}

		public void tap2() {
			two.click();
		}

		public void tap5() {
			five.click();
		}

		public void tapPlus() {
			plus.click();
		}

		public void tapEquals() {
			equals.click();
		}

		public String getResult() {
			return result.getText();
		}

		public void reset() {
			List<WebElement> delete = driver.findElements(MobileBy.AccessibilityId("delete"));
			if (delete.isEmpty() == false) {
				TouchAction touchAction = new TouchAction(driver);
				touchAction.longPress(delete.get(0)).perform();
			}

			List<WebElement> clear = driver.findElements(MobileBy.AccessibilityId("clear"));
			if (clear.isEmpty() == false) {
				TouchAction touchAction = new TouchAction(driver);
				touchAction.longPress(clear.get(0)).perform();
			}
		}
	}

}
