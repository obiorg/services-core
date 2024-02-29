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

    /**
     * Excel Reader allow to specify {@code String} define in parameter Step is
     * to open File at pathfile name defined, than open as inputstream finaly
     * check worbook
     *
     * @param fileName full path name file and extension
     *
     * @throws FileNotFoundException Signals that an attempt to open the file
     * denoted by a specified pathname has failed. This exception will be thrown
     * by the FileInputStream, FileOutputStream, and RandomAccessFile
     * constructors when a file with the specified pathname does not exist. It
     * will also be thrown by these constructors if the file does exist but for
     * some reason is inaccessible, for example when an attempt is made to open
     * a read-only file for writing.
     *
     * @throws IOException Signals that an I/O exception of some sort has
     * occurred. This class is the general class of exceptions produced by
     * failed or interrupted I/O operations.
     */
    public ExcelReader(String fileName) throws FileNotFoundException, IOException {
        File excel = new File(fileName);
        fis = new FileInputStream(excel);
        workBook = new XSSFWorkbook(fis);
    }

    /**
     * Read allow to read a cell in sheet page define by {@code int row}
     * {@code int col}
     *
     * @param row number in a sheet of a woorkbook
     * @param col number in a sheet of a woorkbook ex: A is 1
     * @return object content define in the cell
     */
    public Object read(int row, int col) {
        XSSFSheet sheet = workBook.getSheetAt(0); //workBook.getSheet(currentSheet);
        XSSFRow rowAt = sheet.getRow(row);
        return rowAt.getCell(col).getStringCellValue();
    }

    /**
     * Read surcharge allow to read a full row
     *
     * @param row to be read completly
     * @return an array of cell int the row read or empty list if nothing
     *
     *
     */
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

    /**
     * Switch a sheet defined by {@code String} in a workbook
     *
     * @param sheetName name of the sheet to switch to
     */
    public void switchToSheet(String sheetName) {
        currentSheet = sheetName;
        if (workBook.getSheet(sheetName) == null) {
            workBook.createSheet(currentSheet);
            nextRows.put(currentSheet, 0);
        }
    }

    /**
     * Close the workbook defined
     */
    public void close() {
        try {
            // Close workbook, OutputStream and Excel file to prevent leak
            if (workBook != null) {
                workBook.close();
            }
            if (fis != null) {
                fis.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(ExcelReader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
