package saycheese.triangles.com.saycheese;

import android.app.Activity;

/**
 * Created by hasrika a on 12/16/2014.
 */
public abstract class SuperCamera
{
    CameraStatusListener cameraStatusListener;
    CameraPreview mPreview;
    protected Activity activity;
    public SuperCamera(Activity activity)
    {
        this.activity = activity;
    }
    /*public  SuperCamera getCameraInstance()
    {
        return null;
    }
*/
    public abstract void release();

    public abstract void startPreview();

    protected abstract void captureFromCamera();

    public abstract boolean openCamera();

    public abstract void addCameraToLayout();
    public void capture()
    {
        cameraStatusListener.onCaptureStarted();
        captureFromCamera();
    }


    protected void onPictureTaken(byte[] data)
    {
        cameraStatusListener.onCaptureCompleted();
        PictureWritingThread pictureWriter = new PictureWritingThread(data.clone());
    }

    public CameraPreview getCameraPreview()
    {
        return mPreview;
    }
    public void registerCameraStatusListener(CameraStatusListener listener)
    {
        cameraStatusListener = listener;
    }
}
