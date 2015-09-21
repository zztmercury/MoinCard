package com.lovemoin.card.app.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.lovemoin.card.app.MoinCardApplication;
import com.lovemoin.card.app.R;
import com.lovemoin.card.app.activity.LoginActivity;

/**
 * Created by zzt on 15-9-15.
 */
public class MyFragment extends LazyFragment {
    private MoinCardApplication app;
    private boolean isPrepared = false;
    private TextView textAccount;
    private Button btnExit;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my, container, false);
        app = (MoinCardApplication) getActivity().getApplication();
        textAccount = (TextView) rootView.findViewById(R.id.textAccount);
        btnExit = (Button) rootView.findViewById(R.id.btnExit);
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                app.reset();
                startActivity(new Intent(getContext(), LoginActivity.class));
                getActivity().finish();
            }
        });
        textAccount.setText(app.getCachedUserTel());
        isPrepared = true;
        return rootView;
    }

    @Override
    protected void lazyLoad() {
        if (isPrepared && isVisible) {

        }
    }
}
