package com.example.krzysiek.builddiary;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Date;

//import com.example.krzysiek.builddiary.Item.Status;

public class MainActivity extends Activity {

        private static final int ADD_ITEM_REQUEST = 0;
        private static final int EDIT_ITEM_REQUEST = 0;
        private static final String FILE_NAME = "BuilderDiaryData.txt";
        private static final String TAG = "BuilderDiary";

        // IDs for menu items
        private static final int MENU_DELETE = Menu.FIRST;
        private static final int MENU_DUMP = Menu.FIRST + 1;

        ItemListAdapter mAdapter;
        TextView summaryView;
        ListView listView;
        Button addButton;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Create a new TodoListAdapter for this ListActivity's ListView
            
            mAdapter = new ItemListAdapter(getApplicationContext());

            // Put divider between ToDoItems and FooterView
        //    getListView().setFooterDividersEnabled(true);
            // BYŁODOZROBIENIA - Inflate footerView for footer_view.xml file
           // LayoutInflater inflater = (LayoutInflater)this.getSystemService
           // 		(this.LAYOUT_INFLATER_SERVICE);
           // TextView footerView = (TextView) findViewById(R.id.footerView);
           // TextView footerView = (TextView) ((LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.footer_view, null, false);
        //    LayoutInflater inflater = LayoutInflater.from(this);
        //    TextView footerView = (TextView) inflater.inflate(R.layout.footer_view, null);
            // NOTE: You can remove this block once you've implemented the assignment
            //if (null == footerView) {
            //	return;
            //}
            //DOZROBIENIABYLO - Add footerView to ListView
        //   getListView().addFooterView(footerView);
            // Attach Listener to FooterView
          //addButton = (Button) findViewById(R.id.addbutton);
        //    footerView.setOnClickListener(new OnClickListener() {
        //        @Override
        //        public void onClick(View v) {
        //            Intent intent = new Intent(MainActivity.this, AddItemActivity.class);
        //            startActivityForResult(intent, ADD_TODO_ITEM_REQUEST);
                    //może FOR RESULT? Implement OnClick().
        //        }
        //    });
            // - Attach the adapter to this ListActivity's ListView
          //  setListAdapter(mAdapter);

            setContentView(R.layout.main);
            summaryView = (TextView) findViewById(R.id.summaryView);
            listView = (ListView) findViewById(R.id.listView);
            listView.setAdapter(mAdapter);

            totalCostUpdate();

           //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
          // setSupportActionBar(toolbar);

            addButton = (Button) findViewById(R.id.addbutton);
            addButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, AddItemActivity.class);
                    startActivityForResult(intent, ADD_ITEM_REQUEST);
                }
            });

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                   // Cursor cursor = (Cursor)adapterView.getItemAtPosition(position);
                    Intent intent = new Intent(MainActivity.this, EditItemActivity.class);

                    Item editedItem = (Item) adapterView.getItemAtPosition(position);

                    Toast.makeText(getApplicationContext(), "selected Item Name is " + editedItem.getTitle(), Toast.LENGTH_LONG).show();

                    intent.putExtra("position", position);
                    intent.putExtra("title", editedItem.getTitle());
                    intent.putExtra("cost", editedItem.getCost());
                    intent.putExtra("date", editedItem.getDate());
                    intent.putExtra("category", editedItem.getCategory());

                    startActivityForResult(intent, EDIT_ITEM_REQUEST);
                }
            });

        }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {

            Log.i(TAG,"Entered onActivityResult()");

            // Check result code and request code
            // if user submitted a new ToDoItem
            // Create a new ToDoItem from the data Intent
            // and then add it to the adapter
            if (requestCode == ADD_ITEM_REQUEST) {
                // Make sure the request was successful
                if (resultCode == RESULT_OK) {

                    Item newItem = new Item(data);
                    mAdapter.add(newItem);
                    // The user picked a contact.
                    // The Intent's data Uri identifies which contact was selected.
                    //String result=data.getStringExtra("zwrot");
                    //mUserTextView.setText(result);
                    // Do something with the contact here (bigger example below)
                    totalCostUpdate();
                }
            }
        }

        @Override
        public void onResume() {
            super.onResume();

            // Load saved Items, if necessary

            if (mAdapter.getCount() == 0)
                loadItems();
                totalCostUpdate();

        }


        @Override
        public void onStart() {
        super.onStart();

        // Load saved Items, if necessary

        if (mAdapter.getCount() == 0)
            loadItems();
            totalCostUpdate();
    }

        @Override
        protected void onStop() {
        super.onStop();

        // Save Items
        saveItems();
    }

        @Override
        protected void onDestroy() {
        super.onDestroy();

        // Save Items
        saveItems();
    }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            super.onCreateOptionsMenu(menu);

            menu.add(Menu.NONE, MENU_DELETE, Menu.NONE, "Delete all");
            menu.add(Menu.NONE, MENU_DUMP, Menu.NONE, "Dump to log");
            return true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId()) {
                case MENU_DELETE:
                    mAdapter.clear();
                    return true;
                case MENU_DUMP:
                    dump();
                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }
        }

        private void dump() {

            for (int i = 0; i < mAdapter.getCount(); i++) {
                String data = ((Item) mAdapter.getItem(i)).toLog();
                Log.i(TAG,	"Item " + i + ": " + data.replace(Item.ITEM_SEP, ","));
            }

        }

        // Load stored Items
        private void loadItems() {
            BufferedReader reader = null;
            try {
                FileInputStream fis = openFileInput(FILE_NAME);
                reader = new BufferedReader(new InputStreamReader(fis));

                String title = null;
                String cost = null;
                //String status = null;
                Date date = null;
                String category = null;

                while (null != (title = reader.readLine())) {
                    cost = reader.readLine();
                    //status = reader.readLine();
                    date = Item.FORMAT.parse(reader.readLine());
                    category = reader.readLine();
                    mAdapter.add(new Item(title, Double.valueOf(cost) /*Status.valueOf(status)*/, date, category));
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            } finally {
                if (null != reader) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        // Save Items to file
        private void saveItems() {
            PrintWriter writer = null;
            try {
                FileOutputStream fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
                writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
                        fos)));

                for (int idx = 0; idx < mAdapter.getCount(); idx++) {

                    writer.println(mAdapter.getItem(idx));

                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (null != writer) {
                    writer.close();
                }
            }
        }

        public void totalCostUpdate(){
            double total=0;
            for (int i = 0; i < mAdapter.getCount(); i++) {
                total = total+((Item) mAdapter.getItem(i)).getCost();
            }
            summaryView.setText(new DecimalFormat("##.00").format(total));
           // String.valueOf(total)
        }
    }