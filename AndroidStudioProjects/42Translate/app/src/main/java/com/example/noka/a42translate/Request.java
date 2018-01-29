package com.example.noka.a42translate;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
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

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by noka on 5/3/17.
 */

public class Request extends AppCompatActivity implements View.OnClickListener {

    EditText editTextItem;
    Spinner spinnerLang, spinnerItem;
    Button buttonExit, buttonReq;
    public String search;

    public String userId;
    private static final String REQUEST_URL = "http://192.168.1.111/42translate/sendRequest.php";

    public final String KEY_ITEM = "item";
    public final String KEY_REQ = "request_by";
    public final String KEY_DATE = "date";
    public final String KEY_LANG = "language";
    public final String KEY_TYPE = "item_type";

    ArrayAdapter<CharSequence> dataAdapter, langAdap;
    DatabaseHelper dbhelper;


    public String text;
    public String lang;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.request_activity);

        editTextItem = (EditText) findViewById(R.id.editTextItem);
        spinnerItem = (Spinner) findViewById(R.id.spinnerReq);
        spinnerLang = (Spinner) findViewById(R.id.spinner2);

        buttonExit = (Button) findViewById(R.id.buttonExit);
        buttonReq  = (Button) findViewById(R.id.buttonReq);

        search = getIntent().getExtras().getString("search");
        editTextItem.setText(search);

        buttonReq.setOnClickListener(this);
        buttonExit.setOnClickListener(this);

        dataAdapter = ArrayAdapter.createFromResource(this, R.array.items, android.R.layout.simple_spinner_item);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerItem.setAdapter(dataAdapter);
        spinnerItem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?>arg0, View view, int arg2, long arg3) {

                text=spinnerItem.getSelectedItem().toString();

                // Toast.makeText(getApplicationContext(), text ,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

        langAdap = ArrayAdapter.createFromResource(this, R.array.languages_action, android.R.layout.simple_spinner_item);
        langAdap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLang.setAdapter(langAdap);
        spinnerLang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?>arg0, View view, int arg2, long arg3) {

                lang=spinnerLang.getSelectedItem().toString();

                // Toast.makeText(getApplicationContext(), text ,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

        dbhelper =new DatabaseHelper(this);
        dbhelper.getUserName();

    }

    public  void postItem() {
        try {
            Cursor cus = dbhelper.select_user();
            if (cus.getCount() == 0) {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                Toast.makeText(getApplicationContext(), "You got register first to be able to make a request", Toast.LENGTH_LONG).show();
                finish();


            } else {
                while (cus.moveToNext()) {
                    userId = cus.getString(0);
                }
                final String item = editTextItem.getText().toString().trim();
                final String type = text.trim();
                final String language = lang.trim();
                final String date = getCurrentTime();
                final String user = userId.trim();

                //final String search = searchArea.getText().toString().trim();

                StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.POST, REQUEST_URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                editTextItem.setText(null);
                                Toast.makeText(Request.this, response, Toast.LENGTH_LONG).show();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(Request.this, error.toString(), Toast.LENGTH_LONG).show();
                            }
                        }) {

                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put(KEY_ITEM, item);
                        params.put(KEY_DATE, date);
                        params.put(KEY_LANG, language);
                        params.put(KEY_REQ, user);
                        params.put(KEY_TYPE, type);
                        //params.put(KEY_SEARCH, search);
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
        if(v == buttonExit){
            finish();
        }
        if(v == buttonReq){
            postItem();
        }

    }
}