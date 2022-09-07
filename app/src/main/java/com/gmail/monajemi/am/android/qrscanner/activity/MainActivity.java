package com.gmail.monajemi.am.android.qrscanner.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.view.MenuItem;

import com.gmail.monajemi.am.android.qrscanner.R;
import com.gmail.monajemi.am.android.qrscanner.fragment.HistoryFragment;
import com.gmail.monajemi.am.android.qrscanner.fragment.ScanFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;


public class MainActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {

    private BottomNavigationView navigation;
    private boolean isScanFragmentSelected = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        navigation.setOnItemSelectedListener(this);
        navigation.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), android.R.color.transparent));
        getSupportFragmentManager().beginTransaction().replace(R.id.main_activity_container, new ScanFragment(), getResources().getString(R.string.scan_fragment_tag)).commit();

    }

    private void init() {
        navigation = findViewById(R.id.main_activity_navigation);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_scan:
                if (!isScanFragmentSelected) {
                    isScanFragmentSelected = true;
                    getSupportFragmentManager().beginTransaction().setCustomAnimations(
                                    R.anim.enter_from_left,
                                    R.anim.exit_to_right,
                                    R.anim.enter_from_right,
                                    R.anim.exit_to_left)
                            .replace(R.id.main_activity_container, new ScanFragment(), getResources().getString(R.string.scan_fragment_tag)).commit();
                }
                break;
            case R.id.menu_history:
                if (isScanFragmentSelected) {
                    isScanFragmentSelected = false;
                    getSupportFragmentManager().beginTransaction().setCustomAnimations(
                                    R.anim.enter_from_right,
                                    R.anim.exit_to_left,
                                    R.anim.enter_from_left,
                                    R.anim.exit_to_right)
                            .replace(R.id.main_activity_container, new HistoryFragment(), getResources().getString(R.string.history_fragment_tag)).commit();
                }
                break;
        }
        return true;
    }
}