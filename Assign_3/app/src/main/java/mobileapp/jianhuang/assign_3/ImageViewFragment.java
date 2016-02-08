package mobileapp.jianhuang.assign_3;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * Created by jianhuang on 16-01-30.
 */
public class ImageViewFragment extends android.support.v4.app.Fragment {

    private ImageView mImageView;
    private Button delBtn, backBtn;
    RelativeLayout imageViewLayoutBtn;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
//        Helper.hideNavBar(getActivity());

        View view = inflater.inflate(R.layout.image_display,
                container, false);

        mImageView = (ImageView) view.findViewById(R.id.imageView);
        delBtn = (Button) view.findViewById(R.id.del_btn);
        backBtn = (Button) view.findViewById(R.id.back_btn);
        imageViewLayoutBtn = (RelativeLayout) view.findViewById(R.id.imageViewBtn);
        delBtn.setBackgroundColor(Color.RED);
        backBtn.setBackgroundColor(Color.RED);

        delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Helper.deleteFile(Helper.position);
                restartActivity();
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Helper.showNavBar(getActivity());
                getFragmentManager().popBackStackImmediate();
            }
        });

        imageViewLayoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Helper.hideNavBar(getActivity());
            }
        });

        Log.d("MYDEBUG", Integer.toString(Helper.position));
        displayImage(Helper.position);

        return view;
    }

    public void displayImage(int position) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 1; //original size
        Bitmap bitmap = BitmapFactory.decodeFile(
                Helper.getFiles()[position].getAbsolutePath(), options);
        mImageView.setImageBitmap(bitmap);
    }

    private void restartActivity() {
        Intent intent = getActivity().getIntent();
        getActivity().finish();
        startActivity(intent);
    }
}
