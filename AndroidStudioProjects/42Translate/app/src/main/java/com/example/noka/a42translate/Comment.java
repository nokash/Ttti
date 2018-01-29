package com.example.noka.a42translate;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Comment extends AppCompatActivity implements View.OnClickListener {
    EditText editTextComment;
    Button buttonExit;
    Button buttonComment;
    public String userPut, forum_idPut;
    private static final String COMMENT_URL = "http://192.168.1.111/42translate/addComment.php";
    DatabaseHelper dbhelper;

    public static final String KEY_ID = "forum_id";
    public static final String KEY_DATE = "date_submitted";
    public static final String KEY_COMMENT = "comment";
    public static final String KEY_USER = "submitted_by";

    public String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment_dialog);
        editTextComment = (EditText)findViewById(R.id.ed_comment_content);
        buttonComment = (Button)findViewById(R.id.bill_views);
        buttonExit = (Button)findViewById(R.id.reset);

        forum_idPut =getIntent().getExtras().getString("forum_id");
        userPut = getIntent().getExtras().getString("submitted_by");

        buttonExit.setOnClickListener(this);
        buttonComment.setOnClickListener(this);
        dbhelper =new DatabaseHelper(this);
        dbhelper.getUserName();
       // username = dbhelper.username.toString();
       // Toast.makeText(Comment.this, username, Toast.LENGTH_LONG).show();



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
                  // Toast.makeText(getApplicationContext(),userId, Toast.LENGTH_LONG).show();
                   final String forum_id = forum_idPut.trim();
                   final String comment = editTextComment.getText().toString().trim();
                   //Toast.makeText(Comment.this, comment, Toast.LENGTH_LONG).show();
                   final String date = getCurrentTime();
                   final String user = userId.trim();


                   StringRequest stringRequest = new StringRequest(Request.Method.POST, COMMENT_URL,
                           new Response.Listener<String>() {
                               @Override
                               public void onResponse(String response) {
                                   Toast.makeText(Comment.this, response, Toast.LENGTH_LONG).show();
                               }
                           },
                           new Response.ErrorListener() {
                               @Override
                               public void onErrorResponse(VolleyError error) {
                                   Toast.makeText(Comment.this, error.toString(), Toast.LENGTH_LONG).show();
                               }
                           }) {

                       @Override
                       protected Map<String, String> getParams() {
                           Map<String, String> params = new HashMap<String, String>();
                           params.put(KEY_COMMENT, comment);
                           params.put(KEY_DATE, date);
                           params.put(KEY_ID, forum_id);
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

                Toast.makeText(Comment.this, "You can't post a blank comment nanii", Toast.LENGTH_LONG).show();
            }


        }
        if(v == buttonExit){
          finish();



        }
    }
}
