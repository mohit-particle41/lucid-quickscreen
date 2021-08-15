package com.automation.lucidhearing.android.pages;

import com.microsoft.appcenter.appium.EnhancedAndroidDriver;
import io.appium.java_client.MobileElement;

public class AbstractPage {
     static EnhancedAndroidDriver<MobileElement> driver;
     AbstractPage(EnhancedAndroidDriver<MobileElement> androidDriver){
          driver = androidDriver;
     }
}
