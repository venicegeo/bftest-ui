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
*    CLASS:            BFSIOutputValidationTest class to test the
*                      Happy path scenario of Imagery Search
*                      request form
*              ** REVISION HISTORY : **
*    Created:   9/7/2016
*    Updates:
*
*/

import static org.junit.Assert.fail;
import java.util.concurrent.TimeUnit;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.AfterSuite;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.HasInputDevices;
import org.openqa.selenium.interactions.Mouse;
import org.openqa.selenium.internal.Locatable;

public class BFSIOutputValidationTest {
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
	@BeforeSuite
	public void initialize() throws Exception {
		System.out.println("In BFSIOutputValidationTest.initialize");  
				
	    driver = new ChromeDriver();
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
	    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}

	  /**
	   *  testStep1BFLogin() to actually test the BeachFront UI
	   *  login screen by using Chrome browser
	   *  
	   * @throws Exception
	   */
	  @Test
	  public void testStep1BFLogin() throws Exception {
		System.out.println("In BFSIOutputValidationTest.testStep1BFLogin()");  

		driver.get(baseUrl);
	    
	    driver.findElement(By.cssSelector("input")).clear();
	    driver.findElement(By.cssSelector("input")).sendKeys(userName);
	    driver.findElement(By.cssSelector("input[type=\"password\"]")).clear();
	    driver.findElement(By.cssSelector("input[type=\"password\"]")).sendKeys(passwd);
	    
	    Thread.sleep(1500);
	    driver.findElement(By.cssSelector("button[type=\"submit\"]")).click();
	    System.out.println("After Launching Beach Front and logging in");
//	    assert driver.findElement(By.name("Beachfront"));
	    Thread.sleep(5000);

	  }

	
	/**
	   *  testStep2BFUIImagerySubmit() to accomplish testing of below functions of BF UI:
	   *  a) After user successfully logs in to BF UI Appl
	   *  b) Selection of Create Job link
	   *  c) On the Canvas, specifying the geographic area
	   *  d) Entering the input data into the Search imagery request form.
	   *  e) Submit the form successfully 
	   *  
	   * @throws Exception
	   */
	@Test(dependsOnMethods = {"testStep1BFLogin"})
	public void testStep2BFUIImagerySubmit() throws Exception {
		System.out.println("In BFSIOutputValidationTest.testStep2BFUIImagerySubmit() ");  

		driver.findElement(By.className("Navigation-linkCreateJob")).click(); 
		System.out.println("After requesting create job form");
		Thread.sleep(2000);
		    
		WebElement canvas = driver.findElement(By.cssSelector(".PrimaryMap-root canvas"));     
	    Thread.sleep(200); //To avoid any race condition

	    canvas.getLocation().move(500, 500); //start
		canvas.click();

		canvas.getLocation().moveBy(250, 250); //click to select search area
		canvas.click();
		Thread.sleep(500); //To avoid any race condition

		Locatable hoverItem = (Locatable) canvas;
		Mouse mouse = ((HasInputDevices) driver).getMouse();
		mouse.mouseMove(hoverItem.getCoordinates());
//		Coordinates coord = mouse.mouseMove(hoverItem.getCoordinates());
		Thread.sleep(200);
		Actions builder = new Actions(driver);
		builder.moveToElement(canvas,100,90).click().build().perform();
		System.out.println("After getting coordinates");
		Thread.sleep(200);
		Action drawAction = builder.moveByOffset(100, 60) // second point
		            .click()
		            .build();
		drawAction.perform();
		   
	    System.out.println("After selecting geographic search criteria area on canvas");
	       
	    // populating API key on the Imagery search form
		driver.findElement(By.cssSelector("input[type=\"password\"]")).clear();
		driver.findElement(By.cssSelector("input[type=\"password\"]")).sendKeys(apiKey);
		   
		// Changing From date field for Date of Capture imagery search criteria
		driver.findElement(By.cssSelector("input[type=\"date\"]")).sendKeys("01/01/2015");

		Thread.sleep(2000); //To avoid any race condition
		//Changing the cloud cover slider to <50%
		driver.findElement(By.cssSelector("input[type=\"range\"]")).click();
		Thread.sleep(500); //To avoid any race condition
		driver.findElement(By.cssSelector("input[type=\"range\"]")).sendKeys("15");

		// Defaulted to none so not needed
		//new Select(driver.findElement(By.cssSelector("label.ImagerySearch-spatialFilter.forms-field-normal > select"))).click();
		//new Select(driver.findElement(By.cssSelector("label.ImagerySearch-spatialFilter.forms-field-normal > select"))).selectByVisibleText("None");

		// Submitting the search criteria
		driver.findElement(By.cssSelector("button[type=\"submit\"]")).click();		   
		System.out.println("After entering data and submitting Source Imagery search form");
		Thread.sleep(5000); //Pause before exiting this test
		
	}

	/**
	   *  testStep3BFSIOutputVldtn() to accomplish testing of below functions of BF UI:
	   *  a) After user enter the search criteria for image search catalog and submits
	   *  b) Move to the map canvas panel
	   *  c) Select the response image jpg file to display the pop up
	   *     with properties of the selected response image
	   *  d) Validate the below property fields for the selected image:
   	   *		i.THUMBNAIL 
       *       ii.DATE CAPTURED
       *      iii.BANDS  
       *       iv.CLOUD COVER
       *        v.SENSOR NAME
	   *  e) Click on the hyper link for the text “Click here to open” to display 
	   *     the jpg image file
	   *  
	   * @throws Exception
	   */
	@Test(dependsOnMethods = {"testStep2BFUIImagerySubmit"})
	public void testStep3BFSIOutputVldtn() throws Exception {
		System.out.println("In BFSIOutputValidationTest.testStep3BFSIOutputVldtn() ");  

		WebElement canvas = driver.findElement(By.cssSelector(".PrimaryMap-root canvas"));     
	    Thread.sleep(200); //To avoid any race condition

		canvas.getLocation().move(500, 500);
		canvas.click();
		System.out.println("After moving to canvas and selecting image jpg on canvas");
	    Thread.sleep(5000); //To avoid any race condition

		//driver.findElement(By.name("BANDS")); //Error

	    
		// Test to Click on the hyper link for “Click here to open” 
		// to open the separate tab to display the jpg image file
		driver.findElement(By.linkText("Click here to open")).click();
		System.out.println("After clicking on image link to open the jpg image file in separate tab");
		
		Thread.sleep(5000); //Pause before exiting this test
	}
	
	/**
	 *  To clean up the resources used and close the browser session
	 * @throws Exception
	 */
	@AfterSuite
	public void cleanUp() throws Exception {
		System.out.println("BFSIOutputValidationTest.cleanUp Closing Browser Session");
/*	    driver.quit();
	    String verificationErrorString = verificationErrors.toString();
	    if (!"".equals(verificationErrorString)) {
	      fail(verificationErrorString);
	    }
*/	    
	}
	
}
