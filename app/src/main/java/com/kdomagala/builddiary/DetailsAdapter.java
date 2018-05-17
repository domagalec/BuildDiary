package com.kdomagala.builddiary;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

public class DetailsAdapter extends BaseAdapter {
    private final ArrayList<Map.Entry<String, String>> mData;
    private double mTotal;


    DetailsAdapter(Map<String, String> map, double total) {
        mData = new ArrayList<>();
        mData.addAll(map.entrySet());
        mTotal = total;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Map.Entry<String, String> getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View result;

        if (convertView == null) {
            result = LayoutInflater.from(parent.getContext()).inflate(R.layout.details_adapter, parent, false);
        } else {
            result = convertView;
        }

        Map.Entry<String, String> item = getItem(position);

        String percent;
        String value;

        ((TextView) result.findViewById(R.id.detailsCategoryView)).setText(item.getKey());
        ((TextView) result.findViewById(R.id.detailsCostView)).setText(item.getValue());
        value = item.getValue().replace(',', '.');
        percent = String.valueOf((int) Math.round(Double.parseDouble(value) * 100 / mTotal)+ "%");

        ((TextView) result.findViewById(R.id.detailsPercentView)).setText(percent);
        ((ProgressBar) result.findViewById(R.id.detailsProgressBar)).setProgress((int) Math.round(Double.parseDouble(value) * 100 / mTotal));

        return result;
    }
}