/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.obi.services.util;

import com.google.gson.Gson;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Instant;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 * Util
 * <p>
 * This class coverts
 * 
 *
 *
 * @author r.hendrick
 */
public class Util {

    /**
     * Nom du fichier de sortie de console
     */
    public static final String ISLogFilenamePath = "OBILog.txt";

   
    /**
     * Port utiliser lorsque un appareil n'a pas encore été découvert <br>
     * Valeur par défaut : 54321
     */
    public static final Integer portDiscover = 54321;
    /**
     * Por utiliser lorsque l'appareil a été découvert <br>
     * Valeur par défaut : 55555
     */
    public static final Integer portAdopted = 55555;

    
    //
    public static final String keyPrefix = "0x";
    public static final String keyQuit = "0x0001";
    public static final String keyReboot = "0x0002"; // demande à l'application de redémarrer
    public static final String keyAdoptionRequest = "0x0003";
    public static final String keyAdoptionAccepted = "0x0004";
    public static final String keyAdoptionRejected = "0x0005"; // Mean already adopted
    public static final String keyAdoptionRelease = "0x0006"; // demande de libérer l'adoption
    public static final String keyAdoptionReleaseAccepted = "0x0007";
    public static final String keyAdoptionReleaseRejected = "0x0008";

    // Application
    public static final String keyAppInfos = "0x0010";
    public static final String keyAppFlushLog = "0x0011";
    public static final String keyAppFlushIni = "0x0012";
    public static final String keyApp = "0x0013";
    public static final String keyAppInfos02 = "0x0014";
    public static final String keyAppInfos03 = "0x0015";
    public static final String keyAppInfos04 = "0x0016";
    public static final String keyAppInfos05 = "0x0017";
    public static final String keyAppInfos06 = "0x0018";
    public static final String keyAppInfos07 = "0x0019";
    public static final String keyAppInfos08 = "0x001A";
    //
    public static final String keyImage = "0x0020";
    public static final String keyImageName = "0x0021";
    //

    //
    public static final String keyScreenInfos = "0x0050";
    public static final String keyScreenHostname = "0x0051";
    public static final String keyScreenCode = "0x0052";
    public static final String keyScreenGroup = "0x0053";
    public static final String keyScreenLocation = "0x0054";
    public static final String keyScreenMnemonique = "0x0055";
    public static final String keyScreenSelectInfos = "0x0056";
    public static final String keyScreenUpdateInfos = "0x0057";
    public static final String keyScreenDeleteInfos = "0x0058";
    public static final String keyScreenSpare04 = "0x0059";
    public static final String keyScreenSpare05 = "0x005A";

    public static final String keyBCast = "0xFFFF";

    public static final String msgImgWaitingForSrv = "rsc/is/Diapositive1.PNG";
    public static final String msgImgDisconnected = "rsc/is/Diapositive2.PNG";
    public static final String msgImgWaitingAdoption = "rsc/is/Diapositive3.PNG";
    public static final String msgImgErrorReadingFile = "rsc/is/Diapositive4.PNG";

    //
    //
    //
    //
    /**
     * Cette méthode affiche en sortie de console le message msg précéder de
     * l'instant de sortie. Ecrit également la sortie dans un fichier
     *
     * @param msg message to print out
     */
    public static void out(String msg) {
        String outMsg = Instant.now().toString() + " >> " + msg;
        System.out.println(outMsg);
        PrintWriter printOut;
        try {
            printOut = new PrintWriter(new FileWriter(ISLogFilenamePath, true), true);
        } catch (IOException ex) {
            Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
        printOut.write(outMsg + "\n");
        printOut.close();
    }

    /**
     * Cette méthode permet de récupérer la clé d'identification d'instruction
     * du string "packet". Elle récupére le début de la séquent de 0 à 6 soit
     * les 6 premières lettres.
     *
     * @param packet contenant la clé de la commande
     * @return key of the packet
     */
    public static String decodeKey(String packet) {
        return packet.subSequence(0, 6).toString();
    }

    /**
     * Cette méthode permet de récupérer le contenu d'un message en y éliminant
     * la clé.
     *
     * @param packet contenant le contenu
     * @return un objet contenant le contenu
     */
    public static Object decodeContent(String packet) {
        return packet.replaceFirst(decodeKey(packet), "");
    }

    //
    //
    //
    //
    public static Image urlImage(String urlPath) throws MalformedURLException, IOException {
        String path = urlPath; //"http://chart.finance.yahoo.com/z?s=GOOG&t=6m&q=l";
        System.out.println("Get Image from " + path);
        URL url = new URL(path);
        BufferedImage image = ImageIO.read(url);
        System.out.println("Load image into frame...");
        return image;
    }

    /**
     * This method resizes the given image using Image.SCALE_SMOOTH.
     *
     * @param image the image to be resized
     * @param width the desired width of the new image. Negative values force
     * the only constraint to be height.
     * @param height the desired height of the new image. Negative values force
     * the only constraint to be width.
     * @param max if true, sets the width and height as maximum heights and
     * widths, if false, they are minimums.
     * @return the resized image.
     */
    public static Image resizeImage(Image image, int width, int height, boolean max) {
        if (width < 0 && height > 0) {
            return resizeImageBy(image, height, false);
        } else if (width > 0 && height < 0) {
            return resizeImageBy(image, width, true);
        } else if (width < 0 && height < 0) {
            Util.out("Setting the image size to (width, height) of: ("
                    + width + ", " + height + ") effectively means \"do nothing\"... Returning original image");
            return image;
            //alternatively you can use System.err.println("");
            //or you could just ignore this case
        }
        int currentHeight = image.getHeight(null);
        int currentWidth = image.getWidth(null);
        int expectedWidth = (height * currentWidth) / currentHeight;
        //Size will be set to the height
        //unless the expectedWidth is greater than the width and the constraint is maximum
        //or the expectedWidth is less than the width and the constraint is minimum
        int size = height;
        if (max && expectedWidth > width) {
            size = width;
        } else if (!max && expectedWidth < width) {
            size = width;
        }
        return resizeImageBy(image, size, (size == width));
    }

    /**
     * Resizes the given image using Image.SCALE_SMOOTH.
     *
     * @param image the image to be resized
     * @param size the size to resize the width/height by (see setWidth)
     * @param setWidth whether the size applies to the height or to the width
     * @return the resized image
     */
    public static Image resizeImageBy(Image image, int size, boolean setWidth) {
        if (setWidth) {
            return image.getScaledInstance(size, -1, Image.SCALE_SMOOTH);
        } else {
            return image.getScaledInstance(-1, size, Image.SCALE_SMOOTH);
        }
    }

    public static String readFile(String filename) {
        String outFile = "";
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null) {
                outFile += sCurrentLine;
                //System.out.println(sCurrentLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outFile;
    }

    public static Boolean writeFile(String filename, String content) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            bw.write(content);

            // no need to close it.
            //bw.close();
            //System.out.println("Done");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Convert a class object to a string json that will be return
     *
     * @param object to be convert to a string data
     * @return a string of the object as json or null if unable to convert
     * object
     */
    public static String toJSONString(Object object) {
        if (object == null) {
            return null;
        }

        Gson gson = new Gson();
        //
        return gson.toJson(object);
    }

    /**
     * fromJSONString allow to take a defined object AS STRING and convert it as
     * a class object specified by objectClass
     *
     * @param objString string object corresponding to object
     * @param objectClass the class object to recreate
     * @return the object defined by objectClass from the string defined
     */
    public static Object fromJSONString(String objString, Class objectClass) {
        Gson gson = new Gson();
        Object obj = gson.fromJson(objString, objectClass);
        return obj;
    }
    
    

}
