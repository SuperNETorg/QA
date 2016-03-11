package com.supernet.api.instantdex;

import static org.hamcrest.CoreMatchers.hasItems;

import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
//import org.skyscreamer.jsonassert.JSONAssert;
//import org.testng.Assert;
import org.testng.annotations.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;
import com.supernet.api.BaseTestClass;
import com.supernet.api.utility.ReporterUtil;

public class AllExchange extends BaseTestClass {

	public ReporterUtil reporter;
	private static final Logger logger = LoggerFactory.getLogger(OrderBookTest.class);

	String className = this.getClass().getName();
	private String appURL;

	@BeforeClass
	public void setPreconditions() {
		logger.info("In the class ---", className);
		reporter = getReporter(" Instadex API - All exchange method ", "Test the api lists all the exchanges ");
	}

	@Test
	public void getRequestFindAllExchange() throws JSONException {
		appURL = globalConfig.getString("ALLEXCHANGE_API_URL");
		// Call the rest API
		Response resp = RestAssured.get(appURL);
		RestAssured.given().contentType("ContentType.TEXT");
		logger.info("Check the response status code. It should be 200");
		Assert.assertEquals(resp.getStatusCode(), 200);

		// JSONArray jsonResponse = new JSONArray(resp.asString());
		System.out.println(resp.asString());
		JsonPath jsonPath = new JsonPath(resp.asString());
		System.out.println(jsonPath.getList("result"));
		RestAssured.expect().body("results",
				hasItems("truefx", "instaforex", "fxcm", "PAX", "nxtae", "bitcoin", "jumblr", "bitfinex", "huobi",
						"lakebtc", "quadriga", "okcoin", "poloniex", "bittrex", "btce", "btc38", "coinbase",
						"bitstamp"));

		// System.out.println(jsonPath.getString("result"));
		/*
		 * String actual = resp.asString(); String expected =
		 * "{id:1,name:\"Joe\",friends:[{id:2,name:\"Pat\",pets:[\"dog\"]},{id:3,name:\"Sue\",pets:[\"cat\",\"fish\"]}],pets:[]}";
		 * JSONAssert.assertEquals(expected, actual, false);
		 */

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