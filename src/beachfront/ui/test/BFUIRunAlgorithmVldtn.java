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

import static org.junit.Assert.fail;

import java.util.concurrent.TimeUnit;

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
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

/**
*
*    @author           RahulsIM
*    PROJECT:          Beachfront project
*    CLASS:            BFUIRunAlgorithmVldtn class to test the
*                      Happy path scenario of validating Select
*                      Algorithm form on LHS panel after user 
*                      selects a response image from map canvas.
*              ** REVISION HISTORY : **
*    Created:   9/14/2016
*    Updates:
*
*/
public class BFUIRunAlgorithmVldtn {
	  private WebDriver driver;
	  private String baseUrl;
	  private boolean acceptNextAlert = true;
	  private StringBuffer verificationErrors = new StringBuffer();
	  private String userName;
	  private String passwd;
	  private String apiKey;
	  private BFUITestUtil bfUIUtil;
	  private static final String thumbnail = "Thumbnail";
	  private static final String dateCaptured = "Date Captured";
	  private static final String bands = "Bands";
	  private static final String cloudCover = "Cloud Cover";
	  private static final String sensorName = "Sensor Name";

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeSuite
	public void initialize() throws Exception {
		System.out.println(">>>> In BFUIRunAlgorithmVldtn.initialize  <<<<");  
				
	    driver = new ChromeDriver();

/*		
		Thread.sleep(3000L);
      ProfilesIni profile = new ProfilesIni();
      FirefoxProfile firefoxProfile = profile.getProfile("default");

      driver = new FirefoxDriver(firefoxProfile);
*/
/*		
		Thread.sleep(3000);
		FirefoxProfile fp = new FirefoxProfile();
		fp.setPreference("browser.startup.homepage", "about:blank");
		fp.setPreference("startup.homepage_welcome_url", "about:blank");
		fp.setPreference("startup.homepage_welcome_url.additional", "https://beachfront.int.geointservices.io/login");
		
		driver = new FirefoxDriver(fp);
		System.out.println ("**** After launching firefox");
*/	    
	    bfUIUtil = new BFUITestUtil();
	    
	    userName = bfUIUtil.getUserName();
	    passwd = bfUIUtil.getPasswd();
	    baseUrl = bfUIUtil.getBaseUrl();
	    apiKey = bfUIUtil.getApiKey();
	    
	    System.out.println("userName: "+userName);
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
	   *  testStep1BFLogin() to actually test the BeachFront UI
	   *  login screen
	   *  
	   * @throws Exception
	   */
	  @Test
	  public void testStep1BFLogin() throws Exception {
		System.out.println(">>>> In BFUIRunAlgorithmVldtn.testStep1BFLogin() <<<<");  

		driver.get(baseUrl);
	    
	    driver.findElement(By.cssSelector("input")).clear();
	    driver.findElement(By.cssSelector("input")).sendKeys(userName);
	    driver.findElement(By.cssSelector("input[type=\"password\"]")).clear();
	    driver.findElement(By.cssSelector("input[type=\"password\"]")).sendKeys(passwd);
	    
	    Thread.sleep(1500);
	    driver.findElement(By.cssSelector("button[type=\"submit\"]")).click();
	    Thread.sleep(500);

	    driver.manage().window().maximize();
	    Assert.assertEquals("Beachfront", driver.getTitle());
	    System.out.println("After Launching Beach Front and logging in");

	  }

	
	/**
	 *  testStep2BFUIImagerySubmit() to accomplish testing 
	 *  of below functions of BF UI:
	 *  a) After user successfully logs in to BF UI Application
	 *  b) Selection of Create Job link
	 *  c) On the Canvas, specifying the geographic area
	 *  d) Entering the input data into the Search imagery request form.
	 *  e) Submit the form successfully 
	 *  
	 * @throws Exception
	 */
	@Test(dependsOnMethods = {"testStep1BFLogin"})
	public void testStep2BFUIImagerySubmit() throws Exception {
		System.out.println(">>>> In BFUIRunAlgorithmVldtn.testStep2BFUIImagerySubmit() <<<<");  

		driver.findElement(By.className("Navigation-linkCreateJob")).click(); 
		System.out.println(">> After requesting create job form");
		Thread.sleep(200);
		    
		WebElement canvas = driver.findElement(By.cssSelector(".PrimaryMap-root canvas"));     
	    Thread.sleep(200); //To avoid any race condition

		Locatable hoverItem = (Locatable) canvas;
		Mouse mouse = ((HasInputDevices) driver).getMouse();
		mouse.mouseMove(hoverItem.getCoordinates());
//		Coordinates coord = mouse.mouseMove(hoverItem.getCoordinates());
		Thread.sleep(200);
		Actions builder = new Actions(driver);
		builder.moveToElement(canvas,100,90).click().build().perform();
		System.out.println(">> After getting coordinates");
		Thread.sleep(200);
		Action drawAction = builder.moveByOffset(400, 200) // second point
		            .click()
		            .moveByOffset(520, 120)
		            .click()
		            .build();
		drawAction.perform();
		canvas.click();
		   
	    System.out.println(">> After selecting bounding box as geographic search criteria area on canvas");
	       
	    // populating API key on the Imagery search form
		driver.findElement(By.cssSelector("input[type=\"password\"]")).clear();
		driver.findElement(By.cssSelector("input[type=\"password\"]")).sendKeys(apiKey);
		   
		// Changing From date field for Date of Capture imagery search criteria
		driver.findElement(By.cssSelector("input[type=\"date\"]")).sendKeys("01/01/2015");

		Thread.sleep(2000); //To avoid any race condition
		//Changing the cloud cover slider to <15%
		driver.findElement(By.cssSelector("input[type=\"range\"]")).sendKeys("15");
		Thread.sleep(500); //To avoid any race condition

		// Defaulted to none so not needed
		//new Select(driver.findElement(By.cssSelector("label.ImagerySearch-spatialFilter.forms-field-normal > select"))).click();
		//new Select(driver.findElement(By.cssSelector("label.ImagerySearch-spatialFilter.forms-field-normal > select"))).selectByVisibleText("None");

		// Submitting the search criteria
		driver.findElement(By.cssSelector("button[type=\"submit\"]")).click();		   
		System.out.println(">> After entering data and submitting Source Imagery search form");
		Thread.sleep(2000); //Pause before exiting this test
		
	}

	/**
	 *  testStep3BFSIResponsePopup() to accomplish testing of below functions of BF UI:
	 *  a) After user enter the search criteria for image search catalog and submits
	 *  b) Move to the map canvas panel
	 *  c) Select the response image jpg file to display the pop up
	 *     with properties of the selected response image
	 *  
	 * @throws Exception
	 */
	@Test(dependsOnMethods = {"testStep2BFUIImagerySubmit"})
	public void testStep3BFSIResponsePopup() throws Exception {
		System.out.println(">>>> In BFUIRunAlgorithmVldtn.testStep3BFSIResponsePopup() <<<<");  

		WebElement canvas = driver.findElement(By.cssSelector(".PrimaryMap-root canvas"));     
	    Thread.sleep(200); //To avoid any race condition

//		canvas.getLocation().move(500, 500);
//		canvas.click();
		Actions builder = new Actions(driver);
		builder.moveToElement(canvas,100,90).click().build().perform(); // first point
		System.out.println(">> After getting coordinates");
		Thread.sleep(500);
		Action drawAction = builder.moveByOffset(400, 200) // second point; selects the image for running algorithm job
		            .click()
		            .moveByOffset(520, 120)
		            .click()
		            .build();
		drawAction.perform();

		System.out.println("After moving to canvas and selecting image jpg on canvas");
	    Thread.sleep(2500); //To avoid any race condition

	    // Ensuring the properties windows is displayed for the image selected
	    // LANDSAT image id for selected image should be the title.
	    driver.findElement(By.xpath("//*[@title='LC81240462015273LGN00']"));
//	    driver.findElement(By.xpath("//*[@title='LC81210602015060LGN00']"));
//	    driver.findElement(By.xpath("//*[@title=$imageID]")); //Error with imageID, @imageID, $imageID
	    
		System.out.println("After validating properties popup is displayed for the response image selected");
		Thread.sleep(2000); //Pause before exiting this test
	}
	
	/**
	 *  testStep4BFSIRespPropsVldtn() for properties validation in popup window:
	 *  d) Validate the below property fields for the selected image:
 	 *		i.THUMBNAIL 
   *      ii.DATE CAPTURED
   *      iii.BANDS  
   *      iv.CLOUD COVER
   *      v.SENSOR NAME
	 *  
	 * @throws Exception
	 */
	@Test(dependsOnMethods = {"testStep3BFSIResponsePopup"})
	public void testStep4BFSIRespPropsVldtn() throws Exception {
		System.out.println(">>>> In BFUIRunAlgorithmVldtn.testStep4BFSIRespPropsVldtn() <<<<");  

	    driver.findElement(By.xpath("//*[contains(text(),thumbnail)]"));
		System.out.println(">> After validating THUMBNAIL property is displayed");
	    
	    driver.findElement(By.xpath("//*[contains(text(),dateCaptured)]"));
		System.out.println(">> After validating DATE CAPTURED property is displayed");

	    driver.findElement(By.xpath("//*[contains(text(),bands)]"));
		System.out.println(">> After validating BANDS property is displayed");
		
	    driver.findElement(By.xpath("//*[contains(text(),cloudCover)]"));
		System.out.println(">> After validating CLOUD COVER property is displayed");
	    
	    driver.findElement(By.xpath("//*[contains(text(),sensorName)]"));
		System.out.println(">> After validating SENSOR NAME property is displayed");

		Thread.sleep(500); //Pause before exiting this test
	}

	/**
	 *  testStep5RespImageLink() to accomplish testing of below functions of BF UI:
	 *  e) Click on the hyper link for the text “Click here to open” to display 
	 *     the jpg image file in a separate tab
	 *  
	 * @throws Exception
	 */
	@Test(dependsOnMethods = {"testStep4BFSIRespPropsVldtn"})
	public void testStep5RespImageLink() throws Exception {
		System.out.println(">>>> In BFUIRunAlgorithmVldtn.testStep5RespImageLink() <<<<");  
	    
		// Test to Click on the hyper link for “Click here to open” 
		// to open the separate tab to display the jpg image file
		driver.findElement(By.linkText("Click here to open")).click();
		System.out.println(">> After clicking on image link to open the jpg image file in separate tab");

		//driver.findElement(By.partialLinkText("Click here to open")).click();

		Thread.sleep(2000); //Pause before exiting this test
	}

	/**
	 *  To clean up the resources used and close the browser session
	 * @throws Exception
	 */
	@AfterSuite
	public void cleanUp() throws Exception {
		System.out.println(">>>> BFUIRunAlgorithmVldtn.cleanUp Closing Browser Session <<<<");
	    driver.quit();
	    String verificationErrorString = verificationErrors.toString();
	    if (!"".equals(verificationErrorString)) {
	      fail(verificationErrorString);
	    }	    
	}
	
}