package com.gmail.monajemi.am.android.qrscanner.fragment;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.gmail.monajemi.am.android.qrscanner.R;
import com.gmail.monajemi.am.android.qrscanner.activity.ScannerActivity;
import com.gmail.monajemi.am.android.qrscanner.database.SqliteDatabase;
import com.gmail.monajemi.am.android.qrscanner.model.History;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class ScanFragment extends Fragment implements View.OnClickListener {

    ImageButton scanButton;
    String[] permissions = new String[]{Manifest.permission.CAMERA};
    private static final int CAMERA_REQ_CODE = 100;

    private static final String RESULT_CODE = "result";
    private final DateFormat dateFormat = new SimpleDateFormat("yy/MM/dd\nHH:mm:ss");

    private final ActivityResultLauncher<String> requestPermissionResult = registerForActivityResult(new ActivityResultContracts.RequestPermission(), result -> {
        if (result){
            openCamera();
        }else {
            ActivityCompat.requestPermissions(requireActivity(),permissions,CAMERA_REQ_CODE);
        }
    });

    private final ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK) {
                    if (result.getData() != null) {
                        SqliteDatabase database = new SqliteDatabase(requireContext());
                        String link = result.getData().getStringExtra(RESULT_CODE);
                        database.insertHistory(new History(link, getCurrentDateTime()));
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                        startActivity(intent);
                    }
                }
            });

    public ScanFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scan, container, false);
        scanButton = view.findViewById(R.id.scan_fragment_scan_button);
        scanButton.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.scan_fragment_scan_button:
                    if (ActivityCompat.checkSelfPermission(requireContext(), permissions[0]) == PackageManager.PERMISSION_GRANTED){
                        openCamera();
                    }else {
                        requestPermissionResult.launch(permissions[0]);
                    }
        }
    }

    public void openCamera(){
        Intent intent = new Intent(getActivity(), ScannerActivity.class);
        activityResultLauncher.launch(intent);
    }

    private String getCurrentDateTime() {
        Date date = new Date();
        return dateFormat.format(date);
    }
}