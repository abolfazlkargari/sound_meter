package com.example.myidea.fragments;


import android.icu.text.DecimalFormat;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.myidea.R;
import com.example.myidea.World;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RecordFragment extends Fragment implements View.OnClickListener {

    private View toolbar;
    private Chronometer chronometer;
    private ImageView imageViewRecord, imageViewPlay, imageViewStop;
    private SeekBar seekBar;
    private LinearLayout linearLayoutRecorder, linearLayoutPlay;
    private MediaRecorder mRecorder;
    private MediaPlayer mPlayer;
    private String fileName = null;
    private int lastProgress = 0;
    private Handler mHandler = new Handler();
    private boolean isPlaying = false;
    private final int AUDIO_RECORDING_DELAY = 3000;
    private final double referenceAmplitude = 0.0001;
    private TextView minValTv, maxValTv,mmValTV,curValTv;
    private double max;
    private double min;
    List<Double> amplitudeList;


    private boolean bListener = true;
    private boolean isThreadRun = true;
    private Thread thread;
    float volume = 10000;









    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_record, container, false);
        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        toolbar = view.findViewById(R.id.rl_record_toolbar);


        linearLayoutRecorder = view.findViewById(R.id.ll_record_recorder);
        chronometer = view.findViewById(R.id.chronometer_record);
        chronometer.setBase(SystemClock.elapsedRealtime());
        imageViewRecord = view.findViewById(R.id.iv_record_rec);
        imageViewStop = view.findViewById(R.id.iv_record_stop);
        imageViewPlay = view.findViewById(R.id.iv_record_play);
        linearLayoutPlay = view.findViewById(R.id.ll_record_play);
        seekBar = view.findViewById(R.id.seekBar_record);

        imageViewRecord.setOnClickListener(this);
        imageViewStop.setOnClickListener(this);
        imageViewPlay.setOnClickListener(this);

        minValTv=view.findViewById(R.id.tv_record_minVal);
        maxValTv=view.findViewById(R.id.tv_record_maxVal);
        mmValTV=view.findViewById(R.id.tv_record_mmVal);
        curValTv=view.findViewById(R.id.tv_record_curVal);


    }

    @Override
    public void onClick(View v) {

        if (v == imageViewRecord) {
            prepareforRecording();
            startRecording();
            startListenAudio();
            final Handler handler = new Handler(){
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void handleMessage(Message msg){
                    super.handleMessage(msg);
                    DecimalFormat df1 = null;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        df1 = new DecimalFormat("####.0");
                    }
                    if(msg.what == 1){
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        minValTv.setText(df1.format(World.minDB));
                    }
                    mmValTV.setText(df1.format((World.minDB+World.maxDB)/2));
                    maxValTv.setText(df1.format(World.maxDB));
                    curValTv.setText(df1.format(World.dbCount));
                }


            };


            //final Handler handler = new Handler();
            // handler.postDelayed(new Runnable() {
            //@Override
            // public void run() {
            // final double amplitude = getAmplitude();

            //dbtv.setText(String.format(Locale.US, "%.1f", amplitude));

            //  amplitudeList = new ArrayList<>();
            //  amplitudeList.add(getAmplitude());


            //   handler.postDelayed(this, AUDIO_RECORDING_DELAY);
            //   max = max(amplitudeList);

       // }
        // }, AUDIO_RECORDING_DELAY);

       }else if( v == imageViewStop ){
            prepareforStop();
            stopRecording();
         //   dbtv.setText(Double.toString(max));




        }else if( v == imageViewPlay ){
            if( !isPlaying && fileName != null ){
                isPlaying = true;
                startPlaying();
            }else{
                isPlaying = false;
                stopPlaying();
            }
        }

    }

    private void prepareforStop() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            TransitionManager.beginDelayedTransition(linearLayoutRecorder);
        }
        imageViewRecord.setVisibility(View.VISIBLE);
        imageViewStop.setVisibility(View.GONE);
        linearLayoutPlay.setVisibility(View.VISIBLE);
    }

    private void prepareforRecording() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            TransitionManager.beginDelayedTransition(linearLayoutRecorder);
        }
        imageViewRecord.setVisibility(View.GONE);
        imageViewStop.setVisibility(View.VISIBLE);
        linearLayoutPlay.setVisibility(View.GONE);
    }

    private void stopPlaying() {
        try{
            mPlayer.release();
        }catch (Exception e){
            e.printStackTrace();
        }
        mPlayer = null;
        imageViewPlay.setImageResource(R.drawable.ic_play_arrow_gray_24dp);
        chronometer.stop();
    }

    private void startRecording() {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);

        File root = android.os.Environment.getExternalStorageDirectory();
        File file = new File(root.getAbsolutePath() + "/VoiceRecorderSimplifiedCoding/Audios");
        if (!file.exists()) {
            file.mkdirs();
        }

        fileName =  root.getAbsolutePath() + "/VoiceRecorderSimplifiedCoding/Audios/" + String.valueOf(System.currentTimeMillis() + ".mp3");
        Log.d("filename",fileName);
        mRecorder.setOutputFile(fileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
            mRecorder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        lastProgress = 0;
        seekBar.setProgress(0);
        stopPlaying();
        // making the imageview a stop button
        //starting the chronometer
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();
    }

    private void stopRecording() {

        try{
            mRecorder.stop();
            mRecorder.release();
        }catch (Exception e){
            e.printStackTrace();
        }
        mRecorder = null;
        chronometer.stop();
        chronometer.setBase(SystemClock.elapsedRealtime());
        Toast.makeText(getContext(), "Recording saved successfully.", Toast.LENGTH_SHORT).show();
    }

    private void startPlaying() {
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(fileName);



            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            Log.e("LOG_TAG", "prepare() failed");
        }
        imageViewPlay.setImageResource(R.drawable.ic_pause_gray_24dp);

        seekBar.setProgress(lastProgress);
        mPlayer.seekTo(lastProgress);
        seekBar.setMax(mPlayer.getDuration());
        seekUpdation();
        chronometer.start();


        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                imageViewPlay.setImageResource(R.drawable.ic_play_arrow_gray_24dp);
                isPlaying = false;
                chronometer.stop();
            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if( mPlayer!=null && fromUser ){
                    mPlayer.seekTo(progress);
                    chronometer.setBase(SystemClock.elapsedRealtime() - mPlayer.getCurrentPosition());
                    lastProgress = progress;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            seekUpdation();
        }
    };

    private void seekUpdation() {
        if(mPlayer != null){
            int mCurrentPosition = mPlayer.getCurrentPosition() ;
            seekBar.setProgress(mCurrentPosition);
            lastProgress = mCurrentPosition;
        }
        mHandler.postDelayed(runnable, 100);
    }


    private double getAmplitude(){
        if(mRecorder != null) {
            final double maxAmplitude = mRecorder.getMaxAmplitude();
            final double amplitude = 20 * Math.log10(maxAmplitude / referenceAmplitude);

            return amplitude;
        }

        else{
            return 0;
        }
    }

    //public double maxValue(List<Double> array){
       // List<Double> list = new ArrayList<>();
      //  for (int i = 0; i < array.size(); i++) {
         //   list.add(array.get(i));
     //   }
       // return Collections.max(list);}

  //  public double max(List<Double> array) {
      //  double maximum = array.get(0);   // start with the first value
       // for (int i=1; i<array.size(); i++) {
         //   if (array.get(i) > maximum) {
           //     maximum = array.get(i);   // new maximum
         //   }
      //  }
    //    return maximum;
  //  }

    private void startListenAudio() {
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (isThreadRun) {
                        if(bListener) {
                            volume = mRecorder.getMaxAmplitude();  //Get the sound pressure value
                            if(volume > 0 && volume < 1000000) {
                                World.setDbCount(20 * (float)(Math.log10(volume)));  //Change the sound pressure value to the decibel value
                                // Update with thread
                                Message message = new Message();
                                message.what = 1;

                            }
                        }


                }
            }
        });
        thread.start();
    }

}
