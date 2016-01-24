package mobileapp.jianhuang.assign_2;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by jianhuang on 16-01-21.
 */
public class StoreDialogFragment extends android.support.v4.app.DialogFragment {


    private EditText edit_text;
    private Button confirm_btn;

    DialogBtnClickedListener activityCallback;

    public interface DialogBtnClickedListener {
        public void activityFileNameConfirmBtnClicked(String file_name);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            activityCallback = (DialogBtnClickedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement DialogBtnClickedListener");
        }
    }

    public StoreDialogFragment() {
        // Empty constructor required for DialogFragment
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.store_dialog_fragment, container);
        edit_text = (EditText) view.findViewById(R.id.file_name_field);
        confirm_btn = (Button) view.findViewById(R.id.confirm_btn);
        confirm_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onConfirmBtnClicked(v);
            }
        });

        getDialog().setTitle(R.string.promote_file_name);

        return view;
    }

    public void onConfirmBtnClicked(View v) {
        String file_name = edit_text.getText().toString();
        if (!TextUtils.isEmpty(file_name)) {
            getDialog().dismiss();
        }
        activityCallback.activityFileNameConfirmBtnClicked(file_name);
    }
}
