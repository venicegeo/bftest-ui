# Maven after running the designated tests, also runs the default test suite for which this project has no tests  
# so maven runs an empty test suite that overwrites the report with 0 test cases issue.
# This ReadMe has examples of maven commands to directly execute the Selenium tests through Surefire plugin for maven 
# so maven will not run the default test suite to overwrite the test metrics report.
mvn -e surefire:test
mvn -e surefire:test -Dtest=beachfront.ui.test.BeachFrontLoginTest
