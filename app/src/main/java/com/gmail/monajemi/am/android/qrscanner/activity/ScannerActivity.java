package com.gmail.monajemi.am.android.qrscanner.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.gmail.monajemi.am.android.qrscanner.R;

public class ScannerActivity extends AppCompatActivity {

    private CodeScanner codeScanner;
    private static final String RESULT_CODE = "result";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);
        init();
    }

    public void init() {
        CodeScannerView codeScannerView = findViewById(R.id.scanner_activity_codeScannerView);
        codeScanner = new CodeScanner(this, codeScannerView);
        codeScanner.setDecodeCallback(decodeCallback);
    }

    DecodeCallback decodeCallback = result -> {
        Intent intent = new Intent(ScannerActivity.this, MainActivity.class);
        intent.putExtra(RESULT_CODE, result.getText());
        setResult(RESULT_OK, intent);
        finish();
    };

    @Override
    protected void onResume() {
        super.onResume();
        codeScanner.startPreview();
    }
}