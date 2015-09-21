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
import com.lovemoin.card.app.adapter.ActivityListAdapter;
import com.lovemoin.card.app.db.ActivityInfo;
import com.lovemoin.card.app.db.ActivityInfoDao;
import com.lovemoin.card.app.net.LoadActivityList;
import de.greenrobot.dao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

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

        app = (MoinCardApplication) getActivity().getApplication();
        activityInfoDao = app.getDaoSession().getActivityInfoDao();

        layoutSwipe = (SwipeRefreshLayout) rootView.findViewById(R.id.layoutSwipe);
        layoutSwipe.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        layoutSwipe.setOnRefreshListener(this);

        isPrepared = true;
        return rootView;
    }

    @Override
    protected void lazyLoad() {
        if (isPrepared && isVisible)
            loadActivityListFromServer();
    }

    private void loadActivityListFromDB() {
        List<ActivityInfo> activityList = new ArrayList<>();
        QueryBuilder<ActivityInfo> qb = activityInfoDao.queryBuilder()
                .where(ActivityInfoDao.Properties.IsTop.eq(true))
                .orderDesc(ActivityInfoDao.Properties.StartDate);
        activityList.addAll(qb.list());
        qb = activityInfoDao.queryBuilder()
                .where(ActivityInfoDao.Properties.IsOfficial.eq(true), ActivityInfoDao.Properties.IsTop.eq(false))
                .orderDesc(ActivityInfoDao.Properties.StartDate);
        activityList.addAll(qb.list());
        qb = activityInfoDao.queryBuilder()
                .where(ActivityInfoDao.Properties.IsOfficial.eq(false), ActivityInfoDao.Properties.IsTop.eq(false))
                .orderDesc(ActivityInfoDao.Properties.StartDate);
        activityList.addAll(qb.list());
        mAdapter.clear();
        mAdapter.addAll(activityList);
    }

    private void cacheActivityListToDB() {
        // TODO 活动缓存策略

    }

    private void loadActivityListFromServer() {
        new LoadActivityList(LoadActivityList.TYPE_RELATED, app.getCachedUserId(), app.getCachedLastSearchTime()) {
            @Override
            public void onSuccess(List<ActivityInfo> activityInfoList) {
                app.cacheLastSearchTime(System.currentTimeMillis());
                mAdapter.addAll(activityInfoList);
                layoutSwipe.setRefreshing(false);
            }

            @Override
            public void onFail(String message) {
                layoutSwipe.setRefreshing(false);
                Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
            }
        };
    }

    @Override
    public void onRefresh() {
        loadActivityListFromServer();
    }
}
