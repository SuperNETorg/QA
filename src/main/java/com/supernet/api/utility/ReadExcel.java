package com.supernet.api.utility;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.WorkbookFactory;
//import jxl.read.biff.BiffException;
import org.apache.poi.ss.usermodel.Cell;

public class ReadExcel {

	private Sheet dataSheet = null;

	public ReadExcel(String xlsfile) throws Exception {
		this(xlsfile, 1);
	}

	public ReadExcel(String xlsfile, String sheetName) throws Exception {
		InputStream inp = new FileInputStream(xlsfile);
		dataSheet = WorkbookFactory.create(inp).getSheet(sheetName);

	}

	/**
	 * Sheet number starts with 1
	 * 
	 * @param xlsfile
	 * @param sheetNumber
	 * @throws IndexOutOfBoundsException
	 * @throws BiffException
	 * @throws IOException
	 */
	public ReadExcel(String xlsfile, int sheetNumber) throws Exception {
		InputStream inp = new FileInputStream(xlsfile);
		dataSheet = WorkbookFactory.create(inp).getSheetAt(sheetNumber - 1);

	}

	/**
	 * Returns the column data which row contains 'RowValue'
	 * 
	 * @param RowValue
	 * @param ColumnName
	 * @return
	 * @throws BiffException
	 * @throws IOException
	 */
	public String getTestData(String RowValue, String ColumnName) throws Exception {
		int x = findColumnNumber(ColumnName);
		if (x == -1)
			return null;

		for (Row row : dataSheet) {
			if (getStringVal(row.getCell(0)).equalsIgnoreCase(RowValue)) {
				return getStringVal(row.getCell(x));
			}

		}

		return null;
	}

	public int findColumnNumber(String val) {
		Row row = dataSheet.getRow(0);
		for (Cell cell : row) {
			if (getStringVal(cell).equalsIgnoreCase(val))
				return cell.getColumnIndex();

		}
		return -1;

	}

	public HashMap<String, String> getRowData(String RowValue) throws Exception {
		HashMap<String, String> hm = null;
		Row r2 = null;
		for (Row row : dataSheet) {
			for (Cell cell : row)
				if (getStringVal(cell).equalsIgnoreCase(RowValue)) {
					r2 = row;
					break;
				}
		}
		if (r2 == null)
			return null;
		else {
			Row r1 = dataSheet.getRow(0);
			hm = new HashMap<String, String>();

			for (int i = 0; i <= r1.getLastCellNum() - 1; i++) {
				hm.put(getStringVal(r1.getCell(i)), getStringVal(r2.getCell(i)));
			}
		}

		return hm;

	}

	@SuppressWarnings("unchecked")
	public HashMap<String, String>[][] getTableToHashMapDoubleArray(int noOfRows) throws Exception {
		HashMap<String, String>[][] tabArray = null;
		HashMap<String, String>[] tabData = null;
		try {
			tabArray = new HashMap[noOfRows - 1][1];
			tabData = getTableToHashMapArray(noOfRows);
			for (int i = 0; i < noOfRows - 1; i++) {
				tabArray[i][0] = tabData[i];
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("error in getTableToHashMap()" + e.getMessage());
			throw new Exception("Error is parsing Excel file" + e.getMessage());
		}

		return (tabArray);
	}

	@SuppressWarnings("rawtypes")
	public HashMap[][] getTableToHashMapDoubleArray() throws Exception {
		try {

			return getTableToHashMapDoubleArray(dataSheet.getLastRowNum() + 1);
		} catch (Exception e) {
			e.printStackTrace();
			// logger.error("error in getTableToHashMap()" + e.getMessage());
			throw new Exception("Error is parsing Excel file" + e.getMessage());
		}

	}

	@SuppressWarnings("unchecked")
	public HashMap<String, String>[] getTableToHashMapArray(int noOfRows) throws Exception {
		HashMap<String, String>[] tabArray = null;
		int i = 1;
		try {
			int endCol, ci;

			endCol = dataSheet.getRow(0).getLastCellNum();
			tabArray = new HashMap[noOfRows - 1];
			ci = 0;
			System.out.println("No of rows processing is" + noOfRows);
			for (i = 1; i < noOfRows; i++, ci++) {
				HashMap<String, String> hm = new HashMap<String, String>();
				for (int j = 0; j < endCol; j++) {
					if (dataSheet.getRow(i) != null)
						hm.put(getStringVal(dataSheet.getRow(0).getCell(j)), getStringVal(dataSheet.getRow(i)
								.getCell(j)));
					else
						hm.put(getStringVal(dataSheet.getRow(0).getCell(j)), "");
				}
				tabArray[ci] = new HashMap<String, String>();
				tabArray[ci].putAll(hm);
			}
		} catch (Exception e) {
			e.printStackTrace();
			// logger.debug("Failed for the row "+i);
			// logger.error("error in getTableToHashMap()" + e.getMessage());
			throw new Exception("Error is parsing Excel file" + e.getMessage());
		}

		return (tabArray);
	}

	@SuppressWarnings("rawtypes")
	public HashMap[] getTableToHashMapArray() throws Exception {
		try {

			return getTableToHashMapArray(dataSheet.getLastRowNum() + 1);
		} catch (Exception e) {
			e.printStackTrace();
			// logger.info("error in getTableToHashMap()" + e.getMessage());
			throw new Exception("Error is parsing Excel file" + e.getMessage());
		}

	}

	/**
	 * rowNumber starts with 1
	 * 
	 * @param rowNumber
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public HashMap getRowData(int rowNumber) throws Exception {

		HashMap<String, String> hm = null;
		Row r1 = dataSheet.getRow(0);
		Row r2 = dataSheet.getRow(rowNumber - 1);
		hm = new HashMap<String, String>();

		for (int i = 0; i <= r1.getLastCellNum() - 1; i++) {
			hm.put(getStringVal(r1.getCell(i)), getStringVal(r2.getCell(i)));
		}

		return hm;

	}

	/**
	 * Whole table to Hashmap with key as first column value
	 * 
	 * @return
	 * @throws Exception
	 */
	public HashMap<String, HashMap<String, String>> putWholeTableToHashMap() throws Exception {

		HashMap<String, HashMap<String, String>> tabArray = new HashMap<String, HashMap<String, String>>();

		try {
			int endCol;
			int noOfRows = dataSheet.getLastRowNum() + 1;
			endCol = dataSheet.getRow(0).getLastCellNum();

			for (int i = 1; i < noOfRows; i++) {
				HashMap<String, String> hm = new HashMap<String, String>();

				for (int j = 0; j < endCol; j++) {
					hm.put(getStringVal(dataSheet.getRow(0).getCell(j)), getStringVal(dataSheet.getRow(i).getCell(j)));
				}
				tabArray.put(getStringVal(dataSheet.getRow(i).getCell(0)), hm);

			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("error in getTableToHashMap()" + e.getMessage());
			throw new Exception("Error is parsing Excel file" + e.getMessage());
		}

		return tabArray;
	}

	public String[][] getTableArray() throws Exception {
		try {
			return getTableArray(dataSheet.getLastRowNum() + 1);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("error in getTableArray()" + e.getMessage());
			throw new Exception("Error is parsing Excel file" + e.getMessage());
		}
	}

	public String[][] getTableArray(int noOfRowstoProcess) throws Exception {
		String[][] tabArray = null;
		try {

			int endRow, endCol, ci, cj;
			endRow = dataSheet.getLastRowNum() + 1;
			endCol = dataSheet.getRow(0).getLastCellNum();

			tabArray = new String[noOfRowstoProcess - 1][endCol];
			ci = 0;

			for (int i = 1; i < endRow; i++, ci++) {
				cj = 0;
				for (int j = 0; j < endCol; j++, cj++) {
					tabArray[ci][cj] = getStringVal(dataSheet.getRow(i).getCell(j));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("error in getTableArray()" + e.getMessage());
			throw new Exception("Error is parsing Excel file" + e.getMessage());
		}

		return (tabArray);
	}

	public String getStringVal(Cell c) {
		if (c == null)
			return "";
		else {

			if (c.getCellType() == Cell.CELL_TYPE_BLANK)
				return "";
			else if (c.getCellType() == Cell.CELL_TYPE_BOOLEAN)
				return Boolean.toString(c.getBooleanCellValue());
			else if (c.getCellType() == Cell.CELL_TYPE_NUMERIC)
				return Double.toString(c.getNumericCellValue());
			else
				return c.getStringCellValue();
		}

	}

}
