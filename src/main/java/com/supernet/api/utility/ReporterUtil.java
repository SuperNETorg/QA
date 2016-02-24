package com.supernet.api.utility;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ReporterUtil {
	// private static Logger logger = LoggerFactory.getLogger(ReporterUtil.class
	// .getName());

	private static final String PATH_SEPARATOR = File.separator;
	private static final String REPORT_PATH = "reports";
	private static final String DATE_FORMAT_NOW = "yyyy-MM-dd";
	private static final String TIME_FORMAT_NOW = "HH:mm:ss";
	private static final String DATE_FORMAT_FOR_FOLDER = "yy-MMM-dd_HH-mm-ss";

	private final String testCaseName;
	private final String testDescription;
	private BufferedWriter bufferedWriter;
	private FileWriter fileWriter;
	private int stepNo;
	// private boolean testFailed = false;
	private String htmlReportFileName;
	// private long testStartTime;
	// private long testEndTime;

	private static final String reportDirPath;

	static {
		reportDirPath = createReportsDir();
	}

	public ReporterUtil(String testCaseName, String testDescription)
			throws IOException {
		this.testCaseName = testCaseName;
		this.testDescription = testDescription;
		createDetailedReportHeader();
		// testStartTime = Calendar.getInstance().getTimeInMillis();
	}

	public synchronized static ReporterUtil createReporter(String testCaseName,
			String testDescription) throws IOException {
		ReporterUtil reporter = new ReporterUtil(testCaseName, testDescription);
		return reporter;
	}

	public void flush() {
		try {
			bufferedWriter.flush();
		} catch (IOException e) {
			System.out.println("Exception during flushing the report writer : "
					+ e.getMessage());
		}
	}

	public void writeLog(String strStatus, String expectedResultMsg,
			String actualResultMsg) {

		String strBackgroundColor = null;
		String strStatusColumn = "";
		String link = "";
		strStatus = strStatus.toUpperCase().trim();

		switch (strStatus) {
		case "PASS":
			strBackgroundColor = "#00FF00";
			strStatusColumn = "<td width=75 align=\"center\" bgcolor="
					+ strBackgroundColor
					+ "><FONT FACE=\"Arial\" SIZE=2>PASS</td>";
			break;
		case "RUNNING":
			strBackgroundColor = "#00FF00";
			strStatusColumn = "<td width=75 align=\"center\" bgcolor="
					+ strBackgroundColor
					+ "><FONT FACE=\"Arial\" SIZE=2>RUNNING</td>";
			break;
		case "FAIL":
			strBackgroundColor = "#FF0000";
			strStatusColumn = "<td width=75 align=\"center\" bgcolor="
					+ strBackgroundColor
					+ "><FONT FACE=\"Arial\" SIZE=2>FAIL</td>";

			break;
		case "DONE":
			strBackgroundColor = "#BEBEBE";
			strStatusColumn = "<td width=75 align=\"center\" bgcolor="
					+ strBackgroundColor
					+ "><FONT FACE=\"Arial\" SIZE=2>DONE</a></td>";
			break;
		case "WARNING":
			strBackgroundColor = "#FFFF00";
			strStatusColumn = "<td width=75 align=\"center\" bgcolor="
					+ strBackgroundColor + "><FONT FACE=\"Arial\" SIZE=2>";

			break;
		case "ABORTED":
			strBackgroundColor = "#FF8000";
			strStatusColumn = "<td width=75 align=\"center\" bgcolor="
					+ strBackgroundColor + "><FONT FACE=\"Arial\" SIZE=2>";

			break;
		case "START":
			strBackgroundColor = "#819FF7";
			strStatusColumn = "<td width=75 align=\"center\" bgcolor="
					+ strBackgroundColor
					+ "><FONT FACE=\"Arial\" SIZE=2>START</a></td>";
			break;
		case "SKIPPED":
			strBackgroundColor = "#FFFF00";
			strStatusColumn = "<td width=75 align=\"center\" bgcolor="
					+ strBackgroundColor
					+ "><FONT FACE=\"Arial\" SIZE=2>SKIPPED</a></td>";
			break;

		}
		try {
			String expectedValue = expectedResultMsg;
			String actualValue = actualResultMsg;
			if (expectedValue.length() > 300
					&& (!expectedValue.toLowerCase().contains("tooltip.show"))) {
				String moreInfo = "<input type=\"button\" value =\"More Info\" onclick=\"tooltip.show('";
				String innerText = expectedValue.replaceAll("\"", "&quot;")
						.replaceAll("'", "&quot;").replaceAll("\n", "<BR>");
				moreInfo = moreInfo + innerText + "');\" />";
				expectedValue = expectedValue.substring(0, 200) + "... "
						+ "<BR>" + moreInfo;

			}
			if (actualValue.length() > 300
					&& (!actualValue.toLowerCase().contains("tooltip.show"))) {
				String moreInfo = "<input type=\"button\" value =\"More Info\" onclick=\"tooltip.show('";
				String innerText = actualValue.replaceAll("\"", "&quot;")
						.replaceAll("'", "&quot;").replaceAll("\n", "<BR>");
				moreInfo = moreInfo + innerText + "');\" />";
				actualValue = actualValue.substring(0, 200) + "... " + "<BR>"
						+ moreInfo;
			}

			bufferedWriter
					.write("<tr><td width=80 align=\"center\" bgcolor=\"#eeeee0\"><FONT FACE=\"Arial\" SIZE=2>"
							+ (++stepNo) + "</td>");
			bufferedWriter.write(strStatusColumn);

			switch (strStatus) {
			case "DONE":
				bufferedWriter
						.write("<td width=600 align=\"left\" bgcolor=\"#eeeee0\"><FONT FACE=\"Arial\" SIZE=2><b>"
								+ link + expectedValue + "</b></td>");
				bufferedWriter
						.write("<td width=600 align=\"left\" bgcolor=\"#eeeee0\"><FONT FACE=\"Arial\" SIZE=2><b>"
								+ actualValue + "</b></td></tr>");

				break;
			case "START":
				bufferedWriter
						.write("<td width=600 align=\"left\" bgcolor=\"#eeeee0\"><FONT FACE=\"Arial\" SIZE=2><b>"
								+ link + expectedValue + "</b></td>");
				bufferedWriter
						.write("<td width=600 align=\"left\" bgcolor=\"#eeeee0\"><FONT FACE=\"Arial\" SIZE=2><b>"
								+ actualValue + "</b></td></tr>");
				break;
			case "ABORTED":
				bufferedWriter
						.write("<td width=600 align=\"left\" bgcolor=\"#FF8000\"><FONT FACE=\"Arial\" SIZE=2><b>"
								+ link + expectedValue + "</b></td>");
				bufferedWriter
						.write("<td width=600 align=\"left\" bgcolor=\"#FF8000\"><FONT FACE=\"Arial\" SIZE=2><b>"
								+ actualValue + "</b></td></tr>");
				break;

			default:
				bufferedWriter
						.write("<td width=600 align=\"left\" bgcolor=\"#eeeee0\"><FONT FACE=\"Arial\" SIZE=2>"
								+ link + expectedValue + "</td>");
				bufferedWriter
						.write("<td width=600 align=\"left\" bgcolor=\"#eeeee0\"><FONT FACE=\"Arial\" SIZE=2>"
								+ actualValue + "</td></tr>");
				break;
			}

			expectedResultMsg = expectedResultMsg.replace("</B>", "");
			expectedResultMsg = expectedResultMsg.replace("<B>", "");

			expectedResultMsg = expectedResultMsg.replace("</b>", "");
			expectedResultMsg = expectedResultMsg.replace("<b>", "");

			actualResultMsg = actualResultMsg.replace("</B>", "");
			actualResultMsg = actualResultMsg.replace("<B>", "");

			actualResultMsg = actualResultMsg.replace("</b>", "");
			actualResultMsg = actualResultMsg.replace("<b>", "");
			if (actualResultMsg.toLowerCase().contains("tooltip.show"))
				actualResultMsg = "";
		} catch (IOException io) {
			// logger.error(" IOException in writeLog method: \n", io);
			// closeReport();
		} catch (Exception e) {
			// logger.error(" Exception in writeLog method: \n", e);
			// closeReport();
		}

	}

	public static String createResultScreeshotDir(String reportDirPath) {
		reportDirPath += PATH_SEPARATOR + "screenshots";
		new File(reportDirPath).mkdirs();
		return reportDirPath;
	}

	public static String createReportsDir() {
		try {
			String path = getCurrentDir() + PATH_SEPARATOR + REPORT_PATH;
			path += PATH_SEPARATOR + "smoke-tests-"
					+ getCurrentTime(DATE_FORMAT_FOR_FOLDER);
			// logger.debug("Creating report dir : {}", path);
			File pathObj = new File(path);
			if (!pathObj.exists()) {
				pathObj.mkdirs();
			}
			return path;
		} catch (IOException e) {
			// logger.error("Failed to create Reports directory : ", e);
			return null;
		}
	}

	public static String getReportDirPath() {
		return reportDirPath;
	}

	public void closeReport() {
		try {
			// logger.info(" =>>closing report");
			bufferedWriter.close();
			fileWriter.close();
			// this.testEndTime = Calendar.getInstance().getTimeInMillis();
			// logger.info(" =>>closing report");
			// logger.info("End Time" + testEndTime);
		} catch (IOException io) {
			// logger.error(" IOException in closeReport method: \n", io);
		} catch (Exception e) {
			// logger.error(" Exception in closeReport method: \n", e);
		}
	}

	public static String getCurrentTime() {
		return getCurrentTime(TIME_FORMAT_NOW);
	}

	public static String getCurrentDate() {
		return getCurrentTime(DATE_FORMAT_NOW);
	}

	public static String getCurrentTime(final String timeFormat) {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(timeFormat);
		return sdf.format(cal.getTime());
	}

	public static String getCurrentDir() throws IOException {
		return new File(".").getCanonicalPath();
	}

	public String getHtmlReportFileName() {
		return this.htmlReportFileName;
	}

	private void createDetailedReportHeader() throws IOException {
		this.htmlReportFileName = reportDirPath + PATH_SEPARATOR + testCaseName
				+ ".html";
		File htmlFile = new File(this.htmlReportFileName);
		// logger.debug("Creating html file : {}", htmlFile);
		htmlFile.createNewFile();

		String scriptDate = getCurrentDate();
		String scriptStartTime = getCurrentTime();

		fileWriter = new FileWriter(htmlFile);
		bufferedWriter = new BufferedWriter(fileWriter);
		bufferedWriter
				.write(" <html><HEAD><TITLE>Detailed Test Results</TITLE><link rel=\"stylesheet\" type=\"text/css\" href=\"./../../lib/style.css\" /></HEAD>");
		bufferedWriter.write("<style type=\"text/css\"> ");
		bufferedWriter
				.write("table tr td, table tr th { font-size: 68%;}table.details tr th{ font-weight: bold;text-align:left; background:#a6caf0; }table.details tr td{ background:#eeeee0;}");
		bufferedWriter.write("</style>");
		bufferedWriter
				.write("<script type=\"text/javascript\" language=\"javascript\" src=\"./../../lib/script.js\"></script>");
		bufferedWriter
				.write("<body><h5 align=\"center\"><FONT FACE=\"Arial\"SIZE=4><b> Detailed Report - "
						+ testCaseName + "</b></h5>");
		bufferedWriter
				.write(" <table cellspacing=3 cellpadding=3   border=0> <tr>");
		bufferedWriter
				.write(" <h1> <FONT COLOR=\"660000\" FACE=\"Arial\" SIZE=3> Test Details :</h1>");
		bufferedWriter
				.write(" <td width=150 align=\"left\" bgcolor=\"#a6caf0\"><FONT FACE=\"Arial\" SIZE=2.75><b>Description</b></td>");
		bufferedWriter
				.write(" <td width=150 align=\"left\" bgcolor=\"#eeeee0\"><FONT FACE=\"Arial\" SIZE=2.75>"
						+ testDescription + "</td></tr>");
		bufferedWriter
				.write(" <td width=150 align=\"left\" bgcolor=\"#a6caf0\"><FONT FACE=\"Arial\" SIZE=2.75><b>Run Date</b></td>");
		bufferedWriter
				.write(" <td width=150 align=\"left\" bgcolor=\"#eeeee0\"><FONT FACE=\"Arial\" SIZE=2.75>"
						+ scriptDate + " " + scriptStartTime + "</td></tr>");
		bufferedWriter.write("</table>");
		bufferedWriter
				.write(" <h4> <FONT COLOR=\"660000\" FACE=\"Arial\" SIZE=3> Detailed Report :</h4><table border=0 cellspacing=3 cellpadding=3 ><tr>");
		bufferedWriter
				.write(" <th width=80  align=\"center\" bgcolor=\"#a6caf0\"><FONT FACE=\"Arial\" SIZE=2><b>Step#</b></th>");
		bufferedWriter
				.write(" <th width=75 align=\"center\" bgcolor=\"#a6caf0\"><FONT FACE=\"Arial\" SIZE=2><b>Status</b></th>");
		bufferedWriter
				.write(" <th width=600 align=\"center\" bgcolor=\"#a6caf0\"><FONT FACE=\"Arial\" SIZE=2><b>Expected Result</b></th>");
		bufferedWriter
				.write(" <th width=600 align=\"center\" bgcolor=\"#a6caf0\"><FONT FACE=\"Arial\" SIZE=2><b>Actual Result</th></tr>");
	}

}