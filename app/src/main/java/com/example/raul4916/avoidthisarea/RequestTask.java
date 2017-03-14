package com.example.raul4916.avoidthisarea;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by raul4916 on 5/4/2016.
 */
public class RequestTask extends AsyncTask<Void,Void,JSONObject> {
    static String HOST = "http://danger-app.raulgs.com/";
    private URL url;
    private String method;
    private JSONObject data;
    private JSONObject response;
    boolean ready = false;
    public RequestTask(String url) {
        super();
        try {
            this.url = new URL(url);
        }catch (MalformedURLException e){
            e.printStackTrace();
        }
        this.method = "GET";
        data = null;
    }
    public RequestTask(String url, String method, JSONObject data){
        super();
        try {
            this.url = new URL(url);
        }catch (MalformedURLException e){
            e.printStackTrace();
        }
        this.method = method;
        this.data = data;
        Log.i("TEST","Created here 44");
    }
    @Override
    protected JSONObject doInBackground(Void ... params) {
        Log.i("TEST","Background started");
        JSONObject obj = requestURL(url,method,data);
        try {
            this.response = new JSONObject(obj.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("TEST",this.response.toString());
        ready = true;

        return obj;

    }
    protected void onPostExecute(JSONObject result){
        Log.i("TEST","DONE!!!");
        ready = true;
        this.response = result;
    }

    protected JSONObject requestURL(URL url, String method,JSONObject request){
        BufferedWriter out;
        JSONObject json = new JSONObject();
        HttpURLConnection connection;
        int responseCode;
        String response;
        if(method.equals("GET")){
            try{
                Log.i("TEST","Reached Request GET");
                connection = (HttpURLConnection) url.openConnection();
                responseCode = connection.getResponseCode();
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                response = in.readLine();
                Log.i("TEST",response+" ----"+responseCode);

                json.put("response", new JSONArray(response));
                json.put("responseCode", new JSONObject().put("code",responseCode));
                connection.disconnect();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }else if(method.equals("POST")){

            try{
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                connection.setRequestProperty("Accept", "application/json");

                out = new BufferedWriter(new OutputStreamWriter((connection.getOutputStream())));
                out.write(request.toString());
                out.flush();
//                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//                response = in.readLine();
                responseCode = connection.getResponseCode();
                Log.i("HTTPCode",""+request.toString());
                Log.i("HTTPCode",""+url.toString());
                Log.i("HTTPCode",""+connection.getContentType());
                Log.i("HTTPCode",""+connection.getRequestMethod());

                Log.i("HTTPCode",""+responseCode);
//                json.put("response",  new JSONObject(response));
                json.put("responseCode", responseCode);
                connection.disconnect();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return json;
    }
    public JSONObject getResponse() {
        JSONObject temp = null;
        if (ready) {
            try {
                temp = new JSONObject(this.response.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return temp;

    }
}

