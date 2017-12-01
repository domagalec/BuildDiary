package com.example.krzysiek.builddiary;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.text.ParseException;
import java.util.Date;

import android.content.Intent;

import static java.lang.Double.valueOf;

public class Item {

    public static final String ITEM_SEP = System.getProperty("line.separator");

    public final static String TITLE = "title";
    public final static String COST = "cost";
    public final static String DATE = "date";
    public final static String CATEGORY = "category";
    public final static String FILENAME = "filename";

    public final static SimpleDateFormat FORMAT = new SimpleDateFormat(
            "dd-MM-yyyy", Locale.US);

    private String mTitle = new String();
    private Double mCost = 0.0;
    //private Status mStatus = Status.NOTDONE;
    private Date mDate = new Date();
    private String mCategory = new String();

    Item(String title, Double cost, /*Status status,*/ Date date, String category) {
        this.mTitle = title;
        this.mCost = cost;
        //this.mStatus = status;
        this.mDate = date;
        this.mCategory = category;
    }

    // Create a new Item from data packaged in an Intent

    Item(Intent intent) {

        mTitle = intent.getStringExtra(Item.TITLE);
        mCost = intent.getDoubleExtra(Item.COST, 0.0);
        mCategory = intent.getStringExtra(Item.CATEGORY);
//        mStatus = Status.valueOf(intent.getStringExtra(Item.STATUS));

        //mStatus = Status.valueOf(intent.getStringExtra(Item.STATUS));

        try {
            mDate = Item.FORMAT.parse(intent.getStringExtra(Item.DATE));
        } catch (ParseException e) {
            mDate = new Date();
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

    public void setCategory(String category) {mCategory = category;}

    public void setCost(Double cost) {
        mCost = cost;
    }

   /* public Status getStatus() {
        return mStatus;
    }

    public void setStatus(Status status) {
        mStatus = status;
    }*/

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    // Take a set of String data values and
    // package them for transport in an Intent

    public static void packageIntent(Intent intent, String title,
                                     Double cost, /*Status status,*/ String date, String category) {

        intent.putExtra(Item.TITLE, title);
        intent.putExtra(Item.COST, cost);
        //intent.putExtra(Item.STATUS, status.toString());
        intent.putExtra(Item.DATE, date);
        intent.putExtra(Item.CATEGORY, category);

    }

    public String toString() {
        return mTitle + ITEM_SEP + mCost + /*ITEM_SEP + mStatus +*/ ITEM_SEP
                + FORMAT.format(mDate) + ITEM_SEP + mCategory;
    }

    public String toLog() {
        return "Title:" + mTitle + ITEM_SEP + "Cost:" + mCost + ITEM_SEP + "Category:" + mCategory
                + ITEM_SEP +/* "Status:" + mStatus + ITEM_SEP +*/ "Date:"
                + FORMAT.format(mDate) + "\n";
    }

}
