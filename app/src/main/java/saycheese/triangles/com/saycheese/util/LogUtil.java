package saycheese.triangles.com.saycheese.util;

import android.util.Log;

/**
 * Created by hasrika a on 1/2/2015.
 */
public class LogUtil
{
    public static final String TAG = "tag";
    public static boolean DEBUG = true;

    public static void print(String msg)
    {
        if(DEBUG = true)
        {
            Log.i(TAG, msg);
        }
    }

}
