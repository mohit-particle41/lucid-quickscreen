package com.automation.lucidhearing.android.pages;

import com.automation.lucidhearing.android.utils.WaitUtils;
import com.microsoft.appcenter.appium.EnhancedAndroidDriver;
import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import org.openqa.selenium.By;

public class HomePage extends AbstractPage {
     String quickScreenID = "button_start";
     String firstNameXpath = "/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.ScrollView/android.widget.LinearLayout/android.widget.LinearLayout[1]/android.widget.FrameLayout/android.widget.EditText";
     String lastNameXpath = "/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.ScrollView/android.widget.LinearLayout/android.widget.LinearLayout[2]/android.widget.FrameLayout/android.widget.EditText";
     String emailXpath = "/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.ScrollView/android.widget.LinearLayout/android.widget.LinearLayout[3]/android.widget.FrameLayout/android.widget.EditText";
     String zipCodeXpath = "/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.ScrollView/android.widget.LinearLayout/android.widget.LinearLayout[4]/android.widget.FrameLayout/android.widget.EditText";
     String phoneNumberXpath = "/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.ScrollView/android.widget.LinearLayout/android.widget.LinearLayout[5]/android.widget.FrameLayout/android.widget.EditText";
     String gettingStartedID = "button_submit";
     String errorMessageID = "textinput_error";
     By firstName;
     By lastName;
     By emailId;
     By zipCode;
     By phoneNumber;
     By gettingStartedButton;
     By quickScreenButton;
     By errorMessage;

     public HomePage(EnhancedAndroidDriver<MobileElement> androidDriver) {
          super(androidDriver);
          quickScreenButton = MobileBy.id(quickScreenID);
          firstName = MobileBy.xpath(firstNameXpath);
          lastName = MobileBy.xpath(lastNameXpath);
          emailId = MobileBy.xpath(emailXpath);
          zipCode = MobileBy.xpath(zipCodeXpath);
          phoneNumber = MobileBy.xpath(phoneNumberXpath);
          gettingStartedButton = MobileBy.id(gettingStartedID);
          errorMessage = MobileBy.id(errorMessageID);
     }

     public void clickOnStartQuickScreen() {
          MobileElement quickScreenButtonElement = driver.findElement(quickScreenButton);
          WaitUtils.waitForVisibilityOfElement(driver, 10000,  quickScreenButton, 3, false);
          quickScreenButtonElement.click();
          driver.label("Clicked on Start Quick Screen...");
     }

     public void enterDetails(String firstNameText, String lastNameText, String emailIDText ,String zipCodeText, String phoneNumberText){
          WaitUtils.waitForVisibilityOfElement(driver, 5000, firstName, 3, false);
          MobileElement firstNameElement = driver.findElement(firstName);
          MobileElement lastNameElement = driver.findElement(lastName);
          MobileElement emailIDElement = driver.findElement(emailId);
          MobileElement zipCodeElement = driver.findElement(zipCode);
          MobileElement phoneNumberElement = driver.findElement(phoneNumber);
          firstNameElement.sendKeys(firstNameText);
          driver.label("Enter First Name: " +firstNameText);
          lastNameElement.sendKeys(lastNameText);
          driver.label("Enter Last Name: " +lastNameText);
          emailIDElement.sendKeys(emailIDText);
          driver.label("Enter EmailID: " +emailIDText);
          zipCodeElement.sendKeys(zipCodeText);
          driver.label("Enter Zip code: " +phoneNumberText);
          phoneNumberElement.sendKeys(phoneNumberText);
          driver.label("Enter Phone number: " +phoneNumberText);
     }

     public void clickOnGettingStartedButton() {
          MobileElement gettingStartedElement = driver.findElement(gettingStartedButton);
          WaitUtils.waitForVisibilityOfElement(driver,
                  3000, gettingStartedButton,
                  3,
                  false);
          gettingStartedElement.click();
          driver.label("Click On getting started..");
     }

     public String getErrorMessage(){
          MobileElement errorMessageElement = driver.findElement(errorMessage);
          return errorMessageElement.getText().trim();
     }
}
