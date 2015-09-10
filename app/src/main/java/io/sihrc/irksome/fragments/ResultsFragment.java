package io.sihrc.irksome.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.sihrc.irksome.R;
import io.sihrc.irksome.results.ResultsAdapter;

/**
 * Created by Chris on 9/9/15.
 */
public class ResultsFragment extends Fragment {
    List<Map<String, Object>> results;

    @Bind(R.id.results)
    ListView listView;

    public static ResultsFragment newInstance(List<Map<String, Object>> maps) {
        ResultsFragment fragment = new ResultsFragment();
        fragment.results = maps;

        return fragment;
    }

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_results, container, false);
        ButterKnife.bind(this, rootView);
        ResultsAdapter adapter = new ResultsAdapter(getContext());
        adapter.addAll(results);
        listView.setAdapter(adapter);

        return rootView;
    }
}
