package com.example.myidea;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.TransitionManager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RecordingAdapter extends RecyclerView.Adapter<RecordingAdapter.RecordingViewHolder> {
    private ArrayList<Recording> recordingList;
    private boolean isPlaying=false;
    private MediaPlayer mPlayer;
    private int last_index = -1;

    public RecordingAdapter(ArrayList<Recording> recordingList){
        this.recordingList=recordingList;

    }

    public void deletItem(int position){
        recordingList.remove(position);
        notifyItemRemoved(position);
    }



    @NonNull
    @Override
    public RecordingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recording,parent,false);
        return new RecordingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecordingViewHolder holder, int position) {
        setUpData(holder,position);
    }


    private void setUpData(final RecordingViewHolder holder, int position) {

        Recording recording = recordingList.get(position);
        holder.nameTv.setText(recording.getFileName());

        if( recording.isPlaying() ){
            holder.playbtn.setImageResource(R.drawable.ic_pause_gray_24dp);
            TransitionManager.beginDelayedTransition((ViewGroup) holder.itemView);
            holder.seekBar.setVisibility(View.VISIBLE);
            holder.seekUpdation(holder);
        }else{
            holder.playbtn.setImageResource(R.drawable.ic_play_arrow_gray_24dp);
            TransitionManager.beginDelayedTransition((ViewGroup) holder.itemView);
            holder.seekBar.setVisibility(View.GONE);
        }


        holder.manageSeekBar(holder);
        holder.bindDeleteIv();

    }



    @Override
    public int getItemCount() {
        return recordingList.size();
    }

    public class RecordingViewHolder extends RecyclerView.ViewHolder {
        private ImageView playbtn;
        private TextView nameTv;
        private SeekBar seekBar;
        private ImageView deleteIv;
        private String recordingUri;
        private int lastProgress = 0;
        private Handler mHandler = new Handler();
        RecyclerView.ViewHolder holder;


        public RecordingViewHolder(@NonNull View itemView) {
            super(itemView);
            playbtn = itemView.findViewById(R.id.imageViewPlay);
            nameTv = itemView.findViewById(R.id.textViewRecordingname);
            seekBar = itemView.findViewById(R.id.seekBar);
            deleteIv = itemView.findViewById(R.id.iv_itemRec_delete);

            playbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Recording recording = recordingList.get(position);
                    recordingUri = recording.getUri();

                    if (isPlaying) {
                        stopPlaying();
                        if (position == last_index) {
                            recording.setPlaying(false);
                            stopPlaying();
                            notifyItemChanged(position);
                        } else {
                            recording.setPlaying(true);
                            notifyItemChanged(position);
                            startPlaying(recording, position);
                            last_index = position;
                        }

                    } else {
                        if (recording.isPlaying()) {
                            recording.setPlaying(false);
                            stopPlaying();
                            Log.d("isPlayin", "True");
                        } else {
                            startPlaying(recording, position);
                            recording.setPlaying(true);
                            seekBar.setMax(mPlayer.getDuration());
                            Log.d("isPlayin", "False");
                        }
                        notifyItemChanged(position);
                        last_index = position;
                    }
                }
            });


        }

        private void bindDeleteIv(){
            deleteIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Recording recording = recordingList.get(position);
                    recordingUri = recording.getUri();
                    File file = new File(recordingUri);
                    boolean deleted = file.delete();
                    if(deleted){
                        recordingList.remove(recording);
                        notifyItemRemoved(position);
                        notifyDataSetChanged();
                    }

                }
            });


        }






        public void manageSeekBar(RecordingViewHolder holder){
            holder.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if( mPlayer!=null && fromUser ){
                        mPlayer.seekTo(progress);
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
                seekUpdation((RecordingViewHolder) holder);
            }
        };

        private void seekUpdation(RecordingViewHolder holder) {
            this.holder = holder;
            if(mPlayer != null){
                int mCurrentPosition = mPlayer.getCurrentPosition() ;
                holder.seekBar.setMax(mPlayer.getDuration());
                holder.seekBar.setProgress(mCurrentPosition);
                lastProgress = mCurrentPosition;
            }
            mHandler.postDelayed(runnable, 100);
        }

        private void stopPlaying() {
            try{
                mPlayer.release();
            }catch (Exception e){
                e.printStackTrace();
            }
            mPlayer = null;
            isPlaying = false;
        }
        private void startPlaying(final Recording audio, final int position) {
            mPlayer = new MediaPlayer();
            try {
                mPlayer.setDataSource(recordingUri);
                mPlayer.prepare();
                mPlayer.start();
            } catch (IOException e) {
                Log.e("LOG_TAG", "prepare() failed");
            }
            //showing the pause button
            seekBar.setMax(mPlayer.getDuration());
            isPlaying = true;

            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    audio.setPlaying(false);
                    notifyItemChanged(position);
                }
            });



        }


    }



}
