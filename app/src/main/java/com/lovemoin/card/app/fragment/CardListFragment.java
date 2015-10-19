package com.lovemoin.card.app.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.lovemoin.card.app.MoinCardApplication;
import com.lovemoin.card.app.R;
import com.lovemoin.card.app.activity.GiftPackActivity;
import com.lovemoin.card.app.adapter.CardListAdapter;
import com.lovemoin.card.app.constant.Config;
import com.lovemoin.card.app.db.CardInfo;
import com.lovemoin.card.app.db.CardInfoDao;
import com.lovemoin.card.app.db.GiftPackInfo;
import com.lovemoin.card.app.db.GiftPackInfoDao;
import com.lovemoin.card.app.net.LoadCardList;
import com.lovemoin.card.app.net.LoadGiftPackList;

import java.util.List;

import de.greenrobot.dao.query.QueryBuilder;

/**
 * Created by zzt on 15-8-21.
 */
public class CardListFragment extends LazyFragment implements SwipeRefreshLayout.OnRefreshListener {
    private CardListAdapter mAdapter;
    private SwipeRefreshLayout layoutSwipe;
    private CardInfoDao cardInfoDao;
    private boolean isPrepared;
    private MoinCardApplication app;

    private ImageView imgGiftPack;
    private GiftPackInfoDao giftPackInfoDao;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.refreshable_list, container, false);
        RecyclerView mListCard = (RecyclerView) rootView.findViewById(R.id.list);
        imgGiftPack = (ImageView) rootView.findViewById(R.id.img_gift_pack);
        mListCard.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new CardListAdapter(getContext());

        app = (MoinCardApplication) getActivity().getApplication();
        cardInfoDao = app.getCardInfoDao();
        giftPackInfoDao = app.getGiftPackInfoDao();

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

    private void loadGiftPackListFromServer() {
        new LoadGiftPackList(app.getCachedUserId()) {
            @Override
            public void onSuccess(List<GiftPackInfo> giftPackList) {
                cacheGiftPackListToDB(giftPackList);
                loadGiftPackListFromDB();
            }

            @Override
            public void onFail(String message) {

            }
        };
    }

    private void loadGiftPackListFromDB() {
        QueryBuilder<GiftPackInfo> qb = giftPackInfoDao.queryBuilder();
        qb.where(GiftPackInfoDao.Properties.Ignore.eq(false));
        qb.orderDesc(GiftPackInfoDao.Properties.Priority);
        qb.limit(1);
        GiftPackInfo giftPackInfo = qb.unique();
        if (giftPackInfo != null) {
            imgGiftPack.setVisibility(View.VISIBLE);
            final Intent intent = new Intent(getContext(), GiftPackActivity.class);
            intent.putExtra(Config.KEY_GIFT_PACK, giftPackInfo);
            imgGiftPack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(intent);
                }
            });
            if (giftPackInfo.getPriority() >= 100) {
                startActivity(intent);
            }
        } else
            imgGiftPack.setVisibility(View.GONE);
    }

    private void cacheGiftPackListToDB(List<GiftPackInfo> data) {
        giftPackInfoDao.deleteAll();
        giftPackInfoDao.insertInTx(data);
    }

    public void onRefresh() {
        loadCardListFromServer();
        loadGiftPackListFromServer();
    }

    @Override
    protected void lazyLoad() {
        if (isPrepared && isVisible) {
            loadCardListFromDB();
            loadGiftPackListFromDB();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadCardListFromServer();
    }
}
