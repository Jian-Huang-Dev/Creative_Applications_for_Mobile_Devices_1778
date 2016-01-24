package mobileapp.jianhuang.assign_2;

import android.app.Activity;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by jianhuang on 16-01-21.
 * Fragment for user entering names and ages
 */
public class EnterNamesFragment extends android.support.v4.app.Fragment {

    public EditText name_field, age_field;
    public Spinner movie_names_spinner;
    public Button done_btn, add_btn;
    public String people_detail_info;
    public ImageView image;
    public TypedArray images;
    private ArrayList<String> people_info_list_in_file = new ArrayList<>();
    private InputMethodManager imm;

    EnterNamesDoneListener activityCallback;

    /**
     * Interface listener
     */
    public interface EnterNamesDoneListener {
        public void enterNamesDoneBtnClicked(ArrayList arrayList);
        public ArrayList<String> getPeopleInfoListInFile();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            activityCallback = (EnterNamesDoneListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement interface methods");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.enter_names_fragment,
                container, false);

        name_field = (EditText) view.findViewById(R.id.name_field);
        age_field = (EditText) view.findViewById(R.id.age_field);
        movie_names_spinner = (Spinner) view.findViewById(R.id.movie_names_spinner);
        done_btn = (Button) view.findViewById(R.id.done_btn);
        add_btn = (Button) view.findViewById(R.id.add_btn);

        helper.showSoftKeyboard(getActivity(), name_field);
        helper.showSoftKeyboard(getActivity(), age_field);

        images = getResources().obtainTypedArray(R.array.movie_images);
        image = (ImageView) view.findViewById(R.id.image_view);

        final Spinner spinner = (Spinner) view.findViewById(R.id.movie_names_spinner);

        // set corresponding image for movie selected
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                image.setImageResource(images.getResourceId(spinner.getSelectedItemPosition(), -1));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        done_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                doneBtnClicked(v);
            }
        });

        add_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                addBtnClicked(v);
            }
        });

        return view;
    }

    public void addBtnClicked(View v) {
        String name_field_txt, age_field_txt;

        helper.hideSoftKeyboard(name_field);
        helper.hideSoftKeyboard(age_field);

        name_field_txt = name_field.getText().toString();
        age_field_txt = age_field.getText().toString();

        // only proceed when user entered data into both name_field and age_field
        if (TextUtils.isEmpty(name_field_txt) || TextUtils.isEmpty(age_field_txt)) {
            Toast.makeText(getActivity(), getString(R.string.empty_field_warning),
                    Toast.LENGTH_SHORT).show();
        }
        else {
            // get any previoius created but not yet saved data from main activity
            people_info_list_in_file = activityCallback.getPeopleInfoListInFile();

            // append current person's info into array list
            people_detail_info = getString(R.string.name_tag) +
                    name_field_txt + " - " +
                    getString(R.string.age_tag) +
                    age_field_txt + " - " +
                    getString(R.string.movie_tag) +
                    movie_names_spinner.getSelectedItem().toString();

            people_info_list_in_file.add(people_detail_info);

            // reset those two fields
            name_field.setText("");
            age_field.setText("");
        }
    }

    public void doneBtnClicked(View v) {
        helper.hideSoftKeyboard(name_field);
        helper.hideSoftKeyboard(age_field);

        // communicate back to the main activity
        activityCallback.enterNamesDoneBtnClicked(people_info_list_in_file);
        getFragmentManager().popBackStackImmediate(); // dismiss this fragment
    }

    /**
     * After storing the created data, need to clear/reset the array list
     */
    public void reset_file() {
        people_info_list_in_file.clear();
    }
}