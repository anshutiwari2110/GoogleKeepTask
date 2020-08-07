package com.example.googlekeeptask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, BottomNavigationView.OnNavigationItemSelectedListener, CheckBoxAdapter.NotesClickListener {

    DrawerLayout drawerLayout;
    RecyclerView mRcNoteList;
    DatabaseHelper dbHelper;
    NotesDatabaseHelper noteDBhelper;
    FloatingActionButton mFABadd;
    BottomNavigationView dashboardBottomNav;
    NavigationView navigationView;
    ImageButton mImgBtnLinear;
    LinearLayout mLlHome;
    ImageButton mImgBtnGrid;
    private Dialog imageDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Permission for the Application
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000);
            }
        }

        Toolbar mToolbar = findViewById(R.id.tl_navigation);
        setSupportActionBar(mToolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        mRcNoteList = findViewById(R.id.rc_note_list);
        mImgBtnLinear = findViewById(R.id.ib_linear_view);
        mImgBtnGrid = findViewById(R.id.ib_grid_view);
        mFABadd = findViewById(R.id.fab_add);
        mLlHome = findViewById(R.id.ll_home);


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
                startActivity(new Intent(MainActivity.this, ReminderActivity.class));
                break;
            case R.id.action_create_new_label:
                Snackbar snackbar = Snackbar
                        .make(mLlHome, "Create new label is clicked", Snackbar.LENGTH_LONG);
                snackbar.show();
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
            case R.id.action_add_new_photo:
                addImageNote();

        }


        return false;
    }

    private void addImageNote() {
        imageDialog = new Dialog(MainActivity.this);
        imageDialog.setContentView(R.layout.photo_dailog);
        imageDialog.show();

        RelativeLayout mRlTakePhoto = imageDialog.findViewById(R.id.rl_take_photo);
        RelativeLayout mRlChooseImage = imageDialog.findViewById(R.id.rl_choose_photo);

        mRlTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Camera Popup", Toast.LENGTH_SHORT).show();
                startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE), 100);
                imageDialog.cancel();
            }
        });

        mRlChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readImageFromDevice();
                Toast.makeText(MainActivity.this, "Choose Image ", Toast.LENGTH_SHORT).show();
                imageDialog.cancel();
            }
        });

    }


    @Override
    public void onTaskUpdate(RemainderItems remainderItems, Items items, boolean checked) {
        ArrayList<Items> itemsArrayList = RemainderItems.convertItemsStringToArrayList(remainderItems.items);
        for (Items itemsObj : itemsArrayList) {
            if (itemsObj.itemId == items.itemId) {
                itemsObj.isChecked = checked;
            }
        }
        String itemArrayValue = RemainderItems.convertItemsListToString(itemsArrayList);
        RemainderItems updatedNotes = new RemainderItems();
        updatedNotes.id = remainderItems.id;
        updatedNotes.items = remainderItems.items;
        updatedNotes.title = remainderItems.title;

        dbHelper.updateDataToDatabase(updatedNotes, dbHelper.getWritableDatabase());
        getDataFromDatabase();
    }

    @Override
    public void onDeleteClicked(RemainderItems notes) {
        dbHelper.deleteDataFromDatabase(notes, dbHelper.getWritableDatabase());
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
        mRcNoteList.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
    }

    public void onChangeToGridView(View view) {
        mImgBtnGrid.setVisibility(View.GONE);
        mImgBtnLinear.setVisibility(View.VISIBLE);
        mRcNoteList.setLayoutManager(new StaggeredGridLayoutManager(2, RecyclerView.VERTICAL));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            Bitmap image = (Bitmap) data.getExtras().get("data");
            saveImageToDirectory(image);

        }
    }

    private void saveImageToDirectory(Bitmap image) {
        File directory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getPath(), "Google Keep Task");
        if (!directory.exists()) {
            directory.mkdirs();
        }
        File imageName = new File(directory, "GKT_" + System.currentTimeMillis() + ".png");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(imageName);
            image.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    private void readImageFromDevice() {
        Uri imageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Images.Media.DATA};
        ArrayList<String> images = new ArrayList<>();

        String[] albumProj = {MediaStore.Images.Media._ID,
                MediaStore.Images.Media.BUCKET_ID,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME};
        ArrayList<GalleryPicker> galleryPickerArrayList = new ArrayList<>();
        ArrayList<String> galleryName = new ArrayList<>();

        Cursor albumCursor = getApplication().getContentResolver().query(imageUri,albumProj,null,null,null);
        for (albumCursor.moveToFirst();!albumCursor.isAfterLast();albumCursor.moveToNext()){
            String albumName = albumCursor.getString(albumCursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME));
            String albumID = albumCursor.getString(albumCursor.getColumnIndex(MediaStore.Images.Media._ID));

            Log.i("Album Name"+albumName,"Album ID" + albumID);

            if (!galleryName.contains(albumName)){
                GalleryPicker galleryPicker = new GalleryPicker();
                galleryPicker.albumID = albumID;
                galleryPicker.albumName = albumName;
                galleryPickerArrayList.add(galleryPicker);
                galleryName.add(albumName);
            }
        }
        String[] selectionID = new String[1];
        selectionID[0] = "6006";


        Cursor cursor = getApplicationContext().getContentResolver().query(imageUri, projection, MediaStore.Images.Media.BUCKET_ID+"=?", selectionID, null);

        if (cursor != null) {
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                String image = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
                images.add(image);
            }
            Toast.makeText(MainActivity.this, "Total Images " + images.size(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1000) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                addImageNote();
            } else {
                Toast.makeText(MainActivity.this, "User Denied Permission", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

