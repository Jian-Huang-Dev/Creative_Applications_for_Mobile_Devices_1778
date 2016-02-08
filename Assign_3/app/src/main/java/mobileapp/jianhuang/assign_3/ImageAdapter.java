package mobileapp.jianhuang.assign_3;

/**
 * Created by jianhuang on 16-01-30.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;

    public ImageAdapter(Context c) {
        mContext = c;
    }

    public int getCount() {
//        return mThumbIds.length;
        return Helper.getFilesLength();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(
                mContext.LAYOUT_INFLATER_SERVICE);
        View v;
        TextView textView;
        ImageView imageView;
        String filePath;
        String data;

        if (convertView == null) {
            v = new View(mContext);
            v = inflater.inflate(R.layout.gridview_cell, null);


            filePath = Helper.getFiles()[position].getAbsolutePath();

            textView = (TextView) v.findViewById(R.id.textView);

            data = Helper.GPSDataMap.get(filePath);
            textView.setText(data);

            imageView = (ImageView) v.findViewById(R.id.imageView);
            // loading the bitmap to the imageview
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 4;
            Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
            imageView.setImageBitmap(bitmap);

        } else {
            v = (View) convertView;
        }

        return v;
    }
}
