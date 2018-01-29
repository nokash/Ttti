package com.example.noka.a42translate;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.*;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.R.attr.data;

//import com.example.beni.betsalpha2.app.AppController;
//import com.example.beni.betsalpha2.models.PageModel;

/**
 * Created by Beni on 3/28/2017.
 */


public class PagePostDisplay extends Activity {
    RelativeLayout root_view;
    SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView mRootView;
    private String urlJsonArry = "http://10.42.0.1/42translate/fetchComments.php";
    // temporary string to show the parsed response
    private static final String COMMENT_URL = "http://10.42.0.1/42translate/fetchComments.php";
    ProgressDialog pDialog;
    private String jsonResponse;
    public String parent_width;
    Button btn_refresh;
    FloatingActionButton fab_commemt;
    MyReceiver r;
    RelativeLayout rela_refresh_post;
    TextView expanded_pic, title, owner, comm, timi;
    String content, comment_count, time_bana,person, post_id;
    public static final String KEYID = "forum_id";
    public String forum_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_forum);
        swipeRefreshLayout= (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        btn_refresh= (Button)findViewById(R.id.btn_refresh);
        rela_refresh_post=(RelativeLayout) findViewById(R.id.rela_refresh_post);
        expanded_pic= (TextView)findViewById(R.id.expanded_pic);

        content = getIntent().getExtras().getString("item");
        person = getIntent().getExtras().getString("submitted_by");
        post_id = getIntent().getExtras().getString("id_forum");
        comment_count = getIntent().getExtras().getString("comment_count");
        time_bana = getIntent().getExtras().getString("date");

        fab_commemt = (FloatingActionButton) findViewById(R.id.fab_comment);
        fab_commemt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(PagePostDisplay.this, post_id , Toast.LENGTH_LONG).show();
                showOddPostsDialog();

//                Toast.makeText(PagePostDisplay.this, post_id , Toast.LENGTH_LONG).show();

            }
        });

        title = (TextView)findViewById(R.id.tv_post_post);
        owner = (TextView)findViewById(R.id.tv_post_owner);
        comm = (TextView) findViewById(R.id.comments_kadhaa);
        timi = (TextView) findViewById(R.id.textVieew);
        title.setText(content);
        owner.setText(person);
        comm.setText(comment_count);
        timi.setText(time_bana);




        mRootView= (RecyclerView) findViewById(R.id.my_recycler_view_comments);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        makeJsonArrayRequest();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                refreshItems();
                //Toast.makeText(PagePostDisplay.this, content , Toast.LENGTH_LONG).show();

            }
        });

    }

    private void initRecyclerView(List<PageModel> items) {
        mRootView.setAdapter(new FakePageAdapter(items,getApplicationContext()));

    }


    private static class FakePageAdapter extends RecyclerView.Adapter<FakePageVH> {
        List<PageModel> items;
        String post,comment, submitted_date,post_owner;
        Context context;



        FakePageAdapter(List<PageModel> items, Context context) {
            this.items = items;
            this.context=context;
        }
        @Override
        public FakePageVH onCreateViewHolder(ViewGroup viewGroup, int i) {
            View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.tucomments, viewGroup, false);

            return new FakePageVH(itemView);
        }

        @Override
        //Gets model data and sets it to
        public void onBindViewHolder(final FakePageVH fakePageVH,final int i) {
            fakePageVH.cardview.setLongClickable(true);
            if(items.get(i).getComment()!=null) {
                fakePageVH.tv_post.setText(items.get(i).getComment());
            }else {
                fakePageVH.tv_post.setVisibility(View.GONE);
            }
             fakePageVH.comment_date.setText(items.get(i).getSubmitted_date());
            fakePageVH.tv_post_owner.setText(items.get(i).getSubmitted_by());
        }
        @Override
        public int getItemCount() {
            return items.size();
        }

    }

    private void makeJsonArrayRequest() {
        forum_id=post_id;
        final List<PageModel> list = new ArrayList<>();


        //showpDialog();

        StringRequest req = new StringRequest(Request.Method.POST,COMMENT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("error", response.toString());
                       // Toast.makeText(getApplicationContext(), "Error:"+response.toString(), Toast.LENGTH_LONG).show();
                        try {

                            // Parsing json array response
                            // loop through each json object
                            JSONArray jsonArray  = new JSONArray(response);
                            jsonResponse = "";
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject person = (JSONObject) jsonArray.get(i);
                                String comment = person.getString("comment");
                                String date_submitted = person.getString("date_submitted");
                                String submitted_by = person.getString("submitted_by");
                                String forum_id = person.getString("forum_id");

                                  //Toast.makeText(getApplicationContext(), "Error:"+submitted_by.toString(), Toast.LENGTH_LONG).show();

                                PageModel postModel = new PageModel(comment, date_submitted, submitted_by, forum_id);
                                list.add(postModel);
                                if(list.size()==0) {
                                    btn_refresh.setVisibility(View.VISIBLE);
                                }

                                initRecyclerView(list);

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }

                        hidepDialog();

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
                hidepDialog();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put(KEYID, forum_id);
                return map;
            }
        };



        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(req);

        //}
    }


    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }


    void refreshItems() {
        rela_refresh_post.setVisibility(View.VISIBLE);
        final List<PageModel> list = new ArrayList<>();
        StringRequest req = new StringRequest(urlJsonArry,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("error", response.toString());
                        //Toast.makeText(getApplicationContext(), "Error:"+response.toString(), Toast.LENGTH_LONG).show();


                        try {

                            // Parsing json array response
                            // loop through each json object
                            JSONArray jsonArray  = new JSONArray(response);
                            jsonResponse = "";
                            for (int i = 0; i < jsonArray.length(); i++) {
                                //getting the string array from the script

                                JSONObject person = (JSONObject) jsonArray.get(i);
                                String comment = person.getString("comment");
                                String date_submitted = person.getString("date_submitted");
                                String submitted_by = person.getString("submitted_by");
                                String forum_id = person.getString("forum_id");

                                //feeding them to the model

                                PageModel postModel = new PageModel(comment,submitted_by, date_submitted, forum_id);
                                list.add(postModel);

                                //if the list is empty the refresh button is set to be visible
                                if(list.size()==0) {
                                    btn_refresh.setVisibility(View.VISIBLE);
                                }

                            }
                            initRecyclerView(list);


                             Toast.makeText(getApplicationContext(), ""+jsonResponse, Toast.LENGTH_LONG).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            //Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }

                        rela_refresh_post.setVisibility(View.GONE);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("error_com",volleyError.getMessage().toString());
                rela_refresh_post.setVisibility(View.GONE);
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
                hidepDialog();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(req);
        onItemsLoadComplete();
    }



    void onItemsLoadComplete() {
        // Update the adapter and notify data set changed
        // ...

        // Stop refresh animation
        swipeRefreshLayout.setRefreshing(false);
    }

    private static class FakePageVH extends RecyclerView.ViewHolder {
        protected CardView cardview = null;
        protected TextView tv_post,tv_postcomments,comment_date;
        TextView tv_post_owner;
        LinearLayout img_likes;
        FrameLayout container;

        //Initializing the views of the RecyclerView and settting layout views for them
        FakePageVH(View itemView) {
            super(itemView);
            cardview = (CardView) itemView.findViewById(R.id.card_list_post);
            tv_post = (TextView) itemView.findViewById(R.id.tv_post_post);
            tv_postcomments = (TextView) itemView.findViewById(R.id.tv_postcomments);
            tv_post_owner = (TextView) itemView.findViewById(R.id.tv_comment_owner_post);
            comment_date =(TextView) itemView.findViewById(R.id.date_time);

        }

    }
    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            PagePostDisplay.this.refreshItems();
        }
    }

    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(r);
    }

    public void onResume() {
        super.onResume();
        r = new MyReceiver();
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(r, new IntentFilter("TAG_REFRESH_COMMUNITY"));
    }
    public void showOddPostsDialog() {
        Intent intent =new Intent(this, Comment.class);
        intent.putExtra("forum_id", post_id);
        intent.putExtra("submitted_by", person);
        startActivity(intent);
        //Toast.makeText(getApplicationContext(),post_id, Toast.LENGTH_LONG).show();
        //finish();
    }




}
