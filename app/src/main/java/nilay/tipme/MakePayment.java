package nilay.tipme;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


import com.braintreepayments.api.BraintreePaymentActivity;
import com.braintreepayments.api.PaymentRequest;

import com.braintreepayments.api.models.PaymentMethodNonce;

import com.loopj.android.http.AsyncHttpClient;

import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;


/**
 * A simple {@link Fragment} subclass.
 */
public class MakePayment extends android.support.v4.app.Fragment {
    SharedPreferences sharedPreferences;
    String clientToken;
    EditText username;
    AlertDialog.Builder builder;


    View view;

    public MakePayment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.fragment_make_payment, container, false);
        setHasOptionsMenu(true);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());


        Button button = (Button) view.findViewById(R.id.skipButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText edit = (EditText) getActivity().findViewById(R.id.paymentAmount);
                AlertDialog.Builder error = new AlertDialog.Builder(getActivity());
                username = (EditText) getActivity().findViewById(R.id.recipientEmail);
                if (username.getText().toString().equals("") || edit.getText().toString().equals("")) {
                    error.setTitle("Error");
                    error.setMessage("Please enter an email or payment amount");
                }
                if (username.getText().toString().equals(sharedPreferences.getString("email", " "))) {

                    error.setTitle("Error");
                    error.setMessage("You cannot tip yourself :)");
                    error.create();
                    error.show();
                } else {
                    AsyncHttpClient asyncHttpClient1 = new AsyncHttpClient();
                    final ProgressDialog progressDialog = new ProgressDialog(getActivity());
                    progressDialog.setMessage("Please Wait");
                    progressDialog.setTitle("Loading");
                    progressDialog.show();
                    final RequestParams requestParams = new RequestParams();
                    requestParams.put("Email_Address", sharedPreferences.getString("email", " "));
                    requestParams.put("Employee_Email", username.getText().toString());

                    asyncHttpClient1.post("http://54.149.252.188/TipM/GetToken.php", requestParams, new TextHttpResponseHandler() {
                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                            progressDialog.dismiss();
                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, String responseString) {
                            progressDialog.dismiss();
                            AlertDialog.Builder error2 = new AlertDialog.Builder(getActivity());

                            switch (responseString) {

                                case "2":
                                    error2.setTitle("Error");
                                    error2.setMessage("Recipient is not registered for TipMe");
                                    error2.create();
                                    error2.show();
                                    break;
                                case "4":
                                    error2.setTitle("Error");
                                    error2.setMessage("Recipient is not able to receive tips");
                                    error2.create();
                                    error2.show();
                                    break;
                                default:
                                    clientToken = responseString;
                                    PaymentRequest paymentRequest = new PaymentRequest().clientToken(clientToken);
                                    startActivityForResult(paymentRequest.getIntent(getActivity()), REQUEST_CODE);

                            }


                        }
                    });
                    // Customization customization = new Customization.CustomizationBuilder().primaryDescription("Tip").secondaryDescription(null).amount("$" + amount.getText().toString()).build();


                }
            }
        });


        // Inflate the layout for this fragment
        return view;
    }


    int REQUEST_CODE = 100;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                PaymentMethodNonce paymentMethodNonce = data.getParcelableExtra(
                        BraintreePaymentActivity.EXTRA_PAYMENT_METHOD_NONCE
                );
                String nonce = paymentMethodNonce.getNonce();

                postNonceToServer(nonce);
            }
        }
    }

    public void postNonceToServer(String nonce) {
        final EditText editText = (EditText) getActivity().findViewById(R.id.paymentAmount);

         AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("Amount", editText.getText().toString());
        params.put("Customer_Email", sharedPreferences.getString("email", " "));
        params.put("Employee_Email", username.getText().toString());
        params.put("PaymentMethodNonce", nonce);


        client.post("http://54.149.252.188/TipM/PostPayment.php", params, new TextHttpResponseHandler() {

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {


                switch (responseString) {
                    case "5":
                        builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle("Error");
                        builder.setMessage("Cannot make payment");
                        builder.create();
                        builder.show();
                }

                editText.setText("");
                username.setText("");
            }
        });


    }
}
