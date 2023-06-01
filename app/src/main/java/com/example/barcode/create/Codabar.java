package com.example.barcode.create;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.barcode.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import androidmads.library.qrgenearator.QRGEncoder;

public class Codabar extends AppCompatActivity {
    private ImageView CodeIV12;
    private Button generateBtn12;
    private EditText dataEdt12;
    private Button copyBtn12;
    private static final int STORAGE_PERMISSION_CODE = 100;
    Bitmap bitmap;
    QRGEncoder qrgEncoder;
    private boolean isValidInput(String code) {

        return code.matches("[A-Da-d]+");
    }
    private void saveImage(Bitmap bitmap) {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = "Codabar_" + timeStamp + ".jpg";

        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/Code/";
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File file = new File(path + fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();

            // Update the gallery to show the newly saved image
            MediaScannerConnection.scanFile(Codabar.this, new String[]{file.getAbsolutePath()}, null, null);

            Toast.makeText(Codabar.this, "Codabar code image saved to " + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(Codabar.this, "Error saving Codabar image", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_codabar);

        CodeIV12 = findViewById(R.id.ImageViewId12);
        dataEdt12 = findViewById(R.id.idEdt12);
        generateBtn12 = findViewById(R.id.idBtnGenerate12);
        copyBtn12 = findViewById(R.id.idBtnCopy12);

        Intent intent = getIntent();
        if (intent != null) {
            String fileContent = intent.getStringExtra("text");
            if (fileContent != null) {
                dataEdt12.setText(fileContent);
            }
        }

        generateBtn12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userInput =  dataEdt12.getText().toString().trim();
                if (isValidInput(userInput)) {
                    MultiFormatWriter writer = new MultiFormatWriter();
                try {
                    BitMatrix matrix =writer.encode(userInput, BarcodeFormat.CODABAR,400,170);

                    BarcodeEncoder encoder = new BarcodeEncoder();
                    bitmap = encoder.createBitmap(matrix);
                    CodeIV12.setImageBitmap(bitmap);

                    InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    manager.hideSoftInputFromWindow(dataEdt12.getApplicationWindowToken(),0);
                } catch (WriterException e){
                    e.printStackTrace();
                }
                }else {
                    Toast.makeText(Codabar.this, "Invalid Codabar code. Please enter a valid code.", Toast.LENGTH_SHORT).show();
                }
            }

        });
        copyBtn12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bitmap == null) {
                    Toast.makeText(Codabar.this, "No Codabar code generated", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        requestPermissions(permissions, STORAGE_PERMISSION_CODE);
                    } else {
                        saveImage(bitmap);
                    }
                } else {
                    saveImage(bitmap);
                }
            }
        });

    }
}