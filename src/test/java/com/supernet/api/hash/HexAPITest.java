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
import com.supernet.api.utility.HelperUtil;
import com.supernet.api.utility.ReadExcel;
import com.supernet.api.utility.ReporterUtil;

/**
 * Testing both hex and unhex method
 * 
 * @author Peeyush
 *
 */

public class HexAPITest extends BaseTestClass {
	public ReporterUtil reporter;
	private static final Logger logger = LoggerFactory.getLogger(HexAPITest.class);

	String className = this.getClass().getName();
	private String apiHexURL, apiUnhexURL;

	String uniqueValue = HelperUtil.getUniqueValue();

	//SoftAssert softAssert = new SoftAssert();

	/**
	 * Setting up the precondition
	 */

	@BeforeClass
	public void setPreconditions() {
		logger.info("In the class ---", className);
		reporter = getReporter(" Hash API - hex and unhex method ", "Tests hex and unhex method of hash API ");
	}

	@DataProvider(name = "hexTestData")
	public static Object[][] testData() throws Exception {
		logger.info("In the method ");
		ReadExcel re = new ReadExcel(
				System.getProperty("user.dir") + BaseTestClass.globalConfig.getString("DATASHEET_CONFIG"),
				"sheetHexTest");
		return re.getTableToHashMapDoubleArray();
	}

	@Test(priority = 1, dataProvider = "hexTestData")
	public void hexAndUnhex(HashMap<String, String> hm) throws JSONException {

		// Checking execution flag
		if (hm.get("ExecuteFlag").trim().equalsIgnoreCase("No"))
			throw new SkipException("Skipping the test -->> As per excel entry");

		apiHexURL = globalConfig.getString("HEX_API_URL") + "?" + "message=";
		apiUnhexURL = globalConfig.getString("UNHEX_API_URL") + "?" + "hexmsg=";
		//apibase64encode = globalConfig.getString("BASE64_ENCODE_API_URL") + "?" + "message=";
		

		switch (hm.get("TestScenario")) {
		case "HEX":
			if ("Positive".equalsIgnoreCase(hm.get("ScenarioType"))) {
				// Call the rest API
				apiHexURL = apiHexURL + hm.get("I_Message");
				System.out.println(apiHexURL);
				Response resp = null;
				resp = RestAssured.get(apiHexURL);
				logger.info("Check the response status code. It should be 200");
				Assert.assertEquals(resp.getStatusCode(), 200);
				String responseString = resp.asString();
				JsonPath jsonPath = new JsonPath(responseString);
				logger.info(responseString);
				logger.info(jsonPath.getString("result"));
				Assert.assertEquals(jsonPath.getString("result"), "hash calculated");
				logger.info(jsonPath.getString("message"));
				Assert.assertEquals(jsonPath.getString("message"), hm.get("I_Message"));
				Assert.assertNotNull(jsonPath.getString("hex"));
				Assert.assertEquals(jsonPath.getString("hex"), hm.get("O_Expected"));
				Assert.assertNotNull(jsonPath.getString("tag"));

			} else {
				// Call the rest API
				//apiHexURL = apiHexURL + " ";
				System.out.println(apiHexURL);
				Response resp = null;
				resp = RestAssured.get(apiHexURL);
				logger.info("Check the response status code. It should be 200");
				Assert.assertEquals(resp.getStatusCode(), 200);
				String responseString = resp.asString();
				JsonPath jsonPath = new JsonPath(responseString);
				logger.info(responseString);
				logger.info(jsonPath.getString("error"));
				Assert.assertEquals(jsonPath.getString("error"), "hash needs message");
				Assert.assertNotNull(jsonPath.getString("tag"));
			}

			break;
		case "UNHEX":
			if ("Positive".equalsIgnoreCase(hm.get("ScenarioType"))) {
				// Call the rest API
				apiUnhexURL = apiUnhexURL + hm.get("I_Message");
				System.out.println(apiUnhexURL);
				Response resp = null;
				resp = RestAssured.get(apiUnhexURL);
				logger.info("Check the response status code. It should be 200");
				Assert.assertEquals(resp.getStatusCode(), 200);
				String responseString = resp.asString();
				JsonPath jsonPath = new JsonPath(responseString);
				logger.info(responseString);
				logger.info(jsonPath.getString("result"));
				Assert.assertEquals(jsonPath.getString("result"), "hash calculated");
				logger.info(jsonPath.getString("message"));
				Assert.assertEquals(jsonPath.getString("message"), hm.get("I_Message"));
				Assert.assertNotNull(jsonPath.getString("unhex"));
				Assert.assertEquals(jsonPath.getString("unhex"), hm.get("O_Expected"));
				Assert.assertNotNull(jsonPath.getString("tag"));

			} else {
				

			}

			break;
		case "HEX_TO_UNHEX":
			if ("Positive".equalsIgnoreCase(hm.get("ScenarioType"))) {

			} else {

			}
			break;

		case "UNHEX_TO_HEX":
			if ("Positive".equalsIgnoreCase(hm.get("ScenarioType"))) {

			} else {

			}
			break;
			
		/*
		 * case "BASE64_ENCODE":
		 * if("Positive".equalsIgnoreCase(hm.get("ScenarioType"))){
		 * 
		 * try{ // Call the rest API apibase64encode = apibase64encode +
		 * hm.get("I_Message"); System.out.println(apibase64encode);
		 * Thread.sleep(5000); Response resp = RestAssured.get(apibase64encode);
		 * logger.info("Check the response status code. It should be 200");
		 * Assert.assertEquals(resp.getStatusCode(), 200); String responseString
		 * = resp.asString(); JsonPath jsonPath = new JsonPath(responseString);
		 * logger.info(responseString);
		 * logger.info(jsonPath.getString("result"));
		 * Assert.assertEquals(jsonPath.getString("result"), "hash calculated");
		 * logger.info(jsonPath.getString("message"));
		 * Assert.assertEquals(jsonPath.getString("message"),
		 * hm.get("I_Message"));
		 * Assert.assertNotNull(jsonPath.getString("base64_encode"));
		 * Assert.assertEquals(jsonPath.getString("base64_encode"),
		 * hm.get("O_Expected"));
		 * Assert.assertNotNull(jsonPath.getString("tag"));
		 * 
		 * }catch(Exception e){
		 * 
		 * e.printStackTrace(); }
		 * 
		 * } else {
		 * 
		 * 
		 * }
		 * 
		 * break;
		 */

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
