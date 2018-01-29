package com.example.noka.a42translate;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Beni on 3/28/2017.
 */

public class PagePost extends AppCompatActivity {
    private RecyclerView mRootView;
    TextView tv_post_full,tv_post_owner,tv_postcomments ,tv_postlikes,tv_loading,be_the_first;
    String post,comments,likes,photo,post_owner,post_id,user_id,post_time,comment_content;
    ImageView user_photo;
    LinearLayout image_likes;
    FloatingActionButton fab_comment;
    ProgressDialog pDialog;
    AlertDialog b;
    EditText ed_comment_content;
    String post_id_comment;
    SwipeRefreshLayout swipeRefreshLayout;
    ProgressBar progressbar;
    TextView tv_loading_message;
DatabaseHelper dbhelper = new DatabaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_post);



        pDialog = new ProgressDialog(this);
        mRootView=(RecyclerView) findViewById(R.id.my_recycler_view);
       // tv_post_full=(TextView) findViewById(R.id.tv_post_full);
        //user_photo=(ImageView) findViewById(R.id.imageView_post);
        tv_post_owner=(TextView) findViewById(R.id.tv_post_owner);
        tv_postcomments=(TextView) findViewById(R.id.tv_postcomments);
        //tv_postlikes=(TextView) findViewById(R.id.tv_postlikes);
        //image_likes=(LinearLayout) findViewById(R.id.image_likes);
       // tv_loading=(TextView) findViewById(R.id.tv_postlikes);
        swipeRefreshLayout=(SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        progressbar=(ProgressBar) findViewById(R.id.progressbar);
        tv_loading_message=(TextView) findViewById(R.id.tv_loading_message);
        //be_the_first=(TextView) findViewById(R.id.be_the_first);

        fab_comment = (FloatingActionButton) findViewById(R.id.fab_comment);
        fab_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            //    showOddPostsDialog();

            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                //refresh();
            }
        });

        comments = getIntent().getExtras().getString("comments");

        post_owner = getIntent().getExtras().getString("post_owner");







        tv_post_owner.setText(post_owner);
        tv_post_full.setText(post);
        tv_postcomments.setText(comments);
        tv_postlikes.setText(likes);

        tv_postlikes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                like();
            }
        });
        image_likes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                like();
            }
        });
        if(photo.isEmpty()) {
            Picasso.with(PagePost.this)
                    .load("https://chestiest-bypass.jpg")
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .into(user_photo);

        }else {
            Picasso.with(PagePost.this)
                    .load(photo)
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .placeholder(R.drawable.grey)
                    .error(R.drawable.grey)
                    .into(user_photo, new Callback() {
                        @Override
                        public void onSuccess() {
                            Log.d("Picasso", "Image loaded from cache>>>");
                        }

                        @Override
                        public void onError() {
                            Log.d("Picasso", "Try again in ONLINE mode if load from cache is failed");
                            Picasso.with(PagePost.this)
                                    .load(photo)
                                    .placeholder(R.drawable.grey)
                                    .error(R.drawable.grey)
                                    .into(user_photo, new Callback() {
                                        @Override
                                        public void onSuccess() {
                                            Log.d("Picasso", "Image loaded from web>>>");
                                        }

                                        @Override
                                        public void onError() {
                                            Log.d("Picasso", "Failed to load image online and offline, make sure you enabled INTERNET permission for your app and the url is correct>>>>>>>");
                                        }
                                    });
                        }
                    });
        }





      //  makeJsonArrayRequest_commenents();

    }




    private void like() {
        String url = "https://chestiest-bypass.000webhostapp.com/like_post.php";
        Toast.makeText(PagePost.this, "liked" , Toast.LENGTH_LONG).show();


        StringRequest like = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                String message = null;
                if (volleyError instanceof NetworkError) {
                    message = "Cannot connect to Internet...Please check your connection! and try again";
                    Toast.makeText(PagePost.this,""+message, Toast.LENGTH_LONG).show();
                } else if (volleyError instanceof ServerError) {
                    message = "The server could not be found. Please try again after some time!!";
                    Toast.makeText(PagePost.this,""+message, Toast.LENGTH_LONG).show();
                } else if (volleyError instanceof AuthFailureError) {
                    message = "Cannot connect to Internet...Please check your connection! and try again";
                    Toast.makeText(PagePost.this,""+message, Toast.LENGTH_LONG).show();
                } else if (volleyError instanceof ParseError) {
                    message = "Parsing error! Please try again ";
                    Toast.makeText(PagePost.this,""+message, Toast.LENGTH_LONG).show();
                } else if (volleyError instanceof NoConnectionError) {
                    message = "Cannot connect to Internet...Please check your connection! and try again";
                    Toast.makeText(PagePost.this,""+message, Toast.LENGTH_LONG).show();
                } else if (volleyError instanceof TimeoutError) {
                    message = "Connection TimeOut! try again";
                    Toast.makeText(PagePost.this,""+message, Toast.LENGTH_LONG).show();
                }
            }
        }){

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("game_id", post_id);
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(like);
    }


            //apply on click on your root view


    private static class FakePageVH extends RecyclerView.ViewHolder {
        protected CardView cardview = null;
        ImageView pic;
        TextView tv_comment,tv_comment_likes;

        FakePageVH(View itemView) {
            super(itemView);
            cardview = (CardView) itemView.findViewById(R.id.card_list);
            //pic=(ImageView) itemView.findViewById(R.id.pic);
           // tv_comment=(TextView) itemView.findViewById(R.id.tv_comment);
           // tv_comment_likes=(TextView) itemView.findViewById(R.id.tv_comment_likes);
        }
    }

//    public void showOddPostsDialog() {
//        Button btn_comment,btn_exit;
//
//
//        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
//        LayoutInflater inflater = this.getLayoutInflater();
//        final View dialogView = inflater.inflate(R.layout.comment_dialog, null);
//        btn_comment = (Button) dialogView.findViewById(R.id.bill_views);
//        btn_exit = (Button) dialogView.findViewById(R.id.reset);
//        ed_comment_content= (EditText) dialogView.findViewById(R.id.ed_comment_content);
//        dialogBuilder.setView(dialogView);
//        dialogBuilder.setCancelable(false);
//         b = dialogBuilder.create();
//        btn_comment.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                upload_post();
//
//
//            }
//        });

//        btn_exit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                b.dismiss();
//            }
//        });
//        b.show();
//    }

    public void upload_post() {
        try {
            Cursor cus = dbhelper.select_user();
            if (cus.getCount() == 0) {
                Intent intent=new Intent(this,PagePost.class);
                startActivity(intent);
                Toast.makeText(getApplicationContext(),"You got register first to be able to comment", Toast.LENGTH_LONG).show();
                finish();


            }else {


            }
        } catch (SQLiteException se) {

            Log.e(getClass().getSimpleName(), "Open the database");
        }
        String tag_string_req = "send_post_tag";
        String url = "https://chestiest-bypass.000webhostapp.com/comment_uploader.php";
        comment_content=ed_comment_content.getText().toString();
        post_time= getCurrentTime();



        try {
            Cursor cus = dbhelper.select_post_user();
            if (cus.getCount() == 0) {
            }else {
                while (cus.moveToNext()) {
                    user_id = cus.getString(0);
                }

            }
        } catch (SQLiteException se) {

            Log.e(getClass().getSimpleName(), "Open the database");
        }


        pDialog.setMessage("sending your comment...");
        pDialog.setCancelable(false);
        pDialog.setCanceledOnTouchOutside(false);

        pDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("error", response.toString());
                if (response.toString().contains("OK")) {
                    Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
                    b.dismiss();



                } else if (response.toString().contains("NO")) {
                    Toast.makeText(getApplicationContext(),"unknown error", Toast.LENGTH_LONG).show();
                    b.dismiss();


                }
                pDialog.dismiss();


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {
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
                pDialog.dismiss();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                //Converting Bitmap to String
                Map<String, String> params = new HashMap<String, String>();
                params.put("post_id", post_id);
                params.put("user_id", user_id);
                params.put("comment_content",comment_content);
                params.put("comment_time","");
                return params;
            }

        };

// Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

//    private void makeJsonArrayRequest_commenents() {
//        progressbar.setVisibility(View.VISIBLE);
//        tv_loading_message.setVisibility(View.VISIBLE);
//        String urlJsonArry="https://chestiest-bypass.000webhostapp.com/comment_selector.php";
//        final List<CommentModel> list = new ArrayList<>();
//            tv_loading.setVisibility(View.VISIBLE);
//            StringRequest req = new StringRequest(Request.Method.POST,urlJsonArry,
//                    new Response.Listener<String>() {
//                        @Override
//                        public void onResponse(String response) {
//
//                            if(response.contains("NO")){
//                                progressbar.setVisibility(View.GONE);
//                                tv_loading_message.setVisibility(View.GONE);
//                                be_the_first.setVisibility(View.VISIBLE);
//
//                            }else {
//
//                                try {
//                                    JSONArray jsonarray = new JSONArray(response);
//                                    for (int i = 0; i < jsonarray.length(); i++) {
//
//                                        JSONObject person = (JSONObject) jsonarray.get(i);
//                                        String comment_id = person.getString("comment_id");
//                                        String comment_content = person.getString("comment_content");
//                                        String comment_date = person.getString("comment_date");
//                                        String likes = person.getString("likes");
//                                        String photo = person.getString("photo");
//                                        String name1 = person.getString("name1");
//                                        String name2 = person.getString("name2");
//                                        if (photo.isEmpty()) {
//
//                                        } else {
//
//                                            Picasso.with(PagePost.this).load(photo).fetch();
//                                        }
//
//
//                                        CommentModel commentModel = new CommentModel(comment_id, comment_content, comment_date, likes, photo, name1, name2);
//                                        list.add(commentModel);
//                                    }
//
//                                    initRecyclerView(list);
//
//
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//
//                                progressbar.setVisibility(View.GONE);
//                                tv_loading_message.setVisibility(View.GONE);
//                            }
//
//                        }
//                    }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError volleyError) {
////                    Log.d("error_com",volleyError.getMessage().toString());
//                    progressbar.setVisibility(View.GONE);
//                    tv_loading_message.setVisibility(View.GONE);
//                    String message = null;
//                    if (volleyError instanceof NetworkError) {
//                        message = "Cannot connect to Internet...Please check your connection!";
//                        Toast.makeText(PagePost.this,""+message, Toast.LENGTH_LONG).show();
//                    } else if (volleyError instanceof ServerError) {
//                        message = "The server could not be found. Please try again after some time!!";
//                        Toast.makeText(PagePost.this,""+message, Toast.LENGTH_LONG).show();
//                    } else if (volleyError instanceof AuthFailureError) {
//                        message = "Cannot connect to Internet...Please check your connection!";
//                        Toast.makeText(PagePost.this,""+message, Toast.LENGTH_LONG).show();
//                    } else if (volleyError instanceof ParseError) {
//                        message = "Parsing error! Please try again after some time!!";
//                        Toast.makeText(PagePost.this,""+message, Toast.LENGTH_LONG).show();
//                    } else if (volleyError instanceof NoConnectionError) {
//                        message = "Cannot connect to Internet...Please check your connection!";
//                        Toast.makeText(PagePost.this,""+message, Toast.LENGTH_LONG).show();
//                    } else if (volleyError instanceof TimeoutError) {
//                        message = "Connection TimeOut! Please check your internet connection.";
//                        Toast.makeText(PagePost.this,""+message, Toast.LENGTH_LONG).show();
//                    }
//                }
//            }){
//
//                @Override
//                protected Map<String, String> getParams() {
//                    //Converting Bitmap to String
//                    Map<String, String> params = new HashMap<String, String>();
//                    params.put("post_id", post_id);
//                    return params;
//                }
//
//            };
//
//            // Adding request to request queue
//            AppController.getInstance().addToRequestQueue(req);
//
//
//    }

//    public  void refresh(){
//        tv_loading.setVisibility(View.VISIBLE);
//        String urlJsonArry="https://chestiest-bypass.000webhostapp.com/comment_selector.php";
//        final List<CommentModel> list = new ArrayList<>();
//        JsonArrayRequest req = new JsonArrayRequest(urlJsonArry,
//                new Response.Listener<JSONArray>() {
//                    @Override
//                    public void onResponse(JSONArray response) {
//                        Log.d("error", response.toString());
//                        // Toast.makeText(getContext(), "Error:"+response.toString(), Toast.LENGTH_LONG).show();
//
//
//                        try {
//                            for (int i = 0; i < response.length(); i++) {
//
//                                JSONObject person = (JSONObject) response.get(i);
//                                String comment_id = person.getString("comment_id");
//                                String comment_content = person.getString("comment_content");
//                                String comment_date = person.getString("comment_date");
//                                String likes = person.getString("likes");
//                                String photo = person.getString("photo");
//                                String name1 = person.getString("name1");
//                                String name2 = person.getString("name2");
//                                if (photo.isEmpty()) {
//
//                                } else {
//
//                                    Picasso.with(PagePost.this).load(photo).fetch();
//                                }
//
//
//                                CommentModel commentModel = new CommentModel(comment_id,comment_content, comment_date, likes, photo,name1,name2);
//                                list.add(commentModel);
//                            }
//                            initRecyclerView(list);
//
//
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                            //Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
//                        }
//
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError volleyError) {
//                tv_loading.setVisibility(View.GONE);
////                    Log.d("error_com",volleyError.getMessage().toString());
//                String message = null;
//                if (volleyError instanceof NetworkError) {
//                    message = "Cannot connect to Internet...Please check your connection!";
//                    Toast.makeText(PagePost.this,""+message, Toast.LENGTH_LONG).show();
//                } else if (volleyError instanceof ServerError) {
//                    message = "The server could not be found. Please try again after some time!!";
//                    Toast.makeText(PagePost.this,""+message, Toast.LENGTH_LONG).show();
//                } else if (volleyError instanceof AuthFailureError) {
//                    message = "Cannot connect to Internet...Please check your connection!";
//                    Toast.makeText(PagePost.this,""+message, Toast.LENGTH_LONG).show();
//                } else if (volleyError instanceof ParseError) {
//                    message = "Parsing error! Please try again after some time!!";
//                    Toast.makeText(PagePost.this,""+message, Toast.LENGTH_LONG).show();
//                } else if (volleyError instanceof NoConnectionError) {
//                    message = "Cannot connect to Internet...Please check your connection!";
//                    Toast.makeText(PagePost.this,""+message, Toast.LENGTH_LONG).show();
//                } else if (volleyError instanceof TimeoutError) {
//                    message = "Connection TimeOut! Please check your internet connection.";
//                    Toast.makeText(PagePost.this,""+message, Toast.LENGTH_LONG).show();
//                }
//            }
//        });
//        tv_loading.setVisibility(View.GONE);
//
//
//        // Adding request to request queue
//        AppController.getInstance().addToRequestQueue(req);
//
//
//    }

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

    public String user_id(){

        try {
            Cursor cus = dbhelper.select_post_user();
            if (cus.getCount() == 0) {
            }else {
                while (cus.moveToNext()) {
                    user_id = cus.getString(0);
                }

            }
        } catch (SQLiteException se) {

            Log.e(getClass().getSimpleName(), "Open the database");
        }

        return user_id;

    }
}
