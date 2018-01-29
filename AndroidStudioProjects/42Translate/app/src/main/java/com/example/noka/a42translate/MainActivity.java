package com.example.noka.a42translate;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.R.attr.id;
import static com.example.noka.a42translate.R.id.card_view_request;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawer;
    public String whois;
    DatabaseHelper dbhelper;
    private RecyclerView mRootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //whois = getIntent().getExtras().getString("logged_as");

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.setVisibility(View.VISIBLE);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_log);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Logging you out...", Toast.LENGTH_LONG).show();

                logOut();
               // logOutAdmin();
            }
        });
        dbhelper =new DatabaseHelper(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


//        Intent intent = new Intent(this, LoginActivity.class);
//        //intent.putExtra("username", username);
//        startActivity(intent);

    }


    public void logOut(){
    dbhelper.deleteUser();

    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
    startActivity(intent);
}
    public void logOutAdmin(){
        dbhelper.deleteUser();
        dbhelper.deleteAdmin();

        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
    }
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {

            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

      if (id == R.id.login_form)
      {
          setTitle("Log In");
          Intent intent = new Intent(this, LoginActivity.class);
          //intent.putExtra("username", username);
          //intent.putExtra("logged_as", whois);
          startActivity(intent);
      }
      else if (id == R.id.admin)
      {
              setTitle("Admin");
              Intent intent = new Intent(this, Admin.class);
              //intent.putExtra("logged_as", whois);
              //intent.putExtra("username", username);
              startActivity(intent);}




      else if (id == R.id.forum)
      {

          setTitle("Forum");
          Intent intent = new Intent(this,PageFragmentCommunity.class);

          startActivity(intent);
      }
      else if (id == R.id.lyrics)
      {
            setTitle("Lyrics");
          Intent intent = new Intent(MainActivity.this, Lyrics.class);
         // intent.putExtra("logged_as", whois);
          startActivity(intent);


      }
      else if (id == R.id.phrases)
      {
            setTitle("Phrases");
          Intent intent = new Intent(MainActivity.this, Phrases.class);
          intent.putExtra("logged_as", whois);
          startActivity(intent);

      }
      else if (id == R.id.saying)
      {
            setTitle("Sayings");
          Intent intent = new Intent(MainActivity.this, Sayings.class);
          intent.putExtra("logged_as", whois);
          startActivity(intent);

      }
      else if (id == R.id.words)
      {
            setTitle("Words");
          Intent intent = new Intent(MainActivity.this, Words.class);
          intent.putExtra("logged_as", whois);
          startActivity(intent);

      }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
