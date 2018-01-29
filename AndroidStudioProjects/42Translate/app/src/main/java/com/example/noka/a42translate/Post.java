package com.example.noka.a42translate;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class Post extends AppCompatActivity implements View.OnClickListener{


    EditText editTextComment;
    Button buttonExit;
    Button buttonComment;

    private static final String COMMENT_URL = "http://10.42.0.1/42translate/addPost.php";
    public static final String KEY_DATE = "date_submitted";
    public static final String KEY_POST = "post";
    public static final String KEY_USER = "submitted_by";
    DatabaseHelper dbhelper;

    public String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_stuff);

        editTextComment = (EditText)findViewById(R.id.ed_post);
        buttonComment = (Button)findViewById(R.id.post_up);
        buttonExit = (Button)findViewById(R.id.resetPost);


        buttonExit.setOnClickListener(this);
        buttonComment.setOnClickListener(this);
        dbhelper =new DatabaseHelper(this);
        dbhelper.getUserName();

    }

    public void postStuff() {

        try {
            Cursor cus = dbhelper.select_user();
            if (cus.getCount() == 0) {
                Intent intent=new Intent(this,LoginActivity.class);
                startActivity(intent);
                Toast.makeText(getApplicationContext(),"You got register first to be able to comment", Toast.LENGTH_LONG).show();
                finish();


            }else {
                while(cus.moveToNext()) {
                    userId = cus.getString(0);
                }

                final String post = editTextComment.getText().toString().trim();
                final String date = getCurrentTime();
                final String user = userId.trim();


                StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.POST, COMMENT_URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Toast.makeText(Post.this, response, Toast.LENGTH_LONG).show();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(Post.this, error.toString(), Toast.LENGTH_LONG).show();
                            }
                        }) {

                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put(KEY_POST, post);
                        params.put(KEY_DATE, date);
                        params.put(KEY_USER, user);

                        return params;
                    }

                };

                RequestQueue requestQueue = Volley.newRequestQueue(this);
                requestQueue.add(stringRequest);

            }
        } catch (SQLiteException se) {

            Log.e(getClass().getSimpleName(), "Open the database");
        }

    }
    public static String getCurrentTime() {
        try {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentTimeStamp = dateFormat.format(new Date()); // Find todays date

            return currentTimeStamp;
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }

    @Override
    public void onClick(View v) {
        if(v == buttonComment){
            if(editTextComment.getText().toString().trim().length() > 0){
                postStuff();
                editTextComment.setText(null);
                finish();
            }else{

                Toast.makeText(Post.this, "You can't post a blank comment nanii", Toast.LENGTH_LONG).show();
            }


        }
        if(v == buttonExit){
            finish();



        }
    }

}
