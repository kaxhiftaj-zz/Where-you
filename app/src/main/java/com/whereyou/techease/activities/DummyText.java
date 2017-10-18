package com.whereyou.techease.activities;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.whereyou.techease.R;

public class DummyText extends AppCompatActivity {
    TextView textView;
    Typeface typeface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dummy_text);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Terms and conditions");
        setSupportActionBar(toolbar);


        textView=(TextView)findViewById(R.id.tvLargeText);
        typeface=Typeface.createFromAsset(this.getAssets(), "fonts/Av.otf");
        textView.setTypeface(typeface);


    }

    @Override
    public void onBackPressed() {
//        Intent intent=new Intent(DummyText.this,MainActivity.class);
//        startActivity(intent);
       finish();
    }
}
