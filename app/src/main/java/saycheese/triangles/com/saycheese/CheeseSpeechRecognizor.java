package saycheese.triangles.com.saycheese;


import android.os.AsyncTask;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Queue;

import edu.cmu.pocketsphinx.Assets;
import edu.cmu.pocketsphinx.Hypothesis;
import edu.cmu.pocketsphinx.RecognitionListener;
import edu.cmu.pocketsphinx.SpeechRecognizer;
import saycheese.triangles.com.saycheese.util.LogUtil;

import static edu.cmu.pocketsphinx.SpeechRecognizerSetup.defaultSetup;

/**
 * Created by hasrika a on 12/16/2014.
 */
public class CheeseSpeechRecognizor implements RecognitionListener
{
    private static final String KWS_SEARCH = "wakeup";
    private static final String KEYPHRASE = "cheese";
    private static final String STOP_PHRASE = "stop";
    private SpeechRecognizer recognizer;
    CameraActivity activity;
    public static final String TAG = "tag";

    ArrayList<String> voiceCommandsQ;

    public static int noOftimesRecognized = 0;

    public CheeseSpeechRecognizor(CameraActivity activity)
    {
        this.activity = activity;
        prepareAsyncTask();
        voiceCommandsQ = new ArrayList<>();
    }

    private void prepareAsyncTask()
    {
        new AsyncTask<Void, Void, Exception>()
        {

            @Override
            protected Exception doInBackground(Void... params)
            {
                Log.d(TAG, "preparing asynctask");
                try
                {

                    Assets assets = new Assets(CheeseSpeechRecognizor.this.activity);
                    File assetDir = assets.syncAssets();
                    setupRecognizer(assetDir);
                } catch (IOException e)
                {
                    return e;
                }
                return null;
            }

            @Override
            protected void onPostExecute(Exception result)
            {
                Log.d(TAG, "on post execute");
                if (result != null)
                {
                    //((TextView) findViewById(R.id.caption_text)).setText("Failed to init recognizer " + result);
                } else
                {
                    recognizer.stop();
                    recognizer.startListening(KWS_SEARCH);
                }
            }
        }.execute();
    }

    @Override
    public void onPartialResult(Hypothesis hypothesis)
    {
        //Log.d(TAG, "on partial result");
        if (hypothesis != null)
        {

            String text = hypothesis.getHypstr();
            Log.d(TAG, "on result" + text);
            //Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
            voiceCommandsQ.add(text);
            if (text.equals(KEYPHRASE))
            {
                voiceCommandsQ.add(text);
                Log.d(TAG, "cheese");
                noOftimesRecognized +=1;
                capturePicture();
                recognizer.cancel();
                LogUtil.print("recognizor cancelled");
                recognizer.startListening(KWS_SEARCH);
            }
            if (text.equals(STOP_PHRASE))
            {
                Log.d(TAG, "stop");
                recognizer.stop();
            }
        }
    }

    public void cancel()
    {
        recognizer.cancel();
    }

    @Override
    public void onResult(Hypothesis hypothesis)
    {
        //Log.d(TAG, "on result" + );
        //((TextView) findViewById(R.id.result_text)).setText("");

        if (hypothesis != null)
        {

            String text = hypothesis.getHypstr();
            Log.d(TAG, "on result" + text);
            //Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
            if (text.equals(KEYPHRASE))
            {
                Log.d(TAG, "cheese");
                capturePicture();
            }
            if (text.equals(STOP_PHRASE))
            {
                Log.d(TAG, "stop");
                recognizer.stop();
            }

        }
    }

    @Override
    public void onEndOfSpeech()
    {
        Log.d(TAG, "on endof speech");
        /*
        if (DIGITS_SEARCH.equals(recognizer.getSearchName())
        {

        }*/

    }


    private void setupRecognizer(File assetsDir)
    {
        Log.d(TAG, "setting up the recognizor");
        File modelsDir = new File(assetsDir, "models");
        recognizer = defaultSetup()
                .setAcousticModel(new File(modelsDir, "hmm/en-us-semi"))
                .setDictionary(new File(modelsDir, "dict/cmu07a.dic"))
                .setRawLogDir(assetsDir).setKeywordThreshold(1e-20f)
                .getRecognizer();
        recognizer.addListener(this);

        // Create keyword-activation search.
        recognizer.addKeyphraseSearch(KWS_SEARCH, KEYPHRASE);

    }

    @Override
    public void onBeginningOfSpeech()
    {
        Log.d(TAG, "on beginning of the speech");
    }

    public void capturePicture()
    {
        //activity.dummyCapture();
        activity.capture();
    }

    public void stopListening()
    {
        recognizer.stop();
    }

    public void startListening()
    {
        recognizer.startListening(KWS_SEARCH);
    }
}
