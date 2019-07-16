package com.leoprananta.crudmysql;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

public class canread extends AppCompatActivity implements View.OnClickListener{

    EditText etaidi, etnama, etalamat, etpacar;
    Button btnedit, btnhps;

    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canread);

        Intent intent = getIntent();

        id = intent.getStringExtra(konfigurasi.EMP_ID);

        etaidi   = findViewById(R.id.etaidi);
        etnama   = findViewById(R.id.etnama);
        etalamat = findViewById(R.id.etalamat);
        etpacar  = findViewById(R.id.etpacar);

        btnedit  = findViewById(R.id.btnedit);
        btnhps   = findViewById(R.id.btnhps);

        btnedit.setOnClickListener(this);
        btnhps.setOnClickListener(this);

        etaidi.setText(id);

        getEmployee();
    }

    private void getEmployee(){
        class GetEmployee extends AsyncTask<Void, Void, String>{
            ProgressDialog loading;

            @Override
            protected void onPreExecute(){
                super.onPreExecute();
                loading = ProgressDialog.show(canread.this,"Fetching...", "Wait...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                showEmployee(s);
            }

            @Override
            protected String doInBackground(Void... params){
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(konfigurasi.URL_GET_EMP, id);
                return s;
            }
        }
        GetEmployee ge = new GetEmployee();
        ge.execute();
    }

    private void showEmployee(String json){
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray result = jsonObject.getJSONArray(konfigurasi.TAG_JSON_ARRAY);
            JSONObject c = result.getJSONObject(0);
            String nama = c.getString(konfigurasi.TAG_NAMA);
            String alamat = c.getString(konfigurasi.TAG_ALAMAT);
            String pacar = c.getString(konfigurasi.TAG_PACAR);

            etnama.setText(nama);
            etalamat.setText(alamat);
            etpacar.setText(pacar);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void updateEmployee(){
        final String nama = etnama.getText().toString().trim();
        final String alamat = etalamat.getText().toString().trim();
        final String pacar = etpacar.getText().toString().trim();

        class UpdateEmployee extends AsyncTask<Void, Void, String>{
            ProgressDialog loading;

            @Override
            protected void onPreExecute(){
                super.onPreExecute();
                loading = ProgressDialog.show(canread.this,"Updating...", "Wait...", false, false);
            }

            @Override
            protected void onPostExecute(String s){
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(canread.this, s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... params){
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(konfigurasi.KEY_EMP_ID, id);
                hashMap.put(konfigurasi.KEY_EMP_NAMA, nama);
                hashMap.put(konfigurasi.KEY_EMP_ALAMAT, alamat);
                hashMap.put(konfigurasi.KEY_EMP_PACAR, pacar);

                RequestHandler rh = new RequestHandler();

                String s = rh.sendPostRequest(konfigurasi.URL_UPDATE_EMP, hashMap);

                return s;
            }
        }
        UpdateEmployee ue = new UpdateEmployee();
        ue.execute();
    }

    private void deleteEmployee(){
        class DeleteEmployee extends AsyncTask<Void, Void, String>{
            ProgressDialog loading;

            @Override
            protected void onPreExecute(){
                super.onPreExecute();
                loading = ProgressDialog.show(canread.this, "Updating...", "Tunggu...", false, false);
            }

            @Override
            protected void onPostExecute(String s){
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(canread.this, s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... params){
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(konfigurasi.URL_DELETE_EMP, id);
                return s;
            }
        }
        DeleteEmployee de = new DeleteEmployee();
        de.execute();
    }

    private void confirmDeleteEmployee(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Apakah Kamu Yakin Menghapus Teman ? :v");

        alertDialogBuilder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deleteEmployee();
                startActivity(new Intent(canread.this, tampil.class));
            }
        });

        alertDialogBuilder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void onClick(View v){
        if(v == btnedit){
            updateEmployee();
        }
        if(v == btnhps){
            confirmDeleteEmployee();
        }
    }
}
