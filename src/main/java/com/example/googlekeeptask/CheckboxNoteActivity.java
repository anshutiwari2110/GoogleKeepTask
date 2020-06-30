package com.example.googlekeeptask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class CheckboxNoteActivity<isEdit> extends AppCompatActivity {
    private RelativeLayout mRlAddItem;
    private RelativeLayout mRlNote;
    private LinearLayout mLlDynamicLayout;
    private DatabaseHelper dbhelper;
    private ArrayList<Items> items;
    private ArrayList<Items> retrievedItems;
    private int row = 0;
    private int noteid = 0;
    private EditText mEtTitle;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkbox_note);

        //To add date use dd/mm/yyyy in SimpleDateFormat
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                TextView tdate = (TextView) findViewById(R.id.tv_date);
                                long date = System.currentTimeMillis();
                                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
                                String time = "Edited " + sdf.format(date);
                                tdate.setText(time);
                            }
                        });
                    }

                } catch (InterruptedException e) {
                }
            }
        };
        thread.start();
        //Function used to add time in TextView

        mRlAddItem = findViewById(R.id.rl_add_item);
        mRlNote = findViewById(R.id.rl_note);
        mLlDynamicLayout = findViewById(R.id.ll_dynamic_holder);
        Toolbar mCBtoolbar = findViewById(R.id.tl_cb_toolbar);
        ImageButton mAddCheckbox = findViewById(R.id.ib_checkbox);

        mEtTitle = findViewById(R.id.et_title);
        dbhelper = new DatabaseHelper(CheckboxNoteActivity.this);
     /*   Bundle data = getIntent().getExtras();
        isEdit = data.getBoolean("IS_EDIT");
        editNotes = (RemainderItems) data.getSerializable("NOTES");

        if(isEdit){
            mEtTitle.setText(editNotes.title);
            mEtListItem.setText(editNotes.items);
        }*/

        setSupportActionBar(mCBtoolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        items = new ArrayList<>();
        retrievedItems = new ArrayList<>();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.checkbox_tool_menu, menu);
        return true;
    }
    // Todo toolbar option switch case

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(CheckboxNoteActivity.this, MainActivity.class));
                break;
            case R.id.action_pin:
                startActivity(new Intent(CheckboxNoteActivity.this, MainActivity.class));
                break;
            case R.id.action_add_reminder:
                Toast.makeText(CheckboxNoteActivity.this, "Remainder is pressed", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_done:
                onDoneClicked();
                noteid++;
                break;
        }
        return true;
    }

    //To enter data in database
    private void onDoneClicked() {

        if (items.size() > 0) {
            JSONArray itemsArray = new JSONArray();
            for (Items cbitem : items) {
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("itemid", cbitem.itemId);
                    jsonObject.put("itemname", cbitem.itemName);
                    jsonObject.put("ischecked", cbitem.isChecked);
                    itemsArray.put(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


            String itemArrayValue = itemsArray.toString();

            Log.i("JSON Array", itemsArray.toString());
            try {
                JSONArray retrieveItemArray = new JSONArray(itemArrayValue);
                for (int i = 0; i < retrieveItemArray.length(); i++) {
                    JSONObject currentObject = retrieveItemArray.getJSONObject(i);

                    Items retrievedItem = new Items();
                    retrievedItem.itemId = currentObject.optInt("itemid");
                    retrievedItem.itemName = currentObject.optString("itemname");
                    retrievedItem.isChecked = currentObject.optBoolean("ischecked");

                    retrievedItems.add(retrievedItem);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Toast.makeText(CheckboxNoteActivity.this, "Retrieved Item Size = " + retrievedItems.size(), Toast.LENGTH_SHORT).show();
            //insertion method
            String title = mEtTitle.getText().toString();
            RemainderItems notes = new RemainderItems();
            notes.id = noteid;
            notes.title = title;
            notes.items = itemArrayValue;

                dbhelper.insertDataToDatabase(notes, dbhelper.getWritableDatabase());
            /*
                dbhelper.updateDataToDatabase(notes,dbhelper.getWritableDatabase());
                setResult(Activity.RESULT_OK);
                finish();
            */
        }

    }

    //When AddCheckbox button is Clicked
    public void checkboxaddClicked(View view) {
        mRlNote.setVisibility(View.GONE);
        mRlAddItem.setVisibility(View.VISIBLE);
    }

    //For Adding Cell to the relative layout

    public void addNewItemClicked(View view) {
//        Toast.makeText(CheckboxNoteActivity.this, "RelativeLayout Clicked Successfully", Toast.LENGTH_SHORT).show();
        addNewListItem();
    }

    //For adding in each cell
    private void addNewListItem() {

        View view = LayoutInflater.from(CheckboxNoteActivity.this).inflate(R.layout.cell_item_edit, null);
        final CheckBox checkBox = view.findViewById(R.id.chk_edit_item);
        final EditText mEtListItem = view.findViewById(R.id.et_edit_item);
        final ImageView mIvAddItem = view.findViewById(R.id.iv_add_item);

        mEtListItem.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 1) {
                    mIvAddItem.setVisibility(View.VISIBLE);
                } else {
                    mIvAddItem.setVisibility(View.INVISIBLE);
                }

            }
        });
        //Successfully insert an item
        mIvAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEtListItem.setEnabled(false);
                mRlAddItem.setEnabled(true);
                mIvAddItem.setVisibility(View.INVISIBLE);

                Items cbitem = new Items();
                cbitem.itemId = row;
                cbitem.itemName = mEtListItem.getText().toString();
                cbitem.isChecked = checkBox.isChecked();
                items.add(cbitem);

                Toast.makeText(CheckboxNoteActivity.this, "Items Size = " + items.size()
                        + "Retrieved Items = " + retrievedItems.size(), Toast.LENGTH_SHORT).show();
            }
        });


        mLlDynamicLayout.addView(view);
    }
}
