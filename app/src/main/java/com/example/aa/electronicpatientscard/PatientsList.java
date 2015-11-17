package com.example.aa.electronicpatientscard;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;

public class PatientsList extends AppCompatActivity implements ListView.OnItemClickListener, View.OnClickListener{
    //UI
    private ListView listViewPatients;
    private String JSON_STRING;
    private Button buttonAddPatient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patients_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //UI
        buttonAddPatient=(Button)findViewById(R.id.buttonAddNew);
        listViewPatients=(ListView)findViewById(R.id.listView);

        listViewPatients.setOnItemClickListener(this);
        buttonAddPatient.setOnClickListener(this);
        getJSON();
    }
    //get JSON data from server
    private void getJSON() {
        class GetJSON extends AsyncTask<Void,Void,String>{
            ProgressDialog loading;
            //Show loading dialog before connect to server
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(PatientsList.this,"Fetching Data","Wait...",false,false);
            }
            //Dismiss loading dialog and list all patients received from server
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;
                showPatient();
            }
            //Send get data request do server
            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s=rh.sendGetRequest(Config.URL_GET_ALL);
                return s;
            }
        }
        GetJSON gj= new GetJSON();
        gj.execute();
    }
    //List all patients received from server to listView
    private void showPatient() {
        JSONObject jsonObject = null;
        ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();
        try {
            //Put result from server into array and assign it to ID and last name
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);

            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);
                String id = jo.getString(Config.TAG_ID);
                String lastname = jo.getString(Config.TAG_LASTNAME);

                HashMap<String,String> patients = new HashMap<>();
                patients.put(Config.TAG_ID,id);
                patients.put(Config.TAG_LASTNAME,lastname);
                list.add(patients);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        //View for patients list
        ListAdapter adapter = new SimpleAdapter(
                PatientsList.this, list, R.layout.list_item,
                new String[]{Config.TAG_ID,Config.TAG_LASTNAME},
                new int[]{R.id.id, R.id.lastname});

        listViewPatients.setAdapter(adapter);
    }
    //See patient detail
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, ViewPatient.class);
        HashMap<String,String> map =(HashMap)parent.getItemAtPosition(position);
        String patientId = map.get(Config.TAG_ID).toString();
        intent.putExtra(Config.PATIENT_ID,patientId);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        if(v==buttonAddPatient){
            Intent intent=new Intent(PatientsList.this,AddPatient.class);
            startActivity(intent);
        }
    }
}
