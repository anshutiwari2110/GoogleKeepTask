package com.example.googlekeeptask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar mToolbar = findViewById(R.id.tl_navigation);
        setSupportActionBar(mToolbar);
        bottomNavigationView= findViewById(R.id.bottom_nav);
        drawerLayout = findViewById(R.id.drawer_layout);
  //      NavigationView navigationView = findViewById(R.id.navView);

        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this,drawerLayout,mToolbar,
                R.string.open,R.string.close);
        drawerToggle.syncState();
      //  navigationView.setNavigationItemSelectedListener(this);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        bottomNavigationView.getMenu().findItem(item.getItemId()).setChecked(true);
        if(item.getItemId() == R.id.action_checkbox){
            startActivity(new Intent(MainActivity.this, CheckboxNoteActivity.class));
        }
        return false;
    }

    /* @Override
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
    }*/
}
