package com.example.googlekeeptask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, CheckBoxAdapter.NotesClickListener {

    DrawerLayout drawerLayout;
    RecyclerView mRcNoteList;
    DatabaseHelper dbHelper;
    FloatingActionButton mFABadd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar mToolbar = findViewById(R.id.tl_navigation);
        setSupportActionBar(mToolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        mRcNoteList = findViewById(R.id.rc_note_list);
        ImageView mAddCheckbox = findViewById(R.id.iv_add_checkbox);
        ImageView mAddPaint = findViewById(R.id.iv_add_paint);
        ImageView mAddAudio = findViewById(R.id.iv_add_audio);
        ImageView mAddPhoto = findViewById(R.id.ic_add_photo);
        mFABadd = findViewById(R.id.fab_add);

      //  mRcNoteList.setLayoutManager(new GridLayoutManager(this,2));
        mRcNoteList.setLayoutManager(new StaggeredGridLayoutManager(2,RecyclerView.VERTICAL));
        dbHelper = new DatabaseHelper(this);
        getDataFromDatabase();
        NavigationView navigationView = findViewById(R.id.navView);
        View headerView = navigationView.inflateHeaderView(R.layout.drawer_header);

        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this,drawerLayout,mToolbar,
                R.string.open,R.string.close);
        drawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        mFABadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,CheckboxNoteActivity.class));
            }
        });

    }

    private void getDataFromDatabase() {
        ArrayList<RemainderItems> notes = dbHelper.getDataFromDatabase(dbHelper.getReadableDatabase());

        CheckBoxAdapter adapter;
        adapter = new CheckBoxAdapter(this,notes);
        mRcNoteList.setAdapter(adapter);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
       drawerLayout.closeDrawer(GravityCompat.START);
        switch (item.getItemId()){
            case R.id.action_note:
                startActivity(new Intent(this,MainActivity.class));
                break;
            case R.id.action_reminders:
                Toast.makeText(MainActivity.this, "RemainderClicked", Toast.LENGTH_SHORT).show();
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

        }


        return false;
    }

    public void onAddCheckboxClicked(View view){
        startActivity(new Intent(MainActivity.this,CheckboxNoteActivity.class));
    }


   /* @Override
    public void onCardClicked(RemainderItems notes) {
        Intent viewCardIntent = new Intent(MainActivity.this,CheckboxNoteActivity.class);
        viewCardIntent.putExtra("NOTES", notes);
        viewCardIntent.putExtra("IS_EDIT", true);
        startActivityForResult(viewCardIntent,1001);*/

   // }

    @Override
    public void onDeleteClicked(RemainderItems notes) {
        dbHelper.deleteDataFromDatabase(notes,dbHelper.getWritableDatabase());
        getDataFromDatabase();
    }


/*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1001 && resultCode == Activity.RESULT_OK){
            getDataFromDatabase();
        }
    }*/
}

