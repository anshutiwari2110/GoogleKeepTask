package com.example.googlekeeptask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, BottomNavigationView.OnNavigationItemSelectedListener,CheckBoxAdapter.NotesClickListener {

    DrawerLayout drawerLayout;
    RecyclerView mRcNoteList;
    DatabaseHelper dbHelper;
    NotesDatabaseHelper noteDBhelper;
    FloatingActionButton mFABadd;
    BottomNavigationView dashboardBottomNav;
    NavigationView navigationView;
    ImageButton mImgBtnLinear;
    ImageButton mImgBtnGrid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar mToolbar = findViewById(R.id.tl_navigation);
        setSupportActionBar(mToolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        mRcNoteList = findViewById(R.id.rc_note_list);
        mImgBtnLinear = findViewById(R.id.ib_linear_view);
        mImgBtnGrid = findViewById(R.id.ib_grid_view);
        mFABadd = findViewById(R.id.fab_add);



        dbHelper = new DatabaseHelper(this);
        getDataFromDatabase();
        navigationView = findViewById(R.id.navView);
        dashboardBottomNav = findViewById(R.id.dashboard_bottom_nav);
        dashboardBottomNav.setOnNavigationItemSelectedListener(this);
        View headerView = navigationView.inflateHeaderView(R.layout.drawer_header);

        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, mToolbar,
                R.string.open, R.string.close);
        drawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        mFABadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CheckboxNoteActivity.class));
            }
        });
        getDataFromDatabase();
    }

    private void getDataFromDatabase() {
        ArrayList<RemainderItems> notes = dbHelper.getDataFromDatabase(dbHelper.getReadableDatabase());
//        ArrayList<RemainderItems> newNotes = noteDBhelper.getNote(noteDBhelper.getReadableDatabase());

        CheckBoxAdapter adapter;
        adapter = new CheckBoxAdapter(this, notes);
        mRcNoteList.setLayoutManager(new StaggeredGridLayoutManager(2, RecyclerView.VERTICAL));
        adapter.setListener(this);
        mRcNoteList.setAdapter(adapter);
    }

    //DrawerNavigation

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawerLayout.closeDrawer(GravityCompat.START);
        dashboardBottomNav.getMenu().findItem(item.getItemId()).setChecked(true);
        switch (item.getItemId()) {
            case R.id.action_note:
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.action_reminders:
                startActivity(new Intent(MainActivity.this,ReminderActivity.class));
                break;
            case R.id.action_create_new_label:
                Toast.makeText(MainActivity.this, "Create New Label", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_archive:
                Toast.makeText(MainActivity.this, "ArchiveClicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_delete:
                Toast.makeText(MainActivity.this, "DeleteClicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_setting:
                Toast.makeText(MainActivity.this, "SettingClicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_help:
                Toast.makeText(MainActivity.this, "HelpClicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_add_new_list:
                startActivity(new Intent(MainActivity.this, CheckboxNoteActivity.class));
                break;

        }


        return false;
    }



    @Override
    public void onTaskUpdate(RemainderItems remainderItems, Items items, boolean checked) {
        ArrayList<Items> itemsArrayList = RemainderItems.convertItemsStringToArrayList(remainderItems.items);
        for(Items itemsObj : itemsArrayList){
            if(itemsObj.itemId == items.itemId){
                itemsObj.isChecked = checked;
            }
        }
        String itemArrayValue = RemainderItems.convertItemsListToString(itemsArrayList);
        RemainderItems updatedNotes = new RemainderItems();
        updatedNotes.id = remainderItems.id;
        updatedNotes.items = remainderItems.items;
        updatedNotes.title = remainderItems.title;

        dbHelper.updateDataToDatabase(updatedNotes,dbHelper.getWritableDatabase());
        getDataFromDatabase();
    }

    @Override
    public void onDeleteClicked(RemainderItems notes) {
        dbHelper.deleteDataFromDatabase(notes,dbHelper.getWritableDatabase());
        getDataFromDatabase();
    }


//
//        @Override
//        protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//            super.onActivityResult(requestCode, resultCode, data);
//            if(requestCode == 1001 && resultCode == Activity.RESULT_OK){
//                getDataFromDatabase();
//            }
//        }

    public void onChangeToLinearView(View view) {
        mImgBtnGrid.setVisibility(View.VISIBLE);
        mImgBtnLinear.setVisibility(View.GONE);
        mRcNoteList.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));
    }

    public void onChangeToGridView(View view) {
        mImgBtnGrid.setVisibility(View.GONE);
        mImgBtnLinear.setVisibility(View.VISIBLE);
        mRcNoteList.setLayoutManager(new StaggeredGridLayoutManager(2, RecyclerView.VERTICAL));
    }


}

