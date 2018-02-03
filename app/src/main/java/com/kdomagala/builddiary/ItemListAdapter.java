package com.kdomagala.builddiary;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ItemListAdapter extends BaseAdapter {

    private final List<Item> mItems = new ArrayList<>();
    private final Context mContext;

    private static final String TAG = "BuildDiary";

    public ItemListAdapter(Context context) {

        mContext = context;
    }

    // Add a Item to the adapter
    // Notify observers that the data set has changed

    public void add(Item item) {

        mItems.add(item);
        notifyDataSetChanged();
        Log.i(TAG, "Add item from ItemListAdapter");
    }

    public void edit(Item item, int position){
        mItems.set(position, item);
        notifyDataSetChanged();
        Log.i(TAG, "Edit item from ItemListAdapter");

    }

    public void remove(int position){
        mItems.remove(position);
        notifyDataSetChanged();
    }

    // Clears the list adapter of all items.

    public void clear() {

        mItems.clear();
        notifyDataSetChanged();
    }

    // Returns the number of Items

    @Override
    public int getCount() {

        return mItems.size();
    }

    // Retrieve the number of Items

    @Override
    public Object getItem(int pos) {

        return mItems.get(pos);
    }

    // Get the position for the Item

    @Override
    public long getItemId(int pos) {

        return pos;
    }

    // Create a View for the Item at specified position
    // Remember to check whether convertView holds an already allocated View
    // before created a new View.
    // Consider using the ViewHolder pattern to make scrolling more efficient
    // See: http://developer.android.com/training/improving-layouts/smooth-scrolling.html

    private static class ViewHolder {
        RelativeLayout itemLayout;
        TextView titleView;
        TextView categoryView;
        TextView costView;
        TextView dateView;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;

        ViewHolder holder;
        if(row == null) {

            // Inflate the View for this Item from xml
            LayoutInflater mInflater = LayoutInflater.from(mContext); // context pass to the constructor of adapter
            row = mInflater.inflate(R.layout.item_view, parent, false);
            //Now create the ViewHolder
            holder = new ViewHolder();
            //and set its textView field to the proper value
            holder.itemLayout =  row.findViewById(R.id.RelativeLayout1);
            holder.titleView = row.findViewById(R.id.titleView);
            holder.costView = row.findViewById(R.id.costView);
            holder.categoryView = row.findViewById(R.id.categoryView);
            holder.dateView = row.findViewById(R.id.dateView);

            //and store it as the 'tag' of our view
            row.setTag(holder);
        } else {
            //We've already seen this one before!
            holder = (ViewHolder) row.getTag();
        }

        // Get the current Item
        final Item item = (Item) getItem(position);

        // Fill in specific Item data

        // Display Title in TextView
        holder.titleView.setText(item.getTitle());

        // Display Cost in a TextView
        if (item.getCost() == 0){
            holder.costView.setText(new DecimalFormat("0.00").format(item.getCost()));
        }
        else {
            holder.costView.setText(new DecimalFormat("##.00").format(item.getCost()));
        }

        //Display Category in a TextView
        holder.categoryView.setText(item.getCategory());

        // Display Time and Date.
        holder.dateView.setText(Item.FORMAT.format(item.getDate()));

        // Return the created View
        return row;

    }
}
