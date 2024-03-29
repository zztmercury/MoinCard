package com.lovemoin.card.app.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.lovemoin.card.app.MoinCardApplication;
import com.lovemoin.card.app.R;
import com.lovemoin.card.app.constant.Config;
import com.lovemoin.card.app.fragment.ActivityListFragment;
import com.lovemoin.card.app.fragment.CardListFragment;
import com.lovemoin.card.app.fragment.MyFragment;
import com.lovemoin.card.app.net.CheckVersion;
import com.lovemoin.card.app.net.FileDownloader;
import com.lovemoin.card.app.net.HasNum1Act;
import com.lovemoin.card.app.utils.DateUtil;
import com.lovemoin.card.app.utils.DisplayUtil;

import java.util.Locale;


public class HomeActivity extends BaseActivity {

    public static final String KEY_SECTION = "section";
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every loaded fragment in memory. If this becomes too
     * memory intensive, it may be best to switch to a {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private RadioGroup mNavigationGroup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(3);

        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        mNavigationGroup.check(R.id.navItemCard);
                        break;
                    case 1:
                        mNavigationGroup.check(R.id.navItemActivity);
                        break;
                    case 2:
                        mNavigationGroup.check(R.id.navItemMy);
                        break;
                }
                if (getSupportActionBar() != null)
                    getSupportActionBar().setTitle(mSectionsPagerAdapter.getPageTitle(position));
            }
        });

        mNavigationGroup = (RadioGroup) findViewById(R.id.navigationGroup);

        mNavigationGroup.check(R.id.navItemCard);
        mViewPager.setCurrentItem(0);
        setNavigationImgSize(30, 30);
        mNavigationGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.navItemCard:
                        mViewPager.setCurrentItem(0);
                        break;
                    case R.id.navItemActivity:
                        mViewPager.setCurrentItem(1);
                        break;
                    case R.id.navItemMy:
                        mViewPager.setCurrentItem(2);
                        break;
                }
                System.out.println("check " + checkedId);
            }
        });

        checkVersion();
//        if (app.isUserFirstTime()) {
        new HasNum1Act(app.getCachedUserId()) {
            @Override
            protected void onSuccess(String activityId) {
                Intent i = new Intent(getApplicationContext(), GiftPackActivity.class);
                i.putExtra(GiftPackActivity.TYPE, GiftPackActivity.TYPE_NEW_USER);
                i.putExtra(Config.KEY_ACTIVITY_ID, activityId);
                startActivity(i);
            }

            @Override
            protected void onFail(String message) {

            }
        };
//        }

        app.setFirstTimeInstalled(false);
    }

    @Override
    protected void onPause() {
        super.onPause();
        getIntent().putExtra(KEY_SECTION, mViewPager.getCurrentItem());
    }

    @Override
    protected void onResume() {
        ((MoinCardApplication) getApplication()).setIsExchange(false);
        mViewPager.setCurrentItem(getIntent().getIntExtra(KEY_SECTION, mViewPager.getCurrentItem()));
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_help) {
            startActivity(new Intent(HomeActivity.this, GuideActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * 设置导航栏图标大小
     *
     * @param width  宽度
     * @param height 高度
     */
    private void setNavigationImgSize(int width, int height) {
        for (int i = 0; i < mNavigationGroup.getChildCount(); i++) {
            RadioButton radioButton = (RadioButton) mNavigationGroup.getChildAt(i);
            Drawable drawableTop = radioButton.getCompoundDrawables()[1];
            drawableTop.setBounds(0, 0, DisplayUtil.dp2Px(this, width), DisplayUtil.dp2Px(this, height));
            radioButton.setCompoundDrawables(null, drawableTop, null, null);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    private void checkVersion() {
        new CheckVersion(app.getVersionCode()) {
            @Override
            public void onSuccess(final String apkName) {
                app.cacheHasNewVersion(true);
                if (app.isShowNewVersionOnStart()) {
                    new AlertDialog.Builder(HomeActivity.this)
                            .setTitle(R.string.hint)
                            .setMessage(R.string.hint_found_new_version)
                            .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    FileDownloader fileDownloader = new FileDownloader(HomeActivity.this);
                                    fileDownloader.download(apkName, "Download", "MoinCard" + DateUtil.LongToString(System.currentTimeMillis(), "yyyyMMdd") + ".apk");
                                }
                            })
                            .setNegativeButton(R.string.cancle, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).show();

                }
            }

            @Override
            public void onFail(String message) {
                app.cacheHasNewVersion(false);
            }
        };
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(String.valueOf(this.getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position) {
                case 0:
                    return new CardListFragment();
                case 1:
                    return new ActivityListFragment();
                case 2:
                    return new MyFragment();
            }
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
                case 2:
                    return getString(R.string.title_section3).toUpperCase(l);
            }
            return null;
        }
    }
}
