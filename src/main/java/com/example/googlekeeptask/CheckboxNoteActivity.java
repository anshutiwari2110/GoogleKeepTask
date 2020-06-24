package com.example.googlekeeptask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;

public class CheckboxNoteActivity extends AppCompatActivity {
    RelativeLayout mRlAddItem;
    RelativeLayout mRlNote;


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
        Toolbar mCBtoolbar = findViewById(R.id.tl_cb_toolbar);
        ImageButton mAddCheckbox = findViewById(R.id.ib_checkbox);
        setSupportActionBar(mCBtoolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
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

    public void addIteminLayoutClicked(View view){
        addNewListItem();
    }

    private void addNewListItem() {
        mRlAddItem.setEnabled(false);
        View view = LayoutInflater.from(CheckboxNoteActivity.this).inflate(R.layout.cell_item_edit,null);
        CheckBox checkBox = view.findViewById(R.id.chk_edit_item);
        final EditText mEtListItem = view.findViewById(R.id.et_edit_item);
        final ImageView mIvCancelItem = view.findViewById(R.id.iv_cancel_item);

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
                    mIvCancelItem.setVisibility(View.VISIBLE);
                }else{
                    mIvCancelItem.setVisibility(View.INVISIBLE);
                }

            }
        });
    }
}
