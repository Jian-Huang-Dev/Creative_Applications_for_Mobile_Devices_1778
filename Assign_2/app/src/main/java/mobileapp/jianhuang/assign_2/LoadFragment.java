package mobileapp.jianhuang.assign_2;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Created by jianhuang on 16-01-21.
 */
public class LoadFragment extends android.support.v4.app.Fragment {

    public ListView list_view, detail_view;

    LoadBtnListener activityCallback;

    public interface LoadBtnListener {
        public void activityLoadBtnClicked(ListView list_view);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            activityCallback = (LoadBtnListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement LoadBtnListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.load_fragment,
                container, false);

        // this fragment consisted of two list views
        list_view = (ListView) view.findViewById(R.id.file_list_view);
        detail_view = (ListView) view.findViewById(R.id.file_detail_view);

        activityCallback.activityLoadBtnClicked(list_view);

        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String[] file_details_list;
                String full_file_name = list_view.getItemAtPosition(position).toString();

                // retrieve file info details
                file_details_list = helper.readFile
                        (full_file_name, getActivity().getApplicationContext())
                        .split(", ");

                // list file info details
                ArrayAdapter<String> adapter =
                        new ArrayAdapter<String>(getActivity().getApplicationContext(),
                        R.layout.list_view_text_style, android.R.id.text1, file_details_list);
                detail_view.setAdapter(adapter);

                Toast.makeText(getActivity(), full_file_name, Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
