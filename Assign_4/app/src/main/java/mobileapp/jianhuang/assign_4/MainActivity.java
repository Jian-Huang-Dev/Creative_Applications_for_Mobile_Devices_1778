package mobileapp.jianhuang.assign_4;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity
        extends AppCompatActivity
        implements SwipeViews.SwipeViewsListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private WebView mBrowser;
    private EditText mUrlSearchBox;
    private Button mClearBtn, mPopulateDataBtn, mMoreInfoBtn;
    ProgressDialog mProgressDialog;
    private DBHelper mDataBase;
    private int mNumUpdated = 0;

    /**
     * The {@link ViewPager} that will host the section contents.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        mBrowser = (WebView) findViewById(R.id.webkit);
//        mBrowser.loadUrl(Helper.WEBURL);
        mDataBase = new DBHelper(this);

        mUrlSearchBox = (EditText) findViewById(R.id.urlSearchBox);
        mUrlSearchBox.setText(Helper.WEBURL);

        mClearBtn = (Button) findViewById(R.id.clearBtn);
        mPopulateDataBtn = (Button) findViewById(R.id.populateBtn);
        mMoreInfoBtn = (Button) findViewById(R.id.moreInfoBtn);


        // instantiate it within the onCreate method
        mProgressDialog = new ProgressDialog(MainActivity.this);
        mProgressDialog.setMessage("Downloading...");
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setProgressNumberFormat(null);
        mProgressDialog.setProgressPercentFormat(null);
        mProgressDialog.setCancelable(true);

        // if empty database, disable buttons
        if(mDataBase.getCursor().getCount() == 0) {
            mClearBtn.setClickable(false);
            mMoreInfoBtn.setClickable(false);
            mClearBtn.setAlpha(Helper.FADE_IN_INTENSITY);
            mMoreInfoBtn.setAlpha(Helper.FADE_IN_INTENSITY);
        }

        mClearBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mDataBase.deleteTable();

                mClearBtn.setAlpha(Helper.FADE_IN_INTENSITY);
                mClearBtn.setClickable(false);
                mMoreInfoBtn.setAlpha(Helper.FADE_IN_INTENSITY);
                mMoreInfoBtn.setClickable(false);


                Toast.makeText(getApplicationContext(),
                        "Cleared Database!",
                        Toast.LENGTH_SHORT).show();
            }
        });

        mPopulateDataBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // execute this when the downloader must be fired
                final DownloadTask downloadTask = new DownloadTask(MainActivity.this);
                downloadTask.execute(mUrlSearchBox.getText().toString());

                mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        downloadTask.cancel(true);
                    }
                });

                mMoreInfoBtn.setAlpha(Helper.NO_FADE);
                mMoreInfoBtn.setClickable(true);
                mClearBtn.setAlpha(Helper.NO_FADE);
                mClearBtn.setClickable(true);
            }
        });

        mMoreInfoBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SwipeViews swipeViewsFragment = new SwipeViews();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, swipeViewsFragment);
                transaction.addToBackStack(null); // add to back stack
                transaction.commit(); // Commit the transaction
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A subclass of AsyncTask to handle the dababase downloading and
     * retrieval process
     */
    private class DownloadTask extends AsyncTask<String, Integer, String> {

        private Context context;
        private PowerManager.WakeLock mWakeLock;

        public DownloadTask(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... sUrl) {
            ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
            InputStream input = null;
            OutputStream output = null;
            HttpURLConnection connection = null;
            try {
                URL url = new URL(sUrl[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                // expect HTTP 200 OK, so we don't mistakenly save error report
                // instead of the file
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return "Server returned HTTP " + connection.getResponseCode()
                            + " " + connection.getResponseMessage();
                }

                // this will be useful to display download percentage
                // might be -1: server did not report the length
                int fileLength = connection.getContentLength();

                // download the file
                input = connection.getInputStream();
                output = new FileOutputStream(Helper.getOutputMediaFile());

                byte data[] = new byte[Helper.READBYTE];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    // allow canceling with back button
                    if (isCancelled()) {
                        input.close();
                        return null;
                    }
                    total += count;
                    // publishing the progress....
                    if (fileLength > 0) {// only if total length is known
                        publishProgress((int) (total * 100 / fileLength));
                    }

                    byteOutputStream.write(data, 0, count);
                    output.write(data, 0, count);
                }
            } catch (Exception e) {
                return e.toString();
            } finally {
                try {
                    if (output != null) {
                        mNumUpdated = 0;
                        String string = new String(byteOutputStream.toByteArray());
                        String[] stringArry = string.split("\n");
                        // insert data into database
                        if (mDataBase.getCursor().getCount() == 0) {
                            for (int i = 0; i < stringArry.length; i += 3) {
                                    mNumUpdated++;
                                    Log.d("testtest", "ADDING NEW TABLE");
                                    mDataBase.insertInfo(stringArry[i], stringArry[i + 1], stringArry[i + 2]);
                            }
                        } else {
                            for (int i = 0; i < stringArry.length; i += 3) {
                                if (!Helper.isNameExist(mDataBase, stringArry[i])) {
                                    mNumUpdated++;
                                    Log.d("testtest", "ADDING " + stringArry[i]);
                                    mDataBase.insertInfo(stringArry[i], stringArry[i + 1], stringArry[i + 2]);
                                }
                            }
                        }

                        output.close();
                    }
                    if (input != null)
                        input.close();
                } catch (IOException ignored) {
                }

                if (connection != null)
                    connection.disconnect();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // take CPU lock to prevent CPU from going off if the user
            // presses the power button during download
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                    getClass().getName());
            mWakeLock.acquire();
            mProgressDialog.show();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            // if we get here, length is known, now set indeterminate to false
        }

        @Override
        protected void onPostExecute(String result) {
            mWakeLock.release();
            mProgressDialog.dismiss();
            if (result != null)
                Toast.makeText(context, "Download error: " + result, Toast.LENGTH_LONG).show();
            else
                Toast.makeText(context,"Populated Database, " +
                        mNumUpdated + " item(s) updated",
                        Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        viewPager.setCurrentItem(0);
    }

    public void disableBtn() {
        Log.d("cursor", "activitycalling");
        mClearBtn.setClickable(false);
        mMoreInfoBtn.setClickable(false);
        mClearBtn.setAlpha(Helper.FADE_IN_INTENSITY);
        mMoreInfoBtn.setAlpha(Helper.FADE_IN_INTENSITY);
    }
}
