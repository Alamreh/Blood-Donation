package com.example.blooddonation;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreenActivity extends AppCompatActivity {

    private ImageView  logo;
    private TextView title,Slogan;
    Animation topanimation,bottomanimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_splash_screen );
        getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN );

        title=findViewById( R.id.title );
         logo=findViewById( R.id.logo );
        Slogan=findViewById( R.id.sologan );

        topanimation= AnimationUtils.loadAnimation( this,R.anim.top_animation );

        bottomanimation=AnimationUtils.loadAnimation( this,R.anim.bottom_animation );

        //set the animation in logo
        logo.setAnimation( topanimation );
        title.setAnimation( bottomanimation );
        Slogan.setAnimation( bottomanimation );

        int Splash_Screen=4300;
        new Handler().postDelayed( new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(SplashScreenActivity.this,Loginactivity.class);
                startActivity( intent );
                //finish is use because one time splash screen is visible during open when we press back we dont move to the splashScreen;

                finish();
            }
        },Splash_Screen );


    }
}