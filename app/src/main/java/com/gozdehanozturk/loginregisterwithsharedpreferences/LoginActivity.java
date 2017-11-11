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

public class LoginActivity extends AppCompatActivity {
    SharedPreferences sp;
    static SharedPreferences.Editor editor;

    EditText mail,password;
    String userMail, userPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sp = getSharedPreferences("login",MODE_PRIVATE);
        editor = sp.edit();

        mail = (EditText)findViewById(R.id.txtMail);
        password=(EditText)findViewById(R.id.txtPass);

        String userId = sp.getString("uId", "");

        if (!userId.equals("")) {
            startActivity(new Intent(LoginActivity.this, ProfileActivity.class));
        }

    }

    public void loginProcess(View view) {
        userMail = mail.getText().toString();
        userPassword = password.getText().toString();

        String url = "http://jsonbulut.com/json/userLogin.php?ref=cb226ff2a31fdd460087fedbb34a6023&" +
                "userEmail="+userMail+
                "&userPass="+userPassword+"" +
                "&face=no";

        new JsonData(url, this).execute();
    }

    public void gotoRegister(View view) {
        startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
        finish();
    }
}

class JsonData extends AsyncTask<Void,Void,Void>{

    String url = "";
    String data = "";
    Context cnx;
    ProgressDialog pd;


    public JsonData(String url, Context cnx) {
        this.url = url;
        this.cnx = cnx;
        pd = new ProgressDialog(cnx);
        pd.setMessage("İşlem Gerçekleşiyor. Lütfen Bekleyiniz");
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
            Log.e("Data Json Hatası", "doInBackground", e);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        try {
            JSONObject jo = new JSONObject(data);
            boolean durum = jo.getJSONArray("user").getJSONObject(0).getBoolean("durum");
            String mesaj = jo.getJSONArray("user").getJSONObject(0).getString("mesaj");

            if(durum){
                //Giriş Başarılı
                Toast.makeText(cnx, mesaj, Toast.LENGTH_SHORT).show();
                JSONObject obj = jo.getJSONArray("user").getJSONObject(0).getJSONObject("bilgiler");
                String userId = obj.getString("userId");
                String userName = obj.getString("userName");
                String userSurname = obj.getString("userSurname");

                LoginActivity.editor.putString("uId",userId);
                LoginActivity.editor.putString("uName",userName);
                LoginActivity.editor.putString("uSurname",userSurname);
                LoginActivity.editor.commit();

                Intent i = new Intent(cnx,ProfileActivity.class);
                i.putExtra("name",userName);
                i.putExtra("surname",userSurname);
                cnx.startActivity(i);
            }else{
                //Kayıt başarısız
                Toast.makeText(cnx, mesaj, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            Log.e("Data Json Hatası", "doInBackground", e);
        }
        pd.dismiss();
    }
}
