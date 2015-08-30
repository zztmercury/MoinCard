package com.lovemoin.card.app.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.lovemoin.card.app.R;
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
public class CardListAdapter extends BaseAdapter {
    List<CardInfo> cardInfoList;
    Context context;

    public CardListAdapter(Context context) {
        this.context = context;
        cardInfoList = new ArrayList<>();
    }

    public void add(CardInfo data) {
        cardInfoList.add(data);
        notifyDataSetChanged();
    }

    public void addAll(Collection<CardInfo> dataSet) {
        cardInfoList.addAll(dataSet);
        notifyDataSetChanged();
    }

    public void clear() {
        cardInfoList.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return cardInfoList.size();
    }

    @Override
    public CardInfo getItem(int position) {
        return cardInfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_card, null);
            holder = new ViewHolder();
            holder.imgCard = (ImageView) convertView.findViewById(R.id.imgCard);
            holder.imgCardExchangeStat = (ImageView) convertView.findViewById(R.id.imgCardExchangeStat);
            holder.imgCardOverdueStat = (ImageView) convertView.findViewById(R.id.imgCardOverdueStat);
            holder.textPointProcess = (TextView) convertView.findViewById(R.id.textPointProcess);
            convertView.setTag(holder);
        }
        holder = (ViewHolder) convertView.getTag();

        CardInfo cardInfo = getItem(position);

        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(Config.SERVER_URL + cardInfo.getCardImg(), holder.imgCard, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                Bitmap scaledImg = Bitmap.createBitmap(loadedImage, 0, 0, loadedImage.getWidth(), 80);
                ((ImageView) view).setImageBitmap(scaledImg);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });

        if (DateUtil.dateDifference(System.currentTimeMillis(), cardInfo.getEndDate().getTime()) < 0) {
            holder.imgCardOverdueStat.setVisibility(View.VISIBLE);
            holder.imgCardOverdueStat.setImageDrawable(context.getResources().getDrawable(R.drawable.overdue));
        } else if (DateUtil.dateDifference(System.currentTimeMillis(), cardInfo.getEndDate().getTime()) < 5) {
            holder.imgCardOverdueStat.setVisibility(View.VISIBLE);
            holder.imgCardOverdueStat.setImageDrawable(context.getResources().getDrawable(R.drawable.nearly_overdue));
        }

        if (cardInfo.getCurrentPoint() >= cardInfo.getConvertPoint()) {
            holder.imgCardExchangeStat.setVisibility(View.VISIBLE);
        }

        holder.textPointProcess.setText(String.format("%d/%d", cardInfo.getCurrentPoint(), cardInfo.getConvertPoint()));

        return convertView;
    }

    private class ViewHolder {
        ImageView imgCard;
        ImageView imgCardOverdueStat;
        ImageView imgCardExchangeStat;
        TextView textPointProcess;
    }
}
