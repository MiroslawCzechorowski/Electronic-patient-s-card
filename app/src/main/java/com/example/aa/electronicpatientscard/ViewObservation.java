package com.example.aa.electronicpatientscard;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ViewObservation extends AppCompatActivity {
    private String id;
    private int observation_Type;
    private Spinner spinner;
    private Button buttonAddNewObservation;
    private ListView listViewObservations;
    String JSON_STRING;
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
        observation_Type=1;
        listViewObservations=(ListView)findViewById(R.id.listViewObservation);
        buttonAddNewObservation=(Button)findViewById(R.id.buttonNewObservation);
        buttonAddNewObservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddObservation.class);
                intent.putExtra(Config.PATIENT_ID,id);
                startActivity(intent);
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
                observation_Type=position;
                getObservation();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        getObservation();

    }

    private void getObservation(){
        class GetObservation extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;
            @Override
            //Show loading dialog
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ViewObservation.this,"Fetching...","Wait...",false,false);
            }

            @Override
            //Dismiss loading dialog and show patient data
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;
                showObservation();
            }

            @Override
            //Sed get request with patient id to server
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(Config.URL_GET_OBSERVATION,id+"&observation_id="+observation_Type);
                return s;
            }
        }
        GetObservation go = new GetObservation();
        go.execute();
    }

    private void showObservation(){
        JSONObject jsonObject = null;
        ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();
        try {
            //Put result from server into array and assign it to ID and last name
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);

            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);
                String time = jo.getString(Config.TAG_TIME);
                String value1 = jo.getString(Config.TAG_VALUE1);
                String value2 = jo.getString(Config.TAG_VALUE2);
                String comment = jo.getString(Config.TAG_COMMENT);
                HashMap<String,String> patients = new HashMap<>();
                if((time!="null") && (comment!="null")){
                    patients.put(Config.TAG_TIME, time);
                    if (value1.length() < 1) {
                        patients.put(Config.TAG_VALUE1, value2);
                    } else {
                        patients.put(Config.TAG_VALUE1, value1);
                    }
                    patients.put(Config.TAG_COMMENT, comment);
                    list.add(patients);
                }else{
                    Toast.makeText(getApplicationContext(),"The history of this medical examination is empty",Toast.LENGTH_LONG).show();
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        //View for patients list
        ListAdapter adapter = new SimpleAdapter(
                ViewObservation.this, list, R.layout.single_observation,
                new String[]{Config.TAG_TIME,Config.TAG_VALUE1,Config.TAG_COMMENT},
                new int[]{R.id.textViewTimeObservation, R.id.textViewValueObservation,R.id.textViewCommentObservation});
        listViewObservations.setAdapter(adapter);
    }
}
