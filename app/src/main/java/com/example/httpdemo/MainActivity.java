package com.example.httpdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    private Button btnLoadContent;
    private TextView tvResult;

    private static final String KEY_TEXT = "text";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLoadContent = findViewById(R.id.btn_load_content);
        tvResult = findViewById(R.id.tv_Result);

        btnLoadContent.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                loadWebResult();
            }
        });

        if(savedInstanceState != null){
            if(savedInstanceState.containsKey(KEY_TEXT)){
                tvResult.setText(savedInstanceState.getString(KEY_TEXT));
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putString(KEY_TEXT, tvResult.getText().toString());
    }

    private void loadWebResult() {
        WebRunnable webRunnable = new WebRunnable("https://api.telegram.org/bot1603661014:AAFef7gTKHnAHTItQUayTbuQrT4NVZNovf8/getUpdates");
        new Thread(webRunnable).start();
    }

    class WebRunnable implements Runnable {
        URL url;

        WebRunnable(String url) {
            try {
                this.url = new URL(url);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {

                HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
                urlConn.setRequestMethod("GET");

                InputStream in = urlConn.getInputStream();
                Scanner scanner = new Scanner(in);
                scanner.useDelimiter("\\A");

                Handler mainHandler = new Handler(Looper.getMainLooper());
                String out = "";

                if (scanner.hasNext()) {
                    JSONObject root = new JSONObject(scanner.next());
                    JSONArray results = root.getJSONArray("result");

                    for(int i = 0; i < results.length(); i++){
                        JSONObject result = results.getJSONObject(i);

                        TelegramMessage tm = new TelegramMessage(result);
                        out += tm.getMessageData();


                    }

                    String finalOut = out;



                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            tvResult.setText(finalOut);
                        }
                    });
                }


            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}