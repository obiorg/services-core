/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.obi.services.util.xlsx;

import java.io.File;
import javax.swing.filechooser.FileFilter;
import org.apache.commons.io.FilenameUtils;
import org.obi.services.util.Util;


/**
 * ExcelFilter class
 *
 * @author r.hendrick
 */
public class ExcelFilter extends FileFilter {

    // /////////////////////////////////////////////////////////////////////////
    //
    //
    // Container
    // 
    //
    // /////////////////////////////////////////////////////////////////////////
    /**
     * Accept all directory and file xlsx
     * @param f the file concern
     * @return 
     */
    @Override
    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }

        
        String extension = FilenameUtils.getExtension(f.getAbsolutePath());
        if (extension != null) {
            Util.out("Extension : " + extension);
            return extension.equals("xlsx");
        }

        return false;
    }

    //The description of this filter
    @Override
        public String getDescription() {
        return "Excel XLSX";
    }
    // /////////////////////////////////////////////////////////////////////////
    //
    //
    // Getter / Setter
    // 
    //
    // /////////////////////////////////////////////////////////////////////////

}
