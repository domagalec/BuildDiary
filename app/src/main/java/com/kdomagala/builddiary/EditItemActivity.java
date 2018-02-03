package com.kdomagala.builddiary;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;

public class EditItemActivity extends AppCompatActivity {

    private static final String TAG = "BuildDiary";
    private static String dateString;
    private TextView dateView;
    private Date mDate;
    private EditText mTitleText;
    private EditText mCost;
    private Spinner mCategorySpinner;

    private AdView mAdView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        setContentView(R.layout.new_item);

        Intent i = getIntent();
        final int position = i.getIntExtra("position", 0);
        Double cost = i.getDoubleExtra("cost", 0);
        String category = i.getStringExtra("category");
        Log.i(TAG, "dateString: " + dateString);
        Date date = (Date) i.getSerializableExtra("date");
        Log.i(TAG, "date: " + date.toString());

        mTitleText = findViewById(R.id.title);
        mTitleText.setText(i.getStringExtra("title"));
        mTitleText.setSelection(mTitleText.getText().length());

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        mCost = findViewById(R.id.cost);

        if (cost == 0) {
            mCost.setText(new DecimalFormat("0.00").format(cost));
            String newcost = String.valueOf(mCost.getText());
            mCost.setText(newcost.replace(',', '.'));

        } else {
            mCost.setText(new DecimalFormat("##.00").format(cost));
            String newcost = String.valueOf(mCost.getText());
            mCost.setText(newcost.replace(',', '.'));
        }
        mCategorySpinner = findViewById(R.id.category_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.category_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        mCategorySpinner.setAdapter(adapter);

        mCategorySpinner.setSelection(getIndex(mCategorySpinner, category));

        mCategorySpinner.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm=(InputMethodManager)getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (getCurrentFocus() != null)
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                return false;
            }
        }) ;

        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        dateView = findViewById(R.id.date);
        dateView.setText(Item.FORMAT.format(date));

        // Set the date and time

        setNoChangeDate(date);

        // OnClickListener for the Date button, calls showDatePickerDialog() to
        // show the Date dialog

        final Button datePickerButton = findViewById(R.id.date_picker_button);
        datePickerButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                if (getCurrentFocus() != null)
                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                showDatePickerDialog();
            }
        });

        // OnClickListener for the Cancel Button,

        final Button cancelButton = findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                // Indicate result and finish
                finish();
            }
        });

        // Set up OnClickListener for the Submit Button

        final Button submitButton = findViewById(R.id.submitButton);
        submitButton.setText(R.string.save);
        submitButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTitleText.getText().toString().equals("")){
                    Toast.makeText(EditItemActivity.this,R.string.missing_title_toast,Toast.LENGTH_SHORT).show();
                }
                else {
                    // gather Item data

                    // Get the current cost
                    Double cost;
                    if(mCost.getText().toString().equals("")||mCost.getText().toString().equals(".")||mCost.getText().toString().equals(",")){
                        cost = 0.00;
                    }
                    else {
                        cost = getCost();
                    }

                    // Get the current Item Title

                    String titleString = getToDoTitle();

                    String category = getCategory();

                    // Construct the Date string
                    String fullDate = dateString;

                    // Package Item data into an Intent
                    Intent editedData = new Intent();
                    Item.packageIntent(editedData, titleString, cost, fullDate, category);

                    editedData.putExtra("position", position);
                    // return data Intent and finish

                    setResult(RESULT_OK, editedData);
                    finish();
                }
            }
        });
    }

    private void setNoChangeDate(Date date) {
        mDate = new Date();
        mDate = date;

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        setDateString(day,month,year);
        dateView.setText(dateString);
    }

    private static void setDateString(int dayOfMonth, int monthOfYear, int year) {

        // Increment monthOfYear for Calendar/Date -> Time Format setting
        monthOfYear++;
        String mon = "" + monthOfYear;
        String day = "" + dayOfMonth;

        if (monthOfYear < 10)
            mon = "0" + monthOfYear;
        if (dayOfMonth < 10)
            day = "0" + dayOfMonth;

        dateString = day + "-" + mon + "-" + year;
    }

    private Double getCost() {return Double.valueOf(mCost.getText().toString());}

    private String getToDoTitle() {
        return mTitleText.getText().toString();
    }

    private String getCategory() {return mCategorySpinner.getSelectedItem().toString();}

    // DialogFragment used to pick a ToDoItem deadline date

    public static class DatePickerFragment extends DialogFragment implements
            DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            // Use the current date as the default date in the picker

            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            setDateString(dayOfMonth, monthOfYear, year);
            TextView dateView = getActivity().findViewById(R.id.date);
            dateView.setText(dateString);
        }
    }

    private void showDatePickerDialog() {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }

    private int getIndex(Spinner spinner, String myString)
    {
        int index = 0;

        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                index = i;
                break;
            }
        }
        return index;
    }

}
