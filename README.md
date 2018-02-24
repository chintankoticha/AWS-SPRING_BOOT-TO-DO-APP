
#Team Information
* <Neha Lalwani>, <001268916>, <lalwani.n@husky.neu.edu>
* <Nirali Merchant>, <001268909>, <merchant.n@husky.neu.edu>
* <Chintan Koticha>, <001267049>, <koticha.c@husky.neu.edu>
* <Apoorva Lakhmani>, <001256312>, <lakhmani.a@husky.neu.edu>

#Prerequisites for building and deploying the application locally
* Click a spring boot web application with gradle project and mention all dependencies in build.gradle file
* Developing the source code for Rest based API endpoints.
* Mention persistant database environment (like MySQL) based details in applications.properties file
* Auto build trigger for Travis-CI needs to be handled by travis.yml file. Ensure scripts of artifact integration and test file integrations are written. Sync repository with Travis-CI and ensure it runs only when project contains travis.yml file.
* Build the project as a war package artifact and ensure project structure module has artifact linked to it.
* Edit configurations and add a local server(like tomcat). Create a deployment link to artifact create by project build.

#Instructions to run unit, integration test
* Write unit tests and integration tests in separate Java Class and annotate with required testing platform dependencies.
* For running locally, right click on the test functions and run them.
* For running on manual build trigger through Travis-CI server, ensure travis.yml contains scripts linked to the folder that contains unit tests. Push the code to repository to trigger a build or else trigger manual build from the travis-CI console to get the results.

#Instructions to run load based tests
* Install Apache-jmeter as a load test functional behavior and measuring performance.
* Run ./jmeter.sh through command line to open jmeter GUI console.
* Create a new thread group and set-up load testing platform like number of users and ramp-up time on the server for performance measurement
* Add the following to thread group: 
	- Http Request sampler: Configure for the webpage server, GET/POST method, port number, and REST API endpoint URL.
	- HTTP Authorization Manager: Configure webpage server, Username and password for testing.
	- View results in tree: View results on running the application and jmeter test server.


