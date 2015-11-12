package com.lovemoin.card.app.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lovemoin.card.app.MoinCardApplication;
import com.lovemoin.card.app.R;
import com.lovemoin.card.app.adapter.ActivityListAdapter;
import com.lovemoin.card.app.db.ActivityInfo;
import com.lovemoin.card.app.db.ActivityInfoDao;
import com.lovemoin.card.app.net.LoadActivityList;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.dao.query.QueryBuilder;

/**
 * Created by zzt on 15-8-31.
 */
public class ActivityListFragment extends LazyFragment implements SwipeRefreshLayout.OnRefreshListener {
    private RecyclerView mListActivity;
    private SwipeRefreshLayout layoutSwipe;
    private ActivityListAdapter mAdapter;
    private boolean isPrepared = false;

    private ActivityInfoDao activityInfoDao;

    private MoinCardApplication app;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.refreshable_list, container, false);
        mListActivity = (RecyclerView) rootView.findViewById(R.id.list);
        mListActivity.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new ActivityListAdapter(getContext());
        mListActivity.setAdapter(mAdapter);
//        mListActivity.addItemDecoration(new DividerItemDecoration(getContext(),R.drawable.default_divider));

        app = (MoinCardApplication) getActivity().getApplication();
        activityInfoDao = app.getActivityInfoDao();

        layoutSwipe = (SwipeRefreshLayout) rootView.findViewById(R.id.layoutSwipe);
        layoutSwipe.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        layoutSwipe.setOnRefreshListener(this);

        loadActivityListFromDB();

        isPrepared = true;
        return rootView;
    }

    @Override
    protected void lazyLoad() {
        if (isPrepared && isVisible)
            loadActivityListFromServer();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadActivityListFromServer();
    }

    private void loadActivityListFromDB() {
        List<ActivityInfo> activityList = new ArrayList<>();
        QueryBuilder<ActivityInfo> qb = activityInfoDao.queryBuilder()
                .where(ActivityInfoDao.Properties.IsTop.eq(true))
                .orderDesc(ActivityInfoDao.Properties.Level, ActivityInfoDao.Properties.StartDate);
        activityList.addAll(qb.list());
        qb = activityInfoDao.queryBuilder()
                .where(ActivityInfoDao.Properties.IsTop.eq(false))
                .orderDesc(ActivityInfoDao.Properties.Level, ActivityInfoDao.Properties.StartDate);
        activityList.addAll(qb.list());
//        qb = activityInfoDao.queryBuilder()
//                .where(ActivityInfoDao.Properties.IsOfficial.eq(false), ActivityInfoDao.Properties.IsTop.eq(false))
//                .orderDesc(ActivityInfoDao.Properties.StartDate);
//        activityList.addAll(qb.list());
        mAdapter.clear();
        mAdapter.addAll(activityList);
    }

    private void cacheActivityListToDB(List<ActivityInfo> activityInfoList) {
        activityInfoDao.insertOrReplaceInTx(activityInfoList);
//        QueryBuilder<ActivityInfo> qb = activityInfoDao.queryBuilder();
    }

    private void loadActivityListFromServer() {
        new LoadActivityList(LoadActivityList.TYPE_RELATED, app.getCachedUserId(), 0) {
            @Override
            public void onSuccess(List<ActivityInfo> activityInfoList) {
                app.cacheLastSearchTime(System.currentTimeMillis());
                layoutSwipe.setRefreshing(false);
                cacheActivityListToDB(activityInfoList);
                loadActivityListFromDB();
                layoutSwipe.setRefreshing(false);
            }

            @Override
            public void onFail(String message) {
                layoutSwipe.setRefreshing(false);
                //Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }
        };
    }

    @Override
    public void onRefresh() {
        loadActivityListFromServer();
    }

}
