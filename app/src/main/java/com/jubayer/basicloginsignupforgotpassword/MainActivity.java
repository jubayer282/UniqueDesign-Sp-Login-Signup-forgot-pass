package com.jubayer.basicloginsignupforgotpassword;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.text.style.IconMarginSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    Button Logout;
    ImageView Image;
    TextView Hello;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Logout = findViewById(R.id.log_out);
        Image = findViewById(R.id.img);
        Hello = findViewById(R.id.hello);

        /*logout firebase*/
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "LogOut Successfully ", Toast.LENGTH_SHORT).show();
                firebaseAuth.signOut();
                finish();
                finishAffinity();
                startActivity(new Intent(MainActivity.this, Login.class));

            }
        });

    }
}