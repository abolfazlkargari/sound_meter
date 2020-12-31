package com.example.myidea;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.myidea.fragments.HistoryFragment;
import com.example.myidea.fragments.ProfileFragment;
import com.example.myidea.fragments.RecordFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

    public class MainActivity extends AppCompatActivity {
        private static final int REQUEST_CODE =1001;
        BottomNavigationView bottomNavigationView;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.frame_main_fragment_container,new RecordFragment());
            transaction.commit();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getPermissionToRecordAudio();
            }


            bottomNavigationView= findViewById(R.id.bn_main_tools);
            bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    switch (menuItem.getItemId()){

                        case R.id.item_profile_bn :
                            FragmentTransaction addProfileFragmentTransaction = getSupportFragmentManager().beginTransaction();
                            addProfileFragmentTransaction.replace(R.id.frame_main_fragment_container,new ProfileFragment());
                            addProfileFragmentTransaction.addToBackStack(null);
                            addProfileFragmentTransaction.commit();
                            return true;

                        case R.id.item_history_bn :
                            FragmentTransaction gotoHistoryFragmentTransaction = getSupportFragmentManager().beginTransaction();
                            gotoHistoryFragmentTransaction.replace(R.id.frame_main_fragment_container,new HistoryFragment());
                            gotoHistoryFragmentTransaction.addToBackStack(null);
                            gotoHistoryFragmentTransaction.commit();
                            return true;

                        case R.id.item_record_bn :
                            FragmentTransaction gotoRecordFragmentTransaction = getSupportFragmentManager().beginTransaction();
                            gotoRecordFragmentTransaction.replace(R.id.frame_main_fragment_container,new RecordFragment(),null);
                            gotoRecordFragmentTransaction.addToBackStack(null);
                            gotoRecordFragmentTransaction.commit();
                            return true;


                    }
                    return false;
                }
            });
        }

        public void getPermissionToRecordAudio(){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if(!(checkSelfPermission(Manifest.permission.RECORD_AUDIO)==PackageManager.PERMISSION_GRANTED
                        || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED
                        || checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED)) {
                    requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE},
                            REQUEST_CODE);
                }
            }
        }

        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            if (requestCode==REQUEST_CODE){
                if(grantResults[0]==PackageManager.PERMISSION_GRANTED
                        && grantResults[1]==PackageManager.PERMISSION_GRANTED
                        && grantResults[2]==PackageManager.PERMISSION_GRANTED){

                }else {
                    Toast.makeText(this,R.string.main_toast_need_permission,Toast.LENGTH_SHORT).show();
                    finishAffinity();
                }
            }

        }

    }



