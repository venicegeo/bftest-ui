
	**** TEST EXECUTION ****
# Maven after running the designated tests, also runs the default test suite for which this project has no tests  
# so maven runs an empty test suite that overwrites the report with 0 test cases issue.
# This ReadMe has examples of maven commands to directly execute the Selenium tests through Surefire plugin for maven 
# so maven will not run the default test suite to overwrite the test metrics report.
mvn -e surefire:test
mvn -e surefire:test -Dtest=beachfront.ui.test.BeachFrontLoginTest

	**** TEST EXECUTION PROPERTIES ****
# Also the bftest-ui design uses below properties for the execution of the test scripts. These properties can be set 
# as in the environment properties file. Also they can be overridden by setting them as system environment variables
    baseUrl
    BFUI_USER
    BFUI_PASS
    API_Key
# The BFUIUtil.properties file is located under the src/resources folder of the bftest-ui project

	**** TEST EXECUTION RESULTS REPORTS ****
# When the test are executed, the metrics reports are generated under the below folder:
  > TestNG automation tests execution report is under:
      bftest-ui/target/surefire-reports/testng-native-result
  > JUnit automation tests execution report is under:
      bftest-ui/target/surefire-reports/testng-junit-results
