package com.lovemoin.card.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import com.lovemoin.card.app.R;
import com.lovemoin.card.app.db.StoreInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zzt on 15-9-14.
 */
public class StoreListAdapter extends RecyclerView.Adapter<StoreListAdapter.ViewHolder> {
    private Context context;
    private List<StoreInfo> storeList;

    public StoreListAdapter(Context context) {
        this.context = context;
        this.storeList = new ArrayList<>();
    }

    public void addAll(List<StoreInfo> storeList) {
        this.storeList.addAll(storeList);
        notifyDataSetChanged();
    }

    public void clear() {
        storeList.clear();
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_store_info, parent, false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final StoreInfo storeInfo = storeList.get(position);
        holder.textStoreName.setText(storeInfo.getName());
        holder.textStoreAddr.setText(storeInfo.getAddr());
        holder.btnTel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setAction(Intent.ACTION_DIAL);
                i.setData(Uri.parse("tel:" + storeInfo.getTel()));
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return storeList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView textStoreName;
        TextView textStoreAddr;
        ImageButton btnTel;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
