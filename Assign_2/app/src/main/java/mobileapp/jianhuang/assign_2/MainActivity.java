package mobileapp.jianhuang.assign_2;

/**
 * The main activity
 */

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends android.support.v4.app.FragmentActivity
        implements EnterNamesFragment.EnterNamesDoneListener,
        MainScreenFragment.ButtonClickedListnener,
        LoadFragment.LoadBtnListener,
        StoreDialogFragment.DialogBtnClickedListener {

    public String[] saved_files;
    FileOutputStream outputStream;
    private ArrayList<String> people_info_list_in_file = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // load main screen fragment
        MainScreenFragment mainFragment = new MainScreenFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, mainFragment);
        transaction.addToBackStack(null); // add to back stack
        transaction.commit(); // Commit the transaction
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

        // noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /** This method is triggered when the "Done" button within
     * the enter-name fragment is clicked
     */
    public void enterNamesDoneBtnClicked(ArrayList arrayList) {
        // update the file
        people_info_list_in_file = arrayList;
    }

    /** This method is triggered when the "Load" button within
     * the main screen fragment is clicked
     * @param list_view
     */
    public void activityLoadBtnClicked(ListView list_view) {
        // retrieve all the saved files
        saved_files = getApplicationContext().fileList();

        // place those file_names in a list_view
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.list_view_text_style, android.R.id.text1, saved_files);
        list_view.setAdapter(adapter);
    }

    /** This method is triggered when "Confirm" button within
     * the dialog fragment is clicked
     * @param file_name
     */
    public void activityFileNameConfirmBtnClicked(String file_name) {
        writeToFile(people_info_list_in_file, file_name);
        people_info_list_in_file.clear();
    }

    /** This method is triggered when "View" button within
     * the main screen fragment is clicked
     */
    public void activityViewBtnClicked() {
        saved_files = getApplicationContext().fileList();

        // launch another activity
        Bundle bundle = new Bundle();
        bundle.putStringArray(Integer.toString(R.string.saved_files), saved_files);
        Intent intent = new Intent(this, ViewPeopleListActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    /** This method is triggered when "Exit" button within
     * the main screen fragment is clicked
     */
    public void activityExitBtnClicked() {
        // if there are some created data that has not yet stored
        if (!people_info_list_in_file.isEmpty()) {
            // show a toast warning message
            Toast.makeText(this, getString(R.string.backup_data_warning),
                    Toast.LENGTH_SHORT).show();
            // write the created data into a time-stamp file name
            writeToFile(people_info_list_in_file, helper.generateTimeStamp());
        }
        finish();
    }

    /** Get people infomation list within a file
     *
     * @return array_list
     */
    public ArrayList<String> getPeopleInfoListInFile() {
        return people_info_list_in_file;
    }

    /** Write ArrayList<Strin> into file with user defined file name
     *
     * @param string_array_list
     * @param file_name
     */
    public void writeToFile(ArrayList<String> string_array_list, String file_name) {
        String string_to_write;
        String full_file_name = file_name + getString(R.string.file_format);
        try {
            outputStream = getApplicationContext().openFileOutput(full_file_name, Context.MODE_PRIVATE);

            string_to_write = Arrays.toString(people_info_list_in_file.toArray())
                    .replace("[", "")
                    .replace("]", "");

            outputStream.write(string_to_write.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
