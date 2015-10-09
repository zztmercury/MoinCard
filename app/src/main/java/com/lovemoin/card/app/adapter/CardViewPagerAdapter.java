package com.lovemoin.card.app.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.lovemoin.card.app.db.CardInfo;
import com.lovemoin.card.app.fragment.CardViewFragment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by zzt on 15-9-16.
 */
public class CardViewPagerAdapter extends FragmentPagerAdapter {
    private List<CardInfo> cardList;
    private boolean clickable;

    public CardViewPagerAdapter(FragmentManager fm, boolean clickable) {
        super(fm);
        cardList = new ArrayList<>();
        this.clickable = clickable;
    }

    public void add(CardInfo cardInfo) {
        cardList.add(cardInfo);
        notifyDataSetChanged();
    }

    public void addAll(Collection<CardInfo> dataSet) {
        cardList.addAll(dataSet);
        notifyDataSetChanged();
    }

    public void clear() {
        cardList.clear();
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        return CardViewFragment.newInstance(cardList.get(position), clickable);
    }

    @Override
    public int getCount() {
        return cardList.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
