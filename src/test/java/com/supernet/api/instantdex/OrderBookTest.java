package com.supernet.api.instantdex;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

import org.apache.http.conn.ConnectTimeoutException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
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
		logger.info("In the class ---", className);
		reporter = getReporter("Verify Orderbook mthod of Instadex API ", "Tests OrderBook mthod of Instadex API ");
	}

	@DataProvider(name = "orderBookTestData")
	public static Object[][] testData() throws Exception {
		logger.info("In the method {}");
		ReadExcel re = new ReadExcel(
				System.getProperty("user.dir") + BaseTestClass.globalConfig.getString("DATASHEET_CONFIG"),
				"sheetOrderbookTest");
		return re.getTableToHashMapDoubleArray();
	}

	/**
	 * This method is used to test Response code of api
	 * 
	 * @param hm
	 */
	@Test(priority = 1, dataProvider = "orderBookTestData")
	public void testResponseCode(HashMap<String, String> hm) throws ConnectTimeoutException {

		// Checking execution flag
		if (hm.get("ExecuteFlag").trim().equalsIgnoreCase("No"))
			throw new SkipException("Skipping the test ---->> As per excel entry");

		appURL = globalConfig.getString("ORDERBOOK_API_CONFIG") + "?" + "exchange=" + hm.get("I_Exchange") + "&"
				+ "base=" + hm.get("I_Base") + "&" + "rel=" + hm.get("I_Rel") + "&" + "allfields="
				+ hm.get("I_Allfields") + "&" + "ignore=" + hm.get("I_Ignore");

		int responseCode = 0;
		logger.debug("requestURL  is getting sent as --" + appURL.toString());

		try {

			responseCode = HTTPUtil.sendGet(appURL);

			System.out.println("Response Code is :" + responseCode);

			if (responseCode == 200) {
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

		logger.debug("The response code is ", + responseCode);
		Preconditions.checkArgument(!hm.get("httpstatus").equals(null), "String httpstatus in excel must not be null");

		// Verify response code

		try {
			if (responseCode == Integer.parseInt(hm.get("httpstatus"))) {
				reporter.writeLog("PASS", "Status should be " + (hm.get("httpstatus")), "Status is " + responseCode);
				Assert.assertEquals(responseCode, (Integer.parseInt(hm.get("httpstatus"))));
			} else {
				reporter.writeLog("FAIL", "Status should be " + (hm.get("httpstatus")), "Status is " + responseCode);
				Assert.assertEquals(responseCode, (Integer.parseInt(hm.get("httpstatus"))));
			}
		} catch (Exception e) {
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

		switch ((hm.get("httpstatus"))) {
		case "200":
			try {
				
				orderBookBean getFieldsResponseBean = testAPIAttribute200Response(filePathOfJsonResponse);			
				
				if (getFieldsResponseBean.getExchange().equals(hm.get("I_Exchange"))&
						getFieldsResponseBean.getBase().equals(hm.get("I_Base"))&
						getFieldsResponseBean.getRel().equals(hm.get("I_Rel"))) {				
					reporter.writeLog("PASS ","Expected Exchange, Base, Rel are ","Same as Actual Exchange, Base, Rel");
//					reporter.writeLog("PASS", "Exchange Should be  " + hm.get("I_Exchange"),
//							"Exchange IS" + getFieldsResponseBean.getExchange());						

				} else {
					if (getFieldsResponseBean.getExchange().equals(hm.get("I_Exchange"))){
						Assert.assertEquals(getFieldsResponseBean.getExchange(), hm.get("I_Exchange"),"Exchange is Different");
						
					} else {
						if (getFieldsResponseBean.getBase().equals(hm.get("I_Base"))){
							Assert.assertEquals(getFieldsResponseBean.getBase(), hm.get("I_Base"), "The Base is Different");
							reporter.writeLog("FAIL", "Base Should be " + hm.get("I_Base"),
									"base is " + getFieldsResponseBean.getExchange());
						}
						else{
							Assert.assertEquals(getFieldsResponseBean.getRel(), hm.get("I_Rel"), "The Rel is Different");
							reporter.writeLog("FAIL", "Rel Should be " + hm.get("I_Rel"),
									"Rel is  " + getFieldsResponseBean.getRel());
						}
					}
					
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
	
	private static orderBookBean testAPIAttribute200Response(String jsonFileAbsPath)
			throws JsonParseException, JsonMappingException, IOException {

		ObjectMapper objectMapper = new ObjectMapper();
		orderBookBean response200 = objectMapper.readValue(new File(jsonFileAbsPath), orderBookBean.class);
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