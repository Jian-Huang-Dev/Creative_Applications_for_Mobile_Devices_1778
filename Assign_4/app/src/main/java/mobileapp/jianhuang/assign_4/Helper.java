package mobileapp.jianhuang.assign_4;

import android.database.Cursor;
import android.os.Environment;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by jianhuang on 16-02-13.
 */
public class Helper {
    public static final String STORAGE_PATH = Environment.getExternalStorageDirectory() + "/Assignment4/";
    public static final String IMAGE_STORAGE_PATH = Environment.getExternalStorageDirectory() + "/pics/";
    public static final String WEBURL = "http://www.eecg.utoronto.ca/~jayar/PeopleList.txt";
    public static final String IMAGE_FOLDER_WEBURL = "http://www.eecg.utoronto.ca/~jayar/";
    public static final int READBYTE = 1024;
    public static Cursor db_cs;
    public static final float FADE_IN_INTENSITY = 0.2f;
    public static final float NO_FADE = 1f;
    public static final String IMAGE_TYPE_JPG = "jpg";
    public static final String IMAGE_TYPE_TXT = "txt";
    public static final Integer ID_INDEX = 0;
    public static final Integer NAME_INDEX = 1;
    public static final Integer BIO_INDEX = 2;
    public static final Integer PATH_INDEX = 3;

    public static String getTxtFileName() {
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmmSS").format(new Date());
        String fileName = "db" + timeStamp + ".txt";
        return fileName;
    }

    public static File getOutputMediaFile(String fileName) {
        File mediaFile;
        File mediaStorageDir = new File(Helper.STORAGE_PATH);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }

        mediaFile = new File(mediaStorageDir.getPath() + File.separator + fileName);
        return mediaFile;
    }

    public static File getImageOutputMediaFile(String fileName) {
        File mediaFile;
        File mediaStorageDir = new File(Helper.IMAGE_STORAGE_PATH);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }

        mediaFile = new File(mediaStorageDir.getPath() + File.separator + fileName);
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

    public static int populateDatabase(DBHelper db, ByteArrayOutputStream byteOutputStream) {
        int num = 0;
        String string = new String(byteOutputStream.toByteArray());
        String[] stringArry = string.split("\n");

        // insert data into database
        if (db.getCursor().getCount() == 0) {
            for (int i = 0; i < stringArry.length; i += 3) {
                num++;
                Log.d("testtest", "ADDING NEW TABLE");
                db.insertInfo(stringArry[i],
                        stringArry[i + 1], stringArry[i + 2]);
            }
        } else {
            for (int i = 0; i < stringArry.length; i += 3) {
                if (!Helper.isNameExist(db, stringArry[i])) {
                    num++;
                    Log.d("testtest", "ADDING " + stringArry[i]);
                    db.insertInfo(stringArry[i],
                            stringArry[i + 1], stringArry[i + 2]);
                }
            }
        }
        return num;
    }

    public static String[] getAllImageNames(DBHelper db) {
        Cursor cs = db.getCursor();
        int count = cs.getCount();
        String[] strArr = new String[count];

        for(int i = 0; i < count; i++) {
            cs.moveToPosition(i);
            strArr[i] = cs.getString(Helper.PATH_INDEX).replace("pics/", "");
        }

        return strArr;
    }

    public static String[] getAllImageURLs(DBHelper db) {
        Cursor cs = db.getCursor();
        int count = cs.getCount();
        String[] strArr = new String[count];

        for(int i = 0; i < count; i++) {
            cs.moveToPosition(i);
            strArr[i] = Helper.IMAGE_FOLDER_WEBURL + cs.getString(Helper.PATH_INDEX);
        }

        return strArr;
    }
}
