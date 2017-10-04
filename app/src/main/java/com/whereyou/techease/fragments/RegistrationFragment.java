package com.whereyou.techease.fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.whereyou.techease.utils.Configuration;
import com.whereyou.techease.utils.DialogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegistrationFragment extends Fragment {

    Button btnNextSignUp;
    Fragment fragment;
    EditText etUserName, etEmail, etDob, etPassword, etConfirmPassword;
    String strDevice, strUserName, strDob,  strEmail, strPassword, strConfirmPassword;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ImageView ivBackToLogin;

    public RegistrationFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_registration, container, false);
        sharedPreferences = getActivity().getSharedPreferences(Configuration.MY_PREF, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        ivBackToLogin = (ImageView) view.findViewById(R.id.iv_back_login);
        ivBackToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment = new LoginFragment();
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
            }
        });
        etUserName = (EditText) view.findViewById(R.id.et_username);
        etDob = (EditText) view.findViewById(R.id.et_Dob);
        etEmail = (EditText) view.findViewById(R.id.et_email_signup);
        etPassword = (EditText) view.findViewById(R.id.et_password_signup);
        etConfirmPassword = (EditText) view.findViewById(R.id.et_confirm_password);

        btnNextSignUp = (Button) view.findViewById(R.id.btn_next_signup);
        btnNextSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDataInput();

            }
        });

        return view;
    }


    public void onDataInput() {
        strUserName = etUserName.getText().toString();
        strEmail = etEmail.getText().toString();
        strDob = etDob.getText().toString();
        strPassword = etPassword.getText().toString();
        strConfirmPassword = etConfirmPassword.getText().toString();

        if (strUserName.equals("") || strEmail.length() < 3) {
            etUserName.setError("Enter a valid First name");
        }  else if (strDob.equals("") ) {
            etDob.setError("Enter a valid phone number");
        } else if ((!android.util.Patterns.EMAIL_ADDRESS.matcher(strEmail).matches())) {
            etEmail.setError("Please enter valid email id");
        } else if (!strPassword.equals(strConfirmPassword)) {
            etConfirmPassword.setError("Password doesn't match");
        } else {
            Log.d("zma data", strUserName+"\n"+strEmail+"\n"+strDob+"\n"+strConfirmPassword);
            DialogUtils.showProgressSweetDialog(getActivity(), "Getting registered");
            apiCall();


        }

    }
//

    public void apiCall() {
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, Configuration.USER_URL+"register", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("zma  reg response", response);
                DialogUtils.sweetAlertDialog.dismiss();
                if (response.contains("200")) {
                    try {
                        JSONObject jsonObject = new JSONObject(response).getJSONObject("message");
                        String strApiToken = jsonObject.getString("api_token");
                        editor.putString("api_token", strApiToken).commit();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(getActivity(), "Registration Successful", Toast.LENGTH_SHORT).show();
                    fragment = new LoginFragment();
                    getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();


                } else {
                   DialogUtils.showWarningAlertDialog(getActivity(), "Something went wrong");
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("zma error", String.valueOf(error));
                DialogUtils.sweetAlertDialog.dismiss();
                DialogUtils.showWarningAlertDialog(getActivity(), String.valueOf(error.getCause()));

            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded;charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username", strUserName);
                params.put("email", strEmail);
                params.put("dob", strDob);
                params.put("password", strPassword);

                return params;
            }

        };
        RequestQueue mRequestQueue = Volley.newRequestQueue(getActivity());
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(200000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(stringRequest);
    }

}
