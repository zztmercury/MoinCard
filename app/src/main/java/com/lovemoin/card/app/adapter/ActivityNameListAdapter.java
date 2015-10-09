package com.lovemoin.card.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.lovemoin.card.app.R;
import com.lovemoin.card.app.activity.ActivityDetailType1Activity;
import com.lovemoin.card.app.activity.ActivityDetailType3Activity;
import com.lovemoin.card.app.constant.Config;
import com.lovemoin.card.app.db.ActivityInfo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by zzt on 15-9-21.
 */
public class ActivityNameListAdapter extends RecyclerView.Adapter<ActivityNameListAdapter.ViewHolder> {

    private Context context;
    private List<ActivityInfo> activityList;

    public ActivityNameListAdapter(Context context) {
        this.context = context;
        activityList = new ArrayList<>();
    }

    public void add(ActivityInfo data) {
        activityList.add(data);
        notifyDataSetChanged();
    }

    public void addAll(Collection<ActivityInfo> dataSet) {
        activityList.addAll(dataSet);
        notifyDataSetChanged();
    }

    public void clear() {
        activityList.clear();
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_activity_name, parent, false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ActivityInfo activityInfo = activityList.get(position);
        holder.textActivityName.setText(activityInfo.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.putExtra(Config.KEY_ACTIVITY, activityInfo);
                switch (activityInfo.getType()) {
                    case 1:
                        i.setClass(context, ActivityDetailType1Activity.class);
                        break;
                    case 2:
                        i.setClass(context, ActivityDetailType3Activity.class);
                        break;
                    case 3:
                        i.setClass(context, ActivityDetailType3Activity.class);
                        break;
                }
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return activityList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textActivityName;

        public ViewHolder(View itemView) {
            super(itemView);
            textActivityName = (TextView) itemView.findViewById(R.id.textActivityName);
        }
    }
}
