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

    public CardViewPagerAdapter(FragmentManager fm) {
        super(fm);
        cardList = new ArrayList<>();
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
        return CardViewFragment.newInstans(cardList.get(position));
    }

    @Override
    public int getCount() {
        return cardList.size();
    }
}
