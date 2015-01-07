package saycheese.triangles.com.saycheese;

import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by hasrika a on 12/13/2014.
 */
public class PictureWritingThread implements Runnable
{
    public static final String TAG = "tag";
    File pictureFile = null;
    Thread t;
    private byte[] data;

    public PictureWritingThread(byte[] data)
    {
        this.data = data;
        t = new Thread(this);
        Log.d(TAG, "creating new thread");
        t.run();
    }

    @Override
    public void run()
    {
        Log.d(TAG, "new thread started");
        writeApicture();
    }

    public void writeApicture()
    {
        try
        {
            //String path = getOutputMediaFile().getPath();
            pictureFile = getOutputMediaFile();
            //pictureFile.createNewFile();
            //Log.d(TAG,"file name " +path);

        } catch (IOException e)
        {
            Log.d(TAG, "Error creating media file, check storage permissions: " + e.getMessage());
        } catch (Exception e)
        {
            Log.d(TAG, "exception found " + e.getMessage());
        }

        if (pictureFile.exists())
        {
            Log.d(TAG, "file  exists");

        }
        try
        {
            Log.d(TAG, "started writing in to file");
            FileOutputStream fos = new FileOutputStream(pictureFile);
            fos.write(data);
            fos.close();
            Log.d(TAG, "written successfully");
        } catch (FileNotFoundException e)
        {
            Log.d(TAG, "File not found: " + e.getMessage());
        } catch (IOException e)
        {
            Log.d(TAG, "Error accessing file: " + e.getMessage());
        }
    }

    private Uri getOutputMediaFileUri() throws IOException
    {
        File mediaStorageDir = new File(
                Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES), "DayTwentyNine");
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                .format(new Date());
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator
                + "IMG_" + timeStamp + ".jpg");

        if (mediaFile.exists() == false)
        {
            mediaFile.getParentFile().mkdirs();
            if (mediaFile.createNewFile())
                Log.i(TAG, "new file created.");
            else
                Log.i(TAG, "failed creating a new file");
        }
        return Uri.fromFile(mediaFile);
    }

    private File getOutputMediaFile() throws IOException
    {
        File mediaStorageDir = new File(
                Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES), "DayTwentyNine");
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                .format(new Date());
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator
                + "IMG_" + timeStamp + ".jpg");

        if (mediaFile.exists() == false)
        {
            mediaFile.getParentFile().mkdirs();
            if (mediaFile.createNewFile())
                Log.i(TAG, "new file created.");
            else
                Log.i(TAG, "failed creating a new file");
        }
        return mediaFile;
    }

}
