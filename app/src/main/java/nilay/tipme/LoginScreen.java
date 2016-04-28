package nilay.tipme;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


import com.google.android.gms.iid.InstanceID;
import com.loopj.android.http.*;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.Header;

import java.io.IOException;

import me.pushy.sdk.Pushy;


public class LoginScreen extends AppCompatActivity {
    String token;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Pushy.listen(this);
        setContentView(R.layout.activity_login_screen);
        // Start IntentService to register this application with GCM.

        final AsyncHttpClient asyncHttpClient1 = new AsyncHttpClient();
        asyncHttpClient1.post("http://54.149.252.188/TipM/GetCompanyInfo.php", new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
               String[] info = responseString.split(" ");
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("companyNumber",info[0]);
                editor.putString("companyEmail", info[1]);
                Toast.makeText(getApplicationContext(),info[0],Toast.LENGTH_LONG).show();

            }
        });




    }

    public void login(View view) {




        String authorizedEntity = "tip-me-1178"; // Project id from Google Developer Console
        String scope = "GCM"; // e.g. communicating using GCM, but you can use any
        // URL-safe characters up to a maximum of 1000, or
        // you can also leave it blank.

            token = InstanceID.getInstance(getApplicationContext()).getId();



        EditText editText = (EditText)findViewById(R.id.email);
        final String email = editText.getText().toString();
        EditText editText1 = (EditText)findViewById(R.id.password);
        final String password = editText1.getText().toString();
        if(email.matches("")||password.matches("")) {
            Toast.makeText(this,"Please enter a valid username or password",Toast.LENGTH_SHORT).show();

        }
        else{
            final AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
            RequestParams requestParams = new RequestParams();
            requestParams.put("Device_ID",token);
            requestParams.put("Email_Address",email);
            requestParams.put("Password", password);
            asyncHttpClient.post("http://54.149.252.188/TipM/Login.php", requestParams, new TextHttpResponseHandler() {
                ProgressDialog progressDialog;
                @Override
                public void onStart() {
                     progressDialog = new ProgressDialog(LoginScreen.this);
                    progressDialog.setTitle("Please wait");
                    progressDialog.setMessage("Logging in...");
                    progressDialog.show();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    progressDialog.dismiss();
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        if(responseString.contains("S")) {
                            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("email", email);
                            editor.commit();
                            RegisterForPushNotificationsAsync registerForPushNotificationsAsync = new RegisterForPushNotificationsAsync(getApplicationContext());
                            registerForPushNotificationsAsync.execute();
                            Intent intent =new Intent(getApplicationContext(),Tabs.class);
                            startActivity(intent);
                            progressDialog.dismiss();
                        }
                        else{
                            progressDialog.dismiss();
                           AlertDialog.Builder alertDialog =  new AlertDialog.Builder(LoginScreen.this);
                            alertDialog.setTitle("Login Failure").setMessage("Please enter valid login information");
                            alertDialog.show();
                        }
                }
            });


        }


    }
    public void goToSignUp(View view) {
        Intent intent= new Intent(getApplicationContext(),SignUp.class);
        startActivity(intent);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login_screen, menu);
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



}




