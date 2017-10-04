package com.whereyou.techease.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class EmailVerificationFragment extends Fragment {



    Fragment fragment;
    ImageView ivBackToLogin;
    Button btnSendEmail;
    String strEmail;
    EditText etEmailForgetPassword;
    SweetAlertDialog pDialog;
    TextView tvSkipLogin;

    public EmailVerificationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EmailVerificationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EmailVerificationFragment newInstance(String param1, String param2) {
        EmailVerificationFragment fragment = new EmailVerificationFragment();

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
        View view = inflater.inflate(R.layout.fragment_email_verification, container, false);
        pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#179e99"));
        pDialog.setTitleText("Sending email");
        pDialog.setCancelable(false);
        etEmailForgetPassword = (EditText) view.findViewById(R.id.et_email_forget);
        btnSendEmail = (Button) view.findViewById(R.id.btn_send_email);
        btnSendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonclick();
            }
        });
        tvSkipLogin = (TextView)view.findViewById(R.id.tv_skip_login);
        tvSkipLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment = new LoginFragment();
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();

            }
        });
        ivBackToLogin = (ImageView)view.findViewById(R.id.iv_back_login);
        ivBackToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment = new LoginFragment();
                getFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).commit();
            }
        });
        onButtonclick();
        return view;
    }


    public void onButtonclick() {
        btnSendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strEmail = etEmailForgetPassword.getText().toString();
                if ((!android.util.Patterns.EMAIL_ADDRESS.matcher(strEmail).matches())) {
                    etEmailForgetPassword.setError("Please enter valid email id");
                } else {
                    pDialog.dismiss();
                    apiCall();
                }
            }
        });
    }

    public void apiCall() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Configuration.USER_URL + "forgot-password", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("zma forget res", response);
                if (response.contains("200")) {
                    new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Email sent")
                            .setContentText("Please check your email")
                            .show();
                    Bundle args = new Bundle();
                    fragment = new VerifyCodeFragment();
                    args.putString("email", strEmail);
                    getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
                    fragment.setArguments(args);
                }else{
                    new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Something went wrong, try again")
                            .show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Something went wrong, try again")
                        .show();
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
