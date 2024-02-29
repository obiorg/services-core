/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.obi.services.util;

import java.awt.Image;
import java.net.URL;
import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * Icons class
 *
 * Icons is conveniens tools for all display function in order to reduce the
 * amount of repeating code
 *
 * @author r.hendrick
 */
public class Ico {

    /**
     * Returns an ImageIcon, or null if the path was invalid.
     *
     * @param path Path directory
     * @param description Description file
     * @return ImageIcon Image icon corresponding to path file image
     */
    public static ImageIcon load(String path, String description) {
        URL url = ClassLoader.getSystemClassLoader().getClass().getResource(path);
        if (url != null) {
            return new ImageIcon(url, description);
        } else {
            Util.out("Ico >> createImageIcon >> Couldn't find file : " + path);
            return null;
        }
    }

    /**
     * Returns an ImageIcon, or null if the path was invalid.
     *
     * @param path Path directory
     * @param description Description file
     * @param obj allow to find back resources
     * @return ImageIcon Image icon corresponding to path file image
     */
    public static ImageIcon load(String path, String description, Object obj) {
        URL url = obj.getClass().getResource(path);
        if (url != null) {
            return new ImageIcon(url, description);
        } else {
            Util.out("Ico >> createImageIcon >> Couldn't find file : " + path);
            return null;
        }
    }

    /**
     *
     * Returns an ImageIcon, or null if the path was invalid.
     *
     * @param path Path directory
     * @param description Description file
     * @return ImageIcon Image icon corresponding to path file image
     */
    public ImageIcon createImageIcon(String path, String description) {
        URL url = getClass().getResource(path);
        if (url != null) {
            return new ImageIcon(url, description);
        } else {
            Util.out("Ico >> createImageIcon >> Couldn't find file : " + path);
            return null;
        }
    }

    /**
     * Load image from path
     *
     * @param path full pathname of image
     * @return create an image ico
     */
    public static ImageIcon load(String path) {
        //System.out.println("Path on image load : " + path);
        //Util.out("Path on image load : " + path);
        return load(path, "");
    }

    /**
     * Load image from path
     *
     * @param path full pathname of image
     * @param obj allow to find back resources
     * @return create an image ico
     */
    public static ImageIcon load(String path, Object obj) {
        //System.out.println("Path on image load obj : " + path);
        //Util.out("Path on image load obj : " + path);
        return load(path, "", obj);
    }

    public static Image load(String path, Integer width, Integer height) {
        return load(path).getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
    }

    public static Image load(String path, Integer width, Integer height, Object obj) {
        return load(path, obj).getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
    }

    public static ImageIcon i16(String path) {
        return new ImageIcon(load(path, 16, 16));
    }

    public static ImageIcon i16(String path, Object obj) {
        return new ImageIcon(load(path, 16, 16, obj));
    }

    public static Icon i24(String path) {
        return new ImageIcon(load(path, 24, 24));
    }

    public static Icon i24(String path, Object obj) {
        return new ImageIcon(load(path, 24, 24, obj));
    }

    public static Icon i32(String path) {
        return new ImageIcon(load(path, 32, 32));
    }

    public static Icon i32(String path, Object obj) {
        return new ImageIcon(load(path, 32, 32, obj));
    }

    public static Icon i48(String path) {
        return new ImageIcon(load(path, 48, 48));
    }

    public static Icon i48(String path, Object obj) {
        return new ImageIcon(load(path, 48, 48, obj));
    }

}
