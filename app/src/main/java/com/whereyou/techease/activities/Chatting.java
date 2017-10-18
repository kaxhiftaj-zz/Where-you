package com.whereyou.techease.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.whereyou.techease.R;

public class Chatting extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(Chatting.this,MyInbox.class);
        startActivity(intent);
        finish();
    }
}
