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

/**
 * Because the utility BFUITestUtil class will fetch user credentials from 
 * BFUIUtil properties file hence giving this class package level access and
 * not a public class in a separate util package so that only the package 
 * level classes can retrieve the credentials information. 
 * 
 * BFUIUtil.properties file should be packaged in jar from under 
 * src/main/java/beachfront.ui.test so it remains hidden and is
 * in class path for runtime access
 * 
 */
package beachfront.ui.test;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 	  @author 		RahulsIM
 *    PROJECT: 		Beachfront project 
 *    CLASS: 		BFUITestUtil class to to initialize the Beachfront 
 *    				UI URL and it's credentials
 *              ** REVISION HISTORY : **
 * 	  Created:		8/27/2016
 */
class BFUITestUtil {

	  private String baseUrl;
	  private String userName;
	  private String passwd;
	  private String apiKey;
	  private static final String uIdProp = "BFUI_USER";
	  private static final String uPswdProp = "BFUI_PASS";  
	  private static final String baseUrlProp = "baseUrl";
	  private static final String apiKeyProp = "API_Key";
	  private Properties prop = new Properties();
	  private String propFileName = "BFUIUtil.properties";
	  private InputStream inStream;
	 
	  /*
	   * Constructor - Calls private initializeCredentials()
	   * to initialize the Beachfront UI URL and it's credentials
	   * for test automation to login to Beachfront UI app. 
	   */
	  BFUITestUtil() throws Exception{
		  this.initializeCredentials();
	  }
	
	  /**
	 * @return the baseUrl
	 */
	String getBaseUrl() {
		return baseUrl;
	}

	/**
	 * @return the userName
	 */
	String getUserName() {
		return userName;
	}

	/**
	 * @return the passwd
	 */
	String getPasswd() {
		return passwd;
	}

	/**
	 * @return the apiKey
	 */
	String getApiKey() {
		return apiKey;
	}

	/*
	 * private method initializeCredentials() to initialize the private 
	 * variables:
	 *     baseUrl
	 *     userName
	 *     passwd
	 *     apiKey
	 * by either:
	 * 1> Checking the environment variables 
	 * OR 
	 * 2> Reading the BFUIUtil.properties     
	 *   
	 */
	private void initializeCredentials() throws Exception {
			// initialize the credentials and Beachfront UI URL by checking for
			// environment variables   
			userName = System.getenv(uIdProp);
		    passwd = System.getenv(uPswdProp);
		    baseUrl = System.getenv(baseUrlProp);
		    apiKey = System.getenv(apiKeyProp);

		    // If the Beachfront UI URL and it's credentials are not set as environment
		    // variables then check for them in BFUIUtil.properties file
		    if (userName == null || passwd == null || baseUrl == null || apiKey == null) {
		      try {

		    	// initialize  
		    	inStream = getClass().getClassLoader().getResourceAsStream(propFileName);
		    		 
		    	if (inStream != null) {
		    	  prop.load(inStream);
		    	} else {
		    	  throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
		        }
		        
		    	baseUrl = prop.getProperty(baseUrlProp);
		    	userName = prop.getProperty(uIdProp);
		    	passwd = prop.getProperty(uPswdProp);
		    	apiKey = prop.getProperty(apiKeyProp);
		    	
		      } catch (Exception e) { 
		    	  e.printStackTrace();
		    	  System.out.println("Exception in initializing using BFUIUtil.properties: "+e);
		    	  throw e;
		      }
		      finally { 
		        inStream.close(); 
		      }
		    	
		    }//end of using BFUIUtil.properties file to initialize Beachfront UI URL and it's credentials
		    
	  }// end of initializeCredentials()
	  	
}
