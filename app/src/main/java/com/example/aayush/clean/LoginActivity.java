package com.example.aayush.clean;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    EditText editTextUsername,editTextPassword;
    Button buttonLogin;
    private TextView textViewForgotpassword,textViewRegister;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actvity_login_new);

        if (SharedPrefManager.getInstance(this).isUserLoggedin()){
            finish();
            startActivity(new Intent(this,ProfileActivity.class));
            return;
        }

        editTextUsername = (EditText)findViewById(R.id.editTextUsername);
        editTextPassword = (EditText)findViewById(R.id.editTextPassword);
        buttonLogin = (Button)findViewById(R.id.buttonLogin);
        textViewForgotpassword = (TextView)findViewById(R.id.textViewForgotpassword);
        textViewRegister = (TextView)findViewById(R.id.textViewRegister);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait");
        textViewForgotpassword.setOnClickListener(this);
        textViewRegister.setOnClickListener(this);
        buttonLogin.setOnClickListener(this);
    }

    private void userLogin(){
        final String username = editTextUsername.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();

        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_LOGIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject object = new JSONObject(response);
                    if (!object.getBoolean("error")){
                        SharedPrefManager.getInstance(getApplicationContext()).userLogin(object.getInt("id"),object.getString("username"),object.getString("email"));
                        /*Toast.makeText(LoginActivity.this,"User Login Successfull" , Toast.LENGTH_LONG).show();*/
                        startActivity(new Intent(getApplicationContext(),MainScreen.class));
                        finish();
                        
                    }else{
                        Toast.makeText(LoginActivity.this,object.getString("message") , Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(LoginActivity.this,error.getMessage() , Toast.LENGTH_LONG).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("username",username);
                params.put("password",password);
                return params;
            }
        };

    RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }

    @Override
    public void onClick(View v) {
        if (v == buttonLogin){
            userLogin();
        }
        //TODO:forgot password activity opens
        if (v == textViewForgotpassword){
            startActivity(new Intent(this,MainActivity.class));
        }
        //TODO:Refister here for new user activity opens
        if (v == textViewRegister){
            startActivity(new Intent(this,MainActivity.class));
        }
    }
}
