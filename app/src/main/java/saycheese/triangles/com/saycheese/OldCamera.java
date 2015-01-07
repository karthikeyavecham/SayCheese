package saycheese.triangles.com.saycheese;

import android.hardware.Camera;
import android.widget.Toast;

import saycheese.triangles.com.saycheese.util.LogUtil;

/**
 * Created by hasrika a on 1/2/2015.
 */
public class OldCamera extends SuperCamera
{
    private Camera camera;

    public OldCamera(CameraActivity activity)
    {
        super(activity);
    }
    @Override
    public void release()
    {
        camera.release();
    }

    @Override
    public void startPreview()
    {
        camera.startPreview();
    }

    @Override
    protected void captureFromCamera()
    {
        Toast.makeText(super.activity, "capture.", Toast.LENGTH_SHORT).show();
        camera.takePicture(null, null, mPicture);
        try
        {
            LogUtil.print("no of times = " + CheeseSpeechRecognizor.noOftimesRecognized);

        } catch (Exception e)
        {
            LogUtil.print(e.getMessage());
        }
    }

    @Override
    public boolean openCamera()
    {
        camera = getCameraInstance();

        return true;
    }

    @Override
    public void addCameraToLayout()
    {
        super.mPreview = new CameraPreview(activity, camera);
    }


    private Camera getCameraInstance()
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

    private Camera.PictureCallback mPicture = new Camera.PictureCallback()
    {
        @Override
        public void onPictureTaken(byte[] data, Camera camera)
        {
            LogUtil.print("on picture taken");
            OldCamera.super.onPictureTaken(data);
            //PictureWritingThread pictureWriter = new PictureWritingThread(data.clone());
            //speechRecognizor.startListening();
            //        camera = getCameraInstance();
                try
                {
                    LogUtil.print("restting recCount");
                    CheeseSpeechRecognizor.noOftimesRecognized = 0;
                    OldCamera.this.camera = camera;
                    camera.startPreview();
                }
                catch (Exception e)
                {
                    LogUtil.print(e.getMessage());
                }
                LogUtil.print("preview Started");
        }


    };

}
