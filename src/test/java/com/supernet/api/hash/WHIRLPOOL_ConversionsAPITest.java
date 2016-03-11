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

public class WHIRLPOOL_ConversionsAPITest extends BaseTestClass {
	public ReporterUtil reporter;
	private static final Logger logger = LoggerFactory.getLogger(WHIRLPOOL_ConversionsAPITest.class);

	String className = this.getClass().getName();
	private String whirlpoolApi;

	@BeforeClass
	public void setPreconditions() {
		logger.info("In the class ---", className);
		reporter = getReporter(" Hash API - whirlpoolApi_Conversions method ",
				"Tests whirlpoolApi_Conversions method of hash API ");

	}

	@DataProvider(name = "whirlpoolApi_ConversionsTestData")
	public static Object[][] testData() throws Exception {
		logger.info("In the method ");
		ReadExcel re = new ReadExcel(
				System.getProperty("user.dir") + BaseTestClass.globalConfig.getString("DATASHEET_CONFIG"),
				"sheetWhirlpool_ConversionsTest");
		return re.getTableToHashMapDoubleArray();
	}

	@Test(priority = 1, dataProvider = "whirlpoolApi_ConversionsTestData")
	public void whirlpoolConversions(HashMap<String, String> hm) throws JSONException {
		// Checking execution flag
		if (hm.get("ExecuteFlag").trim().equalsIgnoreCase("No")) {
			throw new SkipException("Skipping the test -->> As per excel entry");
		}
		whirlpoolApi = globalConfig.getString("WHIRLPOOL_API_URL") + "?" + "message=";

		switch (hm.get("TestScenario").toUpperCase().trim()) {

		case "WHIRLPOOL":
			if ("Positive".equalsIgnoreCase(hm.get("ScenarioType"))) {
				// Call the rest API
				whirlpoolApi = whirlpoolApi + hm.get("I_Message");
				System.out.println(whirlpoolApi);
				// Thread.sleep(5000);

				Response resp = null;
				resp = RestAssured.get(whirlpoolApi);

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
				Assert.assertNotNull(jsonPath.getString("whirlpool"));
				// Assert.assertEquals(jsonPath.getString("tiger"),
				// hm.get("O_Expected"));

				// Checking whether the value of tag is not null
				Assert.assertNotNull(jsonPath.getString("tag"));
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
