package nilay.tipme;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;


public class SignUp extends Activity {

AlertDialog alertDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        TextView imageView = (TextView)findViewById(R.id.btnLinkToLoginScreen);
        imageView.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                finish();
            }
            }
        );
    }
    public void next(View view) {
        EditText editText = (EditText)findViewById(R.id.firstName);
        String firstName = editText.getText().toString();
        EditText editText2 = (EditText)findViewById(R.id.lastName);
        String lastName = editText2.getText().toString();
        EditText editText3= (EditText)findViewById(R.id.phoneNumber);
        String phoneNumber = editText3.getText().toString();
        EditText editText4 = (EditText)findViewById(R.id.zipCode);
        String zipCode = editText4.getText().toString();
        EditText emailText = (EditText)findViewById(R.id.emailRegister);
        final String email=emailText.getText().toString();

        EditText passText = (EditText)findViewById(R.id.passwordRegister);
        String password = passText.getText().toString();
        EditText editText1 = (EditText)findViewById(R.id.streetAddress);
        EditText editText5 = (EditText)findViewById(R.id.city);
        EditText editText6 = (EditText)findViewById(R.id.state);
        String streetAddress = editText1.getText().toString();
        String city = editText5.getText().toString();
        String state  = editText6.getText().toString();
        AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams params = new RequestParams();
        params.put("First_Name",firstName);
        params.put("Phone_Number",phoneNumber);
        params.put("Street_Address",streetAddress);
        params.put("City",city);
        params.put("State",state);
        params.put("ZipCode", zipCode);
        params.put("Last_Name", lastName);
        params.put("Email_Address", email);
        params.put("Password", password);

        client.post("http://54.149.252.188/TipM/Signup.php", params, new AsyncHttpResponseHandler() {
            ProgressDialog progressDialog;


            @Override
            public void onStart() {
                progressDialog = new ProgressDialog(SignUp.this);
                progressDialog.setTitle("Please wait");
                progressDialog.setMessage("Registering...");
                progressDialog.show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("email", email);
                editor.commit();
                progressDialog.dismiss();
                 alertDialog = new AlertDialog.Builder(SignUp.this).create();
                alertDialog.setMessage("Would you like to enter your bank account information in order to send tips?");
                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(getApplicationContext(), MerchantAccount.class));

                    }
                });
                alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(getApplicationContext(), Tabs.class));
                    }
                });
                alertDialog.show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                progressDialog.dismiss();


            }

        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_up, menu);
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
