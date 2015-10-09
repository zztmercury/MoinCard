package com.lovemoin.card.app.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.lovemoin.card.app.R;
import com.lovemoin.card.app.activity.MerchantDetailActivity;
import com.lovemoin.card.app.constant.Config;
import com.lovemoin.card.app.db.CardInfo;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * Created by zzt on 15-9-16.
 */
public class CardViewFragment extends Fragment {
    /**
     * 卡片信息
     */
    private CardInfo cardInfo;

    /**
     * 卡图
     */
    private ImageView imgCard;
    /**
     * 积点计数图
     */
    private ImageView imgCardCount;
    /**
     * 当前积点
     */
    private TextView textCurrentPoint;
    /**
     * 还需积点
     */
    private TextView textNeededPoint;
    /**
     * 兑换物品
     */
    private TextView textObject;
    /**
     * 积点计数
     */
    private TextView textCardCounter;
    private View rootView;

    private ImageLoader loader = ImageLoader.getInstance();

    private boolean clickable;

    public CardViewFragment() {
    }

    public static CardViewFragment newInstance(CardInfo cardInfo, boolean clickable) {
        CardViewFragment fragment = new CardViewFragment();
        fragment.cardInfo = cardInfo;
        fragment.clickable = clickable;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.layout_card_info, container, false);
        imgCard = (ImageView) rootView.findViewById(R.id.imgCard);
        imgCardCount = (ImageView) rootView.findViewById(R.id.imgCardCounter);
        textCurrentPoint = (TextView) rootView.findViewById(R.id.textCurrentPoint);
        textNeededPoint = (TextView) rootView.findViewById(R.id.textNeededPoint);
        textObject = (TextView) rootView.findViewById(R.id.textObject);
        textCardCounter = (TextView) rootView.findViewById(R.id.textCardCounter);
        rootView.setClickable(clickable);
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(new Intent(getContext(), MerchantDetailActivity.class));
                getActivity().finish();
            }
        });
        bindData();
        return rootView;
    }

    private void bindData() {
        loader.displayImage(Config.SERVER_URL + cardInfo.getCardImg(), imgCard, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                ((ImageView) view).setImageResource(R.drawable.nocard);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });
        int currentPoint = cardInfo.getCurrentPoint();
        int convertPoint = cardInfo.getConvertPoint();
        if (currentPoint >= convertPoint) {
            imgCardCount.setImageResource(R.drawable.q10);
            textNeededPoint.setText("兑换即获得");
        } else {
            int index = Math.round(currentPoint * 10f / convertPoint);
            imgCardCount.setImageResource(getResources().getIdentifier("q" + index, "drawable", getActivity().getPackageName()));
            textNeededPoint.setText(String.format("再积%d个积点可兑换", convertPoint - currentPoint));
        }
        textCurrentPoint.setText(String.format("%d枚", cardInfo.getCurrentPoint()));
        textObject.setText(cardInfo.getConvertObj());
        textObject.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        textCardCounter.setText(String.format("%d/%d", currentPoint, convertPoint));

    }
}
