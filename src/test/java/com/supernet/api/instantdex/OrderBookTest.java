package com.supernet.api.instantdex;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.AssertJUnit;
import org.testng.SkipException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.google.common.base.Preconditions;
import com.supernet.api.BaseTestClass;
import com.supernet.api.client.instantdex.orderBookBean;
import com.supernet.api.utility.HTTPUtil;
import com.supernet.api.utility.HelperUtil;
import com.supernet.api.utility.ReadExcel;
import com.supernet.api.utility.ReporterUtil;

public class OrderBookTest extends BaseTestClass {

	public ReporterUtil reporter;
	private static final Logger logger = LoggerFactory.getLogger(OrderBookTest.class);

	String className = this.getClass().getName();
	private String appURL;

	String uniqueValue = HelperUtil.getUniqueValue();

	/**
	 * Setting up the precondition
	 */
	
	
	@BeforeClass
	public void setPreconditions() {
		logger.info("In the class ---> {}", className);
		reporter = getReporter("VerifytxTestTest", "Tests OrderBook API");
		}	
	

	@Test	
	@DataProvider(name = "orderBookTestData")
	public static Object[][] testData() throws Exception {
		logger.info("In the method {}");
		ReadExcel re = new ReadExcel(System.getProperty("user.dir")
				+ BaseTestClass.globalConfig.getString("DATASHEET_CONFIG"), "sheetOrderbookTest");
		return re.getTableToHashMapDoubleArray();
	}

	/**
	 * This method is used to test Response code of api
	 * 
	 * @param hm
	 */
	@Test(priority = 1, dataProvider = "orderBookTestData")
	public void testResponseCode(HashMap<String, String> hm) {

		// Checking execution flag
		if (hm.get("ExecuteFlag").trim().equalsIgnoreCase("No"))
			throw new SkipException("Skipping the test ---->> As per excel entry");

		
			appURL = globalConfig.getString("ORDERBOOK_API_CONFIG");


		 System.out.println("URL:"+ appURL);
			

		HashMap<String, String> headerParameters = new HashMap<String, String>();
		
			
			headerParameters.put("exchange", hm.get("I_Exchange"));				
			headerParameters.put("base", hm.get("I_Base"));
			headerParameters.put("rel", hm.get("I_Rel"));
			headerParameters.put("allfields", hm.get("I_Allfields"));
			headerParameters.put("ignore", hm.get("I_Ignore"));
		

		int responseCode = 0;
		logger.debug("requestURL  is getting sent as {}", appURL.toString());

		try {
			responseCode = HTTPUtil.sendGet(appURL, headerParameters);
			
			System.out.println("Response Code:" + responseCode);

			if ((responseCode == 200) || (responseCode == 404)) {
				try {

					String filePathOfJsonResponse = HelperUtil.createOutputDir(this.getClass().getSimpleName())
							+ File.separator + this.getClass().getSimpleName() + hm.get("SerialNo") + ".json";

					HTTPUtil.writeResponseToFile(filePathOfJsonResponse);

				} catch (UnsupportedOperationException e) {
					e.printStackTrace();
					reporter.writeLog("FAIL", "", "Caught Exception :" + e.getMessage());
					Assert.fail("Caught Exception ...");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			reporter.writeLog("FAIL", "", "Caught Exception :" + e.getMessage());
			Assert.fail("Caught Exception ..." + e.getMessage());
		}

		logger.debug("The response code is {}", Integer.valueOf(responseCode).toString());
		Preconditions.checkArgument(!hm.get("httpstatus").equals(null), "String httpstatus in excel must not be null");

		// Verify response code
		if (responseCode==200) {
			reporter.writeLog("PASS", "Status should be " + hm.get("httpstatus"), "Status is " + responseCode);
			AssertJUnit.assertEquals(responseCode, 200);
		} else {
			reporter.writeLog("FAIL", "Status should be " + hm.get("httpstatus"), "Status is " + responseCode);
			AssertJUnit.assertEquals(responseCode, Integer.parseInt(hm.get("httpstatus")));
		}

		System.out.println("-------------------------------------------");
	}

	/**
	 * This method is used to test content of response
	 * 
	 * @param hm
	 */
	@Test(priority = 2, dataProvider = "orderBookTestData")
	public void testContentJSONFileResponses(HashMap<String, String> hm) {

		// Checking execution flag
		if (hm.get("ExecuteFlag").trim().equalsIgnoreCase("No"))
			throw new SkipException("Skipping the test ---->> As per excel entry");

		Preconditions.checkArgument(hm != null, "The hash map parameter must not be null");

		String filePathOfJsonResponse = HelperUtil.createOutputDir(this.getClass().getSimpleName()) + File.separator
				+ this.getClass().getSimpleName() + hm.get("SerialNo") + ".json";

		switch (hm.get("httpstatus")) {
		case "200":
			try {
				orderBookBean getFieldsResponseBean = testAPIAttribute200Response(filePathOfJsonResponse);				

				if ((getFieldsResponseBean.getExchange()).equals(hm.get("I_Exchange"))){
					reporter.writeLog("PASS", "Exchange " + hm.get("I_Exchange"), "Exchange "
							+ getFieldsResponseBean.getExchange());
					AssertJUnit.assertEquals(getFieldsResponseBean.getExchange(), hm.get("I_Exchange"));
				} else {
					reporter.writeLog("FAIL", "Exchange Should be " + hm.get("I_Exchange"), "Exchange "
							+ getFieldsResponseBean.getExchange());
					AssertJUnit.assertEquals(getFieldsResponseBean.getExchange(), hm.get("I_Exchange"));
				}

			} catch (FileNotFoundException e) {
				e.printStackTrace();
				reporter.writeLog("FAIL", "", "Caught Exception : Response not stored in File");
				Assert.fail("Caught Exception : Response not stored in File ...");
			} catch (IOException e) {
				e.printStackTrace();
				reporter.writeLog("FAIL", "", "Caught Exception :" + e.getMessage());
				Assert.fail("Caught Exception ..." + e.getMessage());
			}
			break;

		default:
			logger.debug("Other than 200, 404 response --> {}", hm.get("httpstatus"));
			break;
		}

	}

	/**
	 * This method will return the response body for 200 status code.
	 * 
	 * @param jsonFileAbsPath
	 * @return response
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	@Test(enabled = false)
	private static orderBookBean testAPIAttribute200Response(String jsonFileAbsPath)
			throws JsonParseException, JsonMappingException, IOException {

		ObjectMapper objectMapper = new ObjectMapper();
		orderBookBean response200 = objectMapper.readValue(new File(jsonFileAbsPath),
				orderBookBean.class);
		return response200;

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