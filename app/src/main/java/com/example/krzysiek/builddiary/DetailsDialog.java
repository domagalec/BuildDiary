package com.example.krzysiek.builddiary;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by krzysiek on 16.12.17.
 */

public class DetailsDialog {

    public void showDialog(Activity activity){
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.details_dialog);

        //TextView text = (TextView) dialog.findViewById(R.id.txt_dia);

       // LayoutInflater inflater = getLayoutInflater();
       // View convertView = (View) inflater.inflate(R.layout.details_dialog, null);
        //dialog.setView(convertView);
        dialog.setTitle("List");
       // ListView detailsCategoryList = (ListView) convertView.findViewById(R.id.detailsCategoryList);
       // ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.category_array, android.R.layout.simple_spinner_item);
       // detailsCategoryList.setAdapter(adapter);

       // Button dialogButton = (Button) dialog.findViewById(R.id.btn_yes);
        /*dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });*/

        dialog.show();

    }
}