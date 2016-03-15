package com.supernet.api.hmac;

import java.util.HashMap;

import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;
import com.supernet.api.BaseTestClass;
import com.supernet.api.utility.ReadExcel;
import com.supernet.api.utility.ReporterUtil;

public class Tiger192HMac extends BaseTestClass {
	public ReporterUtil reporter;
	private static final Logger logger = LoggerFactory.getLogger(ShaHmac.class);

	String className = this.getClass().getName();
	private String tiger192;
	

	@BeforeClass
	public void setPreconditions() {
		logger.info("In the class ---", className);
		reporter = getReporter(" HMac API - tiger192 method ", "Tests tiger192_Conversions method of hmac API ");

	}

	@DataProvider(name = "HMAC_TIGER_TestData")
	public static Object[][] testData() throws Exception {
		logger.info("In the method ");
		ReadExcel re = new ReadExcel(
				System.getProperty("user.dir") + BaseTestClass.globalConfig.getString("DATASHEET_CONFIG"),
				"sheetHmacTiger");
		return re.getTableToHashMapDoubleArray();
	}

	@Test(priority = 1, dataProvider = "HMAC_TIGER_TestData")
	public void HMAC_SHA_Conversions(HashMap<String, String> hm) throws JSONException {
		// Checking execution flag
		if (hm.get("ExecuteFlag").trim().equalsIgnoreCase("No")) {
			throw new SkipException("Skipping the test -->> As per excel entry");
		}
		tiger192 = globalConfig.getString("HMAC_TIGER192_API_URL");
		
		switch (hm.get("TestScenario").toUpperCase().trim()) {

		case "TIGER192":
			if ("Positive".equalsIgnoreCase(hm.get("ScenarioType"))) {
				// Call the rest API
				tiger192 = tiger192 + "?" + "message=" + hm.get("I_Message") + "&" + "passphrase=" + hm.get("I_passphrase");
				System.out.println(tiger192);
				// Thread.sleep(5000);

				Response resp = null;
				resp = RestAssured.get(tiger192);

				// Checking the Status code for the response
				logger.info("Check the response status code. It should be 200");
				Assert.assertEquals(resp.getStatusCode(), 200);

				String responseString = resp.asString();
				JsonPath jsonPath = new JsonPath(responseString);
				logger.info(responseString);

				// Checking the value of result key
				logger.info(jsonPath.getString("result"));
				Assert.assertEquals(jsonPath.getString("result"), "hmac calculated");

				// Checking the value of message key
				logger.info(jsonPath.getString("message"));
				Assert.assertEquals(jsonPath.getString("message"), hm.get("I_Message"));

				// Checking the encoded value for base64_encode key
				Assert.assertNotNull(jsonPath.getString("tiger"));
				// Assert.assertEquals(jsonPath.getString("tiger"),
				// hm.get("O_Expected"));

				// Checking whether the value of tag is not null
				Assert.assertNotNull(jsonPath.getString("tag"));
			}
			break;
		}
	}
}
