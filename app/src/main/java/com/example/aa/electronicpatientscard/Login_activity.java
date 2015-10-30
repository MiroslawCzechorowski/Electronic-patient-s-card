package com.example.aa.electronicpatientscard;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import java.util.HashMap;

public class Login_activity extends AppCompatActivity implements View.OnClickListener{

    private EditText editTextLogin;
    private EditText editTextPassword;
    private Button buttonLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ImageView logo = (ImageView)findViewById(R.id.imageView);
        logo.setImageResource(R.drawable.logo);

        editTextLogin=(EditText)findViewById(R.id.editTextLogin);
        editTextPassword=(EditText)findViewById(R.id.editTextPassword);
        buttonLogin=(Button)findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if(v==buttonLogin){
            login();
        }
    }

    private void login() {
        final String login=editTextLogin.getText().toString().trim();
        final String password=editTextPassword.getText().toString().trim();

        class LoginUser extends AsyncTask<Void,Void,String>{
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Login_activity.this,"Logging...","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                if(s.equalsIgnoreCase("success")){
                    Intent intent=new Intent(Login_activity.this,PatientsList.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(),"Invalid user name or password",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            protected String doInBackground(Void... v) {
                HashMap<String,String> params=new HashMap<>();
                params.put(Config.KEY_LOGIN,login);
                params.put(Config.KEY_PASSWORD,password);
                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(Config.URL_LOGIN, params);
                return res;
            }
        }
        LoginUser newLogin = new LoginUser();
        newLogin.execute();
    }

}
