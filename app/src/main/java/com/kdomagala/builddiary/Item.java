package com.kdomagala.builddiary;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.text.ParseException;
import java.util.Date;

import android.content.Intent;
import android.util.Log;

public class Item {

    private static final String ITEM_SEP = System.getProperty("line.separator");
    private static final String TAG = "BuilderDiary";

    public final static String TITLE = "title";
    public final static String COST = "cost";
    public final static String DATE = "date";
    public final static String CATEGORY = "category";
    public final static String FILENAME = "filename";

    public final static SimpleDateFormat FORMAT = new SimpleDateFormat(
            "dd-MM-yyyy", Locale.US);

    private String mTitle;
    private Double mCost = 0.0;
    private Date mDate = new Date();
    private String mCategory;

    Item(String title, Double cost, Date date, String category) {
        this.mTitle = title;
        this.mCost = cost;
        this.mDate = date;
        this.mCategory = category;
    }

    Item(Intent intent) {

        mTitle = intent.getStringExtra(Item.TITLE);
        mCost = intent.getDoubleExtra(Item.COST, 0.0);
        mCategory = intent.getStringExtra(Item.CATEGORY);

        try {
            Log.i(TAG,"Enter parse date" );
            mDate = Item.FORMAT.parse(intent.getStringExtra(Item.DATE));
            Log.i(TAG, "Exit parse date");
        } catch (ParseException e) {
            Log.i(TAG, "Enter catch date");
            mDate = new Date();
            Log.i(TAG, "Exit catch date");
        }
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public Double getCost() {
        return mCost;
    }

    public String getCategory() {return mCategory;}

    //public void setCategory(String category) {mCategory = category;}

    //public void setCost(Double cost) {mCost = cost;}

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    // Take a set of String data values and
    // package them for transport in an Intent

    public static void packageIntent(Intent intent, String title, Double cost, String date, String category) {

        intent.putExtra(Item.TITLE, title);
        intent.putExtra(Item.COST, cost);
        intent.putExtra(Item.DATE, date);
        intent.putExtra(Item.CATEGORY, category);

    }

    public String toString() {
        return mTitle + ITEM_SEP + mCost + ITEM_SEP
                + FORMAT.format(mDate) + ITEM_SEP + mCategory;
    }

    public String toLog() {
        return "Title:" + mTitle + ITEM_SEP + "Cost:" + mCost + ITEM_SEP + "Category:" + mCategory
                + ITEM_SEP + "Date:"
                + FORMAT.format(mDate) + "\n";
    }

}
