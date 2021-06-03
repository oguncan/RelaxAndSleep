package mobi.okmobile.relaxandsleep.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import mobi.okmobile.relaxandsleep.R;

public class SplashActivity extends AppCompatActivity {
    private Animation zoomInAnim, zoomOutAnim;
    private ImageView splash_image;
    private static int SPLASH_SCREEN = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        //Initialize View
        splash_image = findViewById(R.id.splash_image);
        //Animation
        zoomInAnim = AnimationUtils.loadAnimation(this, R.anim.zoom_in_anim);
        zoomOutAnim = AnimationUtils.loadAnimation(this, R.anim.zoom_out_anim);

        splash_image.setAnimation(zoomInAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
        }, SPLASH_SCREEN);
    }
}