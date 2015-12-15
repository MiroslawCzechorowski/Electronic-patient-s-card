package com.example.aa.electronicpatientscard;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
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
    private RelativeLayout relativeLayout;
    private Button buttonUpdate;
    private Button buttonDelete;
    private String id;
    private EditText editTextSex;
    private EditText editTextDateOfBirth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_patient);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Person"));
        tabLayout.addTab(tabLayout.newTab().setText("Images"));
        tabLayout.addTab(tabLayout.newTab().setText("Observation"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
            if (tab.getPosition()==2){
                Intent intent = new Intent(getApplicationContext(), ViewObservation.class);
                intent.putExtra(Config.PATIENT_ID,id);
                startActivity(intent);
            }else if (tab.getPosition()==1){
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
        //UI
        relativeLayout=(RelativeLayout)findViewById(R.id.relativeLayout);
        relativeLayout.requestFocus();

        editTextID=(EditText)findViewById(R.id.editTextID);
        editTextName=(EditText)findViewById(R.id.editTextName);
        editTextLastname=(EditText)findViewById(R.id.editTextLastName);
        editTextDateOfBirth=(EditText)findViewById(R.id.editTextDateOfBirth);
        editTextSex=(EditText)findViewById(R.id.editTextSex);

        buttonDelete=(Button)findViewById(R.id.buttonDelete);
        buttonUpdate=(Button)findViewById(R.id.buttonUpdate);
        buttonUpdate.setOnClickListener(this);
        buttonDelete.setOnClickListener(this);
        editTextID.setText(id);
        //List all data from patient
        getPatient();

    }


    private void getPatient(){
        class GetPatient extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;
            @Override
            //Show loading dialog
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ViewPatient.this,"Fetching...","Wait...",false,false);
            }

            @Override
            //Dismiss loading dialog and show patient data
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                showPatient(s);

                // Check from ID patient sex
                String sex = editTextID.getText().toString().trim();
                sex = sex.substring(9, 10);
                if (Integer.parseInt(sex) % 2 == 0) {
                    editTextSex.setText("Female");
                } else editTextSex.setText("Male");

                //Check patients date of birth
                String idText = editTextID.getText().toString().trim();
                String date = idText.substring(0, 2);
                if (Integer.parseInt(date) > 15) {
                    editTextDateOfBirth.setText("19" + date + ".");
                } else editTextDateOfBirth.setText("20" + date + ".");
                date=idText.substring(3,5);
                editTextDateOfBirth.setText(editTextDateOfBirth.getText()+date+".");
                date=idText.substring(5,7);
                editTextDateOfBirth.setText(editTextDateOfBirth.getText()+date);
            }

            @Override
            //Sed get request with patient id to server
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(Config.URL_GET_PATIENT,id);
                return s;
            }
        }
        GetPatient ge = new GetPatient();
        ge.execute();
    }

    private void showPatient(String json){
        //Assign receive data from server to patients data
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);
            JSONObject c = result.getJSONObject(0);
            String name = c.getString(Config.TAG_NAME);
            String lastname = c.getString(Config.TAG_LASTNAME);
            editTextName.setText(name);
            editTextLastname.setText(lastname);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //Method that allows update patients data and send those changes to server
    private void updatePatient(){
        final String name = editTextName.getText().toString().trim();
        final String lastname = editTextLastname.getText().toString().trim();

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
            //send updated data to server
            protected String doInBackground(Void... params) {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put(Config.KEY_PATIENT_ID,id);
                hashMap.put(Config.KEY_PATIENT_NAME,name);
                hashMap.put(Config.KEY_PATIENT_LASTNAME,lastname);
                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest(Config.URL_UPDATE_PATIENT, hashMap);
                return s;
            }
        }

        UpdatePatient up = new UpdatePatient();
        up.execute();
    }
    //Method that allows user to delete patient from server
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
    //Patient delete confirmation dialog
    private void confirmDeletePatient(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure you want to delete this patient?");

        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        deletePatient();
                        startActivity(new Intent(ViewPatient.this, PatientsList.class));
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
        //onBackPressed();
    }
    //Handle buttons click
    @Override
    public void onClick(View v) {
        if(v == buttonUpdate){
            updatePatient();
            startActivity(new Intent(ViewPatient.this,PatientsList.class));
        }

        if(v == buttonDelete){
            confirmDeletePatient();
            //startActivity(new Intent(ViewPatient.this, PatientsList.class));
        }
    }
}

