package com.example.myidea.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.myidea.R;
import com.example.myidea.fragments.EditProfileFragment;

public class ProfileFragment extends Fragment {
     TextView nameTv;
     TextView bioTv;
     ImageView profileIv;
     TextView scoreTv ;
     TextView increaseTv;
    TextView ageTv;
    TextView emailTv;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_profile,container,false);
        return view ;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        nameTv=view.findViewById(R.id.tv_profile_name);
        nameTv.setText("Abolfazl");

        bioTv=view.findViewById(R.id.tv_profile_bio);
        bioTv.setText("aaaaa");

        ageTv = view.findViewById(R.id.tv_profile_age);
        ageTv.setText("28");

        emailTv = view.findViewById(R.id.tv_profile_email);
        emailTv.setText("abolfazlkargari.1991@gmail.com");

        profileIv=view.findViewById(R.id.iv_profile_profile);


        Button editProfileBtn= view.findViewById(R.id.btn_profile_edit_profile);
        editProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.add(R.id.frame_main_fragment_container,new EditProfileFragment());
                transaction.addToBackStack(null);
                transaction.commit();

            }
        });


        scoreTv=view.findViewById(R.id.tv_card_profile_score);
        scoreTv.setText("555");

        increaseTv=view.findViewById(R.id.tv_card_profile_increase);






    }

}
