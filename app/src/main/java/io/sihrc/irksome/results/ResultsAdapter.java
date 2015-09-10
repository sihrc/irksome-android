package io.sihrc.irksome.results;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.parceler.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.sihrc.irksome.R;

/**
 * Created by Chris on 9/5/15.
 */
public class ResultsAdapter extends ArrayAdapter<Map<String, Object>> {
    Context context;
    List<Map<String, Object>> results;

    public ResultsAdapter(Context context) {
        super(context, R.layout.item_result);
        this.context = context;
        this.results = new ArrayList<>();
    }

    @SuppressWarnings("unchecked")
    private String extractData(Object result, int indent) {
        StringBuilder builder = new StringBuilder();
        if (result instanceof Map) {
            for (Map.Entry<String, Object> entry : ((Map<String, Object>) result).entrySet()) {
                builder.append("\n").append(StringUtils.repeat("   ", indent));
                builder.append(entry.getKey()).append(" : ").append(extractData(entry.getValue(), indent + 1));
            }
        } else {
            builder.append(result);
        }

        return builder.toString();
    }

    @Override public void add(Map<String, Object> object) {
        results.add(object);
    }

    @Override public void addAll(Collection<? extends Map<String, Object>> collection) {
        results.addAll(collection);
    }

    @Override public void clear() {
        results.clear();
    }

    @Override public int getCount() {
        return results.size();
    }

    @Override public Map<String, Object> getItem(int position) {
        return results.get(position);
    }

    @Override public View getView(int position, View convertView, ViewGroup parent) {
        ResultHolder viewHolder;
        if (convertView == null) {
            convertView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.item_result, parent, false);
            viewHolder = new ResultHolder(convertView);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ResultHolder) convertView.getTag();
        }

        Map<String, Object> result = getItem(position);
        String key = result.keySet().toArray()[0].toString();
        viewHolder.maintext.setText(key);
        viewHolder.subtext.setText(extractData(result.get(key), 0));

        return convertView;
    }

    public class ResultHolder {
        @Bind(R.id.maintext) TextView maintext;
        @Bind(R.id.subtext) TextView subtext;

        public ResultHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
