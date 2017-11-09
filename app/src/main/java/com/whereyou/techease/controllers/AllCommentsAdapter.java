package com.whereyou.techease.controllers;


import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.whereyou.techease.R;
import com.whereyou.techease.activities.Comments;

import java.util.List;

/**
 * Created by kaxhiftaj on 4/3/17.
 */

public class AllCommentsAdapter extends RecyclerView.Adapter<AllCommentsAdapter.MyViewHolder> {


    private List<Contestents> contestents;
    private Context context;
    public AllCommentsAdapter(Context context, List<Contestents> contestent) {

        this.context = context;
        this.contestents = contestent;

    }

    @Override
    public AllCommentsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_comments, parent, false);

        return new MyViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Contestents conest = contestents.get(position);

        String s = conest.getLoctionDistance();
        if (! s.equals("")) {
            Double d = Double.parseDouble(s);

            if (d < 62.00) {
                holder.locationName.setText(conest.getLocationName());
                holder.locationDistance.setText(d + " miles");
                holder.locationComment.setText(conest.getLocationComment());
            }
        }
    }


    @Override
    public int getItemCount() {
        return contestents.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView locationName, locationComment, locationDistance;


        public MyViewHolder(View itemView) {
            super(itemView);

            locationName = (TextView)itemView.findViewById(R.id.locationName);
            locationComment = (TextView)itemView.findViewById(R.id.locationComment);
            locationDistance = (TextView)itemView.findViewById(R.id.locationDistance);
        }
    }


}