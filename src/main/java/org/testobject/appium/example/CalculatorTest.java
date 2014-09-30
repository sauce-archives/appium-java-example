package org.testobject.appium.example;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileBy;
import io.appium.java_client.TouchAction;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.remote.MobileCapabilityType;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.PageFactory;

public class CalculatorTest {

	private static final String LOCALHOST = "http://127.0.0.1:4723/wd/hub";

	private AppiumDriver driver;

	@Before
	public void setup() throws MalformedURLException {
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "android");
		capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "android");
		capabilities.setCapability(MobileCapabilityType.APP_PACKAGE, "com.android.calculator2");
		capabilities.setCapability(MobileCapabilityType.APP_ACTIVITY, "Calculator");

		driver = new AppiumDriver(new URL(LOCALHOST), capabilities);
	}

	@After
	public void tearDown() {
		driver.quit();
	}

	@Test
	public void testPlusOperation() {
		CalculatorActivityPageObject calculator = new CalculatorActivityPageObject(driver);
		calculator.reset();

		calculator.tap2();
		calculator.tapPlus();
		calculator.tap5();
		calculator.tapEquals();

		Assert.assertEquals("7", calculator.getResult());
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
