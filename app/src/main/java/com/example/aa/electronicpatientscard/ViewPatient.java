package com.example.aa.electronicpatientscard;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
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
    private RelativeLayout relativeLayout;
    private Button buttonImages;
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

        relativeLayout=(RelativeLayout)findViewById(R.id.relativeLayout);
        relativeLayout.requestFocus();

        editTextID=(EditText)findViewById(R.id.editTextID);
        editTextName=(EditText)findViewById(R.id.editTextName);
        editTextLastname=(EditText)findViewById(R.id.editTextLastName);
        editTextHistory=(EditText)findViewById(R.id.editTextHistory);

        buttonDelete=(Button)findViewById(R.id.buttonDelete);
        buttonUpdate=(Button)findViewById(R.id.buttonUpdate);
        buttonImages=(Button)findViewById(R.id.buttonViewImages);

        buttonImages.setOnClickListener(this);
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
                showPatient(s);
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

    private void showPatient(String json){
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);
            JSONObject c = result.getJSONObject(0);
            String name = c.getString(Config.TAG_NAME);
            String lastname = c.getString(Config.TAG_LASTNAME);
            String history = c.getString(Config.TAG_HISTORY);
            editTextName.setText(name);
            editTextLastname.setText(lastname);
            editTextHistory.setText(history);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void updatePatient(){
        final String name = editTextName.getText().toString().trim();
        final String lastname = editTextLastname.getText().toString().trim();
        final String history = editTextHistory.getText().toString().trim();

        class UpdatePatient extends AsyncTask<Void,Void,String>{
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
            }

            @Override
            protected String doInBackground(Void... params) {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put(Config.KEY_PATIENT_ID,id);
                hashMap.put(Config.KEY_PATIENT_NAME,name);
                hashMap.put(Config.KEY_PATIENT_LASTNAME,lastname);
                hashMap.put(Config.KEY_PATIENT_HISTORY,history);
                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest(Config.URL_UPDATE_PATIENT, hashMap);
                return s;
            }
        }

        UpdatePatient up = new UpdatePatient();
        up.execute();
    }

    private void deletePatient(){
        class DeletePatient extends AsyncTask<Void,Void,String> {
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

        DeletePatient dp = new DeletePatient();
        dp.execute();
    }

    private void confirmDeletePatient(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure you want to delete this patient?");

        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        deletePatient();
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
            updatePatient();
            startActivity(new Intent(ViewPatient.this,PatientsList.class));
        }

        if(v==buttonImages){
            Intent intent = new Intent(this, ViewImages.class);
            intent.putExtra(Config.PATIENT_ID,id);
            startActivity(intent);
        }

        if(v == buttonDelete){
            confirmDeletePatient();
            startActivity(new Intent(ViewPatient.this, PatientsList.class));
        }
    }
}

