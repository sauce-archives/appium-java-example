package org.testobject.appium.example;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileBy;
import io.appium.java_client.TouchAction;
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

public class CalculatorTest {
	
	private static final String LOCALHOST = "http://127.0.0.1:4723/wd/hub";
	
	private AppiumDriver driver;
	
	@Before
	public void setup() throws MalformedURLException{
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "android");
        capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "android");
        capabilities.setCapability(MobileCapabilityType.APP_PACKAGE, "com.android.calculator2");
        capabilities.setCapability(MobileCapabilityType.APP_ACTIVITY, "Calculator");
        
        driver = new AppiumDriver(new URL(LOCALHOST), capabilities);
	}
	
	@After
	public void tearDown(){
		driver.quit();
	}
	
	@Test
	public void testPlusOperation(){
		List<WebElement> delete = driver.findElements(MobileBy.AccessibilityId("delete"));
		if(delete.isEmpty() == false){
			TouchAction touchAction = new TouchAction(driver);
			touchAction.longPress(delete.get(0)).perform();
		}
		
		List<WebElement> clear = driver.findElements(MobileBy.AccessibilityId("clear"));
		if(clear.isEmpty() == false){
			TouchAction touchAction = new TouchAction(driver);
			touchAction.longPress(clear.get(0)).perform();
		}
		
		driver.findElement(MobileBy.name("2")).click();
		
		driver.findElement(MobileBy.AccessibilityId("plus")).click();
		
		driver.findElement(MobileBy.name("5")).click();

		driver.findElement(MobileBy.AccessibilityId("equals")).click();
				
		String result = driver.findElement(MobileBy.className("android.widget.EditText")).getText();
		Assert.assertEquals("7", result);
	}

}
