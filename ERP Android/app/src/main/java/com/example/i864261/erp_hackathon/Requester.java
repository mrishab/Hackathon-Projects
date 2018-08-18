package com.example.i864261.erp_hackathon;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Requester extends AsyncTask<String, Void, Boolean> {

    HttpPost post;
    HttpGet get;
    HttpClient client;
    Context ctx;

    public Requester(Context ctx, String url){
        this.ctx = ctx;
        try{
            post = new HttpPost(url);
            get = new HttpGet(url);
            post.setHeader("Content-Type", "application/json");
            client = new DefaultHttpClient();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected Boolean doInBackground(String... vals) {
        try {
            post.setEntity(new StringEntity(vals[0]));
            HttpResponse response = client.execute(post);
            int responseCode = response.getStatusLine().getStatusCode();
            if (responseCode != 200){
                Log.e("Requester", "Invalid Response Code = " + responseCode);
                return false;
            }
        } catch (IOException e){
            e.printStackTrace();
        }
        return true;
    }

    @Override
    protected void onPostExecute(Boolean b){
        CharSequence text = null;
        if (b){
            text= "Incident report created successfully";
        } else {
            text = "Failed: Backend Service is not available";
        }
        Toast.makeText(ctx, text, Toast.LENGTH_SHORT).show();
    }

}
