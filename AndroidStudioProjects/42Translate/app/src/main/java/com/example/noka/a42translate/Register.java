package com.example.noka.a42translate;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity implements  View.OnClickListener{
    private static final String REGISTER_URL = "http://192.168.1.111/42translate/register.php";

    public static final String KEY_USERNAME="name";
    public static final String KEY_PASSWORD="password";
    public static final String KEY_REPEAT="repeat_password";
    public static final String KEY_USERTYPE="usertype";
    public static final String KEY_EMAIL="email";


    private EditText editTextUsername;
    private EditText editTextPassword;
    private EditText editTextRepeat;
    private EditText editTextEmail;
    private Spinner userSpinner;

    private Button buttonLogin;
    private Button buttonRegister;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        editTextUsername = (EditText) findViewById(R.id.editUsername);
        editTextPassword = (EditText) findViewById(R.id.editPassword);
        editTextRepeat = (EditText) findViewById(R.id.editTextRepeatPass);
        editTextEmail = (EditText) findViewById(R.id.editEmail);

        //userSpinner = (Spinner) findViewById(R.id.userSpinner);

        buttonLogin = (Button) findViewById(R.id.btnLogin);
        buttonRegister = (Button) findViewById(R.id.btnRegister);

        buttonLogin.setOnClickListener(this);
        buttonRegister.setOnClickListener(this);
    }

    private void registerUser(){
        final String name = editTextUsername.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();
        final String email = editTextEmail.getText().toString().trim();
        //final String usertype = userSpinner.toString().trim();
        final String repeat_password = editTextRepeat.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        editTextEmail.setText(null);
                        editTextPassword.setText(null);
                        editTextUsername.setText(null);
                        editTextRepeat.setText(null);
                        Toast.makeText(Register.this,response,Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Register.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){

            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put(KEY_USERNAME,name);
                params.put(KEY_PASSWORD,password);
                params.put(KEY_REPEAT, repeat_password);
                params.put(KEY_EMAIL, email);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    @Override
    public void onClick(View v) {
        if(v == buttonRegister){
            if(editTextPassword.getText().toString().trim().equals(editTextRepeat.getText().toString().trim())){
                registerUser();

            }
            else {
                Toast.makeText(Register.this,"The Passwords must match, dude!",Toast.LENGTH_LONG).show();
            }

        }
        if(v == buttonLogin){
            startActivity(new Intent(this,LoginActivity.class));
        }

    }
}
