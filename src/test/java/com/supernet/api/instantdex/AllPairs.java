package com.supernet.api.instantdex;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeClass;
import org.testng.asserts.SoftAssert;

import com.supernet.api.BaseTestClass;
import com.supernet.api.utility.HelperUtil;
import com.supernet.api.utility.ReporterUtil;

/**
 * Testing is NOT completed here. The coding shall be done later.
 * @author user
 *
 */

public class AllPairs extends BaseTestClass {
	
	public ReporterUtil reporter;
	private static final Logger logger = LoggerFactory.getLogger(OrderBookTest.class);

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
		reporter = getReporter(" Instadex API - AllPairs method ", "Tests AllPairs method of Instadex API ");
	}

}
