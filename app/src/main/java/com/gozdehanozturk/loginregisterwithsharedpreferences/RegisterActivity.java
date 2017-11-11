package com.gozdehanozturk.loginregisterwithsharedpreferences;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.io.IOException;

public class RegisterActivity extends AppCompatActivity {

    EditText etName, etSurname, etMail, etPhone, etPassword;
    static String name, surname;
    String password, mail, phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        etName = findViewById(R.id.regName);
        etSurname = findViewById(R.id.regSurname);
        etMail = findViewById(R.id.regMail);
        etPhone = findViewById(R.id.regPhone);
        etPassword = findViewById(R.id.regPass);
    }

    public void register(View view) {
        name = etName.getText().toString();
        surname = etSurname.getText().toString();
        mail = etMail.getText().toString();
        phone = etPhone.getText().toString();
        password = etPassword.getText().toString();

        String url = "http://jsonbulut.com/json/userRegister.php?ref=cb226ff2a31fdd460087fedbb34a6023" +
                "&userName=" + name +
                "&userSurname=" + surname +
                "&userPhone=" + phone +
                "&userMail=" + mail +
                "&userPass=" + password;

        new jsonKayitData(url, RegisterActivity.this).execute();
    }
}
class jsonKayitData extends AsyncTask<Void, Void, Void> {

    String url = "";
    Context cnx;
    String data = "";
    ProgressDialog pd;

    public jsonKayitData(String url, Context cnx) {
        this.url = url;
        this.cnx = cnx;
        pd = new ProgressDialog(cnx);
        pd.setMessage("Lütfen Bekleyiniz");
        pd.show();

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            data = Jsoup.connect(url).ignoreContentType(true).get().body().text();
        } catch (IOException e) {
            Log.e("Connection hatası", " doInBackground", e);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        try {
            JSONObject jo = new JSONObject(data);
            boolean durum = jo.getJSONArray("user").getJSONObject(0).getBoolean("durum");
            String mesaj = jo.getJSONArray("user").getJSONObject(0).getString("mesaj");

            if (durum) {
                Toast.makeText(cnx, mesaj, Toast.LENGTH_SHORT).show();
                String kid = jo.getJSONArray("user").getJSONObject(0).getString("kullaniciId");
                LoginActivity.editor.putString("uId", kid);
                LoginActivity.editor.putString("userName", RegisterActivity.name);
                LoginActivity.editor.putString("userSurname", RegisterActivity.surname);
                LoginActivity.editor.commit();

                Intent i = new Intent(cnx, ProfileActivity.class);
                cnx.startActivity(i);


            } else {
                Toast.makeText(cnx, mesaj, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        pd.dismiss();

    }
}
