package com.lovemoin.card.app.fragment;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.lovemoin.card.app.MoinCardApplication;
import com.lovemoin.card.app.R;
import com.lovemoin.card.app.activity.ActivityDetailType1Activity;
import com.lovemoin.card.app.activity.ActivityDetailType3Activity;
import com.lovemoin.card.app.activity.ActivityDetailType4Activity;
import com.lovemoin.card.app.activity.BaseActivity;
import com.lovemoin.card.app.activity.GiftPackActivity;
import com.lovemoin.card.app.adapter.CardListAdapter;
import com.lovemoin.card.app.constant.Config;
import com.lovemoin.card.app.db.ActivityInfo;
import com.lovemoin.card.app.db.ActivityInfoDao;
import com.lovemoin.card.app.db.CardInfo;
import com.lovemoin.card.app.db.CardInfoDao;
import com.lovemoin.card.app.db.GiftPackInfo;
import com.lovemoin.card.app.db.GiftPackInfoDao;
import com.lovemoin.card.app.db.IgnoredAdInfo;
import com.lovemoin.card.app.db.IgnoredAdInfoDao;
import com.lovemoin.card.app.entity.AdInfo;
import com.lovemoin.card.app.net.LoadActivityList;
import com.lovemoin.card.app.net.LoadAdList;
import com.lovemoin.card.app.net.LoadCardList;
import com.lovemoin.card.app.net.LoadGiftPackList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import de.greenrobot.dao.query.QueryBuilder;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;

/**
 * Created by zzt on 15-8-21.
 */
public class CardListFragment extends LazyFragment implements SwipeRefreshLayout.OnRefreshListener {
    private static final String SHOWCASE_NEW_TO_BLUETOOTH_MODE = "newToBlueToothMode";
    private static MoinCardApplication app;
    private CardListAdapter mAdapter;
    private SwipeRefreshLayout layoutSwipe;
    private CardInfoDao cardInfoDao;
    private ImageView imgGiftPack;
    private FloatingActionButton btnBleScan;
    private View layoutAd;
    private ViewPager pagerAd;
    private AdPagerAdapter mAdPagerAdapter;
    private ImageView btnClose;
    private boolean isPrepared;
    private ProgressDialog pd;

    private GiftPackInfoDao giftPackInfoDao;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    pagerAd.setCurrentItem(pagerAd.getCurrentItem() + 1);
                    break;
            }
        }
    };

    private Timer timer;
    private TimerTask timerTask;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.refreshable_list, container, false);
        RecyclerView mListCard = (RecyclerView) rootView.findViewById(R.id.list);
        imgGiftPack = (ImageView) rootView.findViewById(R.id.img_gift_pack);
        btnBleScan = (FloatingActionButton) rootView.findViewById(R.id.btn_ble_scan);
        layoutAd = rootView.findViewById(R.id.layout_ad);
        pagerAd = (ViewPager) rootView.findViewById(R.id.pager_ad);
        btnClose = (ImageView) rootView.findViewById(R.id.btn_close);
        mAdPagerAdapter = new AdPagerAdapter(getChildFragmentManager());

        btnClose.setColorFilter(getResources().getColor(android.R.color.tertiary_text_light));

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

        //System.out.println(app.getCachedUserTel());

        pd = ((BaseActivity) getActivity()).getProgressDialog();
        pd.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                ((BaseActivity) getActivity()).disconnectBle();
            }
        });
        pd.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                ((BaseActivity) getActivity()).disconnectBle();
            }
        });
        pd.setCancelable(true);
        pd.setCanceledOnTouchOutside(false);

        btnBleScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                if (bluetoothAdapter != null && !bluetoothAdapter.isEnabled()) {
                    startActivity(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE));
                }
                //pd.setMessage(getString(R.string.scanning_device));
                Message msg = new Message();
                Bundle data = new Bundle();
                data.putString(BaseActivity.KEY_MESSAGE, getString(R.string.find_device_hint));
                msg.setData(data);
                ((BaseActivity) getActivity()).handler.sendMessage(msg);
                ((BaseActivity) getActivity()).startLeScan();
                pd.show();
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup.LayoutParams lp = layoutAd.getLayoutParams();
                lp.height = 0;
                layoutAd.setLayoutParams(lp);
                app.setShowAd(false);
            }
        });

        pagerAd.setAdapter(mAdPagerAdapter);
        pagerAd.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    int position = pagerAd.getCurrentItem();
                    if (mAdPagerAdapter.getCount() > 1) {
                        if (position == 0) {
                            pagerAd.setCurrentItem(mAdPagerAdapter.getCount() - 2, false);
                        } else if (position == mAdPagerAdapter.getCount() - 1) {
                            pagerAd.setCurrentItem(1, false);
                        }
                    }
                }
            }
        });

        timer = new Timer();

        //btnBleScan.setVisibility(View.VISIBLE);

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

    @Override
    public void onPause() {
        super.onPause();
        Log.d(getClass().getName(), "onPause()");
        onInvisible();
    }

    private void loadCardListFromServer() {
        new LoadCardList(app.getCachedUserId()) {
            @Override
            protected void onSuccess(List<CardInfo> cardInfoList) {
                cacheCardListToDB(cardInfoList);
                loadCardListFromDB();
                //Toast.makeText(getContext(), R.string.update_point_card_success, Toast.LENGTH_SHORT).show();
                layoutSwipe.setRefreshing(false);
            }

            @Override
            protected void onFail(String message) {
                //Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
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
            imgGiftPack.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.cycler_scale));
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

    private void loadAdFromServer() {
        new LoadAdList(app.getCachedUserId()) {
            @Override
            protected void onSuccess(List<AdInfo> adList) {
                IgnoredAdInfoDao ignoredAdInfoDao = app.getIgnoredInfoDao();

                //layoutAd.setVisibility(View.VISIBLE);
                mAdPagerAdapter.clear();
                for (AdInfo adInfo :
                        adList) {
                    if (ignoredAdInfoDao.load(adInfo.getActivityId()) == null) {
                        mAdPagerAdapter.add(adInfo);
                    }
                }
                //mAdPagerAdapter.addAll(adList);
                mAdPagerAdapter.notifyDataSetChanged();
                if (mAdPagerAdapter.getCount() > 1) {
                    pagerAd.setCurrentItem(1);
                }

                ViewGroup.LayoutParams lp = layoutAd.getLayoutParams();
                if (mAdPagerAdapter.getCount() == 0)
                    lp.height = 0;
                else
                    lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                layoutAd.setLayoutParams(lp);

                stopTimer();
                startTimer();
            }

            @Override
            protected void onFail(String message) {
                ViewGroup.LayoutParams lp = layoutAd.getLayoutParams();
                lp.height = 0;
                layoutAd.setLayoutParams(lp);
            }
        };
    }

    private void cacheGiftPackListToDB(List<GiftPackInfo> data) {
        giftPackInfoDao.deleteAll();
        giftPackInfoDao.insertInTx(data);
    }

    public void onRefresh() {
        loadCardListFromServer();
        loadGiftPackListFromServer();
        if (app.isShowAd())
            loadAdFromServer();
        else {
            ViewGroup.LayoutParams lp = layoutAd.getLayoutParams();
            lp.height = 0;
            layoutAd.setLayoutParams(lp);
        }
    }

    @Override
    protected void lazyLoad() {
        if (isPrepared && isVisible) {
            loadCardListFromDB();
            loadGiftPackListFromDB();
        }
    }

    @Override
    protected void onInvisible() {
        super.onInvisible();
        if (isPrepared && app.isShowAd())
            stopTimer();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadGiftPackListFromServer();
        loadCardListFromServer();
        btnBleScan.setVisibility(View.VISIBLE);
        switch (app.getConnectMode()) {
            case MoinCardApplication.MODE_BLUETOOTH:
                new MaterialShowcaseView.Builder(getActivity())
                        .setDelay(1000)
                        .setTarget(btnBleScan)
                        .singleUse(SHOWCASE_NEW_TO_BLUETOOTH_MODE)
                        .setContentText(R.string.click_to_get_point)
                        .setUseAutoRadius(true)
                        .setMaskColour(Color.parseColor("#cc000000"))
                        .setDismissOnTouch(true)
                        .show();
                break;
            default:
                //btnBleScan.setVisibility(View.GONE);
        }
        if (app.isShowAd())
            startTimer();
    }

    private void startTimer() {
        timerTask = new TimerTask() {
            @Override
            public void run() {
                Message msg = new Message();
                msg.what = 1;
                mHandler.sendMessage(msg);
            }
        };
        timer.schedule(timerTask, 5000, 5000);
    }

    private void stopTimer() {
        timerTask.cancel();
        timer.purge();
    }

    public static class AdFragment extends Fragment {
        private TextView text;
        private AdInfo adInfo;

        public AdFragment() {
        }

        public static AdFragment newInstance(AdInfo adInfo) {
            AdFragment fragment = new AdFragment();
            fragment.adInfo = adInfo;
            return fragment;
        }

        private void openActivityDetail(ActivityInfo activityInfo) {
            if (activityInfo != null) {
                Intent i = new Intent();
                i.putExtra(Config.KEY_ACTIVITY, activityInfo);
                switch (activityInfo.getType()) {
                    case 1:
                        i.setClass(getActivity(), ActivityDetailType1Activity.class);
                        break;
                    case 3:
                        i.setClass(getActivity(), ActivityDetailType3Activity.class);
                        break;
                    case 4:
                        i.setClass(getActivity(), ActivityDetailType4Activity.class);
                        break;
                    default:
                        return;
                }
                startActivity(i);
            }
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_ad_text, container, false);
            text = (TextView) rootView.findViewById(R.id.text_ad);
            text.setText(adInfo.getMessage());
            text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
            text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    IgnoredAdInfoDao ignoredAdInfoDao = app.getIgnoredInfoDao();
                    ignoredAdInfoDao.insert(new IgnoredAdInfo(adInfo.getActivityId()));
                    final ActivityInfoDao activityInfoDao = app.getActivityInfoDao();
                    ActivityInfo activityInfo = activityInfoDao.load(adInfo.getActivityId());
                    if (activityInfo != null) {
                        openActivityDetail(activityInfo);
                    } else {
                        new LoadActivityList(LoadActivityList.TYPE_RELATED, app.getCachedUserId(), 0) {
                            @Override
                            public void onSuccess(List<ActivityInfo> activityInfoList) {
                                activityInfoDao.deleteAll();
                                activityInfoDao.insertInTx(activityInfoList);
                                openActivityDetail(activityInfoDao.load(adInfo.getActivityId()));
                            }

                            @Override
                            public void onFail(String message) {

                            }
                        };
                    }
                }
            });
            return rootView;
        }


    }

    private class AdPagerAdapter extends FragmentPagerAdapter {
        private List<AdInfo> adList;

        public AdPagerAdapter(FragmentManager fm) {
            super(fm);
            adList = new ArrayList<>();
        }

        public void add(AdInfo data) {
            adList.add(data);
        }

        public void addAll(Collection<AdInfo> dataSet) {
            adList.addAll(dataSet);
        }

        public void clear() {
            adList.clear();
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return AdFragment.newInstance(adList.get(adList.size() - 1));
                default:
                    return AdFragment.newInstance(adList.get((position - 1) % adList.size()));
            }
        }

        @Override
        public int getCount() {
            if (adList.size() > 1)
                return adList.size() + 2;
            else
                return adList.size();
        }
    }


}
