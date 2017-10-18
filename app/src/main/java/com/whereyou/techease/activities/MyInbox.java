package com.whereyou.techease.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.whereyou.techease.R;

public class MyInbox extends AppCompatActivity {
   TextView textView,t2,t3,t4,t5,t6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myinobx);

        textView=(TextView)findViewById(R.id.tvSms1);
        t2=(TextView)findViewById(R.id.tvSms1User);
        t3=(TextView)findViewById(R.id.tvSms2User);
        t4=(TextView)findViewById(R.id.tvSms2);
        t5=(TextView)findViewById(R.id.tvSms3User);
        t6=(TextView)findViewById(R.id.tvSms3);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MyInbox.this,Chatting.class);
                startActivity(intent);
                finish();
            }
        });
        t2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MyInbox.this,Chatting.class);
                startActivity(intent);
                finish();
            }
        });
        t3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MyInbox.this,Chatting.class);
                startActivity(intent);
                finish();
            }
        });
        t4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MyInbox.this,Chatting.class);
                startActivity(intent);
                finish();
            }
        });
        t5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MyInbox.this,Chatting.class);
                startActivity(intent);
                finish();
            }
        });
        t6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MyInbox.this,Chatting.class);
                startActivity(intent);
                finish();
            }
        });
    }
    @Override
    public void onBackPressed() {
       Intent intent=new Intent(MyInbox.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}
