package com.supernet.api.hmac;

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

public class ShaHmac extends BaseTestClass {

	public ReporterUtil reporter;
	private static final Logger logger = LoggerFactory.getLogger(ShaHmac.class);

	String className = this.getClass().getName();
	private String sha224, sha256, sha384, sha512, sha1;

	@BeforeClass
	public void setPreconditions() {
		logger.info("In the class ---", className);
		reporter = getReporter(" HMac API - sha224 method ", "Tests sha224_Conversions method of hmac API ");

	}

	@DataProvider(name = "HMAC_SHA_TestData")
	public static Object[][] testData() throws Exception {
		logger.info("In the method ");
		ReadExcel re = new ReadExcel(
				System.getProperty("user.dir") + BaseTestClass.globalConfig.getString("DATASHEET_CONFIG"),
				"sheetHmacSha");
		return re.getTableToHashMapDoubleArray();
	}

	@Test(priority = 1, dataProvider = "HMAC_SHA_TestData")
	public void HMAC_SHA_Conversions(HashMap<String, String> hm) throws JSONException {
		// Checking execution flag
		if (hm.get("ExecuteFlag").trim().equalsIgnoreCase("No")) {
			throw new SkipException("Skipping the test -->> As per excel entry");
		}
		sha224 = globalConfig.getString("HMAC_SHA224_API_URL");
		sha256 = globalConfig.getString("HMAC_SHA256_API_URL");
		sha384 = globalConfig.getString("HMAC_SHA384_API_URL");
		sha512 = globalConfig.getString("HMAC_SHA512_API_URL");
		sha1 = globalConfig.getString("HMAC_SHA1_API_URL");

		switch (hm.get("TestScenario").toUpperCase().trim()) {

		case "SHA224":
			if ("Positive".equalsIgnoreCase(hm.get("ScenarioType"))) {
				// http://127.0.0.1:7778/api/hmac/sha224?message={string}&passphrase={string}
				// Call the rest API
				sha224 = sha224 + "?" + "message=" + hm.get("I_Message") + "&" + "passphrase=" + hm.get("I_passphrase");
				System.out.println(sha224);
				// Thread.sleep(5000);

				Response resp = null;
				resp = RestAssured.get(sha224);

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
				Assert.assertNotNull(jsonPath.getString("sha224"));
				// Assert.assertEquals(jsonPath.getString("tiger"),
				// hm.get("O_Expected"));

				// Checking whether the value of tag is not null
				Assert.assertNotNull(jsonPath.getString("tag"));
			}
			break;

		case "SHA256":
			if ("Positive".equalsIgnoreCase(hm.get("ScenarioType"))) {
				// http://127.0.0.1:7778/api/hmac/sha224?message={string}&passphrase={string}
				// Call the rest API
				sha256 = sha256 + "?" + "message=" + hm.get("I_Message") + "&" + "passphrase=" + hm.get("I_passphrase");
				System.out.println(sha256);
				// Thread.sleep(5000);

				Response resp = null;
				resp = RestAssured.get(sha256);

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
				Assert.assertNotNull(jsonPath.getString("sha256"));
				// Assert.assertEquals(jsonPath.getString("tiger"),
				// hm.get("O_Expected"));

				// Checking whether the value of tag is not null
				Assert.assertNotNull(jsonPath.getString("tag"));
			}
			break;

		case "SHA384":
			if ("Positive".equalsIgnoreCase(hm.get("ScenarioType"))) {
				// http://127.0.0.1:7778/api/hmac/sha224?message={string}&passphrase={string}
				// Call the rest API
				sha384 = sha384 + "?" + "message=" + hm.get("I_Message") + "&" + "passphrase=" + hm.get("I_passphrase");
				System.out.println(sha384);
				// Thread.sleep(5000);

				Response resp = null;
				resp = RestAssured.get(sha384);

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
				Assert.assertNotNull(jsonPath.getString("sha384"));
				// Assert.assertEquals(jsonPath.getString("tiger"),
				// hm.get("O_Expected"));

				// Checking whether the value of tag is not null
				Assert.assertNotNull(jsonPath.getString("tag"));
			}
			break;

		case "SHA512":
			if ("Positive".equalsIgnoreCase(hm.get("ScenarioType"))) {
				// http://127.0.0.1:7778/api/hmac/sha224?message={string}&passphrase={string}
				// Call the rest API
				sha512 = sha512 + "?" + "message=" + hm.get("I_Message") + "&" + "passphrase=" + hm.get("I_passphrase");
				System.out.println(sha512);
				// Thread.sleep(5000);

				Response resp = null;
				resp = RestAssured.get(sha512);

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
				Assert.assertNotNull(jsonPath.getString("sha512"));
				// Assert.assertEquals(jsonPath.getString("tiger"),
				// hm.get("O_Expected"));

				// Checking whether the value of tag is not null
				Assert.assertNotNull(jsonPath.getString("tag"));
			}
			break;

		case "SHA1":
			if ("Positive".equalsIgnoreCase(hm.get("ScenarioType"))) {
				// http://127.0.0.1:7778/api/hmac/sha224?message={string}&passphrase={string}
				// Call the rest API
				sha1 = sha1 + "?" + "message=" + hm.get("I_Message") + "&" + "passphrase=" + hm.get("I_passphrase");
				System.out.println(sha1);
				// Thread.sleep(5000);

				Response resp = null;
				resp = RestAssured.get(sha1);

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
				Assert.assertNotNull(jsonPath.getString("sha1"));
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
