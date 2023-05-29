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

public class AZTEC extends AppCompatActivity {

    private ImageView CodeIV4;
    private Button generateBtn4;
    private EditText dataEdt4;
    private Button copyBtn4;
    private static final int STORAGE_PERMISSION_CODE = 100;
    Bitmap bitmap;
    QRGEncoder qrgEncoder;
    private void saveImage(Bitmap bitmap) {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = "AZTEC_" + timeStamp + ".jpg";

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
            MediaScannerConnection.scanFile(AZTEC.this, new String[]{file.getAbsolutePath()}, null, null);

            Toast.makeText(AZTEC.this, "AZTEC code image saved to " + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(AZTEC.this, "Error saving AZTEC image", Toast.LENGTH_SHORT).show();
        }
    }
    private boolean isValidInput(String input) {

        String specialChars = "[!@#$%^&*(),.?\":{}|<>]";
        if(input.matches(specialChars)){
            return false;
        }
        return true;

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aztec);

        CodeIV4 = findViewById(R.id.ImageViewId4);
        dataEdt4 = findViewById(R.id.idEdt4);
        generateBtn4 = findViewById(R.id.idBtnGenerate4);
        copyBtn4 = findViewById(R.id.idBtnCopy4);


        generateBtn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userInput =  dataEdt4.getText().toString().trim();
                if (isValidInput(userInput)) {
                    MultiFormatWriter writer = new MultiFormatWriter();
                try {
                    BitMatrix matrix =writer.encode(userInput, BarcodeFormat.AZTEC,270,300);

                    BarcodeEncoder encoder = new BarcodeEncoder();
                    bitmap = encoder.createBitmap(matrix);
                    CodeIV4.setImageBitmap(bitmap);

                    InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    manager.hideSoftInputFromWindow(dataEdt4.getApplicationWindowToken(),0);
                } catch (WriterException e){
                    e.printStackTrace();
                }
                }else {
                    Toast.makeText(AZTEC.this, "Invalid AZTEC code. Please enter a valid code.", Toast.LENGTH_SHORT).show();
                }
            }

        });
        copyBtn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bitmap == null) {
                    Toast.makeText(AZTEC.this, "No AZTEC code generated", Toast.LENGTH_SHORT).show();
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