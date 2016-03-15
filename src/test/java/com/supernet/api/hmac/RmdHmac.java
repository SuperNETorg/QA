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

public class RmdHmac extends BaseTestClass {
	public ReporterUtil reporter;
	private static final Logger logger = LoggerFactory.getLogger(RmdHmac.class);

	String className = this.getClass().getName();
	private String rmd128, rmd160, rmd256, rmd320;

	@BeforeClass
	public void setPreconditions() {
		logger.info("In the class ---", className);
		reporter = getReporter(" HMac API - sha224 method ", "Tests sha224_Conversions method of hmac API ");

	}

	@DataProvider(name = "HMAC_RMD_TestData")
	public static Object[][] testData() throws Exception {
		logger.info("In the method ");
		ReadExcel re = new ReadExcel(
				System.getProperty("user.dir") + BaseTestClass.globalConfig.getString("DATASHEET_CONFIG"),
				"sheetHmacRmd");
		return re.getTableToHashMapDoubleArray();
	}
	
	@Test(priority = 1, dataProvider = "HMAC_RMD_TestData")
	public void HMAC_SHA_Conversions(HashMap<String, String> hm) throws JSONException {
		// Checking execution flag
		if (hm.get("ExecuteFlag").trim().equalsIgnoreCase("No")) {
			throw new SkipException("Skipping the test -->> As per excel entry");
		}
		rmd128 = globalConfig.getString("HMAC_RMD128_API_URL");
		rmd160 = globalConfig.getString("HMAC_RMD160_API_URL");
		rmd256 = globalConfig.getString("HMAC_RMD256_API_URL");
		rmd320 = globalConfig.getString("HMAC_RMD320_API_URL");
		switch (hm.get("TestScenario").toUpperCase().trim()) {

		case "RMD128":
			if ("Positive".equalsIgnoreCase(hm.get("ScenarioType"))) {
				// http://127.0.0.1:7778/api/hmac/sha224?message={string}&passphrase={string}
				// Call the rest API
				rmd128 = rmd128 + "?" + "message=" + hm.get("I_Message") + "&" + "passphrase=" + hm.get("I_passphrase");
				System.out.println(rmd128);
				// Thread.sleep(5000);

				Response resp = null;
				resp = RestAssured.get(rmd128);

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
				Assert.assertNotNull(jsonPath.getString("rmd128"));
				// Assert.assertEquals(jsonPath.getString("tiger"),
				// hm.get("O_Expected"));

				// Checking whether the value of tag is not null
				Assert.assertNotNull(jsonPath.getString("tag"));
				
			}
			break;
		case "RMD160":
			if ("Positive".equalsIgnoreCase(hm.get("ScenarioType"))) {
				// http://127.0.0.1:7778/api/hmac/sha224?message={string}&passphrase={string}
				// Call the rest API
				rmd160 = rmd160 + "?" + "message=" + hm.get("I_Message") + "&" + "passphrase=" + hm.get("I_passphrase");
				System.out.println(rmd160);
				// Thread.sleep(5000);

				Response resp = null;
				resp = RestAssured.get(rmd160);

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
				Assert.assertNotNull(jsonPath.getString("rmd160"));
				// Assert.assertEquals(jsonPath.getString("tiger"),
				// hm.get("O_Expected"));

				// Checking whether the value of tag is not null
				Assert.assertNotNull(jsonPath.getString("tag"));
				
			}
			break;
		case "RMD256":
			if ("Positive".equalsIgnoreCase(hm.get("ScenarioType"))) {
				// http://127.0.0.1:7778/api/hmac/sha224?message={string}&passphrase={string}
				// Call the rest API
				rmd256 = rmd256 + "?" + "message=" + hm.get("I_Message") + "&" + "passphrase=" + hm.get("I_passphrase");
				System.out.println(rmd256);
				// Thread.sleep(5000);

				Response resp = null;
				resp = RestAssured.get(rmd256);

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
				Assert.assertNotNull(jsonPath.getString("rmd256"));
				// Assert.assertEquals(jsonPath.getString("tiger"),
				// hm.get("O_Expected"));

				// Checking whether the value of tag is not null
				Assert.assertNotNull(jsonPath.getString("tag"));
				
			}
			break;
			
		case "RMD320":
			if ("Positive".equalsIgnoreCase(hm.get("ScenarioType"))) {
				// http://127.0.0.1:7778/api/hmac/sha224?message={string}&passphrase={string}
				// Call the rest API
				rmd320 = rmd320 + "?" + "message=" + hm.get("I_Message") + "&" + "passphrase=" + hm.get("I_passphrase");
				System.out.println(rmd320);
				// Thread.sleep(5000);

				Response resp = null;
				resp = RestAssured.get(rmd320);

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
				Assert.assertNotNull(jsonPath.getString("rmd320"));
				// Assert.assertEquals(jsonPath.getString("tiger"),
				// hm.get("O_Expected"));

				// Checking whether the value of tag is not null
				Assert.assertNotNull(jsonPath.getString("tag"));
				
			}
			break;
			
		}
		

	}

}
