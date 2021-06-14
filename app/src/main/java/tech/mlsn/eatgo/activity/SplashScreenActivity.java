
package tech.mlsn.eatgo.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import tech.mlsn.eatgo.R;

public class SplashScreenActivity extends AppCompatActivity {
    ImageView ivLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        initialization();
    }

    private void initialization(){
        ivLogo = findViewById(R.id.ivLogo);
        Glide.with(this).load(R.drawable.logo_eatgo).into(ivLogo);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
                finish();
            }
        },2000);
    }
}