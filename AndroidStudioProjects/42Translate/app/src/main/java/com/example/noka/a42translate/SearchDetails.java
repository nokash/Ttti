package com.example.noka.a42translate;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.*;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class SearchDetails extends AppCompatActivity {

    public String the_lang, the_trans, the_word, the_raw, the_translated,_saying, saying, the_id_word, who;
    private static final String DELETE_URL = "http://10.42.0.1/42translate/deleteWord.php";
    public  String KEY_WORD = "id";

    TextView lang, translated, word, context_raw, context_translated;
    Button btnDel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_details);

        the_lang= getIntent().getExtras().getString("the_lang_key");
        the_trans= getIntent().getExtras().getString("the_trans_key");
        the_word= getIntent().getExtras().getString("the_word_key");
        the_raw= getIntent().getExtras().getString("the_raw_key");
        the_translated= getIntent().getExtras().getString("the_translated_key");
        the_id_word= getIntent().getExtras().getString("the_id_word_key");
        _saying = getIntent().getExtras().getString("Word_key");
        saying = getIntent().getExtras().getString("word_key");
        who = "";


        lang = (TextView)findViewById(R.id.langTxt);
        translated = (TextView)findViewById(R.id.translated);
        word = (TextView)findViewById(R.id.tv_post_owner);
        context_raw = (TextView)findViewById(R.id.usage_raw);
        context_translated = (TextView)findViewById(R.id.usage_trans);
        btnDel = (Button) findViewById(R.id.delBtn);


        if(who.contentEquals("Nugu")){
            btnDel.setVisibility(View.VISIBLE);
        }else {
            btnDel.setVisibility(View.INVISIBLE);
        }

        btnDel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                deleteStuff();
                finish();

            }
        });

        lang.setText("Which is a " +saying+ " in " + the_raw);
        translated.setText("is " + the_trans + " in English" );
        word.setText(_saying+": " + the_word);
        context_raw.setText("Contextual Example: " + the_lang);
        context_translated.setText("Roughly translates to: '" + the_translated + "' in English");
    }

    public void deleteStuff(){
        final String word = the_id_word;
//        final String raw = editTextContext.getText().toString().trim();
//        final String translated = editTextTranslated.getText().toString().trim();
//        final String translated_context = editTextTranslatedContext.getText().toString().trim();
//        final String submitted = editTextSubmitted.getText().toString().trim();
//        final String language = text.trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, DELETE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Toast.makeText(SearchDetails.this, response, Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SearchDetails.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(KEY_WORD, word);

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
