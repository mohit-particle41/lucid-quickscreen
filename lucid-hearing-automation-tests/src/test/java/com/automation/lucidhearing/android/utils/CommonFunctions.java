package com.automation.lucidhearing.android.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CommonFunctions {

     public static void downloadLatestAPKUploadTestsToAppCenter() throws InterruptedException, IOException {
          FileUtils.deleteFileIfExists("app-release.apk");
          ChromeOptions chromeOptions = new ChromeOptions();
          chromeOptions.addArguments("--disable-extensions");
          chromeOptions.addArguments("--disable-gpu");
          chromeOptions.addArguments("enable-automation");
          chromeOptions.addArguments("--headless");
          chromeOptions.addArguments("--window-size=1920,1080");
          chromeOptions.addArguments("--no-sandbox");
          chromeOptions.addArguments("--disable-extensions");
          chromeOptions.addArguments("--dns-prefetch-disable");
          chromeOptions.addArguments("--disable-gpu");
          chromeOptions.addArguments("disable-infobars");
          chromeOptions.addArguments("--start-maximized");
          chromeOptions.addArguments("--disable-extensions");
//        chromeOptions.addArguments("window-size=1200x600");
          chromeOptions.addArguments("--no-sandbox");
          chromeOptions.addArguments("--disable-features=VizDisplayCompositor");
          chromeOptions.setExperimentalOption("useAutomationExtension", false);
          chromeOptions.addArguments("--disable-dev-shm-usage");
          chromeOptions.addArguments("--disable-browser-side-navigation");
          chromeOptions.addArguments("--disable-gpu");
          chromeOptions.setPageLoadStrategy(PageLoadStrategy.NONE);
          chromeOptions.setHeadless(true);
          FileUtils.setFilePermissionToExecutable("drivers/chromedriver_mac");
          System.setProperty("webdriver.chrome.driver", "drivers/chromedriver_mac");
          WebDriver webdriver = new ChromeDriver(chromeOptions);
          webdriver.get("https://install.appcenter.ms/orgs/HearingLabs/apps/LucidQuickscreen");
          Thread.sleep(5000);
          webdriver.findElement(By.xpath("//*[text()='Sign in with email']")).click();
          webdriver.findElement(By.name(("email"))).sendKeys("siddharth@particle41.com");
          webdriver.findElement((By.name(("password")))).sendKeys("Jayashree68!");
          webdriver.findElement((By.xpath("//span[text()='Sign in']"))).click();
          Thread.sleep(10000);
          webdriver.findElement(By.xpath("//span[text()='Download']")).click();
          Thread.sleep(10000);
          Path path = Paths.get("app-release.apk");
          boolean fileExist = Files.exists(path);
          while (!fileExist) {
               Thread.sleep(5000);
               fileExist = Files.exists(path);
               System.out.println("Does the file exist? - " + fileExist);
          }
          webdriver.quit();
          prepareTestsForUpload("shell/test.sh");
          prepareTestsForUpload("shell/appium.sh");
     }

     public static void prepareTestsForUpload(String pathToShellScript) throws IOException, InterruptedException {
          FileUtils.setFilePermissionToExecutable(pathToShellScript);
          String[] cmd = new String[]{pathToShellScript};
          ProcessBuilder processBuilder = new ProcessBuilder(cmd);
          Process process = processBuilder.start();
          BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
          String line = null;
          while ((line = reader.readLine()) != null) {
               System.out.println(line);
          }
     }

     public static void main(String [] args) throws IOException, InterruptedException {
          downloadLatestAPKUploadTestsToAppCenter();
     }
}
