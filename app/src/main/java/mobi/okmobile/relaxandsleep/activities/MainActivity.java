package mobi.okmobile.relaxandsleep.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.WindowManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import mobi.okmobile.relaxandsleep.R;
import mobi.okmobile.relaxandsleep.fragments.SettingsFragment;
import mobi.okmobile.relaxandsleep.fragments.SoundsFragment;
import mobi.okmobile.relaxandsleep.preferences.SharedPrefsHelper;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private static final String TAG = MainActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeViews();
        initializeListeners();
    }

    private void initializeViews(){
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new SoundsFragment()).commit();
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
    }

    private void initializeListeners(){
      bottomNavigationView.setOnNavigationItemSelectedListener(bottomNavSelectListener);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener bottomNavSelectListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            if(item.getItemId() == R.id.bottom_nav_home){
                selectedFragment = new SoundsFragment();
            }
            else if(item.getItemId() == R.id.bottom_nav_settings){
                selectedFragment = new SettingsFragment();
            }
            else {
                selectedFragment = new SoundsFragment();
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, selectedFragment).commit();
            return true;
        }
    };


}