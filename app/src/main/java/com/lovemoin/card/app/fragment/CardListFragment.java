package com.lovemoin.card.app.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.lovemoin.card.app.MoinCardApplication;
import com.lovemoin.card.app.R;
import com.lovemoin.card.app.adapter.CardListAdapter;
import com.lovemoin.card.app.db.CardInfo;
import com.lovemoin.card.app.db.CardInfoDao;
import com.lovemoin.card.app.net.LoadCardList;
import de.greenrobot.dao.query.QueryBuilder;

import java.util.List;

/**
 * Created by zzt on 15-8-21.
 */
public class CardListFragment extends LazyFragment implements SwipeRefreshLayout.OnRefreshListener {
    private CardListAdapter mAdapter;
    private SwipeRefreshLayout layoutSwipe;
    private CardInfoDao cardInfoDao;
    private boolean isPrepared;
    private MoinCardApplication app;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.refreshable_list, container, false);
        RecyclerView mListCard = (RecyclerView) rootView.findViewById(R.id.list);
        mListCard.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new CardListAdapter(getContext());

        app = (MoinCardApplication) getActivity().getApplication();
        cardInfoDao = app.getCardInfoDao();

        mListCard.setAdapter(mAdapter);

        layoutSwipe = (SwipeRefreshLayout) rootView.findViewById(R.id.layoutSwipe);
        layoutSwipe.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        layoutSwipe.setOnRefreshListener(this);

        System.out.println(app.getCachedUserTel());

        onRefresh();

        isPrepared = true;


        return rootView;
    }

    private void loadCardListFromDB() {
        QueryBuilder<CardInfo> queryBuilder = cardInfoDao.queryBuilder();
        queryBuilder.orderDesc(CardInfoDao.Properties.CreateDate);
        List<CardInfo> cardList = queryBuilder.list();
        mAdapter.clear();
        mAdapter.addAll(cardList);
    }

    private void loadCardListFromServer() {
        new LoadCardList(app.getCachedUserId()) {
            @Override
            protected void onSuccess(List<CardInfo> cardInfoList) {
                cacheCardListToDB(cardInfoList);
                loadCardListFromDB();
                Toast.makeText(getContext(), R.string.update_point_card_success, Toast.LENGTH_LONG).show();
                layoutSwipe.setRefreshing(false);
            }

            @Override
            protected void onFail(String message) {
                Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                layoutSwipe.setRefreshing(false);
            }
        };
    }

    private void cacheCardListToDB(List<CardInfo> data) {
        cardInfoDao.deleteAll();
        cardInfoDao.insertInTx(data);
    }

    public void onRefresh() {
        loadCardListFromServer();
    }

    @Override
    protected void lazyLoad() {
        if (isPrepared && isVisible)
            loadCardListFromDB();
    }
}
