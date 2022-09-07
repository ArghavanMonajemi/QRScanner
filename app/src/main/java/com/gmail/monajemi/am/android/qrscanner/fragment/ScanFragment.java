package com.gmail.monajemi.am.android.qrscanner.fragment;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.AlertDialog;
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
import com.gmail.monajemi.am.android.qrscanner.Validation;
import com.gmail.monajemi.am.android.qrscanner.activity.ScannerActivity;
import com.gmail.monajemi.am.android.qrscanner.database.SqliteDatabase;
import com.gmail.monajemi.am.android.qrscanner.model.History;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class ScanFragment extends Fragment implements View.OnClickListener {

    ImageButton scanImage;
    FloatingActionButton scanButton;
    String[] permissions = new String[]{Manifest.permission.CAMERA};
    private static final int CAMERA_REQ_CODE = 100;

    private static final String RESULT_CODE = "result";
    private final DateFormat dateFormat = new SimpleDateFormat("yy/MM/dd\nHH:mm:ss");

    private final ActivityResultLauncher<String> requestPermissionResult = registerForActivityResult(new ActivityResultContracts.RequestPermission(), result -> {
        if (result) {
            openCamera();
        } else {
            ActivityCompat.requestPermissions(requireActivity(), permissions, CAMERA_REQ_CODE);
        }
    });

    private final ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK) {
                    if (result.getData() != null) {
                        SqliteDatabase database = new SqliteDatabase(requireContext());
                        String data = result.getData().getStringExtra(RESULT_CODE);
                        database.insertHistory(new History(data, getCurrentDateTime()));
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity()).setTitle(getString(R.string.successful_scan))
                                .setMessage(data).setNeutralButton(getString(R.string.ok), (dialogInterface, i) -> {

                                });
                        if (Validation.isValidURL(data))
                            alertDialog.setPositiveButton(getString(R.string.open_browser), (dialogInterface, i) -> {
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(data));
                                startActivity(intent);
                            });
                        alertDialog.show();
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
        scanImage = view.findViewById(R.id.scan_fragment_scan_image);
        scanImage.setOnClickListener(this);
        scanButton.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        if (ActivityCompat.checkSelfPermission(requireContext(), permissions[0]) == PackageManager.PERMISSION_GRANTED) {
            openCamera();
        } else {
            requestPermissionResult.launch(permissions[0]);
        }
    }

    public void openCamera() {
        Intent intent = new Intent(getActivity(), ScannerActivity.class);
        activityResultLauncher.launch(intent);
    }

    private String getCurrentDateTime() {
        Date date = new Date();
        return dateFormat.format(date);
    }
}