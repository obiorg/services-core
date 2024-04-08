/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.obi.services.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.ini4j.Wini;

/**
 * Settings class allow to create a init file to save current date of
 * application
 *
 * @author r.hendrick
 */
public class Settings {

    /**
     * Specifiy the default init file name.
     */
    public static String iniFilename = "app";

    /**
     * Section of configuration
     */
    public static final String CONFIG = "CONFIG";
    public static final String COMPANY = "company";
    public static final String GMT = "gmt";
    public static final String URL_OPTI = "urloptimaint";
    public static final String URL_ZEN = "urlzenon";
    public static final String LINK_ZEN = "LINKZEN";

    public static final String URL_OBI = "urlobi";
    public static final String URL_DIMO = "urldimo";
    public static final String COUNTER = "COUNTER";

    public static final String LINK_LINK = "LINK";

    /**
     * Specify max size of a log file
     */
    public static final String LOG_FILE_SIZE_MB = "logFileSizeMB";
    /**
     * Specify max duration of storage in a directory of the backup log file
     */
    public static final String LOG_FILE_DURATION_J = "logFileDurationJ";
    /**
     * Specify the directory in which logs file are backup after duration saving
     *
     * @{link org.obi.services.util.Settings#LOG_FILE_SIZE_MB}
     */
    public static final String LOG_FILE_STORAGE_PATH = "logFileStoragePath";

    /**
     * Cette méthod permet de créer un fichier de préférence ini
     *
     * @param filePathName est une définition d'un fichier du type
     * c:\\newfile.txt
     * @return vrai si le fichier a été créé sinon faux
     */
    private static Boolean createIniFile(String filePathName) {
        String methodName = Settings.class.getSimpleName() + " : createIniFile(String) >> ";
        try {
            File file = new File(filePathName);
            if (file.createNewFile()) {
                Util.out(Util.errLine() + methodName + "Le fichier " + filePathName + " vient d'être créé !");
                return true;
            } else {
                Util.out(Util.errLine() + methodName + "Le fichier " + filePathName + " existe déjà !");
                return false;
            }
        } catch (IOException e) {
            Util.out(Util.errLine() + methodName + "Erreur suivante lors de la création du fichier : " + e.getMessage());
            return false;
        }
    }

    /**
     * Cette méthod permet de créer un fichier de préférence ini avec le nom de
     * fichier définit par iniFilename
     *
     *
     * @return vrai si le fichier a été créé ou si il existe déjà.
     */
    public static Boolean createIniFile() {
        return createIniFile(Settings.iniFilename);
    }

    /**
     * Ecriture d'une données dans le fichier init définit par iniFilename
     *
     * @param section définit le groupe de paramètre
     * @param param le nom du paramètre
     * @param value la valeur du paramètre
     */
    public static void write(String section, String param, Object value) {
        String methodName = Settings.class.getSimpleName() + " : write(section, param, value) >> ";
        try {
            Util.out(Util.errLine() + methodName + "Tentative d'écriture en section (" + section + ") paramètre " + param + " avec la valeur " + value);
            Wini ini = new Wini(new File(iniFilename));
            ini.put(section, param, value);
            ini.store();
        } catch (IOException ex) {
            Util.out(Util.errLine() + methodName + "...Erreur suivante est survenue : " + ex.getMessage());
        }
    }

    /**
     * Fonction de lecture du fichier définit par iniFilename
     *
     * @param section définition de la section concernant le paramètre
     * @param param paramètre à récupérer
     * @return renvoi de la valeur du paramètre spécifié
     */
    public static Object read(String section, String param) {
        String methodName = Settings.class.getSimpleName() + " : read(section, param, value) >> ";
        try {
            //Util.out(Util.errLine() + methodName + "Tentative de lecture de la section (" + section + ") paramètre " + param + "...");
            File f = new File(iniFilename);
            if (!f.exists()) {
                Util.out(Util.errLine() + methodName + " fichier n'existe pas impossible récupérer les paramètres !");
                return null;
            }
            Wini ini = new Wini(f);
            Object obj = ini.get(section, param);
            if (obj == null) {
                Util.out(Util.errLine() + methodName + " Attention section " + section + " avec le paramètre " + param
                        + " n'existe pas ");
            }
            //Util.out(Util.errLine() + methodName + "...Lecture réussie");
            return obj;
        } catch (IOException ex) {
            System.err.println(Util.errLine() + methodName + "...Erreur suivante est survenue pour (" + section + ", " + param + ") : " + ex.getMessage());
            Util.out(Util.errLine() + methodName + "...Erreur suivante est survenue pour (" + section + ", " + param + ") : " + ex.getMessage());
            return null;
        }
    }

    public static Object[] readLink(Integer row) {
        String methodName = Settings.class.getSimpleName() + " : read(section, param, value) >> ";
        try {
            Wini ini = new Wini(new File(iniFilename));
            ArrayList<Object> obj = new ArrayList<>();
            obj.add(row + 1);
            obj.add(ini.get(Settings.LINK_LINK + "\\" + row, "ztable"));
            obj.add(ini.get(Settings.LINK_LINK + "\\" + row, "variable"));
            obj.add(ini.get(Settings.LINK_LINK + "\\" + row, "name"));
            obj.add(ini.get(Settings.LINK_LINK + "\\" + row, "codeEqu"));
            obj.add(ini.get(Settings.LINK_LINK + "\\" + row, "equipement"));
            obj.add(ini.get(Settings.LINK_LINK + "\\" + row, "codeOrg"));
            obj.add(ini.get(Settings.LINK_LINK + "\\" + row, "organe"));
            obj.add(ini.get(Settings.LINK_LINK + "\\" + row, "unite"));
            obj.add(ini.get(Settings.LINK_LINK + "\\" + row, "commentaire"));
            obj.add(ini.get(Settings.LINK_LINK + "\\" + row, "autre"));
            obj.add(Boolean.valueOf(ini.get(Settings.LINK_LINK + "\\" + row, "state") == null ? "true" : ini.get(Settings.LINK_LINK + "\\" + row, "state")));
            return obj.toArray();
        } catch (IOException ex) {
            Util.out(Util.errLine() + methodName + "...Erreur suivante est survenue pour lecture du lien ligne " + row + " : " + ex.getMessage());
            return null;
        }
    }

    /**
     * Allow to read state of a specifed link defined by a row
     *
     * @param row specifying the number of the link
     * @return true if the link is enable
     */
    public static Boolean readLinkState(Integer row) {
        String methodName = Settings.class.getSimpleName() + " : read(section, param, value) >> ";
        try {
            Wini ini = new Wini(new File(iniFilename));
            String v = ini.get(Settings.LINK_LINK + "\\" + row, "state");
            Boolean r = Boolean.valueOf(v);
            return r;
        } catch (IOException ex) {
            Util.out(Util.errLine() + methodName + "...Erreur suivante est survenue pour lecture du lien ligne " + row + " : " + ex.getMessage());
            return false;
        }
    }

    /**
     * Création des paramètres de confirguration par défaut
     */
    public static void writeDefaultClientSetup() {
        try {
            Wini ini = new Wini(new File(iniFilename));
            ini.put(Settings.CONFIG, Settings.COMPANY, "11");
            ini.put(Settings.CONFIG, Settings.GMT, 3);

            ini.put(Settings.CONFIG, Settings.URL_OBI, "jdbc:sqlserver://INSTANCE\\BDD:1433;databaseName=imoka");
            ini.put(Settings.CONFIG, Settings.URL_DIMO, "jdbc:sqlserver:");
            ini.put(Settings.CONFIG, Settings.URL_ZEN, "jdbc:sqlserver:");

            ini.put(Settings.CONFIG, Settings.LOG_FILE_SIZE_MB, 1);
            ini.put(Settings.CONFIG, Settings.LOG_FILE_DURATION_J, 7);
            ini.put(Settings.CONFIG, Settings.LOG_FILE_STORAGE_PATH, "./logs");

            ini.store();
        } catch (IOException ex) {
            Logger.getLogger(Settings.class.getSimpleName()).log(Level.SEVERE, null, ex);
        }
    }

}
