package com.example.googlekeeptask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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

public class CheckboxNoteActivity extends AppCompatActivity {
    RelativeLayout mRlAddItem;
    RelativeLayout mRlNote;
    LinearLayout mLlDynamicLayout;
    private ArrayList<Items> items;
    private ArrayList<Items> retrievedItems;
    private int row = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkbox_note);

//Todo to add date use dd/mm/yyyy in SimpleDateFormat
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                JSONArray itemsArray = new JSONArray();
                for ( Items cbitem : items){
                    try {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("itemid",cbitem.itemId);
                        jsonObject.put("itemname",cbitem.itemName);
                        jsonObject.put("ischecked",cbitem.isChecked);
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }

                String itemArrayValue = itemsArray.toString();
                Log.i("JSON Array", itemsArray.toString());
                try {
                    JSONArray retrieveItemArray = new JSONArray(itemArrayValue);
                    for (int i = 0;i < retrieveItemArray.length();i++){
                        JSONObject currentObject = retrieveItemArray.getJSONObject(i);
                        Items retrievedItem  = new Items();
                        retrievedItem.itemId = currentObject.optInt("itemid");
                        retrievedItem.itemName = currentObject.optString("itemname");
                        retrievedItem.isChecked = currentObject.optBoolean("ischecked");

                        retrievedItems.add(retrievedItem);
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
                Toast.makeText(CheckboxNoteActivity.this, "Retrieved Item Size"+retrievedItems.size(), Toast.LENGTH_SHORT).show();
                onBackPressed();
                break;
            case R.id.action_pin:
                startActivity(new Intent(CheckboxNoteActivity.this, MainActivity.class));
                break;
            case R.id.action_add_reminder:
                Toast.makeText(CheckboxNoteActivity.this, "Remainder is pressed", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }
//Todo when AddCheckbox button is Clicked
    public void checkboxaddClicked(View view) {
        mRlNote.setVisibility(View.GONE);
        mRlAddItem.setVisibility(View.VISIBLE);
    }

    //For Adding Cell to the relative layout

    public void addNewItemClicked(View view){
//        Toast.makeText(CheckboxNoteActivity.this, "RelativeLayout Clicked Successfully", Toast.LENGTH_SHORT).show();
        addNewListItem();
    }

    private void addNewListItem() {
        mRlAddItem.setEnabled(false);
        View view = LayoutInflater.from(CheckboxNoteActivity.this).inflate(R.layout.cell_item_edit,null);
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
                if(s.length() > 1){
                    mIvAddItem.setVisibility(View.VISIBLE);
                }else{
                    mIvAddItem.setVisibility(View.INVISIBLE);
                }

            }
        });

        mIvAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEtListItem.setEnabled(false);
                mRlAddItem.setEnabled(true);
                mIvAddItem.setVisibility(View.INVISIBLE);

                Items cbitem = new Items();
                cbitem.itemId = row ;
                cbitem.itemName = mEtListItem.getText().toString();
                cbitem.isChecked = false;
                items.add(cbitem);
            }
        });


        mLlDynamicLayout.addView(view);
    }
}
