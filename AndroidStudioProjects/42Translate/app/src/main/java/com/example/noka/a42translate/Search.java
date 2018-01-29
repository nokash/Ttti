package com.example.noka.a42translate;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Search extends AppCompatActivity {
    private RecyclerView mRootView;
    private String urlJsonArry = "http://192.168.1.111/42translate/fetchWords.php";
    private String jsonResponse;
    public String muguruki;
    public static String who = "Nugu";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.results);
        muguruki = getIntent().getExtras().getString("searched");


        mRootView = (RecyclerView) findViewById(R.id.recycler_search);
        makeJsonArrayRequest();
    }
//This is the Viewholder for the cardview
    private static class FakePageVH extends RecyclerView.ViewHolder {
        protected CardView cardview = null;
        protected TextView word, lang, translated;
        protected static Button btnDel;

        FakePageVH(View itemView) {
            super(itemView);
            cardview = (CardView) itemView.findViewById(R.id.card_word);
            word = (TextView) itemView.findViewById(R.id.wordTxt);
            lang = (TextView) itemView.findViewById(R.id.lang);
            translated = (TextView) itemView.findViewById(R.id.trans);
        }

    }

    //RecyclerView class initializer
    private void initRecyclerView(List<WordModel> items) {
        mRootView.setAdapter(new FakePageAdapter(items, getApplicationContext()));

    }

    //RecyclerView Adapter class
    private static class FakePageAdapter extends RecyclerView.Adapter<FakePageVH> {
        List<WordModel> items;
        String the_word, the_id_word, the_lang, the_trans, the_raw, _word, word, the_translated;
        Context context;


        FakePageAdapter(List<WordModel> items, Context context) {
            this.items = items;
            this.context = context;
        }

        //What Happens once our viewholder is created
        @Override
        public Search.FakePageVH onCreateViewHolder(ViewGroup viewGroup, int i) {
            //Inflating activity_search layout with the view held in the viewholder
            View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_search, viewGroup, false);

            return new Search.FakePageVH(itemView);
        }

        @Override
        public void onBindViewHolder(final Search.FakePageVH fakePageVH, final int i) {


            fakePageVH.cardview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    the_lang = items.get(i).getLanguage();
                    the_trans = items.get(i).getTranslated();
                    the_word = items.get(i).getText();
                    the_raw = items.get(i).getContext_raw();
                    the_translated = items.get(i).getContenxt_translated();
                    the_id_word = items.get(i).getId_word();
                    _word = "Word";
                    word = "word";


                    Intent intent = new Intent(v.getContext(), SearchDetails.class);
                    intent.putExtra("the_lang_key", the_lang);
                    intent.putExtra("the_trans_key", the_trans);
                    intent.putExtra("the_word_key", the_word);
                    intent.putExtra("the_raw_key", the_raw);
                    intent.putExtra("the_translated_key", the_translated);
                    intent.putExtra("the_id_word_key", the_id_word);
                    intent.putExtra("word_key", word);
                    intent.putExtra("Word_key", _word);


                    v.getContext().startActivity(intent);

                }
            });

            if (items.get(i).getText() != null) {
                fakePageVH.word.setText(items.get(i).getText());

            } else {

                fakePageVH.word.setVisibility(View.GONE);
            }

            fakePageVH.lang.setText(items.get(i).getContext_raw());
            fakePageVH.translated.setText(items.get(i).getTranslated());


        }

        @Override
        public int getItemCount() {
            return items.size();
        }
    }

    private void makeJsonArrayRequest() {

        final List<WordModel> list = new ArrayList<>();

        StringRequest req = new StringRequest(Request.Method.POST,urlJsonArry,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("error", response.toString());
//                       Toast.makeText(getApplicationContext(), "Error:"+response.toString(), Toast.LENGTH_LONG).show();
                        if(response.contains("NO")){
                            Intent intent = new Intent(getApplicationContext(), com.example.noka.a42translate.Request.class);
                            intent.putExtra("search", muguruki);
                            startActivity(intent);
                        }
                        try {

                            // Parsing json array response
                            // loop through each json object
                            JSONArray jsonArray  = new JSONArray(response);
                            jsonResponse = "";
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject person = (JSONObject) jsonArray.get(i);
                                String text = person.getString("text");
                                String translated = person.getString("translated");
                                String language = person.getString("language");
                                String context_raw = person.getString("context_raw");
                                String context_translated = person.getString("context_translated");
                                String id_word = person.getString("id");


                                //Toast.makeText(getApplicationContext(), "Error:"+submitted_by.toString(), Toast.LENGTH_LONG).show();

                                WordModel wordModel = new WordModel(text,translated,language, context_translated, context_raw, id_word);
                                list.add(wordModel);

                                initRecyclerView(list);

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                           // Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }



                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                //Log.d("error_com",volleyError.getMessage().toString());
                String message = null;
                if (volleyError instanceof NetworkError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                    Toast.makeText(getApplicationContext(),""+message, Toast.LENGTH_LONG).show();
                } else if (volleyError instanceof ServerError) {
                    message = "The server could not be found. Please try again after some time!!";
                    Toast.makeText(getApplicationContext(),""+message, Toast.LENGTH_LONG).show();
                } else if (volleyError instanceof AuthFailureError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                    Toast.makeText(getApplicationContext(),""+message, Toast.LENGTH_LONG).show();
                } else if (volleyError instanceof ParseError) {
                    message = "Parsing error! Please try again after some time!!";
                    Toast.makeText(getApplicationContext(),""+message, Toast.LENGTH_LONG).show();
                } else if (volleyError instanceof NoConnectionError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                    Toast.makeText(getApplicationContext(),""+message, Toast.LENGTH_LONG).show();
                } else if (volleyError instanceof TimeoutError) {
                    message = "Connection TimeOut! Please check your internet connection.";
                    Toast.makeText(getApplicationContext(),""+message, Toast.LENGTH_LONG).show();
                }

            }

        }){

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("query", muguruki);
                return params;
            }

        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(req);

        //}
    }
}

