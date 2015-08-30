package com.lovemoin.card.app.fragment;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;
import com.lovemoin.card.app.MoinCardApplication;
import com.lovemoin.card.app.R;
import com.lovemoin.card.app.adapter.CardListAdapter;
import com.lovemoin.card.app.db.CardInfo;
import com.lovemoin.card.app.db.CardInfoDao;
import com.lovemoin.card.app.db.DaoMaster;
import com.lovemoin.card.app.db.DaoSession;
import com.lovemoin.card.app.net.DownloadCardInfo;
import de.greenrobot.dao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zzt on 15-8-21.
 */
public class CardListFragment extends LazyFragment implements SwipeRefreshLayout.OnRefreshListener {
    private ListView mListCard;
    private CardListAdapter mAdapter;
    private SwipeRefreshLayout layoutSwipe;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private CardInfoDao cardInfoDao;
    private boolean isPrepared;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_card_list, container, false);
        mListCard = (ListView) rootView.findViewById(R.id.listCard);
        mAdapter = new CardListAdapter(getActivity());

        initDao();

        mListCard.setAdapter(mAdapter);

        layoutSwipe = (SwipeRefreshLayout) rootView.findViewById(R.id.layoutSwipe);
        layoutSwipe.setColorSchemeColors(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        layoutSwipe.setOnRefreshListener(this);

        System.out.println(((MoinCardApplication) getActivity().getApplication()).getCachedUserTel());

        isPrepared = true;
        lazyLoad();

        onRefresh();

        return rootView;
    }

    private void initDao() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(getActivity(), "moinCard.db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
        cardInfoDao = daoSession.getCardInfoDao();
    }

    private List<CardInfo> loadCardListFromDB() {
        QueryBuilder<CardInfo> queryBuilder = cardInfoDao.queryBuilder();
        return queryBuilder.list();
    }

    private List<CardInfo> loadCardListFromServer() {
        final List<CardInfo> data = new ArrayList<>();
        new DownloadCardInfo(((MoinCardApplication) getActivity().getApplication()).getCachedUserId()) {
            @Override
            protected void onSuccess(List<CardInfo> cardInfoList) {
                data.clear();
                data.addAll(cardInfoList);
                Toast.makeText(getActivity(), R.string.update_point_card_success, Toast.LENGTH_LONG).show();
                layoutSwipe.setRefreshing(false);
            }

            @Override
            protected void onFail(String message) {
                Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
            }
        };

        return data;
    }

    private void cacheCardListToDB(List<CardInfo> data) {
        cardInfoDao.insertInTx(data);
    }

    public void onRefresh() {
        mAdapter.clear();
        List<CardInfo> data = loadCardListFromServer();
        cacheCardListToDB(data);
        mAdapter.addAll(data);
        cacheCardListToDB(data);
    }

    @Override
    protected void lazyLoad() {
        if (isPrepared && isVisible) {
            mAdapter.clear();
            mAdapter.addAll(loadCardListFromDB());
        } else return;
    }
}
