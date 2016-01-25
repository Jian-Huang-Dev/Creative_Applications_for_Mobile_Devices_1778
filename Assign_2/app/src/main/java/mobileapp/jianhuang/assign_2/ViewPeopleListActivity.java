package mobileapp.jianhuang.assign_2;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;

/**
 * Created by jianhuang on 16-01-21.
 */
public class ViewPeopleListActivity extends Activity {

    private ListView people_list_view;
    static final String LINE_SEPRATOR = "\n\n";
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_people_list_activity);

        people_list_view = (ListView) findViewById(R.id.people_list_view);

        Bundle bundle = this.getIntent().getExtras();
        String[] saved_files = bundle.getStringArray(Integer.toString(R.string.saved_files));

        // display all files' people information in list view
        displayAllSavedFileDetails(saved_files);
    }

    /**
     * Display all files' people information
     * @param saved_files
     */
    private void displayAllSavedFileDetails(String[] saved_files) {
        String[] people_info_list_in_file;
        ArrayList<String> people_info_list_in_files = new ArrayList<String>();

        for (int i = 0; i < saved_files.length; i++) {
            // get all people's information from a file
            people_info_list_in_file =
                    helper.readFile(saved_files[i], getApplicationContext()).split(", ");

            for (int j = 0; j < people_info_list_in_file.length; j++) {
                // append each person's information into array list
                people_info_list_in_files.add(people_info_list_in_file[j]);
            }
        }
        // display them in list view
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.list_view_text_style, android.R.id.text1, people_info_list_in_files);
        people_list_view.setAdapter(adapter);
    }
}