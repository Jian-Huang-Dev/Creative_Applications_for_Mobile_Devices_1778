package mobileapp.jianhuang.assign_4;

import android.os.Environment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by jianhuang on 16-02-13.
 */
public class Helper {
    public static final String STORAGE_PATH = Environment.getExternalStorageDirectory() + "/Assignment4/";;
    public static String WEBURL = "http://www.eecg.utoronto.ca/~jayar/PeopleList.txt";

    public static String getFileName() {
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmmSS").format(new Date());
        String fileName = "db" + timeStamp + ".txt";
        return fileName;
    }

    public static File getOutputMediaFile() {
        File mediaFile;
        File mediaStorageDir = new File(Helper.STORAGE_PATH);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }

        mediaFile = new File(mediaStorageDir.getPath() + File.separator + getFileName());
        return mediaFile;
    }
}
