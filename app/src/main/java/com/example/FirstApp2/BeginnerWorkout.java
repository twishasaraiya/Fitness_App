package com.example.FirstApp2;

import android.graphics.Color;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.CountDownTimer;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Locale;

public class BeginnerWorkout extends AppCompatActivity implements View.OnClickListener,TextToSpeech.OnInitListener{

    private TextView[] mtextView;
    private LinearLayout mLinearLayout;
    private ImageView mImageView;
    private boolean toggleButton = false;
    private CountDownTimer countDownTimer;
    private TextToSpeech tts;
    final Handler handler = new Handler();
    int counter = 0;
    String text[] = {
            "Get Ready Dynamic Runner Lunges for 60 Seconds",
            "Up Next Lateral Hip Openers for 60 seconds",
            "Get Ready Reverse Lunge Reaches for 60 seconds"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beginner_workout);
        mtextView = new TextView[4];
        for(int id = 0 ;id< 3;id++){
            String timerId = "timer"+(id+1);
            int resId = getResources().getIdentifier(timerId,"id",getPackageName());
            Log.e("resid", String.valueOf(resId)+ "timerid= " + timerId);
            mtextView[id] = ((TextView) findViewById(resId));
        }
        mLinearLayout = (LinearLayout) findViewById(R.id.drl);
        mImageView = (ImageView) findViewById(R.id.play);
        tts =  new TextToSpeech(this,this);

        findViewById(R.id.play).setOnClickListener(this);
    }

    private void startTimer(long i, final int id) {
        countDownTimer = new CountDownTimer(i * 1000 ,1000) {
            @Override
            public void onTick(long l) {
                mtextView[id].setText(" 00 : "+String.format("%02d",l/1000));
            }

            @Override
            public void onFinish() {
                soundBeep();
                mtextView[id].setText(" 00 : 00");
            }
        };
        countDownTimer.start();
    }

    private void soundBeep() {
        ToneGenerator toneG = new ToneGenerator(AudioManager.STREAM_ALARM, 100);
        toneG.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 200);
    }

    private void speakOut(String text){
            tts.speak(text,TextToSpeech.QUEUE_FLUSH,null);
    }

    @Override
    public void onClick(View view) {
        toggleButton = !toggleButton;
        handler.post(new Runnable() {
            @Override
            public void run() {
                soundBeep();
                speakOut(text[counter]);
                soundBeep();
                startTimer(60,counter);
                counter++;
                handler.postDelayed(this,61*1000);
            }
        });
        if(toggleButton) {
            //change to ic_media_pause
        }
        else {
            //change to ic_media_play
        }
    }

    @Override
    public void onInit(int status) {
        int result = tts.setLanguage(Locale.US);

        if(result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED){
            Log.e("TTS","This language is not supported");
        }
    }
}
