package com.example.firstsqlite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.firstsqlite.adapters.DBHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddActivity extends AppCompatActivity {

    DBHelper dbHelper;
    TextView TvStatus;
    Button BtnProses;
    EditText TxtID,TxtNama,TxtJudul,TxtTglPinjam,TxtTglKembali,TxtStatus;
    long id;
    DatePickerDialog datePickerDialog;
    SimpleDateFormat dateFormatter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        dbHelper = new DBHelper(this);

        id = getIntent().getLongExtra(DBHelper.row_id,0);

        TxtID = findViewById(R.id.txtID);
        TxtNama = findViewById(R.id.txtNamaAnggota);
        TxtJudul = findViewById(R.id.txtJudulBuku);
        TxtTglPinjam = findViewById(R.id.txtTglPinjam);
        TxtTglKembali = findViewById(R.id.txtTglKembali);
        TxtStatus = findViewById(R.id.txtStatus);

        TvStatus = findViewById(R.id.tvStatus);
        BtnProses = findViewById(R.id.btnProses);

        dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);

        getData();

        TxtTglKembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateDialog();
            }
        });

        BtnProses.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                prosesKembali();
            }
        });
        ActionBar menu = getSupportActionBar();
        menu.setDisplayShowHomeEnabled(true);
        menu.setDisplayHomeAsUpEnabled(true);

    }

    private void prosesKembali() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(AddActivity.this);
        builder.setMessage("Proses Pengembalian Buku ?");
        builder.setCancelable(true);
        builder.setPositiveButton("Proses", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String idpinjam = TxtID.getText().toString().trim();
                String kembali = "Dikembalikan";

                ContentValues values = new ContentValues();

                values.put(DBHelper.row_status,kembali);
                dbHelper.updateData(values,id);
                Toast.makeText(AddActivity.this,"Proses Pengembalian Berhasil",Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        builder.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showDateDialog() {
        Calendar calendar = Calendar.getInstance();

        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, month, dayOfMonth);
                TxtTglKembali.setText(dateFormatter.format(newDate.getTime()));

            }
        },calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void getData() {
        Calendar cl = Calendar.getInstance();
        SimpleDateFormat sdfl = new SimpleDateFormat("dd/MM/yyyy",Locale.US);
        String tglPinjam = sdfl.format(cl.getTime());
        TxtTglPinjam.setText(tglPinjam);

        Cursor cur = dbHelper.oneData(id);
        if(cur.moveToFirst()){
            String idpinjam = cur.getString(cur.getColumnIndex(DBHelper.row_id));
            String nama  = cur.getString(cur.getColumnIndex(DBHelper.row_nama));
            String judul = cur.getString(cur.getColumnIndex(DBHelper.row_judul));
            String pinjam  = cur.getString(cur.getColumnIndex(DBHelper.row_pinjam));
            String kembali = cur.getString(cur.getColumnIndex(DBHelper.row_kembali));
            String status = cur.getString(cur.getColumnIndex(DBHelper.row_status));

            TxtID.setText(idpinjam);
            TxtNama.setText(nama);
            TxtJudul.setText(judul);
            TxtTglPinjam.setText(pinjam);
            TxtTglKembali.setText(kembali);
            TxtStatus.setText(status);

            if(TxtID.equals("")){
                TvStatus.setVisibility(View.GONE);
                TxtStatus.setVisibility(View.GONE);
                BtnProses.setVisibility(View.GONE);
            }else{
                TvStatus.setVisibility(View.VISIBLE);
                TxtStatus.setVisibility(View.VISIBLE);
                BtnProses.setVisibility(View.VISIBLE);
            }

            if(status.equals("Dipinjam")){
                BtnProses.setVisibility(View.VISIBLE);
            }else{
                BtnProses.setVisibility(View.GONE);
                TxtNama.setEnabled(false);
                TxtJudul.setEnabled(false);
                TxtTglKembali.setEnabled(false);
                TxtStatus.setEnabled(false);
            }

        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_menu, menu);
        String idpinjam = TxtID.getText().toString().trim();
        String status = TxtStatus.getText().toString().trim();

        MenuItem itemDelete = menu.findItem(R.id.action_delete);
        MenuItem itemClear = menu.findItem(R.id.action_clear);
        MenuItem itemSave = menu.findItem(R.id.action_save);

        if (idpinjam.equals("")){
            itemDelete.setVisible(false);
            itemClear.setVisible(true);
        }else{
            itemDelete.setVisible(true);
            itemClear.setVisible(false);
        }

        if (status.equals("Dikembalikan")){
            itemSave.setVisible(false);
            itemClear.setVisible(false);
            itemDelete.setVisible(true);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_save:
                insertAndUpdate();
        }
        switch (item.getItemId()){
            case R.id.action_clear:
                TxtNama.setText("");
                TxtJudul.setText("");
                TxtTglKembali.setText("");
        }
        switch (item.getItemId()){
            case R.id.action_delete:
                final AlertDialog.Builder builder = new AlertDialog.Builder(AddActivity.this);
                builder.setMessage("Data ini Akan dihapus");
                builder.setCancelable(true);
                builder.setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dbHelper.deleteData(id);
                        Toast.makeText(AddActivity.this,"Terhapus",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
                builder.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
        }

        return super.onOptionsItemSelected(item);
    }

    public void insertAndUpdate(){
        String idpinjam = TxtID.getText().toString().trim();
        String nama = TxtNama.getText().toString().trim();
        String judul = TxtJudul.getText().toString().trim();
        String tglpinjam = TxtTglPinjam.getText().toString().trim();
        String tglkembali = TxtTglKembali.getText().toString().trim();
        String status = "Dipinjam";

        ContentValues values = new ContentValues();

        values.put(DBHelper.row_nama,nama);
        values.put(DBHelper.row_judul,judul);
        values.put(DBHelper.row_kembali,tglkembali);
        values.put(DBHelper.row_status,status);

        if(nama.equals("") || judul.equals("") || tglkembali.equals("")){
            Toast.makeText(AddActivity.this,"Isi Data Dengan Lengkap", Toast.LENGTH_SHORT).show();
        }else{
            if(idpinjam.equals("")){
                values.put(DBHelper.row_pinjam,tglpinjam);
                dbHelper.insertData(values);
            }else{
                dbHelper.updateData(values,id);
            }

            Toast.makeText(AddActivity.this,"Data Tersimpan", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

}