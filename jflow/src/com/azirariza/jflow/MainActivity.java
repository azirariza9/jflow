package com.azirariza.jflow;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class MainActivity extends Activity {
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tv = new TextView(this);
        tv.setText("Loading...");
        setContentView(tv);

        new HttpGetTask().execute($IP);
    }

    private class HttpGetTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String response = null;

            try {
                URI uri = URI.create(urls[0]);
                URL url = uri.toURL();
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setConnectTimeout(5000); // 5 seconds timeout
                urlConnection.setReadTimeout(5000);   // 5 seconds timeout

                int responseCode = urlConnection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    StringBuilder stringBuilder = new StringBuilder();
                    reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

                    String line;
                    while ((line = reader.readLine()) != null) {
                        stringBuilder.append(line);
                    }
                    response = stringBuilder.toString();
                } else {
                    response = "Error: " + responseCode;
                }
            } catch (Exception e) {
                response = "Exception: " + e.getMessage();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            tv.setText(result != null ? result : "No response");
        }
    }
}