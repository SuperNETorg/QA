package com.supernet.api;

import java.io.File;
import java.io.IOException;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.testng.Assert;
import org.testng.annotations.BeforeSuite;

import com.supernet.api.utility.ReporterUtil;



public class BaseTestClass {

	protected static PropertiesConfiguration globalConfig = null;
	protected static String authorization;// = "Bearer 2f16ff3ca5fbbf6240d1b9c20198004e41fc5a8271d82271f9179105f4ddd744";
	protected static String fileId = null;
	protected static String fileType = null;
	protected static String databaseId;
	protected static String datasourceId = "563768ee915819a9485e350c";
	protected static String workAreaId;
	protected static String textFileId;
	protected static String imageFileId;
	protected static String projectId;
	protected static String projectTitle;
	protected static String structuredWorkareaId ;//= "563924ec2631015828fdf752";
	protected static String unstructuredWorkAreaId ;
	protected static String emptyWorkAreaId;
	protected static String groupTitle;
	protected static String groupId ;//= "563b82e5d982b97b6a2b9981";
	protected static String roleId="563c4c06c611387a49e51f9a";
	protected static String userName;
	protected static String userId="563c657126f5e6c651dd06b1";
	protected static String userEmail;
	protected static String resetPasswordToken;
	protected static String password;

	@BeforeSuite
	public void initTest() {
		if ((System.getProperty("os.name")).contains("Windows")) {
			try {

				String propertiesFilePath = new File(".").getCanonicalPath() + File.separator + "config"
						+ File.separator + "globalConfig.properties";

				globalConfig = new PropertiesConfiguration(propertiesFilePath);
			} catch (ConfigurationException | IOException e) {
				System.out.println("Configuration exception occured in class " + this.getClass().getSimpleName());
				e.printStackTrace();
			}
		} else {
			try {

				String propertiesFilePath = new File(".").getCanonicalPath() + File.separator + "config"
						+ File.separator + "globalConfigNonWindows.properties";

				globalConfig = new PropertiesConfiguration(propertiesFilePath);
			} catch (ConfigurationException | IOException e) {
				System.out.println("Configuration exception occured in class " + this.getClass().getSimpleName());
				e.printStackTrace();
			}
		}
	}

	protected ReporterUtil getReporter(String testCaseName, String testCaseDescription) {
		ReporterUtil reporter = null;
		try {
			reporter = ReporterUtil.createReporter(testCaseName, testCaseDescription);
		} catch (IOException e) {
			/*
			 * logger.error("Failed to initialize the reporter for test '{}' : ",
			 * testCaseName, e);
			 */
			Assert.fail("Failed to start the test : " + e);
		}
		return reporter;
	}

}
