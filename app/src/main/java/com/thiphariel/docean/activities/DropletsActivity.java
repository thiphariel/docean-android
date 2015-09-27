package com.thiphariel.docean.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.github.clans.fab.FloatingActionButton;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.thiphariel.docean.DropletsAdapter;
import com.thiphariel.docean.DropletsViewItem;
import com.thiphariel.docean.NavigationDrawerFragment;
import com.thiphariel.docean.R;
import com.thiphariel.docean.classes.DigitalOceanRestClient;
import com.thiphariel.docean.classes.Docean;
import com.thiphariel.docean.classes.DoceanPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HeaderElement;
import cz.msebera.android.httpclient.ParseException;

public class DropletsActivity extends AppCompatActivity implements DropletsAdapter.DropletsClickListener, View.OnClickListener {

    private String mApiToken;
    private RecyclerView mRecyclerView;
    private DropletsAdapter mAdapter;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_droplets);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        // Override activity name
        getSupportActionBar().setTitle(R.string.title_activity_droplets);

        mRecyclerView = (RecyclerView) findViewById(R.id.droplets_recycler_view);
        mAdapter = new DropletsAdapter(this, new ArrayList<DropletsViewItem>());
        mAdapter.setDropletsClickListener(this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setup((DrawerLayout) findViewById(R.id.drawer_layout), toolbar);

        FloatingActionButton addNewDropletButton = (FloatingActionButton) findViewById(R.id.fab_add_droplet);
        addNewDropletButton.setOnClickListener(this);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setTitle("Loading");
        mProgressDialog.setMessage("Fetching data...");

        // Check if user is logged in
        mApiToken = DoceanPreferences.readSharedPreference(this, DoceanPreferences.API_TOKEN, "");

        if (mApiToken.isEmpty()) {
            startActivity(new Intent(this, LoginActivity.class));
        } else {
            Docean.auth = new Header() {
                @Override
                public String getName() {
                    return "Authorization";
                }

                @Override
                public String getValue() {
                    return "Bearer " + mApiToken;
                }

                @Override
                public HeaderElement[] getElements() throws ParseException {
                    return new HeaderElement[0];
                }
            };

            Log.d("TOKEN", mApiToken);
            fetchData();
        }
    }

    private void fetchData() {
        Header[] headers = new Header[]{Docean.auth};

        mProgressDialog.show();

        DigitalOceanRestClient.get(this, "/droplets", headers, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.d("RESPONSE", response.toString());

                updateRecyclerViewWithData(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.d("ERROR", errorResponse.toString());
            }

            @Override
            public void onFinish() {
                super.onFinish();
                mProgressDialog.dismiss();
            }
        });
    }

    private void updateRecyclerViewWithData(JSONObject object) {
        List<DropletsViewItem> data = new ArrayList<>();

        JSONArray droplets = null;
        try {
            droplets = object.getJSONArray("droplets");
            Log.d("DROPLETS", droplets.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (droplets != null) {
            for (int i = 0; i < droplets.length(); i++) {
                try {
                    JSONObject current = droplets.getJSONObject(i);
                    Log.d("CURRENT", current.toString());
                    DropletsViewItem item = new DropletsViewItem(current.getString("id"), current.getString("name"), current.getInt("vcpus"), current.getInt("disk"));
                    Log.d("ITEM", item.getName());
                    data.add(item);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        Log.d("DATA", data.toString());

        mAdapter = new DropletsAdapter(this, data);
        mAdapter.setDropletsClickListener(this);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_refresh) {
            fetchData();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void click(View view, int position) {

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fab_add_droplet) {
            startActivity(new Intent(this, AddDropletActivity.class));
        }
    }
}
