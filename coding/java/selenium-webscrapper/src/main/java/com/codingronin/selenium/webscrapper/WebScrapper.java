package com.codingronin.selenium.webscrapper;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;



public class WebScrapper {

  public static void main(String[] args) {

    String webDriverKey = args[0]; // Ex: webdriver.chrome.driver
    String webDriverValue = args[1]; // Ex:
                                     // src/main/resources/webdrivers/chromedriver_win32/chromedriver.exe

    String username = "user1";
    String password = "abc123";

    System.setProperty(webDriverKey, webDriverValue);

    String url = "http://localhost:9090/SimpleWebApp/login";
    WebDriver driver = new ChromeDriver();
    driver.get(url);

    driver.findElement(By.name("username")).sendKeys(username);
    WebElement pwdElem = driver.findElement(By.name("password"));
    pwdElem.sendKeys(password);
    pwdElem.submit();
  }

}
