package com.example.krzysiek.builddiary;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ListPopupWindow;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.widget.Toast;

import org.w3c.dom.Text;

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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Double.parseDouble;

//import com.example.krzysiek.builddiary.Item.Status;

public class MainActivity extends AppCompatActivity {

        private static final int ADD_ITEM_REQUEST = 1;
        private static final int EDIT_ITEM_REQUEST = 2;
        private static final String FILE_NAME = "BuilderDiaryData.txt";
        private static final String TAG = "BuildDiary";

        // IDs for menu items
        private static final int MENU_DELETE = Menu.FIRST;
        private static final int MENU_DUMP = Menu.FIRST + 1;
        private static final int MENU_BUDGET = Menu.FIRST + 2;

        ItemListAdapter mAdapter;
        TextView summaryView;
        ListView listView;
        TextView showDetails;
        Button addButton;
        ProgressBar budgetBar;
        TextView percentView;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            SharedPreferences settings = getSharedPreferences("preferences", MODE_PRIVATE);
            settings.getString("budget", "0");

            if(getSupportActionBar() != null){
                getSupportActionBar().setDisplayShowHomeEnabled(true);
            }
            getSupportActionBar().setLogo(R.mipmap.ic_launcher);
            getSupportActionBar().setDisplayUseLogoEnabled(true);

            // Create a new TodoListAdapter for this ListActivity's ListView
            mAdapter = new ItemListAdapter(getApplicationContext());

            setContentView(R.layout.main);

            summaryView = (TextView) findViewById(R.id.summaryView);
            listView = (ListView) findViewById(R.id.listView);
            listView.setAdapter(mAdapter);
            registerForContextMenu(listView);
            budgetBar = (ProgressBar) findViewById(R.id.budgetBar);
            percentView = (TextView) findViewById(R.id.percentView);

            showDetails = (Button) findViewById(R.id.ShowDetails);
            showDetails.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {

                    final LinkedHashMap<String,String> listMap =  new LinkedHashMap<String, String>();
                    String catArr[] = getResources().getStringArray(R.array.category_array);

                    for (int i = 0; i < catArr.length; i++) {
                        double categoryCost = 0.0;

                        for (int j=0; j < mAdapter.getCount(); j++) {
                            Item item = (Item) mAdapter.getItem(j);
                            if (item.getCategory().equals(catArr[i])){
                                categoryCost = categoryCost + item.getCost();
                            }
                        }
                        if (categoryCost != 0.0) {
                            listMap.put(catArr[i], String.valueOf(new DecimalFormat("##.00").format(categoryCost)));
                        }
                        Log.i("TAG",catArr[i]);
                    }


                   /*() for (Map.Entry<String,String> entry : listMap.entrySet()) {
                        String key = entry.getKey();
                        String value = entry.getValue();
                        Log.i("TAG", key + value);
                    }*/

                    final AlertDialog.Builder detailsBuilder = new AlertDialog.Builder(MainActivity.this);
                    detailsBuilder.setTitle(R.string.show_details);

                    LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
                    View dialogView = inflater.inflate(R.layout.details_dialog, null);
                    detailsBuilder.setView(dialogView);
                    detailsBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            listMap.clear();
                            dialog.dismiss();
                        }
                    });

                    ListView detailsView = (ListView) dialogView.findViewById(R.id.detailsView);


                    DetailsAdapter adapter = new DetailsAdapter(listMap);

                    //ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(MainActivity.this,
                    //        R.array.category_array, android.R.layout.simple_list_item_1);

                    detailsView.setAdapter(adapter);


                    /*final ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(MainActivity.this,
                            R.array.category_array, android.R.layout.simple_list_item_1);

                    ArrayList<HashMap<String,String>> listMap =  new ArrayList<HashMap<String, String>>();
                    HashMap<String,String> map1 =new HashMap<String, String>();
                    map1.put("title","Amdroid1");
                    HashMap<String,String> map2 =new HashMap<String, String>();
                    map2.put("title","Amdroid2");
                    HashMap<String,String> map3 =new HashMap<String, String>();
                    map3.put("title","Amdroid3");
                    HashMap<String,String> map4 =new HashMap<String, String>();
                    map4.put("title","Amdroid4");
                    HashMap<String,String> map5 =new HashMap<String, String>();
                    map5.put("title","Amdroid5");

                    listMap.add(map1);
                    listMap.add(map2);
                    listMap.add(map3);
                    listMap.add(map4);
                    listMap.add(map5);

                    ArrayList<String> listString = new ArrayList<String>();
                    for(HashMap<String,String> row : listMap){
                        listString.add(row.get("title"));
                    }

                    String[] array = listString.toArray(new String[listString.size()]);
                    System.out.println("Lenght "+array.length);

                    detailsBuilder.setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    detailsBuilder.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            CharSequence strName = arrayAdapter.getItem(which);
                            AlertDialog.Builder builderInner = new AlertDialog.Builder(MainActivity.this);
                            builderInner.setMessage(strName);
                            builderInner.setTitle("Your Selected Item is");
                            builderInner.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,int which) {
                                    dialog.dismiss();
                                }
                            });
                            builderInner.show();
                        }
                    });*/
                    detailsBuilder.create();
                    detailsBuilder.show();
                }
            });

            addButton = (Button) findViewById(R.id.addbutton);
            addButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, AddItemActivity.class);
                    startActivityForResult(intent, 1);
                }
            });

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                   // Cursor cursor = (Cursor)adapterView.getItemAtPosition(position);
                    Intent intent = new Intent(MainActivity.this, EditItemActivity.class);

                    Item editedItem = (Item) adapterView.getItemAtPosition(position);

                    //Toast.makeText(getApplicationContext(), "selected Item Name is " + editedItem.getTitle(), Toast.LENGTH_LONG).show();

                    intent.putExtra("position", position);
                    intent.putExtra("title", editedItem.getTitle());
                    intent.putExtra("cost", editedItem.getCost());
                    intent.putExtra("date", editedItem.getDate());
                    intent.putExtra("category", editedItem.getCategory());

                    startActivityForResult(intent, 2);
                }
            });

            /*listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                    return true;

                }

            });*/

        }

    @Override
    public void onCreateContextMenu(ContextMenu menu,View v, ContextMenu.ContextMenuInfo menuInfo){
        if (v.getId() == R.id.listView){
            AdapterView.AdapterContextMenuInfo info =(AdapterView.AdapterContextMenuInfo)menuInfo;
            MenuItem mnu1=menu.add(0,0,0,R.string.menu_edit);
            MenuItem mnu2=menu.add(0,1,1,R.string.menu_delete);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem menuItem){
        AdapterView.AdapterContextMenuInfo info=(AdapterView.AdapterContextMenuInfo)menuItem.getMenuInfo();
        int position = ((AdapterView.AdapterContextMenuInfo)info).position;

        switch (menuItem.getItemId()) {
            case 0:
                Intent intent = new Intent(MainActivity.this, EditItemActivity.class);
                Item editedItem = (Item) listView.getItemAtPosition(position);

                //Toast.makeText(getApplicationContext(), "selected Item Name is " + editedItem.getTitle(), Toast.LENGTH_LONG).show();

                intent.putExtra("position", position);
                intent.putExtra("title", editedItem.getTitle());
                intent.putExtra("cost", editedItem.getCost());
                intent.putExtra("date", editedItem.getDate());
                intent.putExtra("category", editedItem.getCategory());

                startActivityForResult(intent, 2);
                break;
            case 1:
                mAdapter.remove(position);
                totalCostUpdate();
                budgetBarUpdate();
                Toast.makeText(this, R.string.item_deleted, Toast.LENGTH_SHORT).show();
                break;

            default:
                break;
        }
        return true;
    }


        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {

            Log.i(TAG, "Entered onActivityResult()");

            // Check result code and request code
            // if user submitted a new ToDoItem
            // Create a new ToDoItem from the data Intent
            // and then add it to the adapteR
            if (resultCode == RESULT_OK) {
                if (requestCode == 1) {
                    Item newItem = new Item(data);
                    mAdapter.add(newItem);
                    listView.setSelection(mAdapter.getCount());
                    Log.i(TAG, "Added new item");
                    totalCostUpdate();
                    budgetBarUpdate();
                }
                else if (requestCode == 2) {
                    int position = 0;
                    Item editedItem = new Item(data);
                    position = data.getIntExtra("position", 0);
                    mAdapter.edit(editedItem, position);
                    Log.i(TAG, "Edited item");
                    totalCostUpdate();
                    budgetBarUpdate();
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
                budgetBarUpdate();
        }


        @Override
        public void onStart() {
        super.onStart();

        // Load saved Items, if necessary

        if (mAdapter.getCount() == 0)
            loadItems();
            totalCostUpdate();
            budgetBarUpdate();
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
            menu.add(Menu.NONE, MENU_BUDGET, Menu.NONE, R.string.set_budget);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId()) {
                case MENU_DELETE:
                    mAdapter.clear();
                    totalCostUpdate();
                    budgetBarUpdate();
                    return true;
                case MENU_DUMP:
                    dump();
                    return true;
                case MENU_BUDGET:
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    AlertDialog dialog;

                    final EditText budgetEditText = new EditText(MainActivity.this);
                    budgetEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
                    //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
                    budgetEditText.setSelectAllOnFocus(true);
                    SharedPreferences settings = getSharedPreferences("preferences", MODE_PRIVATE);
                    if (!settings.getString("budget", "0").equals("0")) {
                        budgetEditText.setText(settings.getString("budget", "0"));
                    }
                    builder.setMessage(R.string.set_budget)
                            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    if(!budgetEditText.getText().toString().equals("")){
                                        double budget = Double.parseDouble(budgetEditText.getText().toString());
                                        if (budget>0){
                                            if ((int) Math.round(getTotalCost()*100/budget) <=100) {
                                                budgetBar.setProgress((int) Math.round(getTotalCost() * 100 / budget));
                                                percentView.setText(String.valueOf((int) Math.round(getTotalCost() * 100 / budget))+ "%");
                                                percentView.setTextColor(Color.BLACK);
                                            }
                                            else {
                                                budgetBar.setProgress(100);
                                                percentView.setText(String.valueOf((int) Math.round(getTotalCost() * 100 / budget))+ "%");
                                                percentView.setTextColor(Color.RED);
                                            }
                                        }
                                        else {
                                            budgetBar.setProgress(0);
                                            percentView.setText("0%");
                                            percentView.setTextColor(Color.BLACK);
                                        }
                                    }
                                    else {
                                        budgetBar.setProgress(0);
                                        percentView.setText("0%");
                                        percentView.setTextColor(Color.BLACK);
                                    }

                                    SharedPreferences settings = getSharedPreferences("preferences", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = settings.edit();
                                    editor.putString("budget", budgetEditText.getText().toString());
                                    editor.apply();
                                }
                            })
                            .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // User cancelled the dialog
                                }
                            })
                            .setView(budgetEditText);

                    // Create the AlertDialog object and return it
                    dialog = builder.create();
                    dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                    dialog.show();
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
                listView.setSelection(mAdapter.getCount());


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

            double total = 0;

            for (int i = 0; i < mAdapter.getCount(); i++) {
                   total = total+((Item) mAdapter.getItem(i)).getCost();
            }
            if (total == 0){
                summaryView.setText(new DecimalFormat("0.00").format(total));
            }
            else {
                summaryView.setText(new DecimalFormat("##.00").format(total));
            }
        }

        public double getTotalCost(){
            double total = 0;
            for (int i = 0; i < mAdapter.getCount(); i++) {
                total = total+((Item) mAdapter.getItem(i)).getCost();
            }
            return total;
        }

        public void budgetBarUpdate(){
            SharedPreferences settings = getSharedPreferences("preferences", MODE_PRIVATE);
            settings.getString("budget", "0");
            Double total = getTotalCost();
            //Toast.makeText(MainActivity.this, String.valueOf(total), Toast.LENGTH_SHORT).show();

            if(!settings.getString("budget", "0").equals("")){
                double budget = Double.parseDouble(settings.getString("budget", "0"));
                if (budget>0){
                    if ((int) Math.round(getTotalCost()*100/budget) <=100) {
                        budgetBar.setProgress((int) Math.round(getTotalCost() * 100 / budget));
                        percentView.setText(String.valueOf((int) Math.round(getTotalCost() * 100 / budget))+ "%");
                        percentView.setTextColor(Color.BLACK);

                }
                    else {
                        budgetBar.setProgress(100);
                        percentView.setText(String.valueOf((int) Math.round(getTotalCost() * 100 / budget))+ "%");
                        percentView.setTextColor(Color.RED);                    }
                }
                else {
                    budgetBar.setProgress(0);
                    percentView.setText("0%");
                    percentView.setTextColor(Color.BLACK);

                }
            }
            else {
                budgetBar.setProgress(0);
                percentView.setText("0%");
                percentView.setTextColor(Color.BLACK);
            }
        }
    }