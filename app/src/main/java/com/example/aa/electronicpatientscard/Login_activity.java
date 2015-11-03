package com.example.aa.electronicpatientscard;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import java.util.HashMap;

public class Login_activity extends AppCompatActivity implements View.OnClickListener{

    //Shared preferences data
    public static final String PREFS_NAME="LoginData";
    private static final String PREFS_USERNAME = "username";
    private static final String PREFS_PASSWORD="password";
    //UI
    private EditText editTextLogin;
    private EditText editTextPassword;
    private Button buttonLogin;
    private CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ImageView logo = (ImageView)findViewById(R.id.imageView);
        logo.setImageResource(R.drawable.logo);

        //Read login data from shared preferences
        SharedPreferences Login_pref=getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
        String username = Login_pref.getString(PREFS_USERNAME,null);
        String password = Login_pref.getString(PREFS_PASSWORD,null);

        checkBox=(CheckBox)findViewById(R.id.checkBox);
        editTextLogin=(EditText)findViewById(R.id.editTextLogin);
        editTextPassword=(EditText)findViewById(R.id.editTextPassword);
        buttonLogin=(Button)findViewById(R.id.buttonLogin);

        buttonLogin.setOnClickListener(this);
        checkBox.setOnClickListener(this);

        //Fill login and password
        if(editTextLogin.getText().length()==0 || editTextPassword.getText().length()==0){
            editTextLogin.setText(username);
            editTextPassword.setText(password);
        }
        //Ask user for turn on network connection or quit application
        if(isOnline()==false){
            AlertDialog.Builder turnOnInternet = new AlertDialog.Builder(this);
            turnOnInternet.setMessage("Connect to internet or quit application!").setCancelable(false);
            turnOnInternet.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                }
            });
            turnOnInternet.setNegativeButton("Quit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    System.exit(0);
                }
            });
            AlertDialog alert= turnOnInternet.create();
            alert.show();
        }
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
            //Delete username and password if checkbox is not checked and user press login
            if(checkBox.isChecked()==false){
                getSharedPreferences(PREFS_NAME,MODE_PRIVATE).edit().putString(PREFS_USERNAME,"").putString(PREFS_PASSWORD,"").commit();
            }
            login();
        }
        if(v==checkBox){
            //Save login and password
            if(checkBox.isChecked()){
                getSharedPreferences(PREFS_NAME,MODE_PRIVATE).edit().putString(PREFS_USERNAME,editTextLogin.getText().toString().trim()).putString(PREFS_PASSWORD,editTextPassword.getText().toString().trim()).commit();
            }

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

            // Check response from server
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

            // Send username and password to server
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
    //Check for network connection
    public boolean isOnline(){
        ConnectivityManager connectivityManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info=connectivityManager.getActiveNetworkInfo();
        return info!=null&& info.isConnectedOrConnecting();
    }

}
