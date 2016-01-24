package mobileapp.jianhuang.assign_2;

/**
 * Created by jianhuang on 16-01-21.
 * Main screen fragment
 */

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class MainScreenFragment extends android.support.v4.app.Fragment {

    public TextView textview;
    public Button enter_name_btn, view_btn, store_btn, load_btn, exit_btn;

    ButtonClickedListnener activityCallback;

    public interface ButtonClickedListnener {
        public void activityViewBtnClicked();
        public void activityExitBtnClicked();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            activityCallback = (ButtonClickedListnener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement ButtonClickListnener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_screen_fragment,
                container, false);

        enter_name_btn = (Button) view.findViewById(R.id.enter_name_btn);
        view_btn = (Button) view.findViewById(R.id.view_btn);
        store_btn = (Button) view.findViewById(R.id.store_btn);
        load_btn = (Button) view.findViewById(R.id.load_btn);
        exit_btn = (Button) view.findViewById(R.id.exit_btn);

        enter_name_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                enterNamesBtnClicked(v);
            }
        });

        store_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                storeBtnClicked(v);
            }
        });

        load_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                loadBtnClicked(v);
            }
        });

        view_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                viewBtnClicked(v);
            }
        });

        exit_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                exitBtnClicked(v);
            }
        });

        return view;
    }

    public void enterNamesBtnClicked(View v) {
        EnterNamesFragment enterFragment = new EnterNamesFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, enterFragment);
        transaction.addToBackStack(null);
        // Commit the transaction
        transaction.commit();
    }

    public void storeBtnClicked(View v) {
        StoreDialogFragment storeDialogFragment = new StoreDialogFragment();
        // show the dialog for user to enter file name
        storeDialogFragment.show(getFragmentManager(), "store_dialog_fragment");
    }

    public void loadBtnClicked(View v) {
        LoadFragment loadFragment = new LoadFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, loadFragment);
        transaction.addToBackStack(null);
        // Commit the transaction
        transaction.commit();
    }

    public void viewBtnClicked(View v) {
        activityCallback.activityViewBtnClicked();
    }

    public void exitBtnClicked(View v) {
        activityCallback.activityExitBtnClicked();
    }
}