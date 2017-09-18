package com.automation.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtil {

    private static final Logger LOGGER = Logger.getLogger(ExcelUtil.class);

    @SuppressWarnings("deprecation")
    public static List<List<String>> readExcel(String fileName) {
        List<List<String>> results = new ArrayList<List<String>>();

        try {
            FileInputStream file = new FileInputStream(new File(fileName));
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            String firstSheet = workbook.getSheetName(0);// 1st sheet will store all test cases.

            XSSFSheet sheet = workbook.getSheet(firstSheet);
            int numberOfRow = sheet.getLastRowNum();
            int numberOfColumn = sheet.getRow(0).getLastCellNum();
            if (numberOfRow == 0 || numberOfColumn == 0) {
                file.close();
                workbook.close();
                return null;
            }

            Row row;
            for (int i = 1; i <= numberOfRow; i++) {
                row = sheet.getRow(i);
                if (row == null)
                    continue;
                List<String> testCase = new ArrayList<String>();
                for (int j = 0; j < numberOfColumn; j++) {
                    Cell cell = row.getCell(j);
                    if (cell == null) {
                        testCase.add("");
                    } else {
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        testCase.add(cell.getStringCellValue());
                    }
                }
                results.add(testCase);

            }

            file.close();
            workbook.close();
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }

        return results;
    }

    public static void updateExcel(List<List<String>> datas, String fileName) {
        try {
            FileInputStream file = new FileInputStream(new File(fileName));
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            String firstSheet = workbook.getSheetName(0);

            XSSFSheet sheet = workbook.getSheet(firstSheet);
            int numberOfRow = sheet.getLastRowNum();
            int numberOfColumn = sheet.getRow(0).getLastCellNum();
            Row row;
            for (int i = 1; i <= numberOfRow; i++) {
                row = sheet.getRow(i);
                if (row == null)
                    continue;
                List<String> data = datas.get(i - 1);
                for (int j = 0; j < numberOfColumn; j++) {
                    String cellValue = data.get(j);
                    Cell cell = row.getCell(j);
                    if (cell == null) {
                        row.createCell(j).setCellValue(cellValue);
                    } else {
                        cell.setCellValue(cellValue);
                    }
                }
            }

            FileOutputStream outputStream = new FileOutputStream(new File(fileName));
            workbook.write(outputStream);
            file.close();
            outputStream.close();
            workbook.close();
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }
    
    public static void createExcel(List<List<String>> datas, String fileTemplate, String outputFileName) {
        try {
            FileInputStream file = new FileInputStream(new File(fileTemplate));
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            String firstSheet = workbook.getSheetName(0);

            XSSFSheet sheet = workbook.getSheet(firstSheet);
            int numberOfRow = datas.size();
            int numberOfColumn = sheet.getRow(0).getLastCellNum();
            
            for (int i = 0; i < numberOfRow; i++) {
                List<String> data = datas.get(i);
                Row row = sheet.createRow(i + 1);
                for (int j = 0; j < numberOfColumn; j++) {
                    row.createCell(j).setCellValue(data.get(j));
                }
                
            }
            
            

            FileOutputStream outputStream = new FileOutputStream(new File(outputFileName));
            workbook.write(outputStream);
            file.close();
            outputStream.close();
            workbook.close();
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }

}
