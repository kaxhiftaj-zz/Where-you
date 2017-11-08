package com.whereyou.techease.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.whereyou.techease.controllers.AllCommentsAdapter;
import com.whereyou.techease.controllers.Contestents;
import com.whereyou.techease.utils.Configuration;
import com.whereyou.techease.utils.DialogUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Comments extends AppCompatActivity {

    RecyclerView recyclerView;
    List<Contestents> commentsList ;
    AllCommentsAdapter adapterComments;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String api_token ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        sharedPreferences = this.getSharedPreferences(Configuration.MY_PREF, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        api_token = sharedPreferences.getString("api_token","");
        recyclerView = (RecyclerView) findViewById(R.id.myNews);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        apiCall();
        commentsList = new ArrayList<>();
        adapterComments = new AllCommentsAdapter(this, commentsList);
        recyclerView.setAdapter(adapterComments);
    }




    public void apiCall() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://whereyou.techeasesol.com/api/v1/places?api_token="+api_token
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("zma  reg response", response);
                //   DialogUtils.sweetAlertDialog.dismiss();
                if (response.contains("200")) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject temp = jsonArray.getJSONObject(i);
                            Contestents contes = new Contestents();
                            contes.setLocationName(temp.getString("loc_name"));
                            contes.setLocationComment(temp.getString("comment"));
                            contes.setLoctionDistance(temp.getString("distance"));
                            commentsList.add(contes);

                        }
                        adapterComments.notifyDataSetChanged();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(Comments.this, "error", Toast.LENGTH_SHORT).show();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //DialogUtils.sweetAlertDialog.dismiss();
                // DialogUtils.showErrorTypeAlertDialog(getActivity(), "Server error");
                Log.d("error" , String.valueOf(error.getCause()));

            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded;charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("api_token" , map.token);
                return params;
            }

        };
        RequestQueue mRequestQueue = Volley.newRequestQueue(Comments.this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(200000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(stringRequest);
    }
}
