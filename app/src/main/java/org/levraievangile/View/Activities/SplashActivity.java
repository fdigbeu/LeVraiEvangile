package org.levraievangile.View.Activities;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import org.levraievangile.Presenter.SplashPresenter;
import org.levraievangile.R;
import org.levraievangile.View.Interfaces.SplashView;
import org.levraievangile.View.Receivers.AlarmTimeReceiver;

public class SplashActivity extends AppCompatActivity implements SplashView.ISplash {
    // Ref CountDownTimer
    private CountDownTimer countDownTimer;
    // Ref widgets
    private ImageView logo;
    private TextView title_the, title_true, title_gospel;
    private View container;
    // Ref presenter
    private SplashPresenter splashPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        //--
        splashPresenter = new SplashPresenter(this);
        splashPresenter.loadSplashData(SplashActivity.this);
        // Hide headerBar
        getSupportActionBar().hide();
    }

    @Override
    public void initialize() {
        logo = findViewById(R.id.logo);
        container = findViewById(R.id.container);
        title_the = findViewById(R.id.title_the);
        title_true = findViewById(R.id.title_true);
        title_gospel = findViewById(R.id.title_gospel);
    }

    @Override
    public void events() {
        Animation animLogo = AnimationUtils.loadAnimation(SplashActivity.this, R.anim.rotate_and_move_bottom);
        logo.startAnimation(animLogo);
        //--
        Animation animTitleThe = AnimationUtils.loadAnimation(SplashActivity.this, R.anim.move_right);
        title_the.startAnimation(animTitleThe);
        //--
        Animation animTitleTrue = AnimationUtils.loadAnimation(SplashActivity.this, R.anim.move_left);
        title_true.startAnimation(animTitleTrue);
        //--
        Animation animTitleGospel = AnimationUtils.loadAnimation(SplashActivity.this, R.anim.move_right);
        title_gospel.startAnimation(animTitleGospel);
        //--
        Animation animContainer = AnimationUtils.loadAnimation(SplashActivity.this, R.anim.move_top);
        container.startAnimation(animContainer);
    }

    @Override
    public void launchHomeActivity() {
        countDownTimer = new CountDownTimer(2500, 1000) {
            public void onTick(long millisUntilFinished) {}
            public void onFinish() {
                Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        }.start();
    }

    @Override
    public void startAlarmService() {
        AlarmTimeReceiver alarmTimeReceiver = new AlarmTimeReceiver();
        alarmTimeReceiver.startTimerAlarm(SplashActivity.this);
    }

    @Override
    public void stopAlarmService() {
        AlarmTimeReceiver alarmTimeReceiver = new AlarmTimeReceiver();
        alarmTimeReceiver.stopTimerAlarm(SplashActivity.this);
    }

    @Override
    public void onBackPressed() {
        splashPresenter.cancelCountDownTimer(countDownTimer);
        super.onBackPressed();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            if (hasFocus) {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
            }
        }
    }
}
