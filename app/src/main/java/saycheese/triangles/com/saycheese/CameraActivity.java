package saycheese.triangles.com.saycheese;


import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.io.IOException;

import saycheese.triangles.com.saycheese.util.LogUtil;


public class CameraActivity extends Activity implements OnClickListener, CameraStatusListener
{

    public static final int MEDIA_TYPE_IMAGE = 1;
    Context context = this.context;
    //Camera camera;
    private CameraPreview mPreview;
    private Button shutterButtun;
    private FrameLayout preview = null;
    private SuperCamera camera;

    CheeseSpeechRecognizor speechRecognizor;

    //public static final String TAG = "tag";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(getWindow().FEATURE_NO_TITLE);
        setContentView(R.layout.activity_camera);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE);

        camera = new OldCamera(this);
        camera.registerCameraStatusListener(this);
        camera.openCamera();

        //camera = getCameraInstance();
        /*if (camera == null)
        {
            LogUtil.print("biscuit");
        }*/
        speechRecognizor = new CheeseSpeechRecognizor(this);
        // Create our Preview view and set it as the content of our
        // activity.

        //mPreview = new CameraPreview(this, camera);
        camera.addCameraToLayout();
        mPreview = camera.getCameraPreview();

        preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);

        shutterButtun = (Button) findViewById(R.id.button_capture);
        speechRecognizor = new CheeseSpeechRecognizor(this);
        shutterButtun.setOnClickListener(this);
    }

    public static Camera getCameraInstance()
    {

        LogUtil.print("get camera instance");
        Camera c = null;
        try
        {
            c = Camera.open(); // attempt to get a Camera instance
            LogUtil.print("camera opened");

        } catch (Exception e)
        {
            LogUtil.print("camera unaavailable");
        }
        return c; // returns null if camera is unavailable

    }

    public static boolean isCameraAvailable()
    {
        Camera c = null;
        try
        {
            c = Camera.open(); // attempt to get a Camera instance
            LogUtil.print("camera opened");
            return true;
        } catch (Exception e)
        {
            LogUtil.print("camera unaavailable");
            LogUtil.print(e.getMessage());
            e.printStackTrace();
            return false;
            // Camera is not available (in use or does not exist)
        }
    }


    @Override
    public void onClick(View v)
    {
        capture();
        //speechRecognizor.capturePicture();
    }

    public void capture()
    {
        Toast.makeText(this, "capture.", Toast.LENGTH_SHORT).show();
        //camera.takePicture(null, null, mPicture);
        camera.capture();
        try
        {
            LogUtil.print("no of times = " + CheeseSpeechRecognizor.noOftimesRecognized);

        } catch (Exception e)
        {
            LogUtil.print(e.getMessage());
        }
    }

    protected void onStop()
    {
        LogUtil.print("onstop()");
        preview.removeView(mPreview);
        speechRecognizor.cancel();
        camera.release();
        LogUtil.print("camera released");

        super.onStop();

    }

    protected void onRestart()
    {

        //camera = getCameraInstance();
        camera.openCamera();
        LogUtil.print("preview started");
        //mPreview = new CameraPreview(this, camera);
        camera.addCameraToLayout();
        mPreview = camera.getCameraPreview();
        preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);
        camera.startPreview();
        speechRecognizor.startListening();
        //recognizer.startListening()
        super.onRestart();
    }

/*
    private PictureCallback mPicture = new PictureCallback()
    {
        @Override
        public void onPictureTaken(byte[] data, Camera camera)
        {
            LogUtil.print("on picture taken");
            PictureWritingThread pictureWriter = new PictureWritingThread(data.clone());
            //camera.startPreview();
            speechRecognizor.startListening();
            if (isCameraAvailable())
            {
                camera = getCameraInstance();
            }
            try
            {
                LogUtil.print("restting recCount");
                CheeseSpeechRecognizor.noOftimesRecognized = 0;
                camera.startPreview();
            }
            catch (Exception e)
            {
                LogUtil.print(e.getMessage());
            }
            LogUtil.print("preview Started");
        }


    };
*/
    @Override
    protected void onStart()
    {
        LogUtil.print("onStart()");
        if (camera == null)
        {
            //camera = getCameraInstance();
            camera.openCamera();
        }
        if (camera != null)
        {
            LogUtil.print("preview started");
            camera.startPreview();
        }
        super.onStart();

    }

    protected void onDestroy()
    {
        LogUtil.print("onDestroy()");
        super.onDestroy();

    }

    /**
     * Check if this device has a camera
     */
    private boolean checkCameraHardware(Context context)
    {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA))
        {
            // this device has a camera
            return true;
        } else
        {
            // no camera on this device
            return false;
        }
    }

    public void dummyCapture()
    {
        //Toast.makeText(this, "capture.", Toast.LENGTH_SHORT).show();
        //camera.release();

        //Log.i(TAG," camera "+);
        if (!isCameraAvailable())
        {
            Toast.makeText(this, "camera not available", Toast.LENGTH_SHORT).show();
        } else
        {
            Toast.makeText(this, "camera available", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_camera, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus)
    {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus)
        {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }


    @Override
    public void onCaptureStarted()
    {
        shutterButtun.setClickable(false);
        speechRecognizor.cancel();
    }

    @Override
    public void onCaptureCompleted()
    {
        shutterButtun.setClickable(true);
        speechRecognizor.startListening();
    }

    @Override
    public void onPreviewStarted()
    {

    }
}
