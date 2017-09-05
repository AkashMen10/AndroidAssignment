package viu.vuclip.com.coding_assignment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;



import java.util.HashMap;
import java.util.Map;

import viu.vuclip.com.coding_assignment.constants.AppConstants;
import viu.vuclip.com.coding_assignment.utils.CustomVolleyRequest;

public class LoginActivity extends AppCompatActivity {

    EditText etUserName,etUserPwd;
    Button btnLogin;
    String userName,userPwd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
    }
     void initViews()
     {
         etUserName = (EditText) findViewById(R.id.et_userName);
         etUserPwd  = (EditText) findViewById(R.id.et_userPwd);
         btnLogin = (Button) findViewById(R.id.btnLogin);


         // Progress dialog
         pDialog = new ProgressDialog(this);
         pDialog.setCancelable(false);

         etUserName.addTextChangedListener(new TextWatcher() {
             @Override
             public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

             }

             @Override
             public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

             }

             @Override
             public void afterTextChanged(Editable editable) {
//                 if(editable.toString().matches("[A-Za-z0-9]"))
//                    {
                 if(editable.toString().contains("[a-zA-Z ]+") || !editable.toString().contains("\\d+"))
                 {
                     btnLogin.setEnabled(true);
                            userName=editable.toString();
                     btnLogin.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark,null));
                     btnLogin.setTextColor(getResources().getColor(android.R.color.white,null));

                 }
                 else{

                        etUserName.setError("Username should be alphanumeric!");
                        btnLogin.setEnabled(false);

                 }
             }
         });

         etUserPwd.addTextChangedListener(new TextWatcher() {
             @Override
             public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

             }

             @Override
             public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

             }

             @Override
             public void afterTextChanged(Editable editable) {
   //              if(editable.toString().matches("[^A-Za-z0-9]+"))
     //            {
                 if(editable.toString().contains("[a-zA-Z ]+") || !editable.toString().contains("\\d+"))
                 {
                     btnLogin.setEnabled(true);
                     userPwd = editable.toString();
                     btnLogin.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark,null));
                     btnLogin.setTextColor(getResources().getColor(android.R.color.white,null));
                 }

      //           }
                 else{

                         etUserPwd.setError("Password should be alphanumeric!");
                         btnLogin.setEnabled(false);

                 }
             }
         });

         btnLogin.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {


                 if(userName!=null&&userPwd!=null) {
                     checkLogin(userName,userPwd);
                 }
                 else{
                     Toast.makeText(LoginActivity.this,"Please enter email and password.",Toast.LENGTH_SHORT).show();
                 }




             }
         });


     }
    public boolean containsSpecialCharacter(String s) {
        return (s == null) ? false : s.matches("[^A-Za-z0-9 ]");
    }
    /**
     * function to verify login details in mysql db
     * */
    private ProgressDialog pDialog;
    private void checkLogin(final String email, final String password) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";

        pDialog.setMessage("Logging in ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConstants.loginapiUrl, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("#", "Login Response: " + response.toString());
                hideDialog();

                try {

                        // Launch main activity
                        Intent intent = new Intent(LoginActivity.this,
                                MainActivity.class);
                        startActivity(intent);
                        finish();

                } catch (Exception e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("###", "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();
                params.put("username", email);
                params.put("password", password);

                return params;
            }

        };

        // Adding request to request queue
        CustomVolleyRequest.getInstance(this).addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
