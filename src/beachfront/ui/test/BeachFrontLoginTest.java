/*******************************************************************************
* Copyright 2016, RadiantBlue Technologies, Inc.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*******************************************************************************/

package beachfront.ui.test;

/**
 *
 *    @author 		RahulsIM
 *    PROJECT: 		Beachfront project 
 *    CLASS: 		BeachFrontLoginTest class to test the login 
 *    				functionality of Beachfront UI
 *              ** REVISION HISTORY : **
 *    Created: 		8/19/2016
 *    Updated: 
 *					8/26/2016: Enhancing BF login test automation to 
 *					read user credentials and app URL from environment variables.
 *    			    9/20/2016: Changes for testing and using Firefox 
 *
*/

import static org.junit.Assert.fail;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.testng.Assert;

public class BeachFrontLoginTest {
  private WebDriver driver;
  private String baseUrl;
  private boolean acceptNextAlert = true;
  private StringBuffer verificationErrors = new StringBuffer();
  private String userName;
  private String passwd;
  private BFUITestUtil bfUIUtil;
  
  /**
   *  setUp() to initialize the BeachFront UI login test.
   *  Uses BFUITestUtil to initialize Beachfront UI URL and it's 
   *  login credentials
   * @throws Exception
   */
  @Before
  public void setUp() throws Exception {
  
	System.out.println("In BeachFrontLoginTest.setUp");  
    //driver = new ChromeDriver();
	FirefoxProfile fp = new FirefoxProfile();
	fp.setPreference("browser.startup.homepage", "about:blank");
	fp.setPreference("startup.homepage_welcome_url", "about:blank");
	fp.setPreference("startup.homepage_welcome_url.additional", "https://beachfront.int.geointservices.io/login");
	
	driver = new FirefoxDriver(fp);
	
    bfUIUtil = new BFUITestUtil();
    
    userName = bfUIUtil.getUserName();
    passwd = bfUIUtil.getPasswd();
    baseUrl = bfUIUtil.getBaseUrl();
    
    System.out.println("userName: "+userName);
    System.out.println("passwd: "+passwd);
    System.out.println("baseUrl: "+baseUrl);
    if (userName == null || passwd == null || baseUrl == null) {
    	throw new Exception("Beachfront UI URL and it's credentials failed to initialize from environment variables or properties file");
    }
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
  }

  /**
   *  testBeachFrontLoginTest() to actually test the BeachFront UI
   *  login screen by using Chrome browser
   *  
   * @throws Exception
   */
  @Test
  public void testBeachFrontLoginTest() throws Exception {
	System.out.println("In BeachFrontLoginTest.testBeachFrontLoginTest");  

	driver.get(baseUrl);
    
    driver.findElement(By.cssSelector("input")).clear();
    driver.findElement(By.cssSelector("input")).sendKeys(userName);
    driver.findElement(By.cssSelector("input[type=\"password\"]")).clear();
    driver.findElement(By.cssSelector("input[type=\"password\"]")).sendKeys(passwd);
    
    Thread.sleep(1500);
    driver.findElement(By.cssSelector("button[type=\"submit\"]")).click();
    Thread.sleep(2000);
    Assert.assertEquals("Beachfront", driver.getTitle());

    System.out.println("After Launching Beach Front and logging in");

  }

  /**
   *  To clean up the resources used and shut down the browser session
   * @throws Exception
   */
  @After
  public void tearDown() throws Exception {
	System.out.println("Closing Browser Session");
    driver.quit();
    String verificationErrorString = verificationErrors.toString();
    if (!"".equals(verificationErrorString)) {
      fail(verificationErrorString);
    }

  }
  
  private boolean isElementPresent(By by) {
    try {
      driver.findElement(by);
      return true;
    } catch (NoSuchElementException e) {
      return false;
    }
  }

  private boolean isAlertPresent() {
    try {
      driver.switchTo().alert();
      return true;
    } catch (NoAlertPresentException e) {
      return false;
    }
  }

  private String closeAlertAndGetItsText() {
    try {
      Alert alert = driver.switchTo().alert();
      String alertText = alert.getText();
      if (acceptNextAlert) {
        alert.accept();
      } else {
        alert.dismiss();
      }
      return alertText;
    } finally {
      acceptNextAlert = true;
    }
  }
}
