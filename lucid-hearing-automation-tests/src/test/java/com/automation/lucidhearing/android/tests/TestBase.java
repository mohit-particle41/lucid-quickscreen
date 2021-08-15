package com.automation.lucidhearing.android.tests;

import com.automation.lucidhearing.android.config.Common;
import com.automation.lucidhearing.android.utils.LogUtils;
import com.microsoft.appcenter.appium.EnhancedAndroidDriver;
import com.microsoft.appcenter.appium.Factory;
import io.appium.java_client.MobileElement;
import io.appium.java_client.remote.MobileCapabilityType;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestWatcher;

import org.openqa.selenium.remote.DesiredCapabilities;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class TestBase {

    @Rule
    public TestWatcher watcher = Factory.createWatcher();
    protected static EnhancedAndroidDriver<MobileElement> driver;
    Logger logger = LogUtils.getLogger();

    //Following is required to run cases locally, however app center ignores this
    @Before
    public void startApp() throws MalformedURLException {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "android");
        capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "emulator-5558");
        capabilities.setCapability(MobileCapabilityType.APP, "/Users/siddharthdedgaonkar/Hearing-Labs-Tests/app-release-V10.apk");
        capabilities.setCapability("appPackage", "com.lucidhearing.lucidquickscreen");
        capabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, 7913);
        capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, "uiautomator2");
        URL url = new URL("http://localhost:4723/wd/hub");
        driver =  Factory.createAndroidDriver(url, capabilities);
        driver.manage().timeouts().implicitlyWait(Long.parseLong(Common.MOBILE_IMPLICIT_WAIT.toString()), TimeUnit.SECONDS);
    }

    /**
     * Quit the app after test case is completed
     */
    @After
    public void stopApplication() {
        driver.label("Test Complete!!");
        driver.quit();
    }
}
