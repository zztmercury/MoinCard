package com.lovemoin.card.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.lovemoin.card.app.MoinCardApplication;
import com.lovemoin.card.app.R;
import com.lovemoin.card.app.activity.MerchantDetailActivity;
import com.lovemoin.card.app.constant.Config;
import com.lovemoin.card.app.db.CardInfo;
import com.lovemoin.card.app.utils.DateUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by zzt on 15-8-24.
 */
public class CardListAdapter extends RecyclerView.Adapter<CardListAdapter.ViewHolder> {
    List<CardInfo> mCardInfoList;
    Context context;

    public CardListAdapter(Context context) {
        this.context = context;
        mCardInfoList = new ArrayList<>();
    }

    public void add(CardInfo data) {
        mCardInfoList.add(data);
        notifyDataSetChanged();
    }

    public void addAll(Collection<CardInfo> dataSet) {
        mCardInfoList.addAll(dataSet);
        notifyDataSetChanged();
    }

    public void clear() {
        mCardInfoList.clear();
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_card, parent, false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ImageLoader imageLoader = ImageLoader.getInstance();
        final CardInfo cardInfo = mCardInfoList.get(position);
        holder.textPointProcess.setText(String.format("%d/%d", cardInfo.getCurrentPoint(), cardInfo.getConvertPoint()));

        imageLoader.displayImage(Config.SERVER_URL + cardInfo.getCardImg(), holder.imgCard,
                new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                ((ImageView) view).setImageResource(R.drawable.nocard);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                int height = loadedImage.getHeight();
                Bitmap scaledImg = Bitmap.createBitmap(loadedImage, 0, 0, loadedImage.getWidth(), 80);
                ((ImageView) view).setImageBitmap(scaledImg);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });

        if (DateUtil.dateDifference(System.currentTimeMillis(), cardInfo.getEndDate().getTime()) < 0) {
            holder.imgCardOverdueStat.setVisibility(View.VISIBLE);
            holder.imgCardOverdueStat.setImageResource(R.drawable.overdue);
        } else if (DateUtil.dateDifference(System.currentTimeMillis(), cardInfo.getEndDate().getTime()) < 5) {
            holder.imgCardOverdueStat.setVisibility(View.VISIBLE);
            holder.imgCardOverdueStat.setImageResource(R.drawable.nearly_overdue);
        } else {
            holder.imgCardOverdueStat.setVisibility(View.GONE);
        }

        if (cardInfo.getCurrentPoint() < cardInfo.getConvertPoint()) {
            holder.imgCardExchangeStat.setVisibility(View.GONE);
        } else {
            holder.imgCardExchangeStat.setVisibility(View.VISIBLE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MerchantDetailActivity.class);
                ((MoinCardApplication) context.getApplicationContext()).setCurrentCard(cardInfo);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCardInfoList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imgCard;
        public ImageView imgCardOverdueStat;
        public ImageView imgCardExchangeStat;
        public TextView textPointProcess;

        public ViewHolder(View v) {
            super(v);

            imgCard = (ImageView) v.findViewById(R.id.imgCard);
            imgCardOverdueStat = (ImageView) v.findViewById(R.id.imgCardOverdueStat);
            imgCardExchangeStat = (ImageView) v.findViewById(R.id.imgCardExchangeStat);
            textPointProcess = (TextView) v.findViewById(R.id.textPointProcess);
        }
    }
}
