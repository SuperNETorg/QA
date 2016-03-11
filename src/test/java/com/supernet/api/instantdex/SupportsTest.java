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
import org.testng.asserts.SoftAssert;

import com.google.common.base.Preconditions;
import com.supernet.api.BaseTestClass;
import com.supernet.api.client.instantdex.SupportsBean;
import com.supernet.api.utility.HTTPUtil;
import com.supernet.api.utility.HelperUtil;
import com.supernet.api.utility.ReadExcel;
import com.supernet.api.utility.ReporterUtil;

public class SupportsTest extends BaseTestClass {

	public ReporterUtil reporter;
	private static final Logger logger = LoggerFactory.getLogger(SupportsTest.class);

	String className = this.getClass().getName();
	private String appURL;

	String uniqueValue = HelperUtil.getUniqueValue();
	
	SoftAssert softAssert=new SoftAssert();

	/**
	 * Setting up the precondition
	 */

	@BeforeClass
	public void setPreconditions() {
		logger.info("In the class ---", className);
		reporter = getReporter(" Instadex API - Supports method ", "Tests Supports method of Instadex API ");
	}

	@DataProvider(name = "orderBookTestData")
	public static Object[][] testData() throws Exception {
		logger.info("In the method ");
		ReadExcel re = new ReadExcel(
				System.getProperty("user.dir") + BaseTestClass.globalConfig.getString("DATASHEET_CONFIG"),
				"sheetSupports");
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
			throw new SkipException("Skipping the test -->> As per excel entry");

		appURL = globalConfig.getString("SUPPORTS_API_CONFIG") + "?" + "exchange=" + hm.get("I_Exchange") + "&"
				+ "base=" + hm.get("I_Base") + "&" + "rel=" + hm.get("I_Rel");

		int responseCode = 0;
		
		logger.debug("request URI is  sent as --" + appURL.toString());
		reporter.writeLog("Request" , "Is being sent as", appURL.toString());

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

		logger.debug("The response code is " + responseCode);
		Preconditions.checkArgument(!hm.get("httpstatus").equals(null), "String httpstatus in excel must not be null");

		// Verify response code
		
		System.out.println("A"+Integer.parseInt(hm.get("httpstatus")));
		System.out.println("B is"+responseCode);

		try {
			if (responseCode == Integer.parseInt(hm.get("httpstatus"))) {
				reporter.writeLog("PASS", "Status should be " + (hm.get("httpstatus")), "Status is " + responseCode);
				System.out.println("Yes its here");
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
				
				SupportsBean getFieldsResponseBean = testAPIAttribute200Response(filePathOfJsonResponse);	
				
				reporter.writeLog("verifying", "Json response", "By parsing");
				// If error response, Verify error response is same
				
				if(getFieldsResponseBean.getError()!=null){
				Assert.assertEquals(getFieldsResponseBean.getError(),hm.get("Error"),
						"The Error response is differnt : The Error should be"+hm.get("Error")+
						"But the Error is "+getFieldsResponseBean.getError());	
				break;
				}									
			
				// Result
				Assert.assertTrue(getFieldsResponseBean.getResult().equals(hm.get("Result")), 
						"Result shuld be "+hm.get("Result")+"But the result is "+getFieldsResponseBean.getResult());				
				
				
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
	
	private static SupportsBean testAPIAttribute200Response(String jsonFileAbsPath)
			throws JsonParseException, JsonMappingException, IOException {

		ObjectMapper objectMapper = new ObjectMapper();
		SupportsBean response200 = objectMapper.readValue(new File(jsonFileAbsPath), SupportsBean.class);
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
