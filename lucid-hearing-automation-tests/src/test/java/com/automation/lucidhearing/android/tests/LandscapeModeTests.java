package com.automation.lucidhearing.android.tests;

import com.automation.lucidhearing.android.config.Common;
import com.automation.lucidhearing.android.pages.HomePage;
import com.automation.lucidhearing.android.pages.QuestionsPage;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.ScreenOrientation;

public class LandscapeModeTests extends TestBase {

     @Test
     public void validateAHappyFlow() throws InterruptedException {
          driver.rotate(ScreenOrientation.LANDSCAPE);
          HomePage homePage = new HomePage(driver);
          System.out.println("On test case verifyClickOnQuickStart");
          homePage.clickOnStartQuickScreen();
          homePage.enterDetails("AutomationTestFirstName",
                  "AutomationTestLastName",
                  "automation@particle41.com",
                  "433324",
                  "9856362345");
          homePage.clickOnGettingStartedButton();
          QuestionsPage questionsPage = new QuestionsPage(driver);
          questionsPage.clickOnRandomOptions();
          questionsPage.clickOnNext();
          questionsPage.clickOnNext();
          questionsPage.clickOnNext();
     }

     @Test
     public void validateErrorMessageForFirstName() {
          driver.rotate(ScreenOrientation.LANDSCAPE);
          HomePage homePage = new HomePage(driver);
          System.out.println("On test case validateErrorMessageForFirstName");
          homePage.clickOnStartQuickScreen();
          homePage.enterDetails("",
                  "AutomationTestLastName",
                  "automation@particle41.com",
                  "433324",
                  "9856362345");
          homePage.clickOnGettingStartedButton();
          String errorMessageFirstName = homePage.getErrorMessage();
          Assert.assertEquals(errorMessageFirstName, Common.INVALID_FIRST_NAME.toString());
     }

     @Test
     public void validateErrorMessageForLastName() {
          driver.rotate(ScreenOrientation.LANDSCAPE);
          HomePage homePage = new HomePage(driver);
          System.out.println("On test case validateErrorMessageForLastName");
          homePage.clickOnStartQuickScreen();
          homePage.enterDetails("AutomationTestFirstName",
                  "",
                  "automation@particle41.com",
                  "433324",
                  "9856362345");
          homePage.clickOnGettingStartedButton();
          String errorMessageLastName = homePage.getErrorMessage();
          System.out.println("The error message is: " + errorMessageLastName);
          Assert.assertEquals(errorMessageLastName, Common.INVALID_LAST_NAME.toString());
     }

     @Test
     public void validateErrorMessageWhenEmailNotEntered() {
          driver.rotate(ScreenOrientation.LANDSCAPE);
          HomePage homePage = new HomePage(driver);
          homePage.clickOnStartQuickScreen();
          homePage.enterDetails("AutomationTestFirstName",
                  "AutomationTestLastName",
                  "",
                  "433324",
                  "9856362345");
          homePage.clickOnGettingStartedButton();
          String errorMessageEmailID = homePage.getErrorMessage();
          Assert.assertEquals(errorMessageEmailID, Common.INVALID_EMAIL_ID.toString());
     }

     @Test
     public void validateErrorMessageForZipCode() {
          driver.rotate(ScreenOrientation.LANDSCAPE);
          HomePage homePage = new HomePage(driver);
          System.out.println("On test case validateErrorMessageForLastName");
          homePage.clickOnStartQuickScreen();
          homePage.enterDetails("AutomationTestFirstName",
                  "AutomationTestLastName",
                  "automation@particle41.com",
                  "",
                  "9856362345");
          homePage.clickOnGettingStartedButton();
          String errorMessageZipCode = homePage.getErrorMessage();
          System.out.println("The error message is: " + errorMessageZipCode);
          Assert.assertEquals(errorMessageZipCode, Common.INVALID_ZIP_CODE.toString());
     }

     @Test
     public void validateErrorMessageForPhoneNo() {
          driver.rotate(ScreenOrientation.LANDSCAPE);
          HomePage homePage = new HomePage(driver);
          System.out.println("On test case validateErrorMessageForLastName");
          homePage.clickOnStartQuickScreen();
          homePage.enterDetails("AutomationTestFirstName",
                  "AutomationTestLastName",
                  "automation@particle41.com",
                  "433324",
                  "");
          homePage.clickOnGettingStartedButton();
          String errorMessagePhNo = homePage.getErrorMessage();
          System.out.println("The error message is: " + errorMessagePhNo);
          Assert.assertEquals(errorMessagePhNo, Common.INVALID_PHONE_NO.toString());
     }

     @Test
     public void validateAllErrorMessages() {
          driver.rotate(ScreenOrientation.LANDSCAPE);
          HomePage homePage = new HomePage(driver);
          System.out.println("On test case validateAllErrorMessages");
          homePage.clickOnStartQuickScreen();
          homePage.clickOnGettingStartedButton();
          String errorMessages = homePage.getErrorMessage();
          Assert.assertEquals(errorMessages, Common.INVALID_FIRST_NAME.toString());
     }
}
