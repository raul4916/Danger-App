package com.example.raul4916.avoidthisarea;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.google.android.gms.maps.model.LatLng;

public class CreateReport extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_report);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final Context context = this;
        final LatLng coordinate = getIntent().getParcelableExtra("coordinate");
        final EditText description = (EditText)findViewById(R.id.description_edit) ;
        final Spinner dangerType= (Spinner)findViewById(R.id.danger_type_spinner);
        final RadioGroup dangerLevel=(RadioGroup) findViewById(R.id.danger_level_radio);
        final CheckBox policeOnSite= (CheckBox)findViewById(R.id.police_on_site_check);
        String[] arraySpinner = new String[] {
                "Theft w/o weapon", "Theft w/ weapon", "Sexual Assault", "Kidnapping","Battering and Assault","Homicide"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner);
        dangerType.setAdapter(adapter);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int police;
                if(policeOnSite.isChecked()){
                    police = 1;
                }else {
                    police = 0;
                }
                int intDangerLevel=1;
                int radio = dangerLevel.getCheckedRadioButtonId();
                switch (radio){
                    case R.id.one:
                        intDangerLevel=1;
                        break;
                    case R.id.two:
                        intDangerLevel=2;
                        break;
                    case R.id.three:
                        intDangerLevel=3;
                        break;
                    case R.id.four:
                        intDangerLevel=4;
                        break;
                    case R.id.five:
                        intDangerLevel=5;
                        break;
                }
                int spinner = dangerType.getSelectedItemPosition();
                String dangerTypeStr="Theft";
                switch (spinner){
                    case 0:
                        dangerTypeStr="Theft w/o weapon";
                        break;
                    case 1:
                        dangerTypeStr="Theft w/ weapon";
                        break;
                    case 2:
                        dangerTypeStr="Sexual Assault";
                        break;
                    case 3:
                        dangerTypeStr="Kidnapping";
                        break;
                    case 4:
                        dangerTypeStr="Battering and Assault";
                        break;
                    case 5:
                        dangerTypeStr="Homicide";
                        break;


                }
                Report.sendReport(new Report(context,"raul4916",dangerTypeStr,description.getText().toString(),intDangerLevel,police,coordinate));
                finish();
            }
        });
    }

}
