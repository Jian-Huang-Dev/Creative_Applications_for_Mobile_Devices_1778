package mobileapp.jianhuang.assign_3;

import android.graphics.Bitmap;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by jianhuang on 16-01-28.
 */
public class Helper {
    public static int position;

    public static final String STORAGE_PATH = Environment.getExternalStorageDirectory() + "/DCIM/Camera/";

    public static ArrayList<Bitmap> imageList = null;

    public static int getFileLength() {
        File[] files = getFilesDirectory(STORAGE_PATH).listFiles();
        return files.length;
    }

    public static File[] getFiles() {
        return getFilesDirectory(STORAGE_PATH).listFiles();
    }

    public static File getFilesDirectory(String storage_path) {
        storage_path = STORAGE_PATH;
        File filesDirectory = new File(storage_path);
        return filesDirectory;
    }

    /**
     * Create a File for saving an image or video
     */
    public static File getOutputMediaFile() {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        File mediaStorageDir = new File(Helper.STORAGE_PATH);

        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmmSS").format(new Date());
        File mediaFile;
        String mImageName = "im" + timeStamp + ".jpg";
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
        return mediaFile;
    }

    public static void storeImage(Bitmap image) {
        File pictureFile = getOutputMediaFile();
        if (pictureFile == null) {
            return;
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.close();
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        }
    }

    public static String getFileName(int position) {
        File[] files = getFilesDirectory(STORAGE_PATH).listFiles();
        return files[position].getName();
    }

    public static void deleteFile(int pos) {
        File file = new File(new File(Helper.STORAGE_PATH), getFileName(pos));
        file.delete();
    }
}
