package mobileapp.jianhuang.assign_2;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * Created by jianhuang on 16-01-21.
 */
public class LoadFragment extends android.support.v4.app.Fragment {

    public ListView list_view;

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

        list_view = (ListView) view.findViewById(R.id.file_list_view);
        activityCallback.activityLoadBtnClicked(list_view);

        return view;
    }
}
