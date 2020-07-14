package com.example.googlekeeptask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class CheckboxNoteActivity extends AppCompatActivity {
    private RelativeLayout mRlAddItem;
    private RelativeLayout mRlNote;
    private LinearLayout mLlDynamicLayout;
    private DatabaseHelper dbhelper;
    private NotesDatabaseHelper notedbhelper;
    private EditText mEtNotes;
    private ArrayList<Items> items;
    private ArrayList<Items> retrievedItems;
    private int row = 0;
    private int noteid = 0;
    private int flag = 0;
    private EditText mEtTitle;
    private Dialog dialog;


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
        mEtNotes = findViewById(R.id.et_notes);
        dbhelper = new DatabaseHelper(CheckboxNoteActivity.this);
        notedbhelper = new NotesDatabaseHelper(this);
//
//         Bundle data = getIntent().getExtras();
//        boolean isEdit = data.getBoolean("IS_EDIT");
//
//        RemainderItems editNotes = (RemainderItems) data.getSerializable("NOTES");
//
//        if(!isEdit){
//            mEtTitle.setText(editNotes.title);
//
//        }

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
                addToReminder();
                break;
            case R.id.action_done:
                onDoneClicked();
                noteid++;
                break;
        }
        return true;
    }
    //TODO add reminder Clicked dialog
    private void addToReminder() {

        dialog = new Dialog(CheckboxNoteActivity.this);
        dialog.setContentView(R.layout.reminder_dialog);
        dialog.show();
        final LinearLayout mLlreminderTime =dialog.findViewById(R.id.ll_reminder_time);
        final LinearLayout mLlreminderPlace =dialog.findViewById(R.id.ll_reminder_place);
        Button mDialogCancel = dialog.findViewById(R.id.btn_reminder_cancel);
        Button mDialogSave = dialog.findViewById(R.id.btn_reminder_save);
        RadioGroup mReminderRadio = dialog.findViewById(R.id.radiogrp_reminder);
        final RadioButton mRadioTime = dialog.findViewById(R.id.radio_time);
        RadioButton mRadioPlace = dialog.findViewById(R.id.radio_place);
        Spinner mDateSpinner = dialog.findViewById(R.id.sp_reminder_date);
        Spinner mTimeSpinner = dialog.findViewById(R.id.sp_reminder_time);


        // To set reminder according to time or place
        mReminderRadio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.radio_time){
                    mLlreminderTime.setVisibility(View.VISIBLE);
                    mLlreminderPlace.setVisibility(View.GONE);
                    Toast.makeText(CheckboxNoteActivity.this, "Reminder using time", Toast.LENGTH_SHORT).show();
                }else{
                    mLlreminderTime.setVisibility(View.GONE);
                    mLlreminderPlace.setVisibility(View.VISIBLE);
                    Toast.makeText(CheckboxNoteActivity.this, "Reminder using place", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this,R.array.reminder_date,android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mDateSpinner.setAdapter(arrayAdapter);


        ArrayAdapter<CharSequence> timeAdapter = ArrayAdapter.createFromResource(this,R.array.reminder_time,android.R.layout.simple_spinner_item);
        timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mTimeSpinner.setAdapter(timeAdapter);

        mDialogCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    //To enter data in database
    private void onDoneClicked() {

//       if(flag == 1)
//       {
           if (items.size() > 0) {


               //insertion method
               String title = mEtTitle.getText().toString();
               String itemString = RemainderItems.convertItemsListToString(items);
               RemainderItems notes = new RemainderItems();

               notes.title = title;
               notes.items = itemString;

               dbhelper.insertDataToDatabase(notes, dbhelper.getWritableDatabase());

               Toast.makeText(CheckboxNoteActivity.this, "Retrieved Item Size = " + retrievedItems.size(), Toast.LENGTH_SHORT).show();
       }

       // }

//       else{
//           String title = mEtTitle.getText().toString();
//           String content = mEtNotes.getText().toString();
//           RemainderItems newContent = new RemainderItems();
//           newContent.title = title;
//           newContent.items = content;
//           notedbhelper.insertNote(newContent,notedbhelper.getWritableDatabase());
//       }

    }

    //When AddCheckbox button is Clicked
    public void checkboxaddClicked(View view) {
        flag++;
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
                mEtListItem.setEnabled(true);
                mRlAddItem.setEnabled(true);
                mIvAddItem.setVisibility(View.INVISIBLE);

                Items cbitem = new Items();
                cbitem.itemId = row;
                cbitem.itemName = mEtListItem.getText().toString();
                cbitem.isChecked = false;
                items.add(cbitem);

                Toast.makeText(CheckboxNoteActivity.this, "Items Size = " + items.size()
                        + "Retrieved Items = " + retrievedItems.size(), Toast.LENGTH_SHORT).show();
            }
        });


        mLlDynamicLayout.addView(view);
    }


}
