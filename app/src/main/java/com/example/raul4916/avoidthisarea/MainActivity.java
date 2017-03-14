package com.example.raul4916.avoidthisarea;

import android.Manifest;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.PolygonOptions;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback {
    private DangerMap dangerMap;
    @Override
    protected void onResume(){
        super.onResume();
        if(dangerMap!=null) {
            dangerMap.setChangesMap();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LinearLayout menuLayout =(LinearLayout) findViewById(R.id.sideMenu);
        float alpha = .5f;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        MapFragment mapFragment = MapFragment.newInstance();
        FragmentTransaction fragmentTransaction =
                getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.mainView, mapFragment);
        fragmentTransaction.commit();
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},200);
        mapFragment.getMapAsync(this);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabMain);
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dangerMap.setChangesMap();
            }
        });
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    public void onMapReady(GoogleMap map) {
        dangerMap = new DangerMap(this,map);

        PolygonOptions pol = new PolygonOptions();
        LatLng[] l = {new LatLng(39.9951916, -76.9315962),new LatLng(38.9951916, -77.9315962),new LatLng(37.9951916, -76.9315962),new LatLng(38.9951916, -75.9315962)};
        pol.add(l);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            map.setMyLocationEnabled(true);

        }
//        CircleOptions c = new CircleOptions().fillColor(Color.RED).strokeColor(Color.RED).center(l[0]).radius(1000000.0);
//        map.addCircle(new CircleOptions().fillColor(Color.RED).strokeColor(Color.TRANSPARENT).center(l[0]).radius(1000000.0));
//        map.addCircle(new CircleOptions().fillColor(Color.RED).center(l[1]).radius(1000000.0).strokeColor(Color.TRANSPARENT));
//        map.addCircle(new CircleOptions().fillColor(Color.RED).center(l[2]).radius(1000000.0).strokeColor(Color.TRANSPARENT));
//        map.addCircle(new CircleOptions().fillColor(Color.RED).center(l[3]).radius(1000000.0).strokeColor(Color.TRANSPARENT));

    }




    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.rate_the_app) {
        }
        if(id == R.id.support){
            Log.i("Support","REACHED");
            Intent send = new Intent(Intent.ACTION_SENDTO);
            String uriText = "mailto:" + Uri.encode("raul@raulgs.com") +
                    "?subject=" + Uri.encode("Feedback and Assistance") +
                    "&body=" + Uri.encode("Hello Raul,\n");
            Uri uri = Uri.parse(uriText);

            send.setData(uri);
            startActivity(Intent.createChooser(send, "Send mail..."));
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Log.i("Support","REACHED");

        if (id == R.id.satellite_view) {
            dangerMap.getMap().setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        } else if (id == R.id.street_view) {
            dangerMap.getMap().setMapType(GoogleMap.MAP_TYPE_NORMAL);
        } else if (id == R.id.terrain) {
            dangerMap.getMap().setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        }else  if (id == R.id.rate_the_app) {

        }else  if(id == R.id.support){
            String uriText = "mailto:" + Uri.encode("raul@raulgs.com") +
                    "?subject=" + Uri.encode("Feedback and Assistance") +
                    "&body=" + Uri.encode("Hello Raul,\n");
            Uri uri = Uri.parse(uriText);
            Intent send = new Intent(Intent.ACTION_SENDTO,uri);

            send.setData(uri);
            startActivity(Intent.createChooser(send, "Send mail..."));
            Log.i("Support","REACHED");

        }else  if(id == R.id.show_danger_area){
            Circle[] circles = dangerMap.getCircles();
            if(circles[0].isVisible()) {
                for(int i =0; i<circles.length;i++){
                    circles[i].setVisible(false);
                }
            }else {
                for(int i =0; i<circles.length;i++){
                    circles[i].setVisible(true);
                }
                item.setChecked(false);
            }
        }else  if(id == R.id.show_danger_reported){
            Marker[] marks = dangerMap.getMarkers();
            if(marks[0].isVisible()) {
                for(int i =0; i<marks.length;i++){
                    marks[i].setVisible(false);
                }
            }else {
                for(int i =0; i<marks.length;i++){
                    marks[i].setVisible(true);
                }

            }
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
