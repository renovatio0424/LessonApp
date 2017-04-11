package com.example.kimjungwon.lessonapp;

import android.app.DownloadManager;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.InputMismatchException;

/**
 * Created by kimjungwon on 2017-02-14.
 */

public class PHPRequest {
    private URL url;

    public PHPRequest(String url) throws MalformedURLException {
        this.url = new URL(url);
    }

    private static final String TAG = DownloadManager.Request.class.getSimpleName();

    private String readStream(InputStream in) throws IOException {
        StringBuilder jsonHtml = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
        String line = null;

        while ((line = reader.readLine()) != null)
            jsonHtml.append(line);

        reader.close();
        return jsonHtml.toString();
    }

    public String PHPJOIN(final String id, final String name, final String pw) {
        String postData = "id=" + id + "&" + "name=" + name + "&" + "pw=" + pw;
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(5000);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(postData.getBytes("UTF-8"));
            outputStream.flush();
            outputStream.close();
            String result = readStream(connection.getInputStream());
            connection.disconnect();
            return result;
        } catch (IOException e) {
            Log.i("PHPRequest", "request was failed");
            e.printStackTrace();
            return null;
        }
    }

    public String PHPLOGIN(final String id, final String pw) {
        String postData = "id=" + id + "&" + "pw=" + pw;
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(5000);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(postData.getBytes("UTF-8"));
            outputStream.flush();
            outputStream.close();
            String result = readStream(connection.getInputStream());
            connection.disconnect();
            return result;
        } catch (IOException e) {
            Log.i("PHPRequest", "request was failed");
            e.printStackTrace();
            return null;
        }
    }

    public String POSTJSON(final String json_string) {
        String postData = "json=" + json_string;
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(5000);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(postData.getBytes("UTF-8"));
            outputStream.flush();
            outputStream.close();
            String result = readStream(connection.getInputStream());
            connection.disconnect();
            return result;
        } catch (IOException e) {
            Log.i("PHPRequest", "request was failed");
            e.printStackTrace();
            return null;
        }
    }

    private class AreaTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... Strings) {
            try {
                //0:SearchArea_url 1:json String
                PHPRequest request = new PHPRequest(Strings[0]);
                String result = request.POSTJSON(Strings[1]);
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }


    public static String ImgPOST(String serverUrl,String dataToSend){
        try {
            URL url = new URL(serverUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            //set timeout of 30 seconds
            con.setConnectTimeout(1000 * 30);
            con.setReadTimeout(1000 * 30);
            //method
            con.setRequestMethod("POST");
            con.setDoOutput(true);

            OutputStream os = con.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));

            //make request
            writer.write(dataToSend);
            writer.flush();
            writer.close();
            os.close();

            //get the response
            int responseCode = con.getResponseCode();

            if(responseCode == HttpURLConnection.HTTP_OK){
                //read the response
                StringBuilder sb = new StringBuilder();

                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(con.getInputStream()));
                String line;

                //loop through the response from the server
                while ((line = reader.readLine()) != null){
                    sb.append(line).append("\n");
                }

                //return the response
                return sb.toString();
            }else{
                Log.e(TAG,"ERROR - Invalid response code from server "+ responseCode);
                return null;
            }

        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG,"ERROR "+e);
            return null;
        }
    }

}
