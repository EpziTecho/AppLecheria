package com.example.lecheriaapp.Vista.PrincipalView;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.VideoView;

import com.example.lecheriaapp.R;

public class SplashActivity extends AppCompatActivity {

    private VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Orientacion del video
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        //Hace que sea fullscreen la flashscreem y que no se vea la barra de la parte de arriba
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);

        videoView = (VideoView) findViewById(R.id.videoView);
        videoView.setVideoPath("https://lalecheria.000webhostapp.com/LaLecheria2.3gp");
        //videoView.setMediaController(new MediaController(this));
        videoView.start();
        videoView.requestFocus();
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 10000);
    }
}