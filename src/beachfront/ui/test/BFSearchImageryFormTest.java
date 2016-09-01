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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
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
	  private BFUITestUtil bfUIUtil;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		System.out.println("In BFSearchImageryFormTest.setUp");  
				
	    driver = new ChromeDriver();
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

		    Thread.sleep(2000);
			driver.findElement(By.className("Navigation-linkCreateJob")).click(); 
		    System.out.println("After requesting create job form");
		    Thread.sleep(2000);
		    
		    WebElement canvas = driver.findElement(By.cssSelector(".PrimaryMap-root canvas"));     
			Thread.sleep(200); //To avoid any race condition

		    canvas.getLocation().move(500, 500); //start
		    canvas.click();

		    canvas.getLocation().moveBy(250, 250); //click to select search area
		    canvas.click();
		    Thread.sleep(2000); //To avoid any race condition

		    Locatable hoverItem = (Locatable) canvas;
		    Mouse mouse = ((HasInputDevices) driver).getMouse();
		    mouse.mouseMove(hoverItem.getCoordinates());
//		    Coordinates coord = mouse.mouseMove(hoverItem.getCoordinates());
		    Thread.sleep(200);
		    Actions builder = new Actions(driver);
		    builder.moveToElement(canvas,100,90).click().build().perform();
		    System.out.println("After getting coordinates and clicking");
		    Thread.sleep(200);
		    Action drawAction = builder.moveByOffset(100, 60) // second point
		            .click()
		            .build();
		    drawAction.perform();
		   
		    /*
		    Action drawAction = builder.moveToElement(canvas,100,90)  // start point
		                 .click()
		                 .moveByOffset(100, 60) // second point
		                 .click()
		                 .build();
		       drawAction.perform();*/
		       System.out.println("After selecting canvas");
	}

}
