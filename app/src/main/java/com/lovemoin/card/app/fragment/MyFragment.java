package com.lovemoin.card.app.fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.lovemoin.card.app.MoinCardApplication;
import com.lovemoin.card.app.R;
import com.lovemoin.card.app.activity.CompleteUserInfoActivity;
import com.lovemoin.card.app.activity.EntranceActivity;
import com.lovemoin.card.app.activity.UserModifyActivity;
import com.lovemoin.card.app.constant.Config;
import com.lovemoin.card.app.net.GetHelpMessage;
import com.nostra13.universalimageloader.core.ImageLoader;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by zzt on 15-9-15.
 */
public class MyFragment extends LazyFragment {
    private MoinCardApplication app;
    private boolean isPrepared = false;
    private TextView textAccount;
    private TextView textHint;
    private Button btnExit;
    private CircleImageView imgUser;
    private ImageLoader imageLoader;

    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my, container, false);
        setHasOptionsMenu(true);
        app = (MoinCardApplication) getActivity().getApplication();
        textAccount = (TextView) rootView.findViewById(R.id.textAccount);
        textHint = (TextView) rootView.findViewById(R.id.text_hint_modify_user);
        btnExit = (Button) rootView.findViewById(R.id.btnExit);
        imgUser = (CircleImageView) rootView.findViewById(R.id.img_user);
        imageLoader = ImageLoader.getInstance();
        String imgUrl = app.getCachedUserImg();
        if (!TextUtils.isEmpty(imgUrl)) {
            if (!imgUrl.startsWith("http:")) {
                imgUrl = Config.SERVER_URL + imgUrl;
            }
            imageLoader.displayImage(imgUrl, imgUser);
        }
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(app.getCachedUsername())) {
                    new AlertDialog.Builder(getContext())
                            .setMessage(R.string.hint_exit_visitor_account)
                            .setPositiveButton(R.string.complete_now, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    startActivity(new Intent(getContext(), CompleteUserInfoActivity.class));
                                }
                            })
                            .setNegativeButton(R.string.exit_visitor_account, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    exit();
                                }
                            }).show();
                } else exit();
            }
        });
        rootView.findViewById(R.id.layoutHelp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog pd = new ProgressDialog(getContext());
                pd.show();
                final AlertDialog dialog = new AlertDialog.Builder(getContext())
                        .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create();
                new GetHelpMessage() {
                    @Override
                    protected void onSuccess(String helpMessage) {
                        pd.dismiss();
                        dialog.setMessage(helpMessage);
                        dialog.show();
                    }

                    @Override
                    protected void onFail(String message) {
                        pd.dismiss();
                        dialog.setMessage(getString(R.string.msg_help_desc));
                        dialog.show();
                    }
                };
            }
        });
        rootView.findViewById(R.id.layoutModifyUser).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(app.getCachedUserTel())) {
                    startActivity(new Intent(getContext(), CompleteUserInfoActivity.class));
                } else {
                    startActivity(new Intent(getContext(), UserModifyActivity.class));
                }
            }
        });
        isPrepared = true;
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_fragment_my, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_about_app:
                new AlertDialog.Builder(getContext())
                        .setTitle(R.string.about)
                        .setMessage(String.format(getString(R.string.text_about_app_message), app.getVersionName()))
                        .setPositiveButton(R.string.known, null)
                        .show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void lazyLoad() {
        if (isPrepared && isVisible) {
            if (TextUtils.isEmpty(app.getCachedUsername())) {
                textAccount.setText(R.string.visitor);
            } else {
                textAccount.setText(app.getCachedUsername());
            }
            if (TextUtils.isEmpty(app.getCachedUserTel())) {
                textHint.setText(R.string.complete_user_info);
            } else {
                textHint.setText(R.string.change_pwd);
            }
        }
    }

    private void exit() {
        app.reset();
        startActivity(new Intent(getContext(), EntranceActivity.class));
        getActivity().finish();
    }
}
