package com.example.raul4916.avoidthisarea;

import android.content.Intent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by raul4916 on 5/6/2016.
 */
public class Filters {
    JSONObject filters;
    public Filters(int[] dangerArea, int[] dangerReports ){
        filters = new JSONObject();
        try {
            filters.put("dangerArea",dangerArea);
            filters.put("dangerReports",dangerReports);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public Filters(){
        filters = new JSONObject();
        int[] dangerLevel= {1,2,3,4,5};
        try {
            filters.put("dangerArea",dangerLevel);
            filters.put("dangerReports",dangerLevel);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getGETAttributes()  {
        String path = "?";
        int[] dangerArea={};
        int[] dangerReports = {};
        try {
            dangerArea = (int[])filters.get("dangerArea");
            dangerReports = (int[])filters.get("dangerReports");
        } catch (JSONException e) {

        }
        path += "dangerArea=";
        for(int i = 0; i<dangerArea.length;i++){
            if(i<dangerReports.length-1)
                path += dangerArea[i]+",";
            else
                path += dangerReports[i]+"&";        }
        path += "dangerReports=";
        for(int i = 0; i<dangerReports.length;i++){
            if(i<dangerReports.length-1)
                path += dangerReports[i]+",";
            else
                path += dangerReports[i]+"&";
        }
        return path;
    }
}
