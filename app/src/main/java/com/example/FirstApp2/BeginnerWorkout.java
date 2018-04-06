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
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import java.util.Locale;

public class BeginnerWorkout extends YouTubeBaseActivity implements View.OnClickListener,TextToSpeech.OnInitListener,YouTubePlayer.OnInitializedListener{

    private TextView[] mtextView;
    private LinearLayout mLinearLayout;
    private TextView startWorkout;
    private boolean toggleButton = false;
    private CountDownTimer countDownTimer;
    private TextToSpeech tts;

    // To start next exercise after a delay
    final Handler handler = new Handler();
    // to loop through exercises
    int counter = 0;

    // text to speech during each exercise
    String text[] = {
            "Get Ready Dynamic Runner Lunges for 60 Seconds",
            "Up Next Lateral Hip Openers for 60 seconds",
            "Get Ready Reverse Lunge Reaches for 60 seconds"
    };

    private YouTubePlayerView youTubeView;
    private YouTubePlayer mPlayer;
    //YOUTUBE API KEY
    private String YOUTUBE_API_KEY = "AIzaSyCmJwvmfVQhykedsh27U6t5emdJWf99scE";
    String videoIds[] ={
            "7amGIIJJlsI",
            "9E-1ilf4xlQ",
            "SOee8Lgzxus"
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
        startWorkout = (TextView) findViewById(R.id.start_workout);
        tts =  new TextToSpeech(this,this);

        // loading the videos
        youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);
        youTubeView.initialize(YOUTUBE_API_KEY,this);


        findViewById(R.id.start_workout).setOnClickListener(this);
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
        if(counter == 0 )
                mPlayer.play();
        handler.post(new Runnable() {
            @Override
            public void run() {
                soundBeep();
                speakOut(text[counter]);
                soundBeep();
                if(counter!=0){
                    mPlayer.loadVideo(videoIds[counter]);
                }
                startTimer(60,counter);
                counter++;
                handler.postDelayed(this,61*1000);
            }
        });
        if(toggleButton) {
            //change text to Pause Workout
            startWorkout.setText("PAUSE WORKOUT");
        }
        else {
            //change to ic_media_play
            startWorkout.setText("START WORKOUT");
        }
    }

    @Override
    public void onInit(int status) {
        int result = tts.setLanguage(Locale.US);

        if(result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED){
            Log.e("TTS","This language is not supported");
        }
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        mPlayer = youTubePlayer;
            if(!b){
                mPlayer.cueVideo(videoIds[0]);
            }else{
                mPlayer.play();
            }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        Toast.makeText(this,"Error Loading Video Check Connection",Toast.LENGTH_SHORT);
    }
}
