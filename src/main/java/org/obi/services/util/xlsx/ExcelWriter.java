/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.obi.services.util.xlsx;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * ExcelWriter class
 *
 * @author r.hendrick
 */
public class ExcelWriter {

    private final XSSFWorkbook workBook;

    private Map<String, Integer> nextRows = new HashMap<>();
    private String currentSheet;
    private boolean isSaved;

    public ExcelWriter() {
        workBook = new XSSFWorkbook();
    }

    // /////////////////////////////////////////////////////////////////////////
    //
    //
    // Container
    // 
    //
    // /////////////////////////////////////////////////////////////////////////
    public void writeAndClose(File excelFile) {
        if (isSaved) {
            throw new IllegalArgumentException("Workbook already saved!");
        }
        try {
            workBook.write(new FileOutputStream(excelFile));
            workBook.close();
            isSaved = true;
        } catch (IOException ioe) {
            // TODO log
        }
    }

    public void switchToSheet(String sheetName) {
        currentSheet = sheetName;
        if (workBook.getSheet(sheetName) == null) {
            workBook.createSheet(currentSheet);
            nextRows.put(currentSheet, 0);
        }
    }

    public void writeRow(String... values) {
        XSSFSheet sheet = workBook.getSheet(currentSheet);
        int nextRow = nextRows.get(currentSheet);
        XSSFRow row = sheet.createRow(nextRow);

        for (int i = 0; i < values.length; i++) {
            XSSFCell cell = row.createCell(i);
            cell.setCellType(CellType.STRING);
            cell.setCellValue(values[i]);
        }

        nextRows.put(currentSheet, nextRow + 1);
    }

    public void writeRow(ArrayList<String> values) {
        XSSFSheet sheet = workBook.getSheet(currentSheet);
        int nextRow = nextRows.get(currentSheet);
        XSSFRow row = sheet.createRow(nextRow);

        for (int i = 0; i < values.size(); i++) {
            XSSFCell cell = row.createCell(i);
            cell.setCellType(CellType.STRING);
            cell.setCellValue(values.get(i));
        }

        nextRows.put(currentSheet, nextRow + 1);
    }

    public void setCellColour(int rowNumber, int cellNumber,
            IndexedColors colour) {
        XSSFCellStyle style = workBook.createCellStyle();
        style.setFillForegroundColor(colour.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        int nextRow = nextRows.get(currentSheet);
        if (rowNumber > nextRow) {
            // TODO log or exception?
            rowNumber = nextRow;
        }
        XSSFSheet sheet = workBook.getSheet(currentSheet);
        int lastCell = sheet.getRow(rowNumber - 1).getLastCellNum();
        if (cellNumber > lastCell) {
            // TODO log or exception?
            cellNumber = lastCell;
        }

        sheet.getRow(rowNumber - 1).getCell(cellNumber - 1)
                .setCellStyle(style);
    }

    // /////////////////////////////////////////////////////////////////////////
    //
    //
    // Getter / Setter
    // 
    //
    // /////////////////////////////////////////////////////////////////////////
}
