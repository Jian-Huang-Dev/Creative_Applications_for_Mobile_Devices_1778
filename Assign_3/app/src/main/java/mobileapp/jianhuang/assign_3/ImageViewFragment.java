package mobileapp.jianhuang.assign_3;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

/**
 * Created by jianhuang on 16-01-30.
 */
public class ImageViewFragment extends android.support.v4.app.Fragment {
    private ImageView mImageView;
    private Button delBtn;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.image_display,
                container, false);

        mImageView = (ImageView) view.findViewById(R.id.imageView);
        delBtn = (Button) view.findViewById(R.id.del_btn);

        delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Helper.deleteFile(Helper.position);
                getActivity().onBackPressed();
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
}
