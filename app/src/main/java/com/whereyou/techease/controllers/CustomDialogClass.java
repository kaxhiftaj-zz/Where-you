package com.whereyou.techease.controllers;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.whereyou.techease.R;
import com.whereyou.techease.activities.map;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kaxhiftaj on 10/18/17.
 */

    public class CustomDialogClass extends Dialog implements
            android.view.View.OnClickListener {

    String comm;
    public Activity c;
    public Dialog d;
    public Button yes, no;
    EditText comment;

    public CustomDialogClass(Activity a) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog);
        yes = (Button) findViewById(R.id.btn_yes);
        comment =(EditText)findViewById(R.id.comment);
        yes.setOnClickListener(this);
        Log.d("zma loc lat ", map.loclat);
        Log.d("zma loc long", map.loclong);
        Log.d("zma cur lat ", map.curlat);
        Log.d("zma cur long", map.curlong);
        Log.d("zma loc name", map.locname);
        Log.d("zma token", map.token);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_yes:
                apiCall();
                break;

            default:
                break;
        }
        dismiss();
    }


    public void apiCall() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://whereyou.techeasesol.com/api/v1/places"
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("zma response login", response);
                if (response.contains("200")) {
                    try {

                        String jsonObject = new JSONObject(response).getString("message");
                        Toast.makeText(c, String.valueOf(jsonObject), Toast.LENGTH_SHORT).show();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(c, "error", Toast.LENGTH_SHORT).show();
                    Log.d("error" , "Something went wrong");
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("servre error" , String.valueOf(error.getCause()));
                Toast.makeText(c, "error", Toast.LENGTH_SHORT).show();

            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded;charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                comm = comment.getText().toString();
                params.put("api_token" , map.token);
                params.put("loc_name" , map.locname);
                params.put("loc_lat" , map.loclat);
                params.put("loc_lon" , map.loclong);
                params.put("curr_lat" , map.curlat);
                params.put("curr_lon" , map.curlong);
                params.put("comment" ,  comm );
                params.put("Accept", "application/json");
                return params;
            }

        };
        RequestQueue mRequestQueue = Volley.newRequestQueue(c);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(200000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(stringRequest);
    }

}