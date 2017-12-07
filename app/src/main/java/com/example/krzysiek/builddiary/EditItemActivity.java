package com.example.krzysiek.builddiary;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.widget.Toast;
//import com.example.krzysiek.builddiary.Item.Status;


import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

public class EditItemActivity extends Activity {

    private static final String TAG = "BuildDiary";

    private static String dateString;
    private static TextView dateView;

    private Date mDate;
    private EditText mTitleText;
    private EditText mCost;
    private Spinner mCategorySpinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_item);

        Intent i = getIntent();
        final int position = i.getIntExtra("position", 0);
        Double cost = i.getDoubleExtra("cost", 0);
        String category = i.getStringExtra("category");
//        dateString = i.getStringExtra("date");
        Log.i(TAG, "dateString: " + dateString);
        Date date = (Date) i.getSerializableExtra("date");
        Log.i(TAG, "date: " + date.toString());

        mTitleText = (EditText) findViewById(R.id.title);
        mTitleText.setText(i.getStringExtra("title"));
        // mTitleText.setText(Item.FORMAT.format(date));

        mCost = (EditText) findViewById(R.id.cost);
        if (cost == 0) {
            mCost.setText(new DecimalFormat("0.00").format(cost));
        } else {
            mCost.setText(new DecimalFormat("##.00").format(cost));
        }
        mCategorySpinner = (Spinner) findViewById(R.id.category_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.category_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        mCategorySpinner.setAdapter(adapter);

        mCategorySpinner.setSelection(getIndex(mCategorySpinner, category));
          //  int spinnerPosition = adapter.getPosition(category);
          //  mCategorySpinner.setSelection(spinnerPosition);

        dateView = (TextView) findViewById(R.id.date);
        dateView.setText(Item.FORMAT.format(date));

        // Set the date and time

        setNoChangeDate(date);

        // OnClickListener for the Date button, calls showDatePickerDialog() to
        // show the Date dialog

        final Button datePickerButton = (Button) findViewById(R.id.date_picker_button);
        datePickerButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        // OnClickListener for the Cancel Button,

        final Button cancelButton = (Button) findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                // Indicate result and finish
                finish();
            }
        });

        // Set up OnClickListener for the Reset Button
        final Button resetButton = (Button) findViewById(R.id.resetButton);
        resetButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                // Reset data to default values
                //  mPriorityRadioGroup.check(R.id.medPriority);
                // mStatusRadioGroup.check(R.id.statusNotDone);
                mTitleText.setText(null);
                mCost.setText(null);

                // reset date and time
                setDefaultDateTime();
            }
        });

        // Set up OnClickListener for the Submit Button

        final Button submitButton = (Button) findViewById(R.id.submitButton);
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
                    // Get the current Status
                    //Status status = getStatus();

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

    // Do not modify below this point.

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

    private void setDefaultDateTime() {

        // Default is current time
        mDate = new Date();
        mDate = new Date(mDate.getTime());

        Calendar c = Calendar.getInstance();
        c.setTime(mDate);

        setDateString(c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.MONTH),
                c.get(Calendar.YEAR));

        dateView.setText(dateString);

        //setTimeString(c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE),
        //        c.get(Calendar.MILLISECOND));

        //timeView.setText(timeString);
    }

    /*private void setEditDateTime() {

        // Default is current time
        mDate = new Date();
        mDate = new Date(mDate.getTime());

        Calendar c = Calendar.getInstance();
        c.setTime(mDate);

        setDateString(c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.MONTH),
                c.get(Calendar.YEAR));

        dateView.setText(dateString);

        //setTimeString(c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE),
        //        c.get(Calendar.MILLISECOND));

        //timeView.setText(timeString);
    }*/

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

   /* private static void setTimeString(int hourOfDay, int minute, int mili) {
        String hour = "" + hourOfDay;
        String min = "" + minute;

        if (hourOfDay < 10)
            hour = "0" + hourOfDay;
        if (minute < 10)
            min = "0" + minute;

        timeString = hour + ":" + min + ":00";
    }*/

    private Double getCost() {return Double.valueOf(mCost.getText().toString());}

    /*{return mCost.getText().toString();

       /* switch (mPriorityRadioGroup.getCheckedRadioButtonId()) {
            case R.id.lowPriority: {
                return Priority.LOW;
            }
            case R.id.highPriority: {
                return Priority.HIGH;
            }
            default: {
                return Priority.MED;
            }
        }*/

    /*private Status getStatus() {

       // switch (mStatusRadioGroup.getCheckedRadioButtonId()) {
       //     case R.id.statusDone: {
                return Status.DONE;
        //    }
        //    default: {
        //        return Status.NOTDONE;
        //    }
        }*/

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
