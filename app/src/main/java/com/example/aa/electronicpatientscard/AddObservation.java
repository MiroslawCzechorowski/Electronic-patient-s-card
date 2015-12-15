package com.example.aa.electronicpatientscard;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class AddObservation extends AppCompatActivity {
    private String id;
    private Spinner spinner;
    private EditText testResult;
    private EditText description;
    private EditText date;
    private Button button;
    private int observation_Type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_observation);
        Intent intent =getIntent();
        id=intent.getStringExtra(Config.PATIENT_ID);
        testResult=(EditText)findViewById(R.id.editTextTestResult);
        description=(EditText)findViewById(R.id.editTextDescription);
        date=(EditText)findViewById(R.id.editTextDate);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentDateandTime = sdf.format(new Date());
        date.setText(currentDateandTime);

        button=(Button)findViewById(R.id.buttonAddObservation);
        observation_Type=1;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addObservation();
            }
        });
        spinner=(Spinner)findViewById(R.id.spinner2);

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
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void addObservation() {
        //New user data
        final String result = testResult.getText().toString().trim();
        final String descr = description.getText().toString().trim();
        final String time = date.getText().toString().trim();

        class AddPatients extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;

            @Override
            //Show loading dialog before execute
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(AddObservation.this,"Adding...","Wait...",false,false);
            }

            @Override
            //Dismiss loading dialog after execute
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(AddObservation.this, s, Toast.LENGTH_LONG).show();
            }

            @Override
            //Send new user data to server
            protected String doInBackground(Void... v) {
                HashMap<String,String> params = new HashMap<>();
                params.put(Config.KEY_PATIENT_ID,id);
                String obser=Integer.toString(observation_Type);
                params.put(Config.KEY_OBSERVATION_ID,obser);
                params.put(Config.KEY_DATE,time);
                params.put(Config.KEY_DESCRIPTION,descr);
                params.put(Config.KEY_VALUE,result);

                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(Config.URL_ADD_OBSERVATION, params);
                return res;
            }
        }

        AddPatients newPatient = new AddPatients();
        newPatient.execute();
    }

}
