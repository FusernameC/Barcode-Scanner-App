package com.example.barcode;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.File;
import java.io.FileOutputStream;

import androidmads.library.qrgenearator.QRGEncoder;

public class DATAMATRIX extends AppCompatActivity {
    private ImageView qrCodeIV2;
    private Button generateQrBtn2;
    private EditText dataEdt2;
    private Button copyQrBtn2;
    private static final int STORAGE_PERMISSION_CODE = 100;
    Bitmap bitmap;
    QRGEncoder qrgEncoder;
    private boolean isValidInput(String input) {
        if(input.matches("[!@#$%^&*(),.?\":{}|<>]")){
            return false;
        }
        return true;

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datamatrix);

        qrCodeIV2 = findViewById(R.id.ImageViewId2);
        dataEdt2 = findViewById(R.id.idEdt2);
        generateQrBtn2 = findViewById(R.id.idBtnGenerateQR2);
        copyQrBtn2 = findViewById(R.id.idBtnCopyQR2);

        generateQrBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userInput =  dataEdt2.getText().toString().trim();
                if (isValidInput(userInput)) {
                    MultiFormatWriter writer = new MultiFormatWriter();
                    try {
                        BitMatrix matrix = writer.encode(userInput, BarcodeFormat.DATA_MATRIX, 270, 300);

                        BarcodeEncoder encoder = new BarcodeEncoder();
                        bitmap = encoder.createBitmap(matrix);
                        qrCodeIV2.setImageBitmap(bitmap);

                        InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        manager.hideSoftInputFromWindow(dataEdt2.getApplicationWindowToken(), 0);
                    } catch (WriterException e) {
                        e.printStackTrace();
                    }
                }else {
                    Toast.makeText(DATAMATRIX.this, "Invalid Data Matrix code. Please enter a valid code.", Toast.LENGTH_SHORT).show();
                }
            }

        });
        copyQrBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(dataEdt2.getText().toString())) {
                    Toast.makeText(DATAMATRIX.this, "No text to copy", Toast.LENGTH_SHORT).show();
                } else {
                    File filename;
                    try {
                        String path = Environment.getExternalStorageDirectory().toString();
                        //String path = requireActivity().getExternalFilesDir(null).getAbsolutePath();

                        new File(path + "/folder/subfolder").mkdirs();
                        filename = new File(path + "/folder/subfolder/image.jpg");

                        FileOutputStream out = new FileOutputStream(filename);

                        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
                        out.flush();
                        out.close();
                        MediaStore.Images.Media.insertImage(getContentResolver(), filename.getAbsolutePath(), filename.getName(), filename.getName());

                        Toast.makeText(DATAMATRIX.this, "File is Saved in  " + filename, Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            }
        });
    }
}