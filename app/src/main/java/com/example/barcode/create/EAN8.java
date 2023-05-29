package com.example.barcode.create;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
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

public class EAN8 extends AppCompatActivity {
    private ImageView CodeIV6;
    private Button generateBtn6;
    private EditText dataEdt6;
    private Button copyBtn6;
    private static final int STORAGE_PERMISSION_CODE = 100;
    Bitmap bitmap;
    QRGEncoder qrgEncoder;
    private boolean isValidEAN8(String code) {
        if (code.length() != 8 || code.matches("[0-9]+") || code.matches("[A-Da-d]+")) {
            return false;
        }
        return true;
    }
    private void saveImage(Bitmap bitmap) {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = "EAN8_" + timeStamp + ".jpg";

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
            MediaScannerConnection.scanFile(EAN8.this, new String[]{file.getAbsolutePath()}, null, null);

            Toast.makeText(EAN8.this, "EAN8 code image saved to " + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(EAN8.this, "Error saving EAN8 image", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ean8);

        CodeIV6 =findViewById(R.id.ImageViewId6);
        dataEdt6 = findViewById(R.id.idEdt6);
        generateBtn6 = findViewById(R.id.idBtnGenerate6);
        copyBtn6 = findViewById(R.id.idBtnCopy6);

        generateBtn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userInput =  dataEdt6.getText().toString().trim();
                if (isValidEAN8(userInput)) {
                MultiFormatWriter writer = new MultiFormatWriter();


                try {
                    BitMatrix matrix =writer.encode(userInput, BarcodeFormat.EAN_8,400,170);

                    BarcodeEncoder encoder = new BarcodeEncoder();
                    bitmap = encoder.createBitmap(matrix);
                    CodeIV6.setImageBitmap(bitmap);

                    InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    manager.hideSoftInputFromWindow(dataEdt6.getApplicationWindowToken(),0);
                } catch (WriterException e){
                    e.printStackTrace();
                }
               } else {
                    Toast.makeText(EAN8.this, "Invalid EAN-8 code. Please enter a valid code.", Toast.LENGTH_SHORT).show();
                }
            }

        });
        copyBtn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bitmap == null) {
                    Toast.makeText(EAN8.this, "No EAN8 code generated", Toast.LENGTH_SHORT).show();
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