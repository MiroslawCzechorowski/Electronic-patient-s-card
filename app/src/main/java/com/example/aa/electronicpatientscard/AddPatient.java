package com.example.aa.electronicpatientscard;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

public class AddPatient extends AppCompatActivity implements  View.OnClickListener {
    private EditText editTextID;
    private EditText editTextName;
    private EditText editTextVorname;
    private EditText editTextHistory;
    private Button buttonAdd;
    private Button buttonCancel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_patient);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        editTextID=(EditText)findViewById(R.id.editTextID);
        editTextName=(EditText)findViewById(R.id.editTextName);
        editTextVorname=(EditText)findViewById(R.id.editTextAddVorname);
        editTextHistory=(EditText)findViewById(R.id.editTextHistory);
        buttonAdd=(Button)findViewById(R.id.buttonAddPatient);
        buttonCancel=(Button)findViewById(R.id.buttonCancel);

        buttonCancel.setOnClickListener(this);
        buttonAdd.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        if(v == buttonAdd){
            final String id =editTextID.getText().toString().trim();
            addPatient();
            AlertDialog.Builder addImages = new AlertDialog.Builder(this);
            addImages.setMessage("Do you want add images ?").setCancelable(false);
            addImages.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(AddPatient.this, ViewImages.class);
                    intent.putExtra(Config.PATIENT_ID, id);
                    startActivity(intent);
                }
            });
            addImages.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(AddPatient.this, PatientsList.class);
                    startActivity(intent);
                }
            });
            AlertDialog alert= addImages.create();
            alert.show();
        }

        if(v == buttonCancel){
            startActivity(new Intent(this, PatientsList.class));
        }
    }

    private void addPatient() {

            final String id = editTextID.getText().toString().trim();
            final String name = editTextName.getText().toString().trim();
            final String lastname = editTextVorname.getText().toString().trim();
            final String history= editTextHistory.getText().toString().trim();

            class AddPatients extends AsyncTask<Void,Void,String> {

                ProgressDialog loading;

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    loading = ProgressDialog.show(AddPatient.this,"Adding...","Wait...",false,false);
                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    loading.dismiss();
                    Toast.makeText(AddPatient.this, s, Toast.LENGTH_LONG).show();
                }

                @Override
                protected String doInBackground(Void... v) {
                    HashMap<String,String> params = new HashMap<>();
                    params.put(Config.KEY_PATIENT_NAME,name);
                    params.put(Config.KEY_PATIENT_HISTORY,history);
                    params.put(Config.KEY_PATIENT_LASTNAME,lastname);
                    params.put(Config.KEY_PATIENT_ID,id);
                    // TODO: 28.10.2015 insert images

                    RequestHandler rh = new RequestHandler();
                    String res = rh.sendPostRequest(Config.URL_ADD, params);
                    return res;
                }
            }

            AddPatients newPatient = new AddPatients();
            newPatient.execute();

    }
}
