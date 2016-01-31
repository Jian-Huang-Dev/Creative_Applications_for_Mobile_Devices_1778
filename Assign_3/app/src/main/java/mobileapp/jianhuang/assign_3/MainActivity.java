package mobileapp.jianhuang.assign_3;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class MainActivity extends Activity implements PictureCallback, SurfaceHolder.Callback {
    private Camera mCamera;
    private SurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceHolder;
    private Bitmap mCameraBitmap;
//    private ArrayList<Bitmap> mImageList;

    PictureCallback rawCallback;
    Camera.ShutterCallback shutterCallback;
    PictureCallback jpegCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSurfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        mSurfaceHolder = mSurfaceView.getHolder();

        mSurfaceHolder.addCallback(this);
        mSurfaceHolder.setType(mSurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        jpegCallback = new PictureCallback() {

            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                FileOutputStream outStream = null;
                try {
                    outStream = new FileOutputStream(String.format("/sdcard/%d.jpg", System.currentTimeMillis()));

                    outStream.write(data);
                    outStream.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                }

                Toast.makeText(getApplicationContext(), "Picture Saved", Toast.LENGTH_LONG).show();
                refreshCamera();
            }
        };
    }

    public void captureImage(View v) throws IOException {
        mCamera.takePicture(null, null, jpegCallback);
    }

    public void refreshCamera() {
        Log.d("MYDEBUG", "refreshCamera");
        if (mSurfaceHolder.getSurface() == null) {
            return;
        }

        try {
            mCamera.stopPreview();
        } catch (Exception e) {
        }

        try {
            mCamera.setPreviewDisplay(mSurfaceHolder);
            mCamera.startPreview();
        } catch (Exception e) {
        }
    }

    @Override
    protected void onDestroy() {
        Log.d("MYDEBUG", "onDestroy");
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d("MYDEBUG", "surfaceCreated");
        try {
            mCamera = Camera.open();
            Camera.Parameters params = mCamera.getParameters();
            params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            mCamera.setParameters(params);
            List<Camera.Size> list = mCamera.getParameters().getSupportedPreviewSizes();
            Log.d("DEBUG", Integer.toString(list.get(2).width) + Integer.toString(list.get(2).height));

        } catch (RuntimeException e) {
            System.err.println(e);
            return;
        }

        Camera.Parameters param;
        param = mCamera.getParameters();
        param.setPreviewSize(352, 288);
        mCamera.setParameters(param);

        try {
            mCamera.setPreviewDisplay(mSurfaceHolder);
            mCamera.startPreview();
        } catch (Exception e) {
            System.err.println(e);
            return;
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.d("MYDEBUG", "surfaceChanged");
        refreshCamera();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d("MYDEBUG", "surfaceDestroyed");
        this.closeCamera(mCamera);
    }

    public void imageCapture(View v) {
        mCamera.takePicture(null, null, this);
    }

    public void galleryViewBtnClicked(View v) {
        Intent intent = new Intent(this, ImageViews.class);
        startActivity(intent);
    }

    private void closeCamera(Camera camera) {
        camera.stopPreview();
        camera.release();
        camera = null;
    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        Log.d("MYDEBUG", "onPictureTaken");
        setGpsLatitude(camera);
        setGpsLongitude(camera);
        mCameraBitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        Helper.storeImage(mCameraBitmap);
        refreshCamera();
    }

    private void setGpsLatitude(Camera camera) {
        Location location = getGpsLocation();
        if (location == null) {
            Log.d("DEBUG", "Cant not retrieve location data!");
        } else {
            camera.getParameters().setGpsLatitude(location.getLatitude());
        }
    }

    private void setGpsLongitude(Camera camera) {
        Location location = getGpsLocation();
        if (location == null) {
            Log.d("DEBUG", "Cant not retrieve location data!");
        } else {
            camera.getParameters().setGpsLongitude(location.getLongitude());
        }
    }

    private Location getGpsLocation() {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {

            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            return location;
        }
        return null;
    }
}