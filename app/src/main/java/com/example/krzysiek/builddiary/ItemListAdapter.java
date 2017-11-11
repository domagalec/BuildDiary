package com.example.krzysiek.builddiary;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ItemListAdapter extends BaseAdapter {

    private final List<Item> mItems = new ArrayList<Item>();
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
    }

    // Clears the list adapter of all items.

    public void clear() {

        mItems.clear();
        notifyDataSetChanged();
    }

    // Returns the number of ToDoItems

    @Override
    public int getCount() {

        return mItems.size();
    }

    // Retrieve the number of ToDoItems

    @Override
    public Object getItem(int pos) {

        return mItems.get(pos);
    }

    // Get the ID for the ToDoItem
    // In this case it's just the position

    @Override
    public long getItemId(int pos) {

        return pos;
    }

    // Create a View for the Item at specified position
    // Remember to check whether convertView holds an already allocated View
    // before created a new View.
    // Consider using the ViewHolder pattern to make scrolling more efficient
    // See: http://developer.android.com/training/improving-layouts/smooth-scrolling.html

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the current Item
        final Item item = (Item) getItem(position);

        // Inflate the View for this ToDoItem
        // from todo_item.xml
        LayoutInflater mInflater = LayoutInflater.from(mContext); // context pass to the constructor of adapter
        convertView = mInflater.inflate(R.layout.item_view, parent, false);

        //LinearLayout itemLayout = (LinearLayout)convertView.findViewById(R.id.LinearLayout1);

        RelativeLayout itemLayout = (RelativeLayout)convertView.findViewById(R.id.RelativeLayout1);

        // Fill in specific Item data
        // Remember that the data that goes in this View
        // corresponds to the user interface elements defined
        // in the layout file

		/*public String getTitle() {return mTitle;	}

		public void setTitle(String title) {	mTitle = title;}

		public Priority getPriority() {	return mPriority;}

		public void setPriority(Priority priority) {	mPriority = priority;}

		public Status getStatus() {	return mStatus;}

		public void setStatus(Status status) {	mStatus = status;}

		public Date getDate() {	return mDate;}

		public void setDate(Date date) {	mDate = date;}
		*/

        // Display Title in TextView
        final TextView titleView = (TextView) itemLayout.findViewById(R.id.titleView);
        titleView.setText(item.getTitle().toString());

        //Set up Status CheckBox
        /*final CheckBox statusView = (CheckBox) itemLayout.findViewById(R.id.statusCheckBox);
        if (item.getStatus()== Item.Status.DONE) {
            statusView.setChecked(true);
        }*/

        // Must also set up an OnCheckedChangeListener,
        // which is called when the user toggles the status checkbox

        /*statusView.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (Item.getStatus()==Item.Status.DONE){
                    Item.setStatus(Item.Status.NOTDONE);
                }
                else{
                    Item.setStatus(Item.Status.DONE);
                }
            }
        });*/

        // Display Cost in a TextView
        final TextView costView = (TextView) itemLayout.findViewById(R.id.costView);
        costView.setText(new DecimalFormat("##.00").format(item.getCost()));

        //Display Category in a TextView
        final TextView categoryView = (TextView) itemLayout.findViewById(R.id.categoryView);
        categoryView.setText(item.getCategory().toString());

        // Display Time and Date.
        // Hint - use ToDoItem.FORMAT.format(toDoItem.getDate()) to get date and
        // time String
        final TextView dateView = (TextView) itemLayout.findViewById(R.id.dateView);
        dateView.setText(Item.FORMAT.format(item.getDate()));

        // Return the View you just created
        return itemLayout;

    }
}
