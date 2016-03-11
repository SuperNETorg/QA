package com.supernet.api.hash;

import java.util.HashMap;

import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;
import com.supernet.api.BaseTestClass;
import com.supernet.api.utility.ReadExcel;
import com.supernet.api.utility.ReporterUtil;

public class RMD_ConversionsAPITest extends BaseTestClass {
	
	
	public ReporterUtil reporter;
	private static final Logger logger = LoggerFactory.getLogger(RMD_ConversionsAPITest.class);
	
	String className = this.getClass().getName();
	private String apirmd128, apirmd160, apirmd256, apirmd320;
	
	@BeforeClass
	public void setPreconditions() {
		logger.info("In the class ---", className);
		reporter = getReporter(" Hash API - RMD_Conversions method ", "Tests RMD_Conversions method of hash API ");
		
	}
	
	
	@DataProvider(name = "RMD_ConversionsTestData")
	public static Object[][] testData() throws Exception {
		logger.info("In the method ");
		ReadExcel re = new ReadExcel(
				System.getProperty("user.dir") + BaseTestClass.globalConfig.getString("DATASHEET_CONFIG"),
				"sheetRMD_ConversionsTest");
		return re.getTableToHashMapDoubleArray();
	}
	
	
	@Test(priority = 1, dataProvider = "RMD_ConversionsTestData")
	public void RMD_Conversions(HashMap<String, String> hm) throws JSONException {
		
		// Checking execution flag
		if (hm.get("ExecuteFlag").trim().equalsIgnoreCase("No")){
					
			throw new SkipException("Skipping the test -->> As per excel entry");
		}
				
		apirmd128 = globalConfig.getString("RMD128_API_URL") + "?" + "message=";
		apirmd160 = globalConfig.getString("RMD160_API_URL") + "?" + "message=";
		apirmd256 = globalConfig.getString("RMD256_API_URL") + "?" + "message=";
		apirmd320 = globalConfig.getString("RMD320_API_URL") + "?" + "message=";
		
		
		switch (hm.get("TestScenario")) {
		
		case "RMD128":
			if ("Positive".equalsIgnoreCase(hm.get("ScenarioType"))) {
				
				// Call the rest API
				apirmd128 = apirmd128 + hm.get("I_Message");
				System.out.println(apirmd128);
				
				Response resp = RestAssured.get(apirmd128);
				
				// Checking the Status code for the response
				logger.info("Check the response status code. It should be 200");
				Assert.assertEquals(resp.getStatusCode(), 200);
				
				String responseString = resp.asString();
				JsonPath jsonPath = new JsonPath(responseString);
				logger.info(responseString);
				
				// Checking the value of result key
				logger.info(jsonPath.getString("result"));
				Assert.assertEquals(jsonPath.getString("result"), "hash calculated");
				
				// Checking the value of message key
				logger.info(jsonPath.getString("message"));
				Assert.assertEquals(jsonPath.getString("message"), hm.get("I_Message"));
				
				// Checking the encoded value for base64_encode key
				Assert.assertNotNull(jsonPath.getString("rmd128"));
				Assert.assertEquals(jsonPath.getString("rmd128"), hm.get("O_Expected"));
				
				// Checking whether the value of tag is not null
				Assert.assertNotNull(jsonPath.getString("tag"));
				
				
			}else{
				
				
			}
			
			break;
			
		case "RMD160":
			if ("Positive".equalsIgnoreCase(hm.get("ScenarioType"))) {
				
				// Call the rest API
				apirmd160 = apirmd160 + hm.get("I_Message");
				System.out.println(apirmd160);
				
				Response resp = RestAssured.get(apirmd160);
				
				// Checking the Status code for the response
				logger.info("Check the response status code. It should be 200");
				Assert.assertEquals(resp.getStatusCode(), 200);
				
				String responseString = resp.asString();
				JsonPath jsonPath = new JsonPath(responseString);
				logger.info(responseString);
				
				// Checking the value of result key
				logger.info(jsonPath.getString("result"));
				Assert.assertEquals(jsonPath.getString("result"), "hash calculated");
				
				// Checking the value of message key
				logger.info(jsonPath.getString("message"));
				Assert.assertEquals(jsonPath.getString("message"), hm.get("I_Message"));
				
				// Checking the encoded value for base64_encode key
				Assert.assertNotNull(jsonPath.getString("rmd160"));
				Assert.assertEquals(jsonPath.getString("rmd160"), hm.get("O_Expected"));
				
				// Checking whether the value of tag is not null
				Assert.assertNotNull(jsonPath.getString("tag"));
				
			}else{
				
				
			}
			
			break;
			
		case "RMD256":
			if ("Positive".equalsIgnoreCase(hm.get("ScenarioType"))) {
				
				// Call the rest API
				apirmd256 = apirmd256 + hm.get("I_Message");
				System.out.println(apirmd256);
				
				Response resp = RestAssured.get(apirmd256);
				
				// Checking the Status code for the response
				logger.info("Check the response status code. It should be 200");
				Assert.assertEquals(resp.getStatusCode(), 200);
				
				String responseString = resp.asString();
				JsonPath jsonPath = new JsonPath(responseString);
				logger.info(responseString);
				
				// Checking the value of result key
				logger.info(jsonPath.getString("result"));
				Assert.assertEquals(jsonPath.getString("result"), "hash calculated");
				
				// Checking the value of message key
				logger.info(jsonPath.getString("message"));
				Assert.assertEquals(jsonPath.getString("message"), hm.get("I_Message"));
				
				// Checking the encoded value for base64_encode key
				Assert.assertNotNull(jsonPath.getString("rmd256"));
				Assert.assertEquals(jsonPath.getString("rmd256"), hm.get("O_Expected"));
				
				// Checking whether the value of tag is not null
				Assert.assertNotNull(jsonPath.getString("tag"));
				
			}else{
				
				
			}
			
			break;
			
		case "RMD320":
			if ("Positive".equalsIgnoreCase(hm.get("ScenarioType"))) {
				
				// Call the rest API
				apirmd320 = apirmd320 + hm.get("I_Message");
				System.out.println(apirmd320);
				
				Response resp = RestAssured.get(apirmd320);
				
				// Checking the Status code for the response
				logger.info("Check the response status code. It should be 200");
				Assert.assertEquals(resp.getStatusCode(), 200);
				
				String responseString = resp.asString();
				JsonPath jsonPath = new JsonPath(responseString);
				logger.info(responseString);
				
				// Checking the value of result key
				logger.info(jsonPath.getString("result"));
				Assert.assertEquals(jsonPath.getString("result"), "hash calculated");
				
				// Checking the value of message key
				logger.info(jsonPath.getString("message"));
				Assert.assertEquals(jsonPath.getString("message"), hm.get("I_Message"));
				
				// Checking the encoded value for base64_encode key
				Assert.assertNotNull(jsonPath.getString("rmd320"));
				Assert.assertEquals(jsonPath.getString("rmd320"), hm.get("O_Expected"));
				
				// Checking whether the value of tag is not null
				Assert.assertNotNull(jsonPath.getString("tag"));
				
			}else{
				
				
			}
			
			break;
		}
		
	}
	
	
	@AfterClass
	public void tearDown() {
		
		if (reporter != null) {
			logger.debug("closing local reporter file");
			reporter.flush();
			reporter.closeReport();
		}
	}
	
}
