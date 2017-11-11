package com.gozdehanozturk.loginregisterwithsharedpreferences;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileActivity extends AppCompatActivity {

    SharedPreferences sha;
    SharedPreferences.Editor editor;

    TextView nameSurname;
    String n,s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        nameSurname = findViewById(R.id.namesurname);

        sha = getSharedPreferences("login",MODE_PRIVATE);
        editor = sha.edit();

        n = sha.getString("userName","");
        s = sha.getString("userSurname","");
        nameSurname.setText("Hoşgeldiniz " + n +" " + s);
    }

    public void exit(View view) {
        AlertDialog.Builder uyari =
                new AlertDialog.Builder(this);
        uyari.setTitle("Çıkış Yap");
        uyari.setMessage("Çıkış Yapmak İstediğinizden Emin misiniz ?");
        uyari.setCancelable(true);

        uyari.setPositiveButton
                ("Çıkış Yap", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        editor.remove("uId");
                        if(editor.commit()) {
                            Toast.makeText(ProfileActivity.this, "Çıkış Yaptınız !", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ProfileActivity.this,LoginActivity.class);
                            startActivity(intent);
                        }
                    }
                });

        uyari.setNegativeButton("İptal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(ProfileActivity.this, "İptal Edildi", Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog alt = uyari.create();
        alt.show();

    }
}
