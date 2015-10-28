package com.example.aa.electronicpatientscard;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ViewPatient extends AppCompatActivity implements View.OnClickListener{
    private EditText editTextID;
    private EditText editTextName;
    private EditText editTextLastname;
    private EditText editTextHistory;

    // TODO: 28.10.2015 images
    private Button buttonUpdate;
    private Button buttonDelete;
    private String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_patient);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent =getIntent();
        id=intent.getStringExtra(Config.PATIENT_ID);

        editTextID=(EditText)findViewById(R.id.editTextID);
        editTextName=(EditText)findViewById(R.id.editTextName);
        editTextLastname=(EditText)findViewById(R.id.editTextLastName);
        editTextHistory=(EditText)findViewById(R.id.editTextHistory);

        buttonDelete=(Button)findViewById(R.id.buttonDelete);
        buttonUpdate=(Button)findViewById(R.id.buttonUpdate);

        buttonUpdate.setOnClickListener(this);
        buttonDelete.setOnClickListener(this);
        editTextID.setText(id);
        getPatient();

    }


    private void getPatient(){
        class GetEmployee extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ViewPatient.this,"Fetching...","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getApplicationContext(),id+": to id, a pobral z S: "+s,Toast.LENGTH_LONG).show();
                showEmployee(s);
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(Config.URL_GET_PATIENT,id);
                return s;
            }
        }
        GetEmployee ge = new GetEmployee();
        ge.execute();
    }

    private void showEmployee(String json){
        Toast.makeText(getApplicationContext(),"Kontroln",Toast.LENGTH_SHORT).show();
        try {
            Toast.makeText(getApplicationContext(),"wszedl",Toast.LENGTH_SHORT).show();
            JSONObject jsonObject = new JSONObject(json);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);
            JSONObject c = result.getJSONObject(0);
            Toast.makeText(getApplicationContext(),"pobral: "+c,Toast.LENGTH_LONG).show();
            String name = c.getString(Config.TAG_NAME);
            String lastname = c.getString(Config.TAG_LASTNAME);
            String history = c.getString(Config.TAG_HISTORY);
            // TODO: 28.10.2015 images
            // TODO: 28.10.2015 fix bug. JSON
            editTextName.setText(name);
            editTextLastname.setText(lastname);
            editTextHistory.setText(history);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
/*

 JSONObject jsonObject = null;
        ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);

            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);
                String id = jo.getString(Config.TAG_ID);
                String name = jo.getString(Config.TAG_NAME);

                HashMap<String,String> employees = new HashMap<>();
                employees.put(Config.TAG_ID,id);
                employees.put(Config.TAG_NAME,name);
                list.add(employees);
            }
 */

    private void updateEmployee(){
        final String name = editTextName.getText().toString().trim();
        final String lastname = editTextLastname.getText().toString().trim();
        final String history = editTextHistory.getText().toString().trim();

        class UpdateEmployee extends AsyncTask<Void,Void,String>{
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ViewPatient.this,"Updating...","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(ViewPatient.this, s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... params) {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put(Config.KEY_PATIENT_ID,id);
                hashMap.put(Config.KEY_PATIENT_NAME,name);
                hashMap.put(Config.KEY_PATIENT_LASTNAME,lastname);
                hashMap.put(Config.KEY_PATIENT_HISTORY,history);
                // TODO: 28.10.2015 images
                RequestHandler rh = new RequestHandler();

                String s = rh.sendPostRequest(Config.URL_UPDATE_PATIENT,hashMap);

                return s;
            }
        }

        UpdateEmployee ue = new UpdateEmployee();
        ue.execute();
    }

    private void deleteEmployee(){
        class DeleteEmployee extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ViewPatient.this, "Updating...", "Wait...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(ViewPatient.this, s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(Config.URL_DELETE_PATIENT, id);
                return s;
            }
        }

        DeleteEmployee de = new DeleteEmployee();
        de.execute();
    }

    private void confirmDeleteEmployee(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure you want to delete this patient?");

        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        deleteEmployee();
                        startActivity(new Intent(ViewPatient.this,ViewPatient.class));
                    }
                });

        alertDialogBuilder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void onClick(View v) {
        if(v == buttonUpdate){
            updateEmployee();
        }

        if(v == buttonDelete){
            confirmDeleteEmployee();
        }
    }
}

