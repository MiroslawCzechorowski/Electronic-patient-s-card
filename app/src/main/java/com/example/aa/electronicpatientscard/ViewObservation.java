package com.example.aa.electronicpatientscard;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.internal.widget.ButtonBarLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class ViewObservation extends AppCompatActivity {
    private String id;
    private Spinner spinner;
    private Button buttonAddNewObservation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_observation);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Observation"));
        tabLayout.addTab(tabLayout.newTab().setText("Person"));
        tabLayout.addTab(tabLayout.newTab().setText("Images"));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setSelected(false);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition()==1){
                    Intent intent = new Intent(getApplicationContext(), ViewPatient.class);
                    intent.putExtra(Config.PATIENT_ID,id);
                    startActivity(intent);
                }else if (tab.getPosition()==2){
                    Intent intent = new Intent(getApplicationContext(), ViewImages.class);
                    intent.putExtra(Config.PATIENT_ID,id);
                    startActivity(intent);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        Intent intent =getIntent();

        //Assign ID clicked patient
        id=intent.getStringExtra(Config.PATIENT_ID);
        buttonAddNewObservation=(Button)findViewById(R.id.buttonNewObservation);
        buttonAddNewObservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"nowy ekran",Toast.LENGTH_LONG).show();
            }
        });
        spinner=(Spinner)findViewById(R.id.spinner);
        // Create an ArrayAdapter using the string array and a default spinner
        ArrayAdapter<CharSequence> staticAdapter = ArrayAdapter
                .createFromResource(this, R.array.observation_array, R.layout.spinner_layout);
        // Specify the layout to use when the list of choices appears

        staticAdapter.setDropDownViewResource(R.layout.spinner_layout);

        // Apply the adapter to the spinner
        spinner.setAdapter(staticAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.v("item", (String) parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }
}
