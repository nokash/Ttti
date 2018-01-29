package com.example.noka.a42translate;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


public class SearchLyrics extends AppCompatActivity {
    private RecyclerView mRootView;
    private String urlJsonArry = "http://192.168.1.111/42translate/fetchLyrics.php";
    private String jsonResponse;
    public String muguruki;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.results_lyrics);
        muguruki = getIntent().getExtras().getString("searched");
       //Toast.makeText(Search.this, muguruki, Toast.LENGTH_LONG).show();


        mRootView = (RecyclerView) findViewById(R.id.recycler_search_lyrics);
        makeJsonArrayRequest();
    }
//This is the Viewholder for the cardview
    private static class FakePageVH extends RecyclerView.ViewHolder {
        protected CardView cardview = null;
        protected TextView title, lang, artist;

        FakePageVH(View itemView) {
            super(itemView);
            cardview = (CardView) itemView.findViewById(R.id.card_lyrics);
            title = (TextView) itemView.findViewById(R.id.title_ly);
            lang = (TextView) itemView.findViewById(R.id.langx);
            artist = (TextView) itemView.findViewById(R.id.artist_ly);

        }

    }

    //RecyclerView class initializer
    private void initRecyclerView(List<LyricsModel> items) {
        mRootView.setAdapter(new FakePageAdapter(items, getApplicationContext()));

    }

    //RecyclerView Adapter class
    private static class FakePageAdapter extends RecyclerView.Adapter<FakePageVH> {
        List<LyricsModel> items;
        String id_lyrics, title, body,translated_body, artist,featured, submittedby,language ;
        Context context;


        FakePageAdapter(List<LyricsModel> items, Context context) {
            this.items = items;
            this.context = context;
        }

        //What Happens once our viewholder is created
        @Override
        public SearchLyrics.FakePageVH onCreateViewHolder(ViewGroup viewGroup, int i) {
            //Inflating activity_search layout with the view held in the viewholder
            View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.lyrics_search_card, viewGroup, false);

            return new SearchLyrics.FakePageVH(itemView);
        }

        @Override
        public void onBindViewHolder(final SearchLyrics.FakePageVH fakePageVH, final int i) {


            fakePageVH.cardview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    id_lyrics = items.get(i).getId_lyrics();
                    title = items.get(i).getTitle();
                    body = items.get(i).getBody();
                    translated_body = items.get(i).getTranslated_body();
                    artist = items.get(i).getArtist();
                    featured = items.get(i).getFeatured();
                    submittedby = items.get(i).getSubmittedby();
                    language = items.get(i).getLanguage();



                    Intent intent = new Intent(v.getContext(), LyricsDisplay.class);
                    intent.putExtra("id_key", id_lyrics);
                    intent.putExtra("title_key", title);
                    intent.putExtra("body_key", body);
                    intent.putExtra("translated_body_key", translated_body);
                    intent.putExtra("artist_key", artist);
                    intent.putExtra("featured_key", featured);
                    intent.putExtra("submittedby_key", submittedby);
                    intent.putExtra("language_key", language);


                    v.getContext().startActivity(intent);

                }
            });

            if (items.get(i).getTitle() != null) {
                fakePageVH.title.setText(items.get(i).getTitle());

            } else {
                //Toast.makeText(context, "There are no records matching your search" , Toast.LENGTH_LONG).show();
                fakePageVH.title.setVisibility(View.GONE);
            }
            //Toast.makeText(context, ""+items.get(i).getSubmitted_by() , Toast.LENGTH_LONG).show();
            fakePageVH.lang.setText(items.get(i).getLanguage());
            fakePageVH.artist.setText(items.get(i).getArtist());


        }

        @Override
        public int getItemCount() {
            return items.size();
        }
    }

    private void makeJsonArrayRequest() {

        final List<LyricsModel> list = new ArrayList<>();


        //showpDialog();

        StringRequest req = new StringRequest(Request.Method.POST,urlJsonArry,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("error", response.toString());
                        //Toast.makeText(getApplicationContext(), "Error:"+response.toString(), Toast.LENGTH_LONG).show();
                        if(response.contains("NO")){
                            Intent intent = new Intent(getApplicationContext(), com.example.noka.a42translate.Request.class);
                            intent.putExtra("search", muguruki);
                           startActivity(intent);
                        }
                  // Toast.makeText(getApplicationContext(), "Error:"+response.toString(), Toast.LENGTH_LONG).show();
                        try {

                            // Parsing json array response
                            // loop through each json object
                            JSONArray jsonArray  = new JSONArray(response);
                            jsonResponse = "";
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject person = (JSONObject) jsonArray.get(i);
                               // String text = person.getString("text");
                                String id_lyrics = person.getString("id");
                                String title = person.getString("title");
                                String body = person.getString("body");
                                String translated_body = person.getString("translated");
                                String artist = person.getString("artist");
                                String featured = person.getString("featured");
                                String submittedby = person.getString("submitted_by");
                                String language = person.getString("language");

                                Toast.makeText(getApplicationContext(), "Error:"+title.toString(), Toast.LENGTH_LONG).show();

                                LyricsModel lyricsModel = new LyricsModel(id_lyrics, title, body, translated_body, artist,featured, submittedby, language);
                                list.add(lyricsModel);

                                initRecyclerView(list);

                            }
      //  Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
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

