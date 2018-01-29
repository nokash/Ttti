package com.example.noka.a42translate;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
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

import static android.widget.AdapterView.*;


public class Phrases extends AppCompatActivity implements OnClickListener, OnItemSelectedListener {

    private static final String PHRASE_URL = "http://10.42.0.1/42translate/addPhrase.php";

    public static final String KEY_PHRASE = "text";
    public static final String KEY_TRANSLATED = "translated";
    public static final String KEY_RAW = "context_raw";
    public static final String KEY_CONTEXT = "context_translated";
    public static final String KEY_LANG = "language";
    public static final String KEY_SUBMITED = "submitted_by";

    private EditText searchArea;
    private EditText editTextArea;
    private EditText editTextTranslated;
    private EditText editTextContext;
    private EditText editTextTranslatedContext;
    private EditText editTextSubmitted;
    private Spinner spinnerLanguage;
    DatabaseHelper dbhelper;

    private Button searchButton;
    private Button submitButton;
    public String text, userId, word_got;
    public String searched;

    CheckBox checkBoxTos;

    private ArrayList<String> spinnerlist;
    ArrayAdapter<CharSequence> dataAdapter;

@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_phrases);

        searchArea = (EditText) findViewById(R.id.phrasesearch_area);
        editTextArea = (EditText) findViewById(R.id.editPhrase);
        editTextTranslated = (EditText) findViewById(R.id.editTranslated);
        editTextContext = (EditText) findViewById(R.id.useNative);
        editTextTranslatedContext = (EditText) findViewById(R.id.useTranslate);
        editTextSubmitted = (EditText) findViewById(R.id.submittedBy);

    word_got = getIntent().getExtras().getString("item");
    editTextArea.setText(word_got);

        dbhelper = new DatabaseHelper(this);

    submitButton = (Button) findViewById(R.id.btnSubmit);
    searchButton = (Button) findViewById(R.id.p_search_button);
    submitButton.setVisibility(View.INVISIBLE);
    checkBoxTos = (CheckBox) findViewById(R.id.checkBoxP);

    checkBoxTos.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
    {
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
        {
            if ( isChecked )
            {
                submitButton.setVisibility(View.VISIBLE);
            }
            else{
                submitButton.setVisibility(View.INVISIBLE);
            }
        }
    });
        spinnerLanguage = (Spinner) findViewById(R.id.spinnerLang_phrase);


    dataAdapter = ArrayAdapter.createFromResource(this, R.array.languages_action, android.R.layout.simple_spinner_item);
    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    spinnerLanguage.setAdapter(dataAdapter);
    spinnerLanguage.setOnItemSelectedListener(new OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?>arg0, View view, int arg2, long arg3) {

            text=spinnerLanguage.getSelectedItem().toString();

//            Toast.makeText(getApplicationContext(), text ,
//                    Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
            // TODO Auto-generated method stub

        }
    });

        submitButton.setOnClickListener(this);
        searchButton.setOnClickListener(this);

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
                final String phrase = editTextArea.getText().toString().trim();
                final String raw = editTextContext.getText().toString().trim();
                final String translated = editTextTranslated.getText().toString().trim();
                final String translated_context = editTextTranslatedContext.getText().toString().trim();
                final String submitted = userId.trim();
                final String language = text.trim();

                StringRequest stringRequest = new StringRequest(Request.Method.POST, PHRASE_URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                editTextArea.setText(null);
                                editTextContext.setText(null);
                                editTextTranslated.setText(null);
                                editTextTranslatedContext.setText(null);
                                editTextSubmitted.setText(null);
                                //Toast.makeText(Phrases.this, response, Toast.LENGTH_LONG).show();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(Phrases.this, error.toString(), Toast.LENGTH_LONG).show();
                            }
                        }) {

                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put(KEY_PHRASE, phrase);
                        params.put(KEY_RAW, raw);
                        params.put(KEY_TRANSLATED, translated);
                        params.put(KEY_CONTEXT, translated_context);
                        params.put(KEY_SUBMITED, submitted);
                        params.put(KEY_LANG, language);
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
        if (v == submitButton) {
            if(editTextSubmitted.getText().toString().trim().length()==0 ||
                    editTextTranslatedContext.getText().toString().trim().length()==0 ||
                    editTextTranslated.getText().toString().trim().length()==0 ||
                    editTextContext.getText().toString().trim().length()==0 ||
                    editTextArea.getText().toString().trim().length()==0) {
                Toast.makeText(Phrases.this, "Please enter text. Si ujinga ujinga hapa!", Toast.LENGTH_LONG).show();
            }else {
                getDetails();
            }
        }
        if (v == searchButton) {
            if (searchArea.getText().toString().trim().length() == 0) {
                Toast.makeText(Phrases.this, "Please enter text to search. Si ujinga ujinga hapa!", Toast.LENGTH_LONG).show();
            } else {
                searched = searchArea.getText().toString().trim();
                Intent intent = new Intent(getApplicationContext(), SearchPhrase.class);
                intent.putExtra("searched", searched);
                //Toast.makeText(getApplicationContext(),searched , Toast.LENGTH_LONG).show();
                startActivity(intent);


            }

        }


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}


