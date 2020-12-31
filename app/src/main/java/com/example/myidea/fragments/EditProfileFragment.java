package com.example.myidea.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.myidea.R;

public class EditProfileFragment extends Fragment {
    ImageView okIv;
    ImageView cancelIv;
    ImageView profileIv;
    TextView changeProfileTv;
    EditText nameEt;
    EditText emailEt;
    EditText bioEt;
    View toolbar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test,container,false);
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toolbar=view.findViewById(R.id.rl_edit_test_toolbar);
        okIv = view.findViewById(R.id.iv_edit_test_ok);
        cancelIv= view.findViewById(R.id.iv_edit_test_cancel);
        profileIv = view.findViewById(R.id.iv_edit_test_profile);
        changeProfileTv=view.findViewById(R.id.tv_edit_test_change);
        nameEt= view.findViewById(R.id.et_edit_test_name);
        emailEt=view.findViewById(R.id.et_edit_test_email);
        bioEt=view.findViewById(R.id.et_edit_test_bio);



    }
}
