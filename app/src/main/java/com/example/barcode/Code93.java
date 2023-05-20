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

public class Code93 extends AppCompatActivity {
    private ImageView CodeIV10;
    private Button generateBtn10;
    private EditText dataEdt10;
    private Button copyBtn10;
    private static final int STORAGE_PERMISSION_CODE = 100;
    Bitmap bitmap;
    QRGEncoder qrgEncoder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code93);

        CodeIV10 = findViewById(R.id.ImageViewId10);
        dataEdt10 = findViewById(R.id.idEdt10);
        generateBtn10 = findViewById(R.id.idBtnGenerate10);
        copyBtn10 = findViewById(R.id.idBtnCopy10);

        generateBtn10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userInput =  dataEdt10.getText().toString().trim();
                MultiFormatWriter writer = new MultiFormatWriter();
                try {
                    BitMatrix matrix =writer.encode(userInput, BarcodeFormat.CODE_93,400,170);

                    BarcodeEncoder encoder = new BarcodeEncoder();
                    bitmap = encoder.createBitmap(matrix);
                    CodeIV10.setImageBitmap(bitmap);

                    InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    manager.hideSoftInputFromWindow(dataEdt10.getApplicationWindowToken(),0);
                } catch (WriterException e){
                    e.printStackTrace();
                }
            }

        });
        copyBtn10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(dataEdt10.getText().toString())) {
                    Toast.makeText(Code93.this, "No text to copy", Toast.LENGTH_SHORT).show();
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

                        Toast.makeText(Code93.this, "File is Saved in  " + filename, Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            }
        });
    }
}