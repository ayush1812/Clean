package com.example.aayush.clean;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    EditText editTextUsername, editTextEmail, editTextPassword, getEditTextPhoneno ,editTextState;
    Button buttonRegister;
    private ProgressDialog progressDialog;
    RequestQueue requestQueue;
    String statere;
    Spinner spinnerCity,spinnerState;
    List<String> list;
    String state;
    String city;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_new);

        /*if (SharedPrefManager.getInstance(this).isUserLoggedin()) {
            finish();
            startActivity(new Intent(this, ProfileActivity.class));
            return;
        }*/

        editTextUsername = (EditText) findViewById(R.id.editTextUsername);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        getEditTextPhoneno = (EditText) findViewById(R.id.editTextPhoneno);
        buttonRegister = (Button) findViewById(R.id.buttonRegister);
        spinnerCity=(Spinner)this.findViewById(R.id.spinnerCity);
        spinnerState=(Spinner)this.findViewById(R.id.spinnerState);
        spinnerState.setOnItemSelectedListener(this);
        progressDialog = new ProgressDialog(this);
        buttonRegister.setOnClickListener(this);

    }


    private void registerUser() {
        Log.e("MainActvity","Register User");
        final String username = editTextUsername.getText().toString().trim();
        final String email = editTextEmail.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();
        final String phone = getEditTextPhoneno.getText().toString().trim();
        state = spinnerState.getSelectedItem().toString();
        /*inal String State=state;*/
        city=spinnerCity.getSelectedItem().toString();
        Log.e("state:",state);
        Log.e("city:",city);



        if (username.isEmpty()) {
            Log.e("username ","error");
            //Toast.makeText(this, "username", Toast.LENGTH_SHORT).show();
            editTextUsername.setError("Username can't be empty");
            editTextUsername.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            Log.e("password ","error");
            //Toast.makeText(this, "password", Toast.LENGTH_SHORT).show();
            editTextPassword.setError("Password field cannot be empty");
            editTextPassword.requestFocus();
            return;
        }
        if (email.isEmpty()) {
            //Toast.makeText(this, "Email", Toast.LENGTH_SHORT).show();
            editTextEmail.setError("Enter Mail");
            editTextEmail.requestFocus();
            return;
        }
        if (phone.isEmpty()) {
            getEditTextPhoneno.setError("Enter 10 digit phone no.");
            getEditTextPhoneno.requestFocus();
            return;
        }

        String Email = "";
        final boolean email1 = isValidMail(email);
        if (email1 == true) {
            Email = editTextEmail.getText().toString().trim();
        } else {

            getEditTextPhoneno.setText("");
            getEditTextPhoneno.requestFocus();
        }

        String pno = "";
        final boolean phoneno = isValidMobile(phone);
        Log.e("MainActvity","phodskjakfhjksadn");
        if (phoneno == true && phone.length() == 10) {

            pno = getEditTextPhoneno.getText().toString().trim();
        } else {

            getEditTextPhoneno.setText("");
            getEditTextPhoneno.requestFocus();
        }

        progressDialog.setMessage("Registering user.....");
        progressDialog.show();

        final String finalPno = pno;
        final String finalEmail = Email;

        getState();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_REGISTER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                // Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                if (username.toString().isEmpty()) {
                    Log.e("MainActivity","Hash Map for username");
                    editTextUsername.setError("Username can't be empty");

                } else if (username.length() < 5) {
                    Log.e("MainActivity","Hash Map for username length");
                    editTextUsername.setError("Username Should min of 6");
                                   } else {
                    params.put("username", username);

                }

                if (password.isEmpty()) {
                    editTextPassword.setError("Password field cannot be empty");

                } else if (password.length() < 5 || password.contains(" ")) {
                    editTextPassword.setError("Password Should min of 6 and No spaces");

                } else {
                    params.put("password", password);

                }
                params.put("email", finalEmail);
                params.put("phoneno", finalPno);
                params.put("state",state);
                params.put("city",city);
                return params;
            }
        };

        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }

    @Override
    public void onClick(View v) {
        if (v == buttonRegister)
            if (checkFields()) {
                registerUser();
            } else
                Toast.makeText(this, "Complete Profile Properly", Toast.LENGTH_SHORT).show();
    }

    public boolean checkFields() {

       /* if (editTextUsername.getText().toString() == null) {
            Toast.makeText(this, "username", Toast.LENGTH_SHORT).show();
            editTextUsername.setError("Username can't be empty");
            editTextUsername.requestFocus();
            return false;
        }
        if (editTextPassword == null) {
            Toast.makeText(this, "password", Toast.LENGTH_SHORT).show();
            editTextPassword.setError("Password field cannot be empty");
            editTextPassword.requestFocus();
            return false;
        }
        if (editTextEmail == null) {
            Toast.makeText(this, "Email", Toast.LENGTH_SHORT).show();
            editTextEmail.setError("Enter Mail");
            editTextEmail.requestFocus();
            return false;
        }
        if (getEditTextPhoneno == null) {
            getEditTextPhoneno.setError("Enter 10 digit phone no.");
            getEditTextPhoneno.requestFocus();
            return false;
        }*/
        return true;

    }

    private boolean isValidMobile(String phone) {
        boolean check = false;
        if (!Pattern.matches("[a-zA-Z]+", phone)) {
            if (phone.length() != 10) {
                check = false;
                getEditTextPhoneno.setError("Not Valid Number");
            } else {
                check = true;
            }
        } else {
            check = false;
        }
        return check;
    }

    private boolean isValidMail(String email) {
        boolean check;
        Pattern p;
        Matcher m;

        String EMAIL_STRING = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        p = Pattern.compile(EMAIL_STRING);

        m = p.matcher(email);
        check = m.matches();

        if (!check) {
            editTextEmail.setError("Not Valid Email");
        }
        return check;
    }

    public void getState(){
        requestQueue =  Volley.newRequestQueue(this);
        JsonArrayRequest request = new JsonArrayRequest("https://api.myjson.com/bins/urt55", new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for(int i=0;i<response.length();i++){
                    try {
                        String str =response.getJSONObject(i).getString("state");
                        /*tv.append(str+"\n");*/


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(request);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        statere = spinnerState.getSelectedItem().toString();
        RequestQueue requestQueue = Volley.newRequestQueue(this);


        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Loading..");
        dialog.show();

        JsonArrayRequest jsonArrayRequest =
                new JsonArrayRequest("https://api.myjson.com/bins/urt55", new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            String[] str = new String[response.length()];
                            list = new ArrayList<>();
                            int k=0;
                            for (int i = 0; i < response.length(); i++) {
                                if(response.getJSONObject(i).getString("state").contentEquals(statere)) {
                                    str[i] = response.getJSONObject(i).getString("city_name");
                                    //Log.e("City",str[i]);
                                    list.add(str[i]);
                                    k++;
                                }
                            }
                            spinnerCity.setAdapter(
                                    new ArrayAdapter<String>
                                            (MainActivity.this,R.layout.support_simple_spinner_dropdown_item,list));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        dialog.dismiss();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                        Toast.makeText(MainActivity.this, "ERROR: "+error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        requestQueue.add(jsonArrayRequest);

/////
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}
