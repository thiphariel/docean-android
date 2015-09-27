package com.thiphariel.docean.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.thiphariel.docean.R;
import com.thiphariel.docean.classes.DigitalOceanRestClient;
import com.thiphariel.docean.classes.Docean;
import com.thiphariel.docean.classes.DoceanPreferences;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mContext = this;

        Button loginButton = (Button) findViewById(R.id.login_button);
        Button registerButton = (Button) findViewById(R.id.register_button);
        loginButton.setOnClickListener(this);
        registerButton.setOnClickListener(this);

        Uri uri = getIntent().getData();

        if (uri != null) {
            String code = uri.getQueryParameter("code");

            RequestParams params = new RequestParams();
            params.put("client_id", Docean.CLIENT_ID);
            params.put("client_secret", Docean.CLIENT_SECRET);
            params.put("code", code);
            params.put("grant_type", "authorization_code");
            params.put("redirect_uri", Docean.REDIRECT_URL);

            DigitalOceanRestClient.postWithOverrideUrl("https://cloud.digitalocean.com/v1/oauth/token", params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Log.d("SUCCESS", response.toString());
                    // Write token in shared preferences
                    try {
                        DoceanPreferences.saveToSharedPreference(mContext, DoceanPreferences.API_TOKEN, response.getString("access_token"));
                        startActivity(new Intent(mContext, DropletsActivity.class));
                        finish();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    throwable.printStackTrace();
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.login_button) {
            startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://cloud.digitalocean.com/v1/oauth/authorize?client_id=" + Docean.CLIENT_ID + "&redirect_uri=" + Docean.REDIRECT_URL + "&response_type=code&scope=read%20write")));
        } else if (v.getId() == R.id.register_button) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(new ContextThemeWrapper(this, android.R.style.Theme_DeviceDefault_Dialog_Alert));
            alertDialogBuilder.setTitle("Register");
            alertDialogBuilder.setMessage("You will be redirect to Digitalocean register page.")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://www.digitalocean.com/?refcode=9677f52238f1")));
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }
}
