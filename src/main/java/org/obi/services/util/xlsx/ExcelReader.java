/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.obi.services.util.xlsx;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * ExcelWriter class
 *
 * @author r.hendrick
 */
public class ExcelReader {

    private final XSSFWorkbook workBook;
    FileInputStream fis;
    private Map<String, Integer> nextRows = new HashMap<>();
    private String currentSheet;
    private boolean isSaved;

    public ExcelReader(String fileName) throws FileNotFoundException, IOException {
        File excel = new File(fileName);
        fis = new FileInputStream(excel);
        workBook = new XSSFWorkbook(fis);
    }

    // /////////////////////////////////////////////////////////////////////////
    //
    //
    // Container
    // 
    //
    // /////////////////////////////////////////////////////////////////////////
    public Object read(int row, int col) {
        XSSFSheet sheet = workBook.getSheetAt(0); //workBook.getSheet(currentSheet);
        XSSFRow rowAt = sheet.getRow(row);
        return rowAt.getCell(col).getStringCellValue();
    }

    public ArrayList<Object> read(int row) {
        XSSFSheet sheet = workBook.getSheetAt(0); //workBook.getSheet(currentSheet);
        XSSFRow rowAt = sheet.getRow(row);
        ArrayList<Object> rowObject = new ArrayList<>();
        if (rowAt != null) {
            for (int i = 0; i < rowAt.getLastCellNum(); i++) {
                rowObject.add(rowAt.getCell(i).getStringCellValue());
            }
        }
        return rowObject;
    }

    public void switchToSheet(String sheetName) {
        currentSheet = sheetName;
        if (workBook.getSheet(sheetName) == null) {
            workBook.createSheet(currentSheet);
            nextRows.put(currentSheet, 0);
        }
    }

    public void close() {
        try {
            // Close workbook, OutputStream and Excel file to prevent leak
            workBook.close();
            fis.close();
        } catch (IOException ex) {
            Logger.getLogger(ExcelReader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // /////////////////////////////////////////////////////////////////////////
    //
    //
    // Getter / Setter
    // 
    //
    // /////////////////////////////////////////////////////////////////////////
}
