package com.automation.lucidhearing.android.utils;

import com.microsoft.appcenter.appium.EnhancedAndroidDriver;
import io.appium.java_client.MobileElement;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class WaitUtils {
     public static MobileElement waitForVisibilityOfElement(EnhancedAndroidDriver<MobileElement> driver, long timeOut, By locator, int retryCount,
                                                            boolean ignoreReport) {

          MobileElement element = null;
          for (int i = 1; i <= retryCount; i++) {
               try {
                    element = (MobileElement) new WebDriverWait(driver, timeOut).until(ExpectedConditions.visibilityOfElementLocated(locator));
                    break;
               } catch (StaleElementReferenceException e) {
                    if (retryCount == 1 || i != retryCount) {
                         try {
                              element = (MobileElement) new WebDriverWait(driver, timeOut).until(ExpectedConditions.visibilityOfElementLocated(locator));
                              break;
                         } catch (Exception ex) {
                              if (retryCount == 1) {
                                   break;
                              } else {
                                   i++;
                              }
                         }
                    }
               } catch (Exception e) {
                    if (i == retryCount) {
                         String action = "visible";
                         if (ignoreReport) {
                              //logInfoMessage(locator, action, timeOut, extentTest);
                         } else {
                              //logErrorMessage(locator, action, timeOut, extentTest);
                         }
                    }
               }
          }
          return element;
     }
}
