package com.lovemoin.card.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lovemoin.card.app.R;
import com.lovemoin.card.app.activity.ActivityDetailType1Activity;
import com.lovemoin.card.app.activity.ActivityDetailType3Activity;
import com.lovemoin.card.app.activity.ActivityDetailType4Activity;
import com.lovemoin.card.app.constant.Config;
import com.lovemoin.card.app.db.ActivityInfo;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zzt on 15-9-2.
 */
public class ActivityListAdapter extends RecyclerView.Adapter<ActivityListAdapter.ViewHolder> {
    private List<ActivityInfo> mActivityInfoList;
    private Context context;

    public ActivityListAdapter(Context context) {
        this.context = context;
        mActivityInfoList = new ArrayList<>();
    }

    public void add(ActivityInfo data) {
        mActivityInfoList.add(data);
        notifyDataSetChanged();
    }

    public void addAll(List<ActivityInfo> dataSet) {
        mActivityInfoList.addAll(dataSet);
        notifyDataSetChanged();
    }

    public void clear() {
        mActivityInfoList.clear();
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_activity, parent, false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ImageLoader imageLoader = ImageLoader.getInstance();
        final ActivityInfo activityInfo = mActivityInfoList.get(position);
        holder.textName.setText(activityInfo.getName());
        holder.textBrief.setText(activityInfo.getBrief());
        imageLoader.displayImage(Config.SERVER_URL + "/moinbox/" + activityInfo.getBriefImg(), holder.imgMin, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//                ((ImageView)view).setImageBitmap(loadedImage);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });
        if (activityInfo.getIsOfficial()) {
            holder.imgFlag.setImageResource(R.drawable.official_activity);
        } else if (activityInfo.getIsTop()) {
            holder.imgFlag.setImageResource(R.drawable.top_activity);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                switch (activityInfo.getType()) {
                    case 1:
                        intent = new Intent(context, ActivityDetailType1Activity.class);
                        break;
                    case 2:
                        return;
                    case 3:
                        intent = new Intent(context, ActivityDetailType3Activity.class);
                        break;
                    case 4:
                        intent = new Intent(context, ActivityDetailType4Activity.class);
                        break;
                    default:
                        return;

                }
                intent.putExtra(Config.KEY_ACTIVITY, activityInfo);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mActivityInfoList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imgMin;
        public ImageView imgFlag;
        public TextView textName;
        public TextView textBrief;
        private CardView r;

        public ViewHolder(View itemView) {
            super(itemView);
            imgMin = (ImageView) itemView.findViewById(R.id.imgMin);
            imgFlag = (ImageView) itemView.findViewById(R.id.imgFlag);
            textName = (TextView) itemView.findViewById(R.id.textName);
            textBrief = (TextView) itemView.findViewById(R.id.textBrief);
        }
    }
}
