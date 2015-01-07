package saycheese.triangles.com.saycheese;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.os.Build;

import java.util.concurrent.Semaphore;

import saycheese.triangles.com.saycheese.util.LogUtil;

/**
 * Created by hasrika a on 12/16/2014.
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class LollipopCamera extends SuperCamera
{

    //public Class CameraCallBack;
    CameraManager manager;
    CameraDevice mCameraDevice;
    /**
     * A {@link Semaphore} to prevent the app from exiting before closing the camera.
     */
    private Semaphore mCameraOpenCloseLock = new Semaphore(1);

    CameraStateCallback callBack = new CameraStateCallback();

    public LollipopCamera(Activity activity)
    {
        super(activity);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SuperCamera getCameraInstance()
    {
        manager = (CameraManager) activity.getSystemService(Context.CAMERA_SERVICE);
        String[] cameraIdList = new String[0];
        try
        {
            cameraIdList = manager.getCameraIdList();
            manager.openCamera(cameraIdList[0], callBack, null);
        } catch (CameraAccessException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void release()
    {

    }

    @Override
    protected void captureFromCamera()
    {

    }

    @Override
    public boolean openCamera()
    {
        return false;
    }

    @Override
    public void addCameraToLayout()
    {

    }


    public void addCameraToLayout(CameraPreview mPreview)
    {

    }

    @Override
    public void startPreview()
    {

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    protected class CameraStateCallback extends CameraDevice.StateCallback
    {

        @Override
        public void onOpened(CameraDevice cameraDevice)
        {
            cameraDevice = mCameraDevice;
            mCameraOpenCloseLock.release();
            mCameraDevice = cameraDevice;
            //createCameraPreviewSession();
        }

        @Override
        public void onDisconnected(CameraDevice cameraDevice)
        {
            mCameraOpenCloseLock.release();
            cameraDevice.close();
            mCameraDevice = null;
        }

        @Override
        public void onError(CameraDevice cameraDevice, int error)
        {
            mCameraOpenCloseLock.release();
            cameraDevice.close();
            mCameraDevice = null;
            LogUtil.print("error occured while connecting to camera : " + error);
        }
    }


    /**
     * The current state of camera state for taking pictures.
     *
     * @see #mCaptureCallback
     */
    private int mState = STATE_PREVIEW;

    /**
     * Camera state: Showing camera preview.
     */
    private static final int STATE_PREVIEW = 0;

    /**
     * Camera state: Waiting for the focus to be locked.
     */
    private static final int STATE_WAITING_LOCK = 1;
    /**
     * Camera state: Waiting for the exposure to be precapture state.
     */
    private static final int STATE_WAITING_PRECAPTURE = 2;
    /**
     * Camera state: Waiting for the exposure state to be something other than precapture.
     */
    private static final int STATE_WAITING_NON_PRECAPTURE = 3;
    /**
     * Camera state: Picture was taken.
     */
    private static final int STATE_PICTURE_TAKEN = 4;

    /**
     * A {@link CameraCaptureSession.CaptureCallback} that handles events related to JPEG capture.
     */
    private CameraCaptureSession.CaptureCallback mCaptureCallback
            = new CameraCaptureSession.CaptureCallback()
    {

        private void process(CaptureResult result)
        {
            switch (mState)
            {
                case STATE_PREVIEW:
                {
                    // We have nothing to do when the camera preview is working normally.
                    break;
                }
                case STATE_WAITING_LOCK:
                {
                    int afState = result.get(CaptureResult.CONTROL_AF_STATE);
                    if (CaptureResult.CONTROL_AF_STATE_FOCUSED_LOCKED == afState ||
                            CaptureResult.CONTROL_AF_STATE_NOT_FOCUSED_LOCKED == afState)
                    {
                        // CONTROL_AE_STATE can be null on some devices
                        Integer aeState = result.get(CaptureResult.CONTROL_AE_STATE);
                        if (aeState == null ||
                                aeState == CaptureResult.CONTROL_AE_STATE_CONVERGED)
                        {
                            mState = STATE_WAITING_NON_PRECAPTURE;
                            captureStillPicture();
                        } else
                        {
                            //runPrecaptureSequence();
                        }
                    }
                    break;
                }
                case STATE_WAITING_PRECAPTURE:
                {
                    // CONTROL_AE_STATE can be null on some devices
                    Integer aeState = result.get(CaptureResult.CONTROL_AE_STATE);
                    if (aeState == null ||
                            aeState == CaptureResult.CONTROL_AE_STATE_PRECAPTURE ||
                            aeState == CaptureRequest.CONTROL_AE_STATE_FLASH_REQUIRED)
                    {
                        mState = STATE_WAITING_NON_PRECAPTURE;
                    }
                    break;
                }
                case STATE_WAITING_NON_PRECAPTURE:
                {
                    // CONTROL_AE_STATE can be null on some devices
                    Integer aeState = result.get(CaptureResult.CONTROL_AE_STATE);
                    if (aeState == null || aeState != CaptureResult.CONTROL_AE_STATE_PRECAPTURE)
                    {
                        mState = STATE_PICTURE_TAKEN;
                        captureStillPicture();
                    }
                    break;
                }
            }
        }

        @Override
        public void onCaptureProgressed(CameraCaptureSession session, CaptureRequest request,
                                        CaptureResult partialResult)
        {
            process(partialResult);
        }

        @Override
        public void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request,
                                       TotalCaptureResult result)
        {
            process(result);
        }

    };
    private void captureStillPicture()
    {
        try
        {
            // This is the CaptureRequest.Builder that we use to take a picture.
            final CaptureRequest.Builder captureBuilder =
                    mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            //captureBuilder.addTarget(mImageReader.getSurface());

        }catch(Exception e)
        {
            LogUtil.print(e.getMessage());
        }

    }
}

