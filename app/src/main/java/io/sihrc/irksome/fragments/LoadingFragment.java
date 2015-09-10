package io.sihrc.irksome.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.sihrc.irksome.MainActivity;
import io.sihrc.irksome.R;

/**
 * Created by Chris on 9/9/15.
 */
public class LoadingFragment extends Fragment {
    MainActivity activity;

    @Bind(R.id.cancel)
    View cancel;

    public static LoadingFragment newInstance(MainActivity activity) {
        LoadingFragment fragment = new LoadingFragment();
        fragment.activity = activity;
        return fragment;
    }

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_loading, container, false);
        ButterKnife.bind(this, rootView);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                MainActivity.requestCancelled = true;
                activity.switchFragment(SearchFragment.getInstance(activity), false);
            }
        });
        return rootView;
    }
}
