package com.lovemoin.card.app.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.lovemoin.card.app.R;
import com.lovemoin.card.app.widget.PlusStarDrawView;

/**
 * Created by zzt on 15-9-15.
 */
public class MyFragment extends LazyFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_card_selector, container, false);
        PlusStarDrawView drawView = (PlusStarDrawView) rootView.findViewById(R.id.starView);
        drawView.setCount(12);
        return rootView;
    }

    @Override
    protected void lazyLoad() {

    }
}
