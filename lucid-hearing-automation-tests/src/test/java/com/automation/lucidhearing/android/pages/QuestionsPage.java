package com.automation.lucidhearing.android.pages;

import com.automation.lucidhearing.android.utils.WaitUtils;
import com.microsoft.appcenter.appium.EnhancedAndroidDriver;
import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import org.openqa.selenium.By;

public class QuestionsPage extends AbstractPage {
     String troubleHearingInRestaurantsIDTrue = "radio_button_rg1_true";
     String troubleHearingInResautantsXpath = "/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.RelativeLayout/androidx.recyclerview.widget.RecyclerView/android.widget.RelativeLayout[1]/android.widget.RadioGroup/android.widget.RadioButton[1]";
     String turnOnTVXpath = "/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.RelativeLayout/androidx.recyclerview.widget.RecyclerView/android.widget.RelativeLayout[2]/android.widget.RadioGroup/android.widget.RadioButton[2]";
     String askePeopleXpath = "/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.RelativeLayout/androidx.recyclerview.widget.RecyclerView/android.widget.RelativeLayout[3]/android.widget.RadioGroup/android.widget.RadioButton[1]";
     String doYouWantToListenXpath = "/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.RelativeLayout/androidx.recyclerview.widget.RecyclerView/android.widget.RelativeLayout[4]/android.widget.RadioGroup/android.widget.RadioButton[2]";
     String troubleHearingInRestaurantsIDFalse = "radio_button_rg1_false";
     String turnTVUpToHearDialogueTrue = "radio_button_rg2_true";
     String turnTVUpToHearDialogueFalse = "radio_button_rg2_false";
     String askPeopleToRepeatTrue = "radio_button_rg3_true";
     String askPeopleToRepeatFalse = "radio_button_rg3_false";
     String listenMusicForMoreThanAnHourTrue = "radio_button_rg4_true";
     String listenMusicForMoreThanAnHourFalse = "radio_button_rg4_false";
     String nextButtonID = "button_next";
     By troubleHearingInRestaurantsYES;
     By troubleHearingInRestaurantsNO;
     By turnTvUpToHearDialogueYES;
     By turnTvUpToHearDialogueNo;
     By askPeopleToRepeatYES;
     By askPeopleToRepeatNO;
     By listenToMusicYES;
     By listenToMusicNO;
     By nextButton;
     //Just TO test
     By troubleHearingInRestaurants;
     By turnTVUp;
     By askPeopleTORepeat;
     By listenToMusic;


     public QuestionsPage(EnhancedAndroidDriver<MobileElement> androidDriver) {
          super(androidDriver);
          //JustToTest
          troubleHearingInRestaurants = MobileBy.xpath(troubleHearingInResautantsXpath);
          turnTVUp = MobileBy.xpath(turnOnTVXpath);
          askPeopleTORepeat = MobileBy.xpath(askePeopleXpath);
          listenToMusic = MobileBy.xpath(doYouWantToListenXpath);
          //To TEST the above xpath
          troubleHearingInRestaurantsYES = MobileBy.id(troubleHearingInRestaurantsIDTrue);
          troubleHearingInRestaurantsNO = MobileBy.id(troubleHearingInRestaurantsIDFalse);
          turnTvUpToHearDialogueYES = MobileBy.id(turnTVUpToHearDialogueTrue);
          turnTvUpToHearDialogueNo = MobileBy.id(turnTVUpToHearDialogueFalse);
          askPeopleToRepeatYES = MobileBy.id(askPeopleToRepeatTrue);
          askPeopleToRepeatNO = MobileBy.id(askPeopleToRepeatFalse);
          listenToMusicYES = MobileBy.id(listenMusicForMoreThanAnHourTrue);
          listenToMusicNO = MobileBy.id(listenMusicForMoreThanAnHourFalse);
          nextButton = MobileBy.id(nextButtonID);
     }

     public void clickOnRandomOptions() {
          WaitUtils.waitForVisibilityOfElement(driver, 5000, troubleHearingInRestaurants, 3, false);
          MobileElement troubleHearingInRestaurantsYESElement = driver.findElement(troubleHearingInRestaurants);
          troubleHearingInRestaurantsYESElement.click();
          MobileElement turnTVUpElement = driver.findElement(turnTVUp);
          turnTVUpElement.click();
          MobileElement askPeopleTORepeatElement = driver.findElement(askPeopleTORepeat);
          askPeopleTORepeatElement.click();
          MobileElement listenToMusicElement = driver.findElement(listenToMusic);
          listenToMusicElement.click();
     }

     public void troubleInHearingInRestaurantsAndOtherEnvironments(boolean isTrue){
          if(isTrue){
               MobileElement troubleHearingInRestaurantsYESElement = driver.findElement(troubleHearingInRestaurantsYES);
               troubleHearingInRestaurantsYESElement.click();
               driver.label("Click Yes, for option Trouble hearing in restaurants...");
          } else {
               MobileElement troubleHearingInRestaurantsNoElement = driver.findElement(troubleHearingInRestaurantsNO);
               troubleHearingInRestaurantsNoElement.click();
               driver.label("Click No, for option Trouble hearing in restaurants...");
          }
     }

     public void turnTheTvUpToHearDialogue(boolean isTrue){
          if(isTrue){
               MobileElement turnUpTVToHearDialogueYESElement = driver.findElement(turnTvUpToHearDialogueYES);
               turnUpTVToHearDialogueYESElement.click();
               driver.label("Click Yes, for option Turn up TV to hear dialogue...");
          } else {
               MobileElement turnUpTVToHearDialogueNOElement = driver.findElement(turnTvUpToHearDialogueNo);
               turnUpTVToHearDialogueNOElement.click();
               driver.label("Click No, for option Turn up TV to hear dialogue...");
          }
     }

     public void frequentlyAskPeopleToRepeat(boolean isTrue){
          if(isTrue){
               MobileElement frequentlyAskPeopleToRepeatYESElement = driver.findElement(askPeopleToRepeatYES);
               frequentlyAskPeopleToRepeatYESElement.click();
               driver.label("Click Yes, for option frequently ask people to repeat...");
          } else {
               MobileElement frequentlyAskPeopleToRepeatNOElement = driver.findElement(askPeopleToRepeatNO);
               frequentlyAskPeopleToRepeatNOElement.click();
               driver.label("Click No, for option frequently ask people to repeat...");
          }
     }

     public void listenToMusicMoreThanHour(boolean isTrue){
          if(isTrue){
               MobileElement listenToMusicMoreThanHourYESElement = driver.findElement(listenToMusicYES);
               listenToMusicMoreThanHourYESElement.click();
               driver.label("Click Yes, for option listen to music more than an hour...");
          } else {
               MobileElement listenToMusicMoreThanHourNOElement = driver.findElement(listenToMusicNO);
               listenToMusicMoreThanHourNOElement.click();
               driver.label("Click No, for option listen to music more than an hour...");
          }
     }

     public void clickOnNext() {
          WaitUtils.waitForVisibilityOfElement(driver, 5000, nextButton, 3, false);
          MobileElement nextButtonElement = driver.findElement(nextButton);
          nextButtonElement.click();
          driver.label("Click Next button...");
     }
}
