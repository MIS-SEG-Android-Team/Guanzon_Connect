/*
 * Created by Android Team MIS-SEG Year 2021
 * Copyright (c) 2021. Guanzon Central Office
 * Guanzon Bldg., Perez Blvd., Dagupan City, Pangasinan 2400
 * Project name : GhostRider_Android
 * Module : GhostRider_Android.griderScanner
 * Electronic Personnel Access Control Security System
 * project file created : 4/24/21 3:19 PM
 * project file last modified : 4/24/21 3:18 PM
 */

package org.rmj.guanzongroup.useraccount.scanner.helpers;

import android.graphics.Bitmap;
import android.net.Uri;

import java.io.Serializable;

public class ScannerConstants implements Serializable {
    public static Bitmap selectedImageBitmap;
    public static Uri selectedImageUri;
    public static String cropText="Crop",backText="Close",
            imageError= "No image selected, please try again.",
            cropError="You have not selected a valid field. Please make corrections until the lines turn blue.";
    public static String cropColor="#6666ff",backColor="#ff0000",progressColor="#f88222";
    public static boolean saveStorage=false;
    public static String TransNox;
    public static String DocTransNox;
    public static String FileCode;
    public static String PhotoPath;

    public static String Folder;
    public static String SubFolder;
    public static String Usage;
    public static int EntryNox;
    public static String FileName;
    public static String FileDesc;
    public static double Latt;
    public static double Longi;
    public static String SourceCode;

}
