package com.whereyou.techease.fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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
import com.whereyou.techease.activities.MainActivity;
import com.whereyou.techease.utils.Configuration;
import com.whereyou.techease.utils.DialogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class LoginFragment extends Fragment {

    Fragment fragment;
    Button btnLogin;
    EditText etEmail, etPassword;
    String strEmail, strPassword;
    TextView tvForgetPassword, tvRegisterHere;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ImageView ivBackToLogin;
    Typeface t1;
    String strAPIToken ;

    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        sharedPreferences = getActivity().getSharedPreferences(Configuration.MY_PREF, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        t1= Typeface.createFromAsset(getActivity().getAssets(), "fonts/Av.otf");

        etEmail = (EditText) view.findViewById(R.id.et_email_login);
        etPassword = (EditText) view.findViewById(R.id.et_password_login);
        tvForgetPassword = (TextView) view.findViewById(R.id.tv_forget_password);
        btnLogin = (Button) view.findViewById(R.id.btn_login);
        tvRegisterHere = (TextView) view.findViewById(R.id.tv_register_here);

        etEmail.setTypeface(t1);
        etPassword.setTypeface(t1);
        btnLogin.setTypeface(t1);
        tvForgetPassword.setTypeface(t1);

        tvRegisterHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment = new RegistrationFragment();
                getFragmentManager().beginTransaction().
                        replace(R.id.fragment_container, fragment).addToBackStack("tag").commit();
            }
        });
        tvForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment = new EmailVerificationFragment();
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDataInput();
            }
        });
        return view;
    }

    public void onDataInput() {
        strEmail = etEmail.getText().toString();
        strPassword = etPassword.getText().toString();
        if ((!android.util.Patterns.EMAIL_ADDRESS.matcher(strEmail).matches())) {
            etEmail.setError("Please enter valid email id");
        } else if (strPassword.equals("")) {
            etPassword.setError("Please enter your password");
        } else {
            DialogUtils.showProgressSweetDialog(getActivity(), "Getting Login");
            apiCall();
        }
    }

    public void apiCall() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Configuration.USER_URL + "login"
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("zma response login", response);
                if (response.contains("200")) {
                    try {
                        DialogUtils.sweetAlertDialog.dismiss();
                        JSONObject jsonObject = new JSONObject(response).getJSONObject("data");
                         strAPIToken = jsonObject.getString("api_token");
                        editor.putString("api_token", strAPIToken).commit();
                        Log.d("zma token", String.valueOf(strAPIToken));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    startActivity(new Intent(getActivity(), MainActivity.class));
                    Toast.makeText(getActivity(), strAPIToken, Toast.LENGTH_SHORT).show();

                } else {
                    DialogUtils.sweetAlertDialog.dismiss();
                   DialogUtils.showWarningAlertDialog(getActivity(), "Something went wrong");
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                DialogUtils.sweetAlertDialog.dismiss();
               DialogUtils.showErrorTypeAlertDialog(getActivity(), "Server error");

            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded;charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", strEmail);
                params.put("password", strPassword);
                params.put("Accept", "application/json");
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
