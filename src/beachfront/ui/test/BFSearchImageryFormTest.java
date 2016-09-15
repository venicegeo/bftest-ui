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
*    @author           RahulsIM
*    PROJECT:          Beachfront project
*    CLASS:            BFSearchImageryFormTest class to test the
*                      Happy path scenario of Imagery Search
*                      request form
*              ** REVISION HISTORY : **
*    Created:   8/31/2016
*    Updates:
*    		    9/1/16-Automated select create job, select geographic area to 
*    			 successfully display Source imagery search form	
*    			9/2/16-Automated data entry into Source imagery search form and 
*    			submit to get the response imagery results
*    			9/6/16 - Changes to have the cloud cover slider set to <50% and
*    			close the browser after submitting Source imagery search form. 
*    			09/08/2016 - Added Warning message if API Key is NULL. 
*
*/

import static org.junit.Assert.fail;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.HasInputDevices;
import org.openqa.selenium.interactions.Mouse;
import org.openqa.selenium.internal.Locatable;

public class BFSearchImageryFormTest {
	  private WebDriver driver;
	  private String baseUrl;
	  private boolean acceptNextAlert = true;
	  private StringBuffer verificationErrors = new StringBuffer();
	  private String userName;
	  private String passwd;
	  private String apiKey;
	  private BFUITestUtil bfUIUtil;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	    System.out.println("In BFSearchImageryFormTest.setUp");  
				
//	    driver = new ChromeDriver();

    	    Thread.sleep(3000);
    	    FirefoxProfile fp = new FirefoxProfile();
    	    fp.setPreference("browser.startup.homepage", "about:blank");
    	    fp.setPreference("startup.homepage_welcome_url", "about:blank");
    	    fp.setPreference("startup.homepage_welcome_url.additional", "about:blank");
		
    	    driver = new FirefoxDriver(fp);

	    bfUIUtil = new BFUITestUtil();
	    
	    userName = bfUIUtil.getUserName();
	    passwd = bfUIUtil.getPasswd();
	    baseUrl = bfUIUtil.getBaseUrl();
	    apiKey = bfUIUtil.getApiKey();
	    
	    System.out.println("userName: "+userName);
	    System.out.println("passwd: "+passwd);
	    System.out.println("baseUrl: "+baseUrl);
	    System.out.println("apiKey: "+apiKey);
	    if (userName == null || passwd == null || baseUrl == null) {
	    	throw new Exception("Beachfront UI URL and it's credentials failed to initialize from environment variables or properties file");
	    }

	    if (apiKey == null) {
	    	System.out.println("**** WARNING !!! The API Key to authenticate with Image source system is NULL");
	    }	    
	    
	    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}

	/**
	 *  To clean up the resources used and shut down the browser session
	 * @throws Exception
	 */
	@After
	public void tearDown() throws Exception {
		System.out.println("BFSearchImageryFormTest.Closing Browser Session");
	    driver.quit();
	    String verificationErrorString = verificationErrors.toString();
	    if (!"".equals(verificationErrorString)) {
	      fail(verificationErrorString);
	    }
	    
	}

	/**
	   *  testBFSearchImageryFormTest() to acomplish testing of below functions of BF UI:
	   *  a) After user successfully logs in to BF UI Appl
	   *  b) Selection of Create Job link
	   *  c) On the Canvas, specifying the geographic area
	   *  d) Entering the input data into the Search imagery request form.
	   *  e) Submit the form successfully 
	   *  
	   * @throws Exception
	   */
	@Test
	public void testBFSearchImageryFormTest() throws Exception {
		System.out.println("In BFSearchImageryFormTest.testBFSearchImageryFormTest() ");  

		  driver.get(baseUrl);
		    
		    driver.findElement(By.cssSelector("input")).clear();
		    driver.findElement(By.cssSelector("input")).sendKeys(userName);
		    driver.findElement(By.cssSelector("input[type=\"password\"]")).clear();
		    driver.findElement(By.cssSelector("input[type=\"password\"]")).sendKeys(passwd);
		    
		    Thread.sleep(1500);
		    driver.findElement(By.cssSelector("button[type=\"submit\"]")).click();
		    System.out.println("After Launching Beach Front and logging in");

		    Thread.sleep(5000);
			driver.findElement(By.className("Navigation-linkCreateJob")).click(); 
//			driver.findElement(By.className("Navigation__linkCreateJobs")).click(); 
		    System.out.println("After requesting create job form");
		    Thread.sleep(2000);
		    
		    WebElement canvas = driver.findElement(By.cssSelector(".PrimaryMap-root canvas"));     
//		    WebElement canvas = driver.findElement(By.cssSelector(".PrimaryMap__root canvas"));     
			Thread.sleep(200); //To avoid any race condition

		    canvas.getLocation().move(500, 500); //start
		    canvas.click();

		    canvas.getLocation().moveBy(250, 250); //click to select search area
		    canvas.click();
		    Thread.sleep(500); //To avoid any race condition

		    Locatable hoverItem = (Locatable) canvas;
		    Mouse mouse = ((HasInputDevices) driver).getMouse();
		    mouse.mouseMove(hoverItem.getCoordinates());
//		    Coordinates coord = mouse.mouseMove(hoverItem.getCoordinates());
		    Thread.sleep(200);
		    Actions builder = new Actions(driver);
		    builder.moveToElement(canvas,100,90).click().build().perform();
		    System.out.println("After getting coordinates");
		    Thread.sleep(200);
		    Action drawAction = builder.moveByOffset(100, 60) // second point
		            .click()
		            .build();
		    drawAction.perform();
		   
	       System.out.println("After selecting canvas");
	       
	       // populating API key on the Imagery search form
		   driver.findElement(By.cssSelector("input[type=\"password\"]")).clear();
		   driver.findElement(By.cssSelector("input[type=\"password\"]")).sendKeys(apiKey);
		   
		   // Changing From date field for Date of Capture imagery search criteria
		   driver.findElement(By.cssSelector("input[type=\"date\"]")).sendKeys("01/01/2015");
//		   driver.findElement(By.cssSelector("input[type=\"date\"]")).sendKeys("2015-01-01");

			Thread.sleep(2000); //To avoid any race condition
		    //Changing the cloud cover slider to <50%
		    driver.findElement(By.cssSelector("input[type=\"range\"]")).click();
			Thread.sleep(500); //To avoid any race condition
		    driver.findElement(By.cssSelector("input[type=\"range\"]")).sendKeys("15");

			// Defaulted to none so not needed
		    //new Select(driver.findElement(By.cssSelector("label.ImagerySearch-spatialFilter.forms-field-normal > select"))).selectByVisibleText("None");

		    // Submitting the search criteria
		    driver.findElement(By.cssSelector("button[type=\"submit\"]")).click();		   
		    System.out.println("After entering data and submitting Source Imagery search form");
			Thread.sleep(5000); //To avoid any race condition
	}

}
