package mobileapp.jianhuang.assign_3;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
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

public class CameraActivity extends Activity implements PictureCallback,
        SurfaceHolder.Callback,
        AccelerometerListener {

    private static final double ASPECT_TOLERANCE = 0.1;
    private Camera mCamera;
    private SurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceHolder;
    private Bitmap mCameraBitmap;

    public String longitude, latitude;

    public LocationManager locationMangaer = null;
    public LocationListener locationListener = null;

    PictureCallback rawCallback;
    Camera.ShutterCallback shutterCallback;
    PictureCallback jpegCallback;
    Camera.Parameters params;
    Camera.Size cameraPreviewSize, cameraPictueSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_activity);

        locationMangaer = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);

        mSurfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        mSurfaceHolder = mSurfaceView.getHolder();

        mSurfaceHolder.addCallback(this);
        mSurfaceHolder.setType(mSurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        if (displayGpsStatus()) {

            LocationManager lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            if (ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED) {

                Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }
            locationListener = new MyLocationListener();
            locationMangaer.requestLocationUpdates(LocationManager
                    .GPS_PROVIDER, 5000, 10,locationListener);

        } else {
            alertbox("Gps Status!!", "Your GPS is: OFF");
        }

        jpegCallback = new PictureCallback() {

            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                FileOutputStream outStream = null;
                try {
                    String filePath = Helper.getFilePath();
                    outStream = new FileOutputStream(filePath);
                    outStream.write(data);
                    outStream.close();

                    if (longitude == null && latitude == null) {
                        Helper.GPSDataMap.put(filePath, "No Data" + "\n" + "No Data");
                    }
                    else {
                        Helper.GPSDataMap.put(filePath, longitude + "\n" + latitude);
                    }
                    Helper.saveHashMapData(Helper.GPSDataMap);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                }

                Toast.makeText(getApplicationContext(), "Picture Saved", Toast.LENGTH_SHORT).show();
                refreshCamera();
            }
        };
    }

//    public void captureImage(View v) throws IOException {
//        mCamera.takePicture(null, null, jpegCallback);
//    }

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

        //Check device supported Accelerometer senssor or not
        if (AccelerometerManager.isListening()) {

            //Start Accelerometer Listening
            AccelerometerManager.stopListening();

            Toast.makeText(getBaseContext(), "onDestroy Accelerometer Stoped",
                    Toast.LENGTH_SHORT).show();
        }
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

            params = mCamera.getParameters();
            params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);

            cameraPreviewSize = getBestPreviewSize(Helper.getFullScreenSizeWidth(this),
                    Helper.getFullScreenSizeHeight(this), params);
            cameraPictueSize = getBestPictureSize(Helper.getFullScreenSizeWidth(this),
                    Helper.getFullScreenSizeHeight(this), params);

            params.setPreviewSize(cameraPreviewSize.width, cameraPreviewSize.height);
            params.setPictureSize(1920, 1080);
            mCamera.setParameters(params);

        } catch (RuntimeException e) {
            System.err.println(e);
            return;
        }

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

    public void imageCapture() throws IOException {
        mCamera.takePicture(null, null, jpegCallback);
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

//        mCameraBitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
//        Helper.storeImage(mCameraBitmap);
        refreshCamera();
    }

    public void onAccelerationChanged(float x, float y, float z) {
        // TODO Auto-generated method stub

    }

    public void onShake(float force) {
        // Called when Motion Detected
        Toast.makeText(getBaseContext(), "Motion detected",
                Toast.LENGTH_SHORT).show();

        if (AccelerometerManager.isListening()) {
            //Start Accelerometer Listening
            AccelerometerManager.stopListening();
        }

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                mCamera.takePicture(null, null, jpegCallback);

                listenOnAccelerometer();
            }
        }, getResources().getInteger(R.integer.time_to_take_pic));
    }

    @Override
    public void onResume() {
        super.onResume();
        listenOnAccelerometer();
    }

    @Override
    public void onStop() {
        super.onStop();

        //Check device supported Accelerometer senssor or not
        if (AccelerometerManager.isListening()) {

            //Start Accelerometer Listening
            AccelerometerManager.stopListening();
        }

    }

    public void listenOnAccelerometer() {
        //Check device supported Accelerometer senssor or not
        if (AccelerometerManager.isSupported(this)) {

            //Start Accelerometer Listening
            AccelerometerManager.startListening(this);
        }
    }

    public Camera.Size getBestPreviewSize(int width, int height, Camera.Parameters parameters){
        Camera.Size bestSize = null;
        List<Camera.Size> sizeList = parameters.getSupportedPreviewSizes();

        bestSize = sizeList.get(sizeList.size() - 1);

        for(int i = 1; i < sizeList.size(); i++){
            if((sizeList.get(i).width * sizeList.get(i).height) >
                    (bestSize.width * bestSize.height)){
                if (isAspectRatioMatch((double) width / height,
                        (double) (sizeList.get(i).width) / (sizeList.get(i).height))) {
                    bestSize = sizeList.get(i);
                }
            }
        }

        return bestSize;
    }

    public Camera.Size getBestPictureSize(int width, int height, Camera.Parameters parameters){
        Camera.Size bestSize = null;
        List<Camera.Size> sizeList = parameters.getSupportedPictureSizes();

        bestSize = sizeList.get(sizeList.size() - 1);

        for (int i = 1; i < sizeList.size(); i++) {
            if ((sizeList.get(i).width * sizeList.get(i).height) >
                    (bestSize.width * bestSize.height)) {
                if (isAspectRatioMatch((double) width / height,
                        (double) (sizeList.get(i).width) / (sizeList.get(i).height))) {
                    bestSize = sizeList.get(i);
                }
            }
        }

        return bestSize;
    }

    public boolean isAspectRatioMatch(double ratio1, double ratio2) {
        if (Math.abs(ratio1 - ratio2) > ASPECT_TOLERANCE) {
            return false;
        }
        return true;
    }

    private Boolean displayGpsStatus() {
        ContentResolver contentResolver = getBaseContext()
                .getContentResolver();
        boolean gpsStatus = Settings.Secure
                .isLocationProviderEnabled(contentResolver,
                        LocationManager.GPS_PROVIDER);
        if (gpsStatus) {
            return true;

        } else {
            return false;
        }
    }

    protected void alertbox(String title, String mymessage) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your Device's GPS is Disable")
                .setCancelable(false)
                .setTitle("Gps Status")
                .setPositiveButton("Turn On GPS",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // finish the current activity
                                // AlertBoxAdvance.this.finish();
                                Intent myIntent = new Intent(
                                        Settings.ACTION_SETTINGS);
                                startActivity(myIntent);
                                dialog.cancel();
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // cancel the dialog box
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private class MyLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location loc) {

            longitude = "long: " + Double.toString(loc.getLongitude());
            Log.v("mygps", longitude);
            latitude = "lat: " + Double.toString(loc.getLatitude());
            Log.v("mygps", latitude);
        }

        public String getGPSLongitude() {
            return longitude;
        }

        public String getGPSLatitude() {
            return latitude;
        }

        @Override
        public void onProviderDisabled(String provider) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onProviderEnabled(String provider) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onStatusChanged(String provider,
                                    int status, Bundle extras) {
            // TODO Auto-generated method stub
        }
    }
}