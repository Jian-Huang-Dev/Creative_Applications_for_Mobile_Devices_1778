package mobileapp.jianhuang.assign_3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Created by jianhuang on 16-02-02.
 */
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void cameraViewBtnClicked(View v) {
        Intent intent = new Intent(this, CameraActivity.class);
        startActivity(intent);
    }

    public void galleryViewBtnClicked(View v) {
        Intent intent = new Intent(this, ImageViews.class);
        startActivity(intent);
    }
}
