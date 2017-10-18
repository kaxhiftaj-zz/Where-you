package com.whereyou.techease.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
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

public class ChangePasswordFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    EditText etNewPassword, etConfirmPassword;
    String strNewPassword, strConfirmPassowrd, strAPIToken;
    Button btnChangePassword;
    ImageView ivBackToLogin;
    Fragment fragment;
    private OnFragmentInteractionListener mListener;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    SweetAlertDialog pDialog;
    TextView tvSkipLogin;

    public ChangePasswordFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static ChangePasswordFragment newInstance(String param1, String param2) {
        ChangePasswordFragment fragment = new ChangePasswordFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_change_password, container, false);
        sharedPreferences = getActivity().getSharedPreferences(Configuration.MY_PREF, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        tvSkipLogin = (TextView)view.findViewById(R.id.tv_skip_login);
        tvSkipLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment = new LoginFragment();
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();

            }
        });
        ivBackToLogin = (ImageView)view.findViewById(R.id.iv_back_forget);
        ivBackToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment = new LoginFragment();
                getFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).commit();
            }
        });
        sharedPreferences = getActivity().getSharedPreferences(Configuration.MY_PREF, Context.MODE_PRIVATE);
        strAPIToken = sharedPreferences.getString("api_token","");
        pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#179e99"));
        pDialog.setTitleText("Updating password");
        pDialog.setCancelable(false);
        btnChangePassword = (Button) view.findViewById(R.id.btn_update_password);
        etNewPassword = (EditText) view.findViewById(R.id.et_new_password_change);
        etConfirmPassword = (EditText) view.findViewById(R.id.et_confirm_password_change);
        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDataInput();
            }
        });
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    public void onDataInput() {

        strNewPassword = etNewPassword.getText().toString();
        strConfirmPassowrd = (etConfirmPassword).getText().toString();
        if (strNewPassword.equals("")) {
            etNewPassword.setError("Please enter a valid new password");
        } else if (strConfirmPassowrd.equals("")) {
            etConfirmPassword.setError("Please enter a valid confirm password");
        } else if (!strNewPassword.equals(strConfirmPassowrd)) {
            etConfirmPassword.setError("Password doesn't match");
        } else {
            pDialog.show();
            apiCall();
        }
    }


    public void apiCall() {
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, Configuration.USER_URL +
                "change-password", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.contains("200")) {
                    pDialog.dismiss();
                    new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Password Changed Successfully")
                            .show();
                    fragment = new LoginFragment();
                    getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
                } else {
                    pDialog.dismiss();
                    new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Something Went Wrong, Try again")
                            .show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Something Went Wrong, Try again")
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
                params.put("api_token", strAPIToken);
                params.put("password", strNewPassword);
                params.put("password_confirmation", strConfirmPassowrd);
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
