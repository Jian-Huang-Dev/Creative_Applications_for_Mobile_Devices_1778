package mobileapp.jianhuang.assign_4;

import android.database.Cursor;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by jianhuang on 16-02-13.
 */
public class Helper {
    public static final String STORAGE_PATH = Environment.getExternalStorageDirectory() + "/Assignment4/";;
    public static String WEBURL = "http://www.eecg.utoronto.ca/~jayar/PeopleList.txt";
    public static int READBYTE = 1024;
    public static Cursor db_cs;
    public static float FADE_IN_INTENSITY = 0.2f;
    public static float NO_FADE = 1f;
    public static final Integer ID_INDEX = 0;
    public static final Integer NAME_INDEX = 1;
    public static final Integer BIO_INDEX = 2;
    public static final Integer PATH_INDEX = 3;

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

    public static boolean isNameExist(DBHelper dbHelper, String name) {
        Cursor cs = dbHelper.getCursor();
        cs.moveToPosition(-1);

        while(cs.moveToNext()) {
            String string = cs.getString(Helper.NAME_INDEX);
            Log.d("testtest-Helper", string);
            Log.d("testtest-HelperName", name);
            if(string.equals(name)) {
                Log.d("testtest-TRUE", "TRUE");
                return true;
            }
        }
        return false;
    }
}
