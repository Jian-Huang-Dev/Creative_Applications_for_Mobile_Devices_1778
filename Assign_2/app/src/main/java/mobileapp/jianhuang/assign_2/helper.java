package mobileapp.jianhuang.assign_2;

import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by jianhuang on 16-01-22.
 *
 * Includes some useful utilities
 */
public class helper {

    static InputMethodManager imm;
    static final String LINE_SEPRATOR = "##";

    /** Show soft keyboard when editText is focused
     *
     * @param activity
     * @param edit_text
     */
    public static void showSoftKeyboard(Activity activity, EditText edit_text) {

        imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(edit_text, InputMethodManager.SHOW_IMPLICIT);
    }

    /** Hide soft keyboard
     *
     * @param edit_text
     */
    public static void hideSoftKeyboard(EditText edit_text) {
        imm.hideSoftInputFromWindow(edit_text.getWindowToken(), 0);
    }

    /** Randomly generate file name based on current time stamp
     *
     */
    public static String generateTimeStamp() {
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("hhmmss");
        String currentDateTimeString = sdf.format(d);
        return currentDateTimeString;
    }
}
