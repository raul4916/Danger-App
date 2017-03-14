package com.example.raul4916.avoidthisarea;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by raul4916 on 5/5/2016.
 */
public class Report {
    String type, description,username;
    int dangerLevel;
    int policeOnSite;
    LatLng coordinate;
    static Context context;
    public Report(Context context, String username, String type, String description, int dangerLevel, int policeOnSite, LatLng coordinate){
        this.username = username;
        this.type = type;
        this.description = description;
        this.dangerLevel = dangerLevel;
        this.policeOnSite = policeOnSite;
        this.coordinate = coordinate;
        this.context = context;
    }
    public boolean sendReport(){
        return sendReport(this);
    }
    public static boolean sendReport(Report report){
        String url = RequestTask.HOST+"json/report";
        Toast.makeText(context,url,Toast.LENGTH_LONG).show();
        RequestTask request = new RequestTask(url,"POST",report.toJSON());
        request.execute();
        while(!request.ready){
        }
        try {
            if((int)request.getResponse().get("responseCode")==200){
                return true;

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }
    public JSONObject toJSON(){
        JSONObject json = new JSONObject();
        try {
            json.put("username",username);
            json.put("type",type);
            json.put("description",description);
            json.put("dangerLevel",""+dangerLevel);
            json.put("policeOnSite", ""+policeOnSite);
            json.put("lat",""+coordinate.latitude);
            json.put("long",""+coordinate.longitude);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }
    public String toString(){
        return toJSON().toString();
    }
    public String toQuery(){
        return null;
    }
    public static Report getReports(double lat,double lng, int radius){
        return null;
    }
    public View displayReport(){
        return null;
    }
}
