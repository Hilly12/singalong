package org.bizzjiscuit.speechtotext;


import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import cz.msebera.android.httpclient.client.methods.CloseableHttpResponse;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.impl.client.HttpClientBuilder;
import cz.msebera.android.httpclient.impl.client.CloseableHttpClient;
import info.androidhive.speechtotext.R;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

public class MainActivity extends Activity {

    private TextView txtSpeechInput;
    private ImageButton btnSpeak;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    private AudioManager am;

    private MediaPlayer mediaPlayer;
    private int playbackPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtSpeechInput = (TextView) findViewById(R.id.txtSpeechInput);
        btnSpeak = (ImageButton) findViewById(R.id.btnSpeak);

        // hide the action bar
        getActionBar().hide();

        btnSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSpeechInput();
            }
        });

    }

    /**
     * Showing google speech input dialog
     */
    private void getSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
//        am = (AudioManager) getBaseContext().getSystemService(getApplicationContext().AUDIO_SERVICE);
//        am.setStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER, 0);
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Receiving speech input
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    txtSpeechInput.setText(result.get(0));
                    // String s = Backend(result.get(0));
                    Backend(result.get(0));
                }
                break;
            }

        }
    }

    public void Backend(String lyrics) {
            new ServerSendTask().execute(lyrics);

//        HttpClient httpClient = new DefaultHttpClient();
//
//        HttpPost post = new HttpPost("http://singarokev2.herokuapp.com/");
//        post.setHeader("Content-Type", "application/json");
//
//        Gson gson = new Gson();
//
//        Map<String, String> jsonData = new HashMap<String, String>();
//        jsonData.put("lyrics", lyrics);
//        String jsonStr = gson.toJson(jsonData);
//        post.setEntity(URL(jsonStr));

// execute method and handle any error responses.
        // InputStream in = post.getResponseBodyAsStream();

        // String path = "http://www.hochmuth.com/mp3/Bloch_Prayer.mp3";
//        am = (AudioManager) getBaseContext().getSystemService(getApplicationContext().AUDIO_SERVICE);
//        am.setStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, 0);
//        try {
//            playAudio(path);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    class ServerSendTask extends AsyncTask<String, Void, Void> {

        private Exception exception;
        private String audioURL;

//        String responseHandler(String response) {
//            return response;
//        }

        protected Void doInBackground(String... lyrics) { // Second void

            Gson gson = new Gson();

            Map<String, String> jsonData = new HashMap<String, String>();
            jsonData.put("lyrics", lyrics[0]);
            String dataDataData = gson.toJson(jsonData);
            CloseableHttpClient client = null;
            try {
                client = HttpClientBuilder.create().build();
            } catch (Exception e) {
                System.out.println("oops");
            }
            HttpPost req = new HttpPost("https://singarokev2.herokuapp.com/");
            req.setHeader("Content-Type", "application/json");
            try {
                req.setEntity(new StringEntity(dataDataData));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
//            String respBody = client.execute(req, responseHandler);
//            System.out.println(respBody);
            Gson gson2 = new Gson();

            Map<String, String> jsonData2 = new HashMap<String, String>();
            jsonData.put("lyrics", lyrics[0]);
            String dataDataData2 = gson.toJson(jsonData);
            byte[] b;
            try {
                CloseableHttpResponse resp = client.execute(req);
                b = new byte[(int)resp.getEntity().getContentLength()];
                resp.getEntity().getContent().read(b);
                dataDataData2 = new String(b, StandardCharsets.UTF_8);
                jsonData2 = gson2.fromJson(dataDataData2, Map.class);
                audioURL = jsonData2.get("mp3url");
                System.out.println(audioURL);
            } catch (Exception e) {
                e.printStackTrace();
            }
//            try {
//                URL url;
//                try {
//                    url = new URL("https", "singarokev2.herokuapp.com", "/");
//                } catch (MalformedURLException e) {
//                    return null;
//                }
//
//                Gson gson = new Gson();
//
//                Map<String, String> jsonData = new HashMap<String, String>();
//                jsonData.put("lyrics", lyrics[0]);
//                byte[] dataDataData = gson.toJson(jsonData).getBytes();
//                HttpURLConnection conn;
//                OutputStream out;
//                DataInputStream inStream;
//                String path = "";
//                byte[] b = new byte[10];
//                try {
//                    conn = (HttpURLConnection) url.openConnection();
//                    conn.setRequestMethod("POST");
//                    conn.setRequestProperty("Content-Type", "application/json");
//                    conn.setDoOutput(true);
//                    conn.setDoInput(true);
//                    //conn.setDoOutput(true);
//                    out = conn.getOutputStream();
////                    inStream = new DataInputStream(conn.getInputStream());
//                    out.write(dataDataData);
//                    out.flush();
//                    conn.setDoOutput(false);
//                    System.out.println(conn.getResponseCode());
////                    in.read(b);
////                    System.out.println(b);
//                    audioURL = conn.getResponseMessage();
//                    System.out.println(audioURL);
//                    System.err.println("adwuigdsiudgidsfvhsdfgdsfgiudfsdsfuidsfiufsiug" + conn.getResponseMessage());
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//            } catch (Exception e) {
//                this.exception = e;
//
//                return null;
//            }
            return null;
        }

        protected void onPostExecute(Void v) {
            // TODO: check this.exception
            // TODO: do something with the feed
            try {playAudio(audioURL);}
            catch (Exception e) {
                System.out.println("Error in playAudio");
            }
        }
    }

    private void playAudio(String url) throws Exception {
        killMediaPlayer();
        mediaPlayer = new MediaPlayer();
        mediaPlayer.reset();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setDataSource(getApplicationContext(), Uri.parse(url));
        mediaPlayer.prepare();
        mediaPlayer.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        killMediaPlayer();
    }

    private void killMediaPlayer() {
        if (mediaPlayer != null) {
            try {
                mediaPlayer.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

}
