package com.leoprananta.crudmysql;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.view.View;
import android.widget.Toast;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    EditText etnama, etalamat, etpacar;
    Button btnadd, btnedit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etnama = findViewById(R.id.etnama);
        etalamat = findViewById(R.id.etalamat);
        etpacar = findViewById(R.id.etpacar);

        btnadd = findViewById(R.id.btnadd);
        btnedit = findViewById(R.id.btnedit);

        btnadd.setOnClickListener(this);
        btnedit.setOnClickListener(this);
    }

    private void addEmployee(){
        final String nama = etnama.getText().toString().trim();
        final String alamat = etalamat.getText().toString().trim();
        final String pacar = etpacar.getText().toString().trim();

        class AddEmployee extends AsyncTask<Void, Void, String>{

            ProgressDialog loading;


            @Override
            protected void onPreExecute(){
                super.onPreExecute();
                loading = ProgressDialog.show(MainActivity.this,"Menambahkan...", "Tunggu...", false, false);
            }

            @Override
            protected void onPostExecute(String s){
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(MainActivity.this, s, Toast.LENGTH_LONG).show();
            }

            protected String doInBackground(Void... v){
                HashMap<String, String> params = new HashMap<>();
                params.put(konfigurasi.KEY_EMP_NAMA, nama);
                params.put(konfigurasi.KEY_EMP_ALAMAT, alamat);
                params.put(konfigurasi.KEY_EMP_PACAR, pacar);

                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(konfigurasi.URL_ADD, params);
                return res;
            }
        }

        AddEmployee ae = new AddEmployee();
        ae.execute();
    }

    @Override
    public void onClick(View v){
        if(v == btnadd){
            addEmployee();
        }
        if(v == btnedit){
            startActivity(new Intent(this,tampil.class));
        }
    }

}
