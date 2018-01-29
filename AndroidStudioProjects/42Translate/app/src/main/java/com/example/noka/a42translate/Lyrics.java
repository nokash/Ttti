package com.example.noka.a42translate;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.noka.a42translate.R.id.bSearch;
import static com.example.noka.a42translate.R.id.spinnerLang;


public class Lyrics extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private static final String LYRICS_URL = "http://192.168.0.111/42translate/addLyrics.php";

    public static final String KEY_TITLE = "title";
    public static final String KEY_TRANSLATED = "translated";
    public static final String KEY_BODY = "body";
    public static final String KEY_ARTIST = "artist";
    public static final String KEY_LANG = "language";
    public static final String KEY_FEATURED = "featured";
    public static final String KEY_SUBMITED = "submitted_by";
    public static final String KEY_SEARCH = "search";

    private EditText editTextSearch;
    private EditText editTextTitle;
    private EditText editTextArtist;
    private EditText editTextFeatured;
    private EditText editTextBody;
    private EditText editTextTranslation;
    private EditText editTextSubmitted;

    private Spinner spinnerLanguage;
    private CheckBox checkBoxTos;
    private Button btnSubmit;
    private Button btnSearch;

    DatabaseHelper dbhelper;
    public String userId;
    public String searched, word_got;

    private ArrayList<String> spinnerlist;
    ArrayAdapter<CharSequence> dataAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_lyrics);
        // Inflate the layout for this fragment

        editTextSearch = (EditText) findViewById(R.id.search_area);
        editTextTitle = (EditText) findViewById(R.id.editTitle);
        editTextArtist = (EditText) findViewById(R.id.editArtist);
        editTextFeatured = (EditText) findViewById(R.id.artistFeat);
        editTextBody = (EditText) findViewById(R.id.editTextBody);
        editTextTranslation = (EditText) findViewById(R.id.editTextTranslate);
        editTextSubmitted = (EditText) findViewById(R.id.submittedBy);

//        word_got = getIntent().getExtras().getString("item");
        editTextTitle.setText(word_got);


        spinnerLanguage = (Spinner) findViewById(R.id.spinnerLang_age);
        dbhelper = new DatabaseHelper(this);


        dataAdapter = ArrayAdapter.createFromResource(this, R.array.languages_action, android.R.layout.simple_spinner_item);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLanguage.setAdapter(dataAdapter);


        
        //checkBoxTos = (CheckBox) findViewById(R.id.checkBoxTOS);
        btnSubmit = (Button) findViewById(R.id.bSubmit);
        btnSearch = (Button) findViewById(R.id.bSearch);

        btnSubmit.setVisibility(View.INVISIBLE);
        checkBoxTos = (CheckBox) findViewById(R.id.checkBoxTOSL);

        checkBoxTos.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if ( isChecked )
                {
                    btnSubmit.setVisibility(View.VISIBLE);
                }
                else{
                    btnSubmit.setVisibility(View.INVISIBLE);
                }
            }
        });

        btnSearch.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
    }

    private void getDetails() {
        try {
            Cursor cus = dbhelper.select_user();
            if (cus.getCount() == 0) {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                Toast.makeText(getApplicationContext(), "You got register first to be able to comment", Toast.LENGTH_LONG).show();
                finish();


            } else {
                while (cus.moveToNext()) {
                    userId = cus.getString(0);
                }
                final String title = editTextTitle.getText().toString().trim();
                final String artist = editTextArtist.getText().toString().trim();
                final String featured = editTextFeatured.getText().toString().trim();
                final String body = editTextBody.getText().toString().trim();
                final String translated = editTextTranslation.getText().toString().trim();
                final String submitted = userId.trim();
                final String search = editTextSearch.getText().toString().trim();

                StringRequest stringRequest = new StringRequest(Request.Method.POST, LYRICS_URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                editTextArtist.setText(null);
                                editTextSubmitted.setText(null);
                                editTextTranslation.setText(null);
                                editTextBody.setText(null);
                                editTextTitle.setText(null);
                                editTextFeatured.setText(null);
                                Toast.makeText(Lyrics.this, response, Toast.LENGTH_LONG).show();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(Lyrics.this, error.toString(), Toast.LENGTH_LONG).show();
                            }
                        }) {

                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put(KEY_TITLE, title);
                        params.put(KEY_ARTIST, artist);
                        params.put(KEY_FEATURED, featured);
                        params.put(KEY_BODY, body);
                        params.put(KEY_TRANSLATED, translated);
                        params.put(KEY_SUBMITED, submitted);
                        params.put(KEY_SEARCH, search);
                        return params;
                    }

                };

                RequestQueue requestQueue = Volley.newRequestQueue(this);
                requestQueue.add(stringRequest);
            }
        }
            catch (SQLiteException se) {

                Log.e(getClass().getSimpleName(), "Open the database");
            }

        }



    @Override
    public void onClick(View v) {
       if (v == btnSubmit) {
            getDetails();
        }
if (v == btnSearch){
    if (editTextSearch.getText().toString().trim().length()==0) {
        Toast.makeText(Lyrics.this, "Please enter text to search. Si ujinga ujinga hapa!", Toast.LENGTH_LONG).show();
    } else {
        searched = editTextSearch.getText().toString().trim();
        Intent intent = new Intent(getApplicationContext(), SearchLyrics.class);
        intent.putExtra("searched", searched);
        //Toast.makeText(getApplicationContext(),searched , Toast.LENGTH_LONG).show();
        startActivity(intent);

    }
}

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();
       // Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}



