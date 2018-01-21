package com.kdomagala.builddiary;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

public class AddItemActivity extends AppCompatActivity {

    private static final String TAG = "BuildDiary";
    private static String dateString;
    private TextView dateView;
    private Date mDate;
    private EditText mTitleText;
    private EditText mCost;
    private Spinner mCategorySpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        setContentView(R.layout.new_item);

        mTitleText = findViewById(R.id.title);
        mCost = findViewById(R.id.cost);
        mCategorySpinner = findViewById(R.id.category_spinner);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.category_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        mCategorySpinner.setAdapter(adapter);

        mCategorySpinner.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm=(InputMethodManager)getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (getCurrentFocus() != null)
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                return false;
            }
        }) ;

        dateView = findViewById(R.id.date);

        // Set the date and time

        setDefaultDateTime();

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
        submitButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTitleText.getText().toString().equals("")){
                    Toast.makeText(AddItemActivity.this,R.string.missing_title_toast,Toast.LENGTH_SHORT).show();
                }
                else{
                    // Gather Item data
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
                    Intent data = new Intent();
                    Item.packageIntent(data, titleString, cost, fullDate, category);

                    // return data Intent and finish

                    setResult(RESULT_OK, data);
                    finish();
                }
            }
        });
    }

    private void setDefaultDateTime() {

        // Default is current time
        mDate = new Date();
        mDate = new Date(mDate.getTime());

        Calendar c = Calendar.getInstance();
        c.setTime(mDate);

        setDateString(c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.MONTH),
                c.get(Calendar.YEAR));

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

    private String getToDoTitle() {return mTitleText.getText().toString();}

    private String getCategory() {return mCategorySpinner.getSelectedItem().toString();}

    // DialogFragment used to pick a Item date

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
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            setDateString(dayOfMonth, monthOfYear, year);
            TextView dateView = getActivity().findViewById(R.id.date);
            dateView.setText(dateString);
        }
    }

    private void showDatePickerDialog() {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }



}
