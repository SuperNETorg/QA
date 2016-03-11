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

public class SHA_ConversionsAPITest extends BaseTestClass {
	
	public ReporterUtil reporter;
	private static final Logger logger = LoggerFactory.getLogger(SHA_ConversionsAPITest.class);
	
	String className = this.getClass().getName();
	private String apisha224, apisha256, apisha384, apisha512, apisha1;
	
	
	@BeforeClass
	public void setPreconditions() {
		logger.info("In the class ---", className);
		reporter = getReporter(" Hash API - SHA_Conversions method ", "Tests SHA_Conversions method of hash API ");
		
	}
	
	
	@DataProvider(name = "SHA_ConversionsTestData")
	public static Object[][] testData() throws Exception {
		logger.info("In the method ");
		ReadExcel re = new ReadExcel(
				System.getProperty("user.dir") + BaseTestClass.globalConfig.getString("DATASHEET_CONFIG"),
				"sheetSHA_ConversionsTest");
		return re.getTableToHashMapDoubleArray();
	}
	
	
	@Test(priority = 1, dataProvider = "SHA_ConversionsTestData")
	public void SHA_Conversions(HashMap<String, String> hm) throws JSONException {
		
		// Checking execution flag
		if (hm.get("ExecuteFlag").trim().equalsIgnoreCase("No")){
			
			throw new SkipException("Skipping the test -->> As per excel entry");
		}
		
		apisha224 = globalConfig.getString("SHA224_API_URL") + "?" + "message=";
		apisha256 = globalConfig.getString("SHA256_API_URL") + "?" + "message=";
		apisha384 = globalConfig.getString("SHA384_API_URL") + "?" + "message=";
		apisha512 = globalConfig.getString("SHA512_API_URL") + "?" + "message=";
		apisha1 = globalConfig.getString("SHA1_API_URL") + "?" + "message=";
		
		switch (hm.get("TestScenario")) {
		
		case "SHA224":
			if ("Positive".equalsIgnoreCase(hm.get("ScenarioType"))) {
				
				// Call the rest API
				apisha224 = apisha224 + hm.get("I_Message");
				System.out.println(apisha224);
				
				Response resp = RestAssured.get(apisha224);
				
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
				Assert.assertNotNull(jsonPath.getString("sha224"));
				Assert.assertEquals(jsonPath.getString("sha224"), hm.get("O_Expected"));
				
				// Checking whether the value of tag is not null
				Assert.assertNotNull(jsonPath.getString("tag"));
				
			}else{
				
				
			}
			
			break;
			
		case "SHA256":
			if ("Positive".equalsIgnoreCase(hm.get("ScenarioType"))) {
				
				// Call the rest API
				apisha256 = apisha256 + hm.get("I_Message");
				System.out.println(apisha256);
				
				Response resp = RestAssured.get(apisha256);
				
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
				Assert.assertNotNull(jsonPath.getString("sha256"));
				Assert.assertEquals(jsonPath.getString("sha256"), hm.get("O_Expected"));
				
				// Checking whether the value of tag is not null
				Assert.assertNotNull(jsonPath.getString("tag"));
				
			}else{
				
				
			}
			
			break;
			
			
		case "SHA384":
			if ("Positive".equalsIgnoreCase(hm.get("ScenarioType"))) {
				
				// Call the rest API
				apisha384 = apisha384 + hm.get("I_Message");
				System.out.println(apisha384);
				
				Response resp = RestAssured.get(apisha384);
				
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
				Assert.assertNotNull(jsonPath.getString("sha384"));
				Assert.assertEquals(jsonPath.getString("sha384"), hm.get("O_Expected"));
				
				// Checking whether the value of tag is not null
				Assert.assertNotNull(jsonPath.getString("tag"));
				
			}else{
				
				
			}
			
			break;
			
		case "SHA512":
			if ("Positive".equalsIgnoreCase(hm.get("ScenarioType"))) {
				
				// Call the rest API
				apisha512 = apisha512 + hm.get("I_Message");
				System.out.println(apisha512);
				
				Response resp = RestAssured.get(apisha512);
				
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
				Assert.assertNotNull(jsonPath.getString("sha512"));
				Assert.assertEquals(jsonPath.getString("sha512"), hm.get("O_Expected"));
				
				// Checking whether the value of tag is not null
				Assert.assertNotNull(jsonPath.getString("tag"));
				
			}else{
				
				
			}
			
			break;
			
		case "SHA1":
			if ("Positive".equalsIgnoreCase(hm.get("ScenarioType"))) {
				
				// Call the rest API
				apisha1 = apisha1 + hm.get("I_Message");
				System.out.println(apisha1);
				
				Response resp = RestAssured.get(apisha1);
				
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
				Assert.assertNotNull(jsonPath.getString("sha1"));
				Assert.assertEquals(jsonPath.getString("sha1"), hm.get("O_Expected"));
				
				// Checking whether the value of tag is not null
				Assert.assertNotNull(jsonPath.getString("tag"));
				
			}else{
				
				
			}
			
			break;
			
		default:
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
