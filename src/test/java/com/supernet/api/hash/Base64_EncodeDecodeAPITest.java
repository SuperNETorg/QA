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

public class Base64_EncodeDecodeAPITest extends BaseTestClass {
	
	public ReporterUtil reporter;
	private static final Logger logger = LoggerFactory.getLogger(Base64_EncodeDecodeAPITest.class);
	
	String className = this.getClass().getName();
	private String apibase64encode, apibase64decode;
	
	
	@BeforeClass
	public void setPreconditions() {
		logger.info("In the class ---", className);
		reporter = getReporter(" Hash API - Base64_EncodeDecode method ", "Tests Base64_EncodeDecode method of hash API ");
		
	}
	
	
	@DataProvider(name = "Base64_EncodeDecodeTestData")
	public static Object[][] testData() throws Exception {
		logger.info("In the method ");
		ReadExcel re = new ReadExcel(
				System.getProperty("user.dir") + BaseTestClass.globalConfig.getString("DATASHEET_CONFIG"),
				"sheetBase64_EncodeDecodeTest");
		return re.getTableToHashMapDoubleArray();
	}
	
	
	@Test(priority = 1, dataProvider = "Base64_EncodeDecodeTestData")
	public void Base64_EncodeDecode(HashMap<String, String> hm) throws JSONException {
		
		// Checking execution flag
		if (hm.get("ExecuteFlag").trim().equalsIgnoreCase("No"))
			throw new SkipException("Skipping the test -->> As per excel entry");
		
		try{
			apibase64encode = globalConfig.getString("BASE64_ENCODE_API_URL");
			apibase64decode = globalConfig.getString("BASE64_DECODE_API_URL");
			
			switch (hm.get("TestScenario")) {
			
			case "BASE64_ENCODE":
				if ("Positive".equalsIgnoreCase(hm.get("ScenarioType"))) {
					
					// Call the rest API
					apibase64encode = apibase64encode + "?" + "message=" + hm.get("I_Message");
					System.out.println(apibase64encode);
					Thread.sleep(5000);
					
					Response resp = RestAssured.get(apibase64encode);
					
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
					Assert.assertNotNull(jsonPath.getString("base64_encode"));
					Assert.assertEquals(jsonPath.getString("base64_encode"), hm.get("O_Expected"));
					
					// Checking whether the value of tag is not null
					Assert.assertNotNull(jsonPath.getString("tag"));
					
				}else{
					
					
				}
				
				break;
				
				
			case "BASE64_DECODE":
				if("Positive".equalsIgnoreCase(hm.get("ScenarioType"))){
					
					// Call the rest API
					apibase64decode = apibase64decode + hm.get("I_Message");
					System.out.println(apibase64decode);
					Thread.sleep(5000);
					
					Response resp = RestAssured.get(apibase64decode);
					
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
					
					// Checking the decoded value for base64_decode key
					Assert.assertNotNull(jsonPath.getString("base64_decode"));
					Assert.assertEquals(jsonPath.getString("base64_decode"), hm.get("O_Expected"));
					
					// Checking whether the value of tag is not null
					Assert.assertNotNull(jsonPath.getString("tag"));
					
				}else{
					
					
				}
				
				break;
				
			default:
				break;
				
			}
			
		}catch(Exception e){
			
			//e.printStackTrace();
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
