package com.example.noka.a42translate;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Admin extends AppCompatActivity implements View.OnClickListener {

    Button managePosts, deleteUser, createAdmin, respondBtn;
    TextView txtUser;
    DrawerLayout drawer;
    public String whois;
    DatabaseHelper dbhelper;
    private RecyclerView mRootView;
    private String urlJsonArry = "http://10.42.0.1/42translate/fetchContent.php";
    private String jsonResponse;
    public String muguruki;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.requested);



        mRootView = (RecyclerView) findViewById(R.id.my_recycler_viewrequested);
        makeJsonArrayRequest();
        dbhelper = new DatabaseHelper(this);

        


    }

    @Override
    public void onClick(View v) {
        
    }

    //This is the Viewholder for the cardview
    private static class FakePageVH extends RecyclerView.ViewHolder {
        protected CardView cardviews = null;
        protected TextView title, itemtype, langu, date, requsetby;

        FakePageVH(View itemView) {
            super(itemView);
            cardviews = (CardView) itemView.findViewById(R.id.card_view_request);
            title = (TextView) itemView.findViewById(R.id.item_requested);
            langu = (TextView) itemView.findViewById(R.id.textView4);
            itemtype = (TextView) itemView.findViewById(R.id.categorytime);
            date = (TextView) itemView.findViewById(R.id.datetv);
            requsetby = (TextView) itemView.findViewById(R.id.requested_by);
        }

    }

    //RecyclerView class initializer
    private void initRecyclerView(List<RequestModel> items) {
        mRootView.setAdapter(new Admin.FakePageAdapter(items, getApplicationContext()));

    }

    //RecyclerView Adapter class
    private static class FakePageAdapter extends RecyclerView.Adapter<Admin.FakePageVH> {
        List<RequestModel> items;
        String id_request, item_type, item, language, date, request_by;
        Context context;


        FakePageAdapter(List<RequestModel> items, Context context) {
            this.items = items;
            this.context = context;
        }

        //What Happens once our viewholder is created
        @Override
        public Admin.FakePageVH onCreateViewHolder(ViewGroup viewGroup, int i) {
            //Inflating activity_search layout with the view held in the viewholder
            View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.content_main, viewGroup, false);

            return new Admin.FakePageVH(itemView);
        }

        @Override
        public void onBindViewHolder(final Admin.FakePageVH fakePageVH, final int i) {


            fakePageVH.cardviews.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    id_request = items.get(i).getId_request();
                    item_type = items.get(i).getItem_type();
                    item = items.get(i).getItem();
                    language = items.get(i).getLanguage();
                    date = items.get(i).getDate();
                    request_by = items.get(i).getRequest_by();



                    if(item_type.contentEquals("Word")) {
                        Intent intent = new Intent(v.getContext(), Words.class);
                        intent.putExtra("item", item);
                        v.getContext().startActivity(intent);
                    }
                    if(item_type.contentEquals("Phrase")) {
                        Intent intent = new Intent(v.getContext(), Phrases.class);
                        intent.putExtra("item", item);
                        v.getContext().startActivity(intent);
                    }
                    if(item_type.contentEquals("Lyrics")) {
                        Intent intent = new Intent(v.getContext(), Lyrics.class);
                        intent.putExtra("item", item);
                        v.getContext().startActivity(intent);
                    }
                    if(item_type.contentEquals("Saying")) {
                        Intent intent = new Intent(v.getContext(), Sayings.class);
                        intent.putExtra("item", item);
                        v.getContext().startActivity(intent);
                    }


                }
            });

            if (items.get(i).getItem() != null) {
                fakePageVH.title.setText(items.get(i).getItem());

            } else {
                //Toast.makeText(context, "There are no records matching your search" , Toast.LENGTH_LONG).show();
                fakePageVH.title.setVisibility(View.GONE);
            }
            //Toast.makeText(context, ""+items.get(i).getSubmitted_by() , Toast.LENGTH_LONG).show();
            fakePageVH.langu.setText(items.get(i).getLanguage());
            fakePageVH.itemtype.setText(items.get(i).getItem_type());
            fakePageVH.requsetby.setText(items.get(i).getRequest_by());
            fakePageVH.date.setText(items.get(i).getDate());

        }

        @Override
        public int getItemCount() {
            return items.size();
        }
    }

    private void makeJsonArrayRequest() {

        final List<RequestModel> list = new ArrayList<>();


        //showpDialog();

        StringRequest req = new StringRequest(urlJsonArry,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("error", response.toString());
                        //Toast.makeText(getApplicationContext(), "Error:"+response.toString(), Toast.LENGTH_LONG).show();
                        if (response.contains("NO")) {
                            Intent intent = new Intent(getApplicationContext(), com.example.noka.a42translate.Request.class);
                            intent.putExtra("search", muguruki);
                            startActivity(intent);
                        }
                        // Toast.makeText(getApplicationContext(), "Error:"+response.toString(), Toast.LENGTH_LONG).show();
                        try {

                            // Parsing json array response
                            // loop through each json object
                            JSONArray jsonArray = new JSONArray(response);
                            jsonResponse = "";
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject person = (JSONObject) jsonArray.get(i);
                                // String text = person.getString("text");
                                String id_request = person.getString("id_request");
                                String item_type = person.getString("item_type");
                                String item = person.getString("item");
                                String language = person.getString("language");
                                String date = person.getString("date");
                                String request_by = person.getString("request_by");


                                //   Toast.makeText(getApplicationContext(), "Error:"+title.toString(), Toast.LENGTH_LONG).show();

                                RequestModel requestModel = new RequestModel(id_request, item_type, item, language, date, request_by);
                                list.add(requestModel);

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
                    Toast.makeText(getApplicationContext(), "" + message, Toast.LENGTH_LONG).show();
                } else if (volleyError instanceof ServerError) {
                    message = "The server could not be found. Please try again after some time!!";
                    Toast.makeText(getApplicationContext(), "" + message, Toast.LENGTH_LONG).show();
                } else if (volleyError instanceof AuthFailureError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                    Toast.makeText(getApplicationContext(), "" + message, Toast.LENGTH_LONG).show();
                } else if (volleyError instanceof ParseError) {
                    message = "Parsing error! Please try again after some time!!";
                    Toast.makeText(getApplicationContext(), "" + message, Toast.LENGTH_LONG).show();
                } else if (volleyError instanceof NoConnectionError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                    Toast.makeText(getApplicationContext(), "" + message, Toast.LENGTH_LONG).show();
                } else if (volleyError instanceof TimeoutError) {
                    message = "Connection TimeOut! Please check your internet connection.";
                    Toast.makeText(getApplicationContext(), "" + message, Toast.LENGTH_LONG).show();
                }

            }


        }) {

//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("query", muguruki);
//                return params;
//            }

        };


        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(req);

        //}
    }
}
