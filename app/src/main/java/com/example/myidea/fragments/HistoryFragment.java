package com.example.myidea.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myidea.R;
import com.example.myidea.Recording;
import com.example.myidea.RecordingAdapter;

import java.io.File;
import java.util.ArrayList;

public class HistoryFragment extends Fragment {
    private RecyclerView historyRv;
    private ArrayList<Recording> recordingArraylist;
    private RecordingAdapter recordingAdapter;
    private View emptyState;
    private View toolbar;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        recordingArraylist= new ArrayList<Recording>();


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history,container,false);
        return view ;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        toolbar=view.findViewById(R.id.rl_history_toolbar);
        historyRv=view.findViewById(R.id.rv_history);
        historyRv.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));
        recordingAdapter= new RecordingAdapter(recordingArraylist);
        historyRv.setAdapter(recordingAdapter);
        emptyState=view.findViewById(R.id.frame_history_empty);

        fetchRecordings();
    }

    private void fetchRecordings() {

        File root = android.os.Environment.getExternalStorageDirectory();
        String path = root.getAbsolutePath() + "/VoiceRecorderSimplifiedCoding/Audios";

        File directory = new File(path);
        File[] files = directory.listFiles();

        if( files!=null ) {

            for (int i = 0; i < files.length; i++) {

                String fileName = files[i].getName();
                String recordingUri = root.getAbsolutePath() + "/VoiceRecorderSimplifiedCoding/Audios/" + fileName;

                Recording recording = new Recording(recordingUri, fileName, false);
                recordingArraylist.add(recording);
            }


        }
        else {
            historyRv.setVisibility(View.GONE);
            emptyState.setVisibility(View.VISIBLE);

        }

    }



}
