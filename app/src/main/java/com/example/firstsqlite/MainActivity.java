package com.example.firstsqlite;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import com.example.firstsqlite.adapters.CustomCursorAdapter;
import com.example.firstsqlite.adapters.DBHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ListView Is;
    DBHelper dbHelper;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AddActivity.class));
            }
        });

        dbHelper = new DBHelper(this);
        Is = (ListView)findViewById(R.id.list_pinjam);
        Is.setOnItemClickListener(this);

        setupListView();
    }

    private void setupListView() {
        Cursor cursor = dbHelper.allData();
        CustomCursorAdapter customCursorAdapter = new CustomCursorAdapter(this,cursor,1);
        Is.setAdapter(customCursorAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long I) {
        TextView getID = findViewById(R.id.listID);
        final long id = Long.parseLong(getID.getText().toString());
        Cursor cur = dbHelper.oneData(id);
        cur.moveToFirst();

        Intent idpinjam = new Intent(MainActivity.this, AddActivity.class);
        idpinjam.putExtra(DBHelper.row_id,id);
        startActivity(idpinjam);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupListView();
    }
}