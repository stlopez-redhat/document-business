package com.freddiemac.mf.service.rhpam.cucumber;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(
		features = "src/test/resources/features/integration",
		glue = {"com/freddiemac/mf/foundation/rhpam/cucumber"},
		plugin = {"pretty"}, 
		monochrome = true,
		tags = {"not @ignore"})		
public class RunCucumberIntegrationTest {

}
