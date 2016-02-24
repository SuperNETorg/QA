package com.supernet.api.utility;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class FileUtil {
	public static final String PATH_SEPARATOR = File.separator;

	public static String getCurrentDir() throws IOException {
		return new File(".").getCanonicalPath();
	}

	/**
	 * Generates file name with '.jpg' extension using the method argument as
	 * keyword. FORMAT - YEAR-MONTH-DAY-HOUR-MIN-SEC-filename.jpg
	 * 
	 * @param filename
	 *            keyword to be used during creation of file
	 * @return file name
	 */
	public static String generateRandomImageFilename(String filename) {
		Calendar c = Calendar.getInstance();
		filename = "" + c.get(Calendar.YEAR) + "-" + c.get(Calendar.MONTH)
				+ "-" + c.get(Calendar.DAY_OF_MONTH) + "-"
				+ c.get(Calendar.HOUR_OF_DAY) + "-" + c.get(Calendar.MINUTE)
				+ "-" + c.get(Calendar.SECOND) + "-" + filename + ".jpg";
		return filename;
	}
	
	public static String generateRandomJsonFilename(String filename) {
		Calendar c = Calendar.getInstance();
		filename = "" + c.get(Calendar.YEAR) + "-" + c.get(Calendar.MONTH)
				+ "-" + c.get(Calendar.DAY_OF_MONTH) + "-"
				+ c.get(Calendar.HOUR_OF_DAY) + "-" + c.get(Calendar.MINUTE)
				+ "-" + c.get(Calendar.SECOND) + "-" + filename + ".json";
		return filename;
	}

	public static String getCurrentTime() {
		return getCurrentTime("HH:mm:ss");
	}

	public static String getCurrentDate() {
		return getCurrentTime("yyyy-MM-dd");
	}

	public static String getCurrentTime(final String timeFormat) {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(timeFormat);
		return sdf.format(cal.getTime());
	}

}
