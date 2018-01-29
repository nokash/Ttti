package com.example.noka.a42translate;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Users extends AppCompatActivity {
    RelativeLayout root_view;
    SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView mRootView;
    private String urlJsonArry = "http://10.42.0.1/42translate/fetchUser.php";
    private static final String DELETE_URL = "http://10.42.0.1/42translate/dropUser.php";
    private static final String ADMIN_ADD = "http://10.42.0.1/42translate/addAdmin.php";
    public  static String KEY_ID = "id";

    private String jsonResponse;
    public  String whois;

    RelativeLayout rela_refresh;
    TextView expanded_pic;
   
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        
        mRootView= (RecyclerView) findViewById(R.id.recycler_search_users);

        
        makeJsonArrayRequest();

       
        
    }

    private void initRecyclerView(List<UserModel> items) {
        mRootView.setAdapter(new Users.FakePageAdapter(items,getApplicationContext()));

    }



    private static class FakePageAdapter extends RecyclerView.Adapter<Users.FakePageVH> {
        List<UserModel> items;
        String username,usertype, userid;
        Context context;

        FakePageAdapter(List<UserModel> items, Context context) {
            this.items = items;
            this.context=context;

        }
        @Override
        public Users.FakePageVH onCreateViewHolder(ViewGroup viewGroup, int i) {
            View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.users, viewGroup, false);

            return new Users.FakePageVH(itemView);
        }



        @Override
        public void onBindViewHolder(final Users.FakePageVH fakePageVH, final int i) {
            username=items.get(i).getName();
            userid=items.get(i).getUser_id();
            usertype=items.get(i).getUsertype();



            fakePageVH.cardview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    username=items.get(i).getName();
                    userid=items.get(i).getUser_id();
                    usertype=items.get(i).getUsertype();

                    Intent intent=new Intent(v.getContext(),PagePostDisplay.class);
                    intent.putExtra("name",username);
                    intent.putExtra("id", userid);
                    intent.putExtra("usertype",usertype);

                    v.getContext().startActivity(intent);
                    //Toast.makeText(context, "", Toast.LENGTH_LONG).show();
                }
            });

            fakePageVH.delBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteUser();
                    //((Activity) context).finish();
                    //Toast.makeText(context, "", Toast.LENGTH_LONG).show();
                }
            });

            fakePageVH.addMtu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addAdmin();
                    //((Activity) context).finish();
                    //Toast.makeText(context, "", Toast.LENGTH_LONG).show();
                }
            });


            if(items.get(i).getName()!=null) {
                fakePageVH.user.setText(items.get(i).getName());

            }else {
                fakePageVH.user.setVisibility(View.GONE);
            }
            fakePageVH.usertyp.setText(items.get(i).getUsertype());
        }
        @Override
        public int getItemCount() {
            return items.size();
        }

        public void deleteUser(){
            final String user_id = userid;

            StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.POST, DELETE_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            Toast.makeText(context, response, Toast.LENGTH_LONG).show();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(KEY_ID, user_id);

                    return params;
                }

            };

            RequestQueue requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(stringRequest);
        }
        public void addAdmin(){
            final String user_id = userid;

            StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.POST, ADMIN_ADD,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            Toast.makeText(context, response, Toast.LENGTH_LONG).show();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(KEY_ID, user_id);

                    return params;
                }

            };

            RequestQueue requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(stringRequest);
        }



}




    private void makeJsonArrayRequest() {
        final List<UserModel> list = new ArrayList<>();

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

                                JSONObject person = (JSONObject) jsonArray.get(i);
                                String user_id = person.getString("id");
                                String name = person.getString("name");
                                String usertype = person.getString("usertype");

                                UserModel postModel = new UserModel(user_id, name, usertype);
                                list.add(postModel);


                                initRecyclerView(list);

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            //Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
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
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(req);

    }

    private static class FakePageVH extends RecyclerView.ViewHolder {
        protected CardView cardview = null;
        protected TextView user,usertyp;
        Button delBtn, addMtu;

        LinearLayout img_likes;
        FrameLayout container;

        FakePageVH(View itemView) {
            super(itemView);
            cardview = (CardView) itemView.findViewById(R.id.card_list_user);
            user = (TextView) itemView.findViewById(R.id.user);
            usertyp = (TextView) itemView.findViewById(R.id.usertype);

            delBtn = (Button) itemView.findViewById(R.id.delUser);
            //addMtu = (Button) itemView.findViewById(R.id.adminUser);




        }

    }


}

