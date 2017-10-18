package com.whereyou.techease.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.whereyou.techease.R;

public class Notifications extends AppCompatActivity {
    TextView tv1,tv2,tv3,tv4,tv5,tv6,tv7,tv8,tv9,tv10,tv11,tv12,tv13,tv14,tv15;
    Typeface t1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        t1=Typeface.createFromAsset(this.getAssets(), "fonts/Av.otf");

        tv1=(TextView)findViewById(R.id.tvNotif1);
        tv2=(TextView)findViewById(R.id.tvNotif2);
        tv3=(TextView)findViewById(R.id.tvNotif3);
        tv4=(TextView)findViewById(R.id.tvn1it1);
        tv5=(TextView)findViewById(R.id.tvn1it2);
        tv6=(TextView)findViewById(R.id.tvn1it3);
        tv7=(TextView)findViewById(R.id.tvn2it1);
        tv8=(TextView)findViewById(R.id.tvn2it2);
        tv9=(TextView)findViewById(R.id.tvn2it3);
        tv10=(TextView)findViewById(R.id.tvn2it2tv2);
        tv11=(TextView)findViewById(R.id.tvn2it3tv2);
        tv12=(TextView)findViewById(R.id.tvn3it1);
        tv13=(TextView)findViewById(R.id.tvn3it2);
        tv14=(TextView)findViewById(R.id.tvn3it3);
        tv15=(TextView)findViewById(R.id.tvn3it3tv2);

        tv1.setTypeface(t1);
        tv2.setTypeface(t1);
        tv3.setTypeface(t1);
        tv4.setTypeface(t1);
        tv5.setTypeface(t1);
        tv6.setTypeface(t1);
        tv7.setTypeface(t1);
        tv8.setTypeface(t1);
        tv9.setTypeface(t1);
        tv10.setTypeface(t1);
        tv11.setTypeface(t1);
        tv12.setTypeface(t1);
        tv13.setTypeface(t1);
        tv14.setTypeface(t1);
        tv15.setTypeface(t1);



    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(Notifications.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}
