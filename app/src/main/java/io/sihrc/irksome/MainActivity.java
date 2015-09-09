package io.sihrc.irksome;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.sihrc.irksome.network.IrksomeClient;
import io.sihrc.irksome.network.PostPackage;
import io.sihrc.irksome.results.ResultsAdapter;
import retrofit.RetrofitError;

public class MainActivity extends AppCompatActivity {
    IrksomeClient client;
    ResultsAdapter adapter;

    @Bind(R.id.search)
    SearchView searchView;

    @Bind(R.id.results)
    ListView results;

    @Bind(R.id.progress)
    View progress;

    @Bind(R.id.darken)
    View darken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        client = new IrksomeClient();

        ButterKnife.bind(this);

        setupSearch();
        setupResults();
    }


    private void setupSearch() {
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);
        searchView.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override public boolean onQueryTextSubmit(String query) {
                performSearch(query.toLowerCase().trim());
                return false;
            }

            @Override public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void setupResults() {
        showProgressDialog(false);
        adapter = new ResultsAdapter(this);
        results.setAdapter(adapter);
    }

    private void performSearch(String query) {
        if (results == null)
            return;

        showProgressDialog(true);
        client.api.search(PostPackage.keys("query").values(query), new retrofit.Callback<List<Map<String, Object>>>() {
            @Override
            public void success(List<Map<String, Object>> maps, retrofit.client.Response response) {
                showProgressDialog(false);

                if (response.getStatus() != 200) {
                    Toast.makeText(MainActivity.this, "Sorry, there seems to be a problem with the query", Toast.LENGTH_SHORT).show();
                    return;
                }

                adapter.clear();
                adapter.addAll(maps);
                adapter.notifyDataSetChanged();
            }

            @Override public void failure(RetrofitError error) {
                showProgressDialog(false);
                Toast.makeText(MainActivity.this, "Sorry, there seems to be a problem with our servers", Toast.LENGTH_SHORT).show();
                Log.e("Retrofit Error", error.toString());
            }
        });
    }

    private void showProgressDialog(boolean show) {
        progress.setVisibility(show ? View.VISIBLE : View.GONE);
        darken.setVisibility(show ? View.VISIBLE : View.GONE);
    }
}
