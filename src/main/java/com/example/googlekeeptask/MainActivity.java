package com.example.googlekeeptask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

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

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    BottomNavigationView bottomNavigationView;
    RecyclerView mRcNoteList;
    DatabaseHelper dbHelper;
    FloatingActionButton mFABadd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar mToolbar = findViewById(R.id.tl_navigation);
        setSupportActionBar(mToolbar);
//        bottomNavigationView= findViewById(R.id.bottom_nav);
        drawerLayout = findViewById(R.id.drawer_layout);
        mRcNoteList = findViewById(R.id.rc_note_list);
        ImageView mAddCheckbox = findViewById(R.id.iv_add_checkbox);
        ImageView mAddPaint = findViewById(R.id.iv_add_paint);
        ImageView mAddAudio = findViewById(R.id.iv_add_audio);
        ImageView mAddPhoto = findViewById(R.id.ic_add_photo);
        mFABadd = findViewById(R.id.fab_add);

        mRcNoteList.setLayoutManager(new GridLayoutManager(this,2));
        dbHelper = new DatabaseHelper(this);

        NavigationView navigationView = findViewById(R.id.navView);

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
}
