package com.example.raul4916.avoidthisarea;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by raul4916 on 5/5/2016.
 */
public class DangerMap {
    public double LAT_TO_METER = 111319.49;
    public double LNG_TO_METER = 111302.53;
    public final Filters filters = new Filters();
    protected MapFragment mapFragment;
    protected ArrayList<Marker> currMarkers;
    protected ArrayList<Circle> currCircle;
    protected ArrayList<Marker> prevMarkers;
    protected ArrayList<Circle> prevCircle;

    protected LatLng currCord;
    protected GoogleMap map;

    protected CameraPosition currCameraPos;
    protected DangerMap(final Context context, GoogleMap map) {
        mapFragment = MapFragment.newInstance();
        this.map = map;
        currMarkers = new ArrayList<Marker>();
        currCircle = new ArrayList<Circle>();
        prevMarkers = new ArrayList<Marker>();
        prevCircle = new ArrayList<Circle>();
        currCameraPos = map.getCameraPosition();
        currCord = currCameraPos.target;
        setChangesMap();
        map.setOnMapLongClickListener( new GoogleMap.OnMapLongClickListener(){
            @Override
            public void onMapLongClick(LatLng latLng) {
                Intent intent  = new Intent(context,CreateReport.class);
                Bundle bun = new Bundle();
                intent.putExtra("coordinate",latLng);
                context.startActivity(intent);
            }
        });
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(38.9837911,-76.9459447),14.2f));

    }

    public void setChangesMap(){
        LatLng coord = currCameraPos.target;
//        String urlArea = RequestTask.HOST + "/json/report/area" + filters.getGETAttributes() + "&lat=" + coord.latitude + "&lng=" + coord.longitude;
        String urlReport = RequestTask.HOST + "json/report/info" + filters.getGETAttributes() + "&lat=" + coord.latitude + "&lng=" + coord.longitude;

//        RequestTask requestArea = new RequestTask(urlArea);
        RequestTask requestReport = new RequestTask(urlReport);
//        requestArea.execute();
        requestReport.execute();
//        while (!requestArea.ready) {
//        }
        while (!requestReport.ready) {
        }
        JSONObject resp = requestReport.getResponse();
//        setDangerAreas(makeDangerZones(resp[0]));
        setMarkers(makeMarkers(resp));
        setDangerAreas(makeDangerZones(resp));
        prevMarkers = new ArrayList<Marker>(currMarkers);
//        prevPolygons = new ArrayList<Polygon>(currCircle);
    }
    public float getZoom(){
        return map.getCameraPosition().zoom;
    }
    public LatLng getTarget(){
        return map.getCameraPosition().target;
    }
    public DangerMap(GoogleMap map, double startLat, double startLng) {
        currCord = new LatLng(startLat,startLng);
        mapFragment = MapFragment.newInstance();
    }
    public void setDangerAreas(CircleOptions ... zones){
        for(int i = 0; i<zones.length; i++)
            currCircle.add(map.addCircle(zones[i]));
    }
    public void setMap(GoogleMap map){
        this.map = map;
    }
    public GoogleMap getMap(){
        return map;
    }
    public void setMarkers(MarkerOptions ... markers){
        try {
            for (int i = 0; i < markers.length; i++) {
                Log.i("TEST","REACHED"+markers[i].getSnippet());
                Marker mark = map.addMarker(markers[i]);
                if (prevMarkers.contains(mark)) {
                    prevMarkers.remove(mark);
                }
                currMarkers.add(mark);
                Log.i("TEST",currMarkers.get(0).getSnippet());
            }
            for (int i = 0; i < prevMarkers.size(); i++) {
                prevMarkers.get(i).remove();
            }
            prevMarkers = new ArrayList<Marker>(currMarkers);
        }catch (NullPointerException e){
            e.printStackTrace();
        }
    }
    public Circle[] getCircles(){
        Circle[] circles = new Circle[currMarkers.size()];
        for(int i =0 ;i<currCircle.size();i++){
            circles[i] = currCircle.get(i);
        }
        return circles;
    }
    public Marker[] getMarkers(){
        Marker[] markers = new Marker[currMarkers.size()];
        for(int i =0 ;i<currMarkers.size();i++){
            markers[i] = currMarkers.get(i);
        }
        return markers;
    }
    public static CircleOptions[] makeDangerZones(JSONObject resp){
        CircleOptions[] marks = null;
        try {
            JSONArray response = ((JSONArray)resp.get("response"));
            int respLen = response.length();
            Log.i("DangerMap",response.toString());

            marks = new CircleOptions[respLen];
            for(int i = 0; i<respLen;i++) {
                JSONObject json = (JSONObject) response.get(i);
                Log.i("JSON",json.toString());
                JSONArray coordArr = (JSONArray)json.get("coordinate");
                marks[i] = makeDangerZone((Integer) json.get("dangerLevel"),new LatLng((new Double((String)coordArr.get(0))),new Double((String)coordArr.get(1))));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return marks;
    }
        public static CircleOptions makeDangerZone(int dangerLevel, LatLng coords){
        CircleOptions zone = new CircleOptions().center(coords).radius(100).strokeColor(Color.TRANSPARENT);
            float alpha = .35f;
            switch (dangerLevel){
            case 1:
                zone.fillColor(Color.TRANSPARENT);
                break;
            case 2:
                zone.fillColor(Color.argb(Math.round(Color.alpha(Color.parseColor("#66ff66"))*alpha),102,255,102));
                break;
            case 3:
                zone.fillColor(Color.argb(Math.round(Color.alpha(Color.parseColor("#ffff80"))*alpha),255,255,128));
                break;
            case 4:
                zone.fillColor(Color.argb(Math.round(Color.alpha(Color.parseColor("#ffa500"))*alpha),255,165,0));
                break;
            case 5:
                zone.fillColor(Color.argb(Math.round(Color.alpha(Color.parseColor("#ff4d4d"))*alpha),255,77,77));
                break;
        }
        return zone;
    }
    public MarkerOptions[] makeMarkers(JSONObject resp){
        MarkerOptions[] marks = null;
        try {
            JSONArray response = ((JSONArray)resp.get("response"));
            int respLen = response.length();
            Log.i("DangerMap",response.toString());

            marks = new MarkerOptions[respLen];
            for(int i = 0; i<respLen;i++) {
                JSONObject json = (JSONObject) response.get(i);
                Log.i("JSON",json.toString());
                JSONArray coordArr = (JSONArray)json.get("coordinate");
                marks[i] = makeMarker((String) json.get("type"),(String) json.get("description"),(Integer) json.get("dangerLevel"), new Double((String)coordArr.get(0)),new Double((String)coordArr.get(1)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return marks;
    }
    public static MarkerOptions makeMarker(String type, String description,int dangerLevel, double lat,double lng){

        Log.i("MakeMarker", type+" "+description+" "+dangerLevel+" "+lat+" "+lng);
        MarkerOptions marker = new MarkerOptions();
        marker.position(new LatLng(lat,lng)).title(type).snippet(description + "\nDanger Level:" + dangerLevel).icon(iconMarker(dangerLevel));

        return marker;
    }
    public static double measure(double lat1, double lon1, double lat2, double lon2){  // generally used geo measurement function
        double R = 6378.137; // Radius of earth in KM
        double dLat = (lat2 - lat1) * Math.PI / 180;
        double dLon = (lon2 - lon1) * Math.PI / 180;
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(lat1 * Math.PI / 180) * Math.cos(lat2 * Math.PI / 180) *
                        Math.sin(dLon/2) * Math.sin(dLon/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double d = R * c;
        return d * 1000; // meters
    }

    public static BitmapDescriptor iconMarker(int dangerLevel) {
        BitmapDescriptor icon = null;
        switch (dangerLevel) {
            case 1:
                icon = BitmapDescriptorFactory.fromResource(R.drawable.safe);
                return icon;
            case 2:
                icon = BitmapDescriptorFactory.fromResource(R.drawable.suspicious);
                return icon;
            case 3:
                icon = BitmapDescriptorFactory.fromResource(R.drawable.warning);
                return icon;
            case 4:
                icon = BitmapDescriptorFactory.fromResource(R.drawable.warning);
                return icon;
            case 5:
                icon = BitmapDescriptorFactory.fromResource(R.drawable.hazardous);
                return icon;
        }
        return null;
    }


}
