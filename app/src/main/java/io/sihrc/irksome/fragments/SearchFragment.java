package io.sihrc.irksome.fragments;

import android.app.SearchManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.sihrc.irksome.MainActivity;
import io.sihrc.irksome.R;
import io.sihrc.irksome.network.PostPackage;
import retrofit.RetrofitError;

/**
 * Created by Chris on 9/9/15.
 */
public class SearchFragment extends Fragment {
    public static SearchFragment instance;
    MainActivity activity;
    @Bind(R.id.search)
    SearchView searchView;
    @Bind(R.id.voice_button)
    View voiceButton;

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MainActivity.REQ_CODE_SPEECH_INPUT) {
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            searchView.setQuery(result.get(0), true);
        }

    }

    @Override public void onAttach(Context context) {
        super.onAttach(context);
        activity.setHeader("SEARCH");
    }

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.bind(this, rootView);
        setupSearch();
        setupVoice();
        return rootView;
    }

    private void setupSearch() {
        SearchManager searchManager = (SearchManager) activity.getSystemService(Context.SEARCH_SERVICE);

        searchView.setSearchableInfo(searchManager.getSearchableInfo(activity.getComponentName()));
        searchView.setIconifiedByDefault(false);
        searchView.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override public boolean onQueryTextSubmit(String query) {
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
                performSearch(query.toLowerCase().trim());
                return false;
            }

            @Override public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        int searchSrcTextId = getResources().getIdentifier("android:id/search_src_text", null, null);
        EditText searchEditText = (EditText) searchView.findViewById(searchSrcTextId);
        searchEditText.setTextColor(Color.BLACK);
        searchEditText.setHintTextColor(Color.LTGRAY);
        searchEditText.setGravity(Gravity.CENTER);
        int magId = getResources().getIdentifier("android:id/search_mag_icon", null, null);
        ImageView magImage = (ImageView) searchView.findViewById(magId);
        magImage.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
        final int textViewID = searchView.getContext().getResources().getIdentifier("android:id/search_src_text",null, null);
        final AutoCompleteTextView searchTextView = (AutoCompleteTextView) searchView.findViewById(textViewID);
        try {
            Field mCursorDrawableRes = TextView.class.getDeclaredField("mCursorDrawableRes");
            mCursorDrawableRes.setAccessible(true);
            mCursorDrawableRes.set(searchTextView, 0); //This sets the cursor resource ID to 0 or @null which will make it visible on white background
        } catch (Exception ignored) {}
    }

    private void setupVoice() {
        voiceButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                    getString(R.string.speech_prompt));
                try {
                    startActivityForResult(intent, MainActivity.REQ_CODE_SPEECH_INPUT);
                } catch (ActivityNotFoundException a) {
                    Toast.makeText(getContext(),
                        getString(R.string.speech_not_supported),
                        Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void performSearch(final String query) {
        MainActivity.requestCancelled = false;
        activity.setHeader(query);
        activity.switchFragment(LoadingFragment.newInstance(activity), true);
        activity.getClient().api.search(PostPackage.keys("query").values(query), new retrofit.Callback<List<Map<String, Object>>>() {
            @Override
            public void success(List<Map<String, Object>> maps, retrofit.client.Response response) {

                if (response.getStatus() != 200) {
                    Toast.makeText(activity, "Sorry, there seems to be a problem with the query", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!MainActivity.requestCancelled) {
                    activity.setHeader("Results for " + query);
                    activity.switchFragment(ResultsFragment.newInstance(maps), true);
                } else {
                    MainActivity.requestCancelled = false;
                }
            }

            @Override public void failure(RetrofitError error) {
                activity.switchFragment(SearchFragment.getInstance(activity), false);
                Toast.makeText(activity, "Sorry, there seems to be a problem with our servers", Toast.LENGTH_SHORT).show();
                Log.e("Retrofit Error", error.toString());
            }
        });
    }

    public static SearchFragment getInstance(MainActivity activity) {
        if (instance == null) {
            instance = new SearchFragment();
            instance.activity = activity;
        }
        return instance;
    }
}
