package com.lovemoin.card.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lovemoin.card.app.R;
import com.lovemoin.card.app.entity.CardRecord;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by zzt on 15-9-14.
 */
public class CardRecordAdapter extends RecyclerView.Adapter<CardRecordAdapter.ViewHolder> {
    private List<CardRecord> recordList;
    private Context context;

    public CardRecordAdapter(Context context) {
        this.context = context;
        recordList = new ArrayList<>();
    }

    public void add(CardRecord data) {
        recordList.add(data);
        notifyDataSetChanged();
    }

    public void addAll(Collection<CardRecord> dataSet) {
        recordList.addAll(dataSet);
        notifyDataSetChanged();
    }

    public void clear() {
        recordList.clear();
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card_record, parent, false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CardRecord record = recordList.get(position);
        holder.textRecordDate.setText(record.getDateStr());
        holder.textRecordDesc.setText(record.getOperateStr());
    }

    @Override
    public int getItemCount() {
        return recordList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textRecordDate;
        public TextView textRecordDesc;

        public ViewHolder(View itemView) {
            super(itemView);
            textRecordDate = (TextView) itemView.findViewById(R.id.textRecordDate);
            textRecordDesc = (TextView) itemView.findViewById(R.id.textRecordDesc);
        }
    }
}
