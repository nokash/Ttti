package com.example.noka.a42translate;

import android.app.Activity;
import android.app.Dialog;
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
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.R.attr.data;
import static java.security.AccessController.getContext;


public class PageFragmentCommunity extends Activity {
    RelativeLayout root_view;
    SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView mRootView;
    private String urlJsonArry = "http://10.42.0.1/42translate/fetchForum.php";
    public static final String KEY_ID = "id_forum";
    // temporary string to show the parsed response
    ProgressDialog pDialog;
    private String jsonResponse;
    public  String whois;
    Button btn_refresh;
    MyReceiver r;
    RelativeLayout rela_refresh;
    TextView expanded_pic;
FloatingActionButton fab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_page_community);
        swipeRefreshLayout= (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        btn_refresh= (Button)findViewById(R.id.btn_refresh);
        rela_refresh=(RelativeLayout) findViewById(R.id.rela_refresh);
        expanded_pic= (TextView)findViewById(R.id.expanded_pic);
//        whois = getIntent().getExtras().getString("logged_as");

       // Toast.makeText(this, whois, Toast.LENGTH_LONG).show();
        fab = (FloatingActionButton)findViewById(R.id.fab_post);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              Intent intent = new Intent(getApplicationContext(), Post.class);
                startActivity(intent);
            }
        });


        mRootView= (RecyclerView) findViewById(R.id.my_recycler_view);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        makeJsonArrayRequest();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                refreshItems();
            }
        });
        btn_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshItems();
            }
        });

    }

    private void initRecyclerView(List<PostModel> items) {
        mRootView.setAdapter(new FakePageAdapter(items,getApplicationContext()));

    }

    

    private static class FakePageAdapter extends RecyclerView.Adapter<FakePageVH> {
        List<PostModel> items;
        String post,comments, likes,id_forum,post_owner;
        Context context;




        FakePageAdapter(List<PostModel> items, Context context) {
            this.items = items;
            this.context=context;
        }
        @Override
        public FakePageVH onCreateViewHolder(ViewGroup viewGroup, int i) {
            View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_community, viewGroup, false);

            return new FakePageVH(itemView);
        }

        @Override
        public void onBindViewHolder(final FakePageVH fakePageVH,final int i) {


            fakePageVH.cardview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    post=items.get(i).getItem();
                    //comments=items.get(i).getComments();
                    likes=items.get(i).getDate_submitted();
                    post_owner=items.get(i).getSubmitted_by();
                    id_forum = items.get(i).getId_forum();
                   // nossa = PageFragmentCommunity.whois;


                    Intent intent=new Intent(v.getContext(),PagePostDisplay.class);
                    intent.putExtra("item",post);
                    intent.putExtra("id_forum", id_forum);
                    intent.putExtra("submitted_by",post_owner);
                    intent.putExtra("date",likes);
                    //intent.putExtra("comment_count", comments);

                    v.getContext().startActivity(intent);
                    //Toast.makeText(context, "", Toast.LENGTH_LONG).show();
                }
            });
            fakePageVH.cardview.setLongClickable(true);
           // fakePageVH.cardview.setOnLongClickListener(new View.OnLongClickListener() {
//                                                           public boolean onLongClick(View view) {
//                                                               post=items.get(i).getItem();
//                                                               comments=items.get(i).getPost_comments();
//                                                               likes=items.get(i).getDate_submitted();
//                                                               post_owner=items.get(i).getSubmitted_by();
//                                                               id_forum = items.get(i).getId_forum();

                                                               fakePageVH.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                                                                   @Override
                                                                   public boolean onLongClick(View view) {
                                                                      // Notes notes = mNotes.get(position);
                                                                 //Toast.makeText(context, "Long Click", Toast.LENGTH_LONG).show();
                                                                       confirmDialog();
                                                                       return false;
                                                                   }
//                                                               });
//                                                               return true;
//                                                           }
                                                       }
            );

            if(items.get(i).getItem()!=null) {
                fakePageVH.tv_post.setText(items.get(i).getItem());

            }else {
                fakePageVH.tv_post.setVisibility(View.GONE);
            }
            //Toast.makeText(context, ""+items.get(i).getSubmitted_by() , Toast.LENGTH_LONG).show();
          //  fakePageVH.date.setText(items.get(i).getComments()+" " + "Comments");
           fakePageVH.tv_post_owner.setText(items.get(i).getSubmitted_by());
            fakePageVH.tv_postcomments.setText(items.get(i).getDate_submitted());


            }
        @Override
        public int getItemCount() {
            return items.size();
        }

        private void confirmDialog() {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);

            builder
                    .setMessage("Are you sure you want to continue?")
                    .setPositiveButton("Yes",  new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog,int id) {
                            dialog.cancel();
                        }
                    })
                    .show();
        }

    }


    private void makeJsonArrayRequest() {
        final List<PostModel> list = new ArrayList<>();



            showpDialog();

            StringRequest req = new StringRequest(Request.Method.POST,urlJsonArry,
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

                                    JSONObject person = (JSONObject) jsonArray.get(i);
                                    String id_forum = person.getString("id_forum");
                                    String item = person.getString("item");
                                    String submitted_by = person.getString("submitted_by");
                                    String date_submitted = person.getString("date_submitted");
                                  //  String comment = person.getString("count");
                                  //  Toast.makeText(getApplicationContext(), "Error:"+submitted_by.toString(), Toast.LENGTH_LONG).show();
                                    //String name2 = person.getString("name2");


                                    PostModel postModel = new PostModel(id_forum,item,
                                            submitted_by, date_submitted);
                                    list.add(postModel);
                                    if(list.size()==0) {
                                        btn_refresh.setVisibility(View.VISIBLE);
                                    }

                                    initRecyclerView(list);

                                }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    //Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
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
                    hidepDialog();
                }
            }){
//                @Override
//                protected Map<String, String> getParams() throws AuthFailureError {
//                    Map<String, String> map = new HashMap<String, String>();
//                    map.put("id_forum", );
//
//                    return map;
//                }
            };






            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(req);

            //}
        }


    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
            pDialog.setContentView(R.layout.splash);
        pDialog.setContentView(R.layout.splash);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }


    void refreshItems() {
        rela_refresh.setVisibility(View.VISIBLE);
        final List<PostModel> list = new ArrayList<>();
        StringRequest req = new StringRequest(urlJsonArry,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("error", response.toString());
                        if(response.toString().contentEquals("NO")) {
                            Toast.makeText(getApplicationContext(), "Move On Guys!! There's Nothing Here", Toast.LENGTH_LONG).show();
                        }

                        try {

                            // Parsing json array response
                            // loop through each json object
                            JSONArray jsonArray  = new JSONArray(response);
                            jsonResponse = "";
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject person = (JSONObject) jsonArray.get(i);
                                String id_forum = person.getString("id_forum");
                                String item = person.getString("item");
                                String submitted_by = person.getString("submitted_by");
                                String date_submitted = person.getString("date_submitted");
                                //String comment = person.getString("count");

                                PostModel postModel = new PostModel(id_forum,item,submitted_by,date_submitted);
                                list.add(postModel);

                                if(list.size()==0) {
                                    btn_refresh.setVisibility(View.VISIBLE);
                                }

                            }
                            initRecyclerView(list);


                            // Toast.makeText(getApplicationContext(), ""+jsonResponse, Toast.LENGTH_LONG).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            //Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }

                        rela_refresh.setVisibility(View.GONE);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("error_com",volleyError.getMessage().toString());
                rela_refresh.setVisibility(View.GONE);
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
        protected TextView tv_post,tv_postcomments;
        ImageView imagepic;
        String whois;
        TextView tv_post_owner, date;
        LinearLayout img_likes;
        FrameLayout container;

        FakePageVH(View itemView) {
            super(itemView);
            cardview = (CardView) itemView.findViewById(R.id.card_list);
            tv_post = (TextView) itemView.findViewById(R.id.tv_post);
            date = (TextView) itemView.findViewById(R.id.tv_postcomments);
            //tv_postlikes = (TextView) itemView.findViewById(R.id.tv_postlikes);
            tv_post_owner=(TextView) itemView.findViewById(R.id.tv_post_owner);
            tv_postcomments = (TextView) itemView.findViewById(R.id.datesub);



        }

    }
    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            PageFragmentCommunity.this.refreshItems();
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


}
