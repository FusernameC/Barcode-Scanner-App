package com.example.barcode;

import androidx.appcompat.app.AppCompatActivity;
import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.media.MediaScannerConnection;
import android.os.Build;
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
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import androidmads.library.qrgenearator.QRGEncoder;

public class QRCODE extends AppCompatActivity {
    private ImageView qrCodeIV1;
    private Button generateQrBtn1;
    private EditText dataEdt1;
    private Button copyQrBtn1;
    private static final int STORAGE_PERMISSION_CODE = 100;
    Bitmap bitmap;
    QRGEncoder qrgEncoder;

    private void saveImage(Bitmap bitmap) {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = "QR_" + timeStamp + ".jpg";

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
            MediaScannerConnection.scanFile(QRCODE.this, new String[]{file.getAbsolutePath()}, null, null);

            Toast.makeText(QRCODE.this, "QR code image saved to " + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(QRCODE.this, "Error saving QR code image", Toast.LENGTH_SHORT).show();
        }
    }
    public Bitmap resizeImage(Bitmap image, int new_height, int new_width) {
        return Bitmap.createScaledBitmap(image, new_width, new_height, true);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);

        qrCodeIV1 = findViewById(R.id.ImageViewId1);
        dataEdt1 = findViewById(R.id.idEdt1);
        generateQrBtn1 = findViewById(R.id.idBtnGenerateQR1);
        copyQrBtn1 = findViewById(R.id.idBtnCopyQR1);

        generateQrBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userInput = dataEdt1.getText().toString().trim();

                MultiFormatWriter writer = new MultiFormatWriter();
                try {
                    BitMatrix matrix = writer.encode(userInput, BarcodeFormat.QR_CODE, 270, 300);

                    BarcodeEncoder encoder = new BarcodeEncoder();
                    bitmap = encoder.createBitmap(matrix);
                    qrCodeIV1.setImageBitmap(bitmap);

                    InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    manager.hideSoftInputFromWindow(dataEdt1.getApplicationWindowToken(), 0);
                } catch (WriterException e) {
                    e.printStackTrace();
                }

            }
        });

        copyQrBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bitmap == null) {
                    Toast.makeText(QRCODE.this, "No QR code generated", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
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


        /*copyQrBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(dataEdt1.getText().toString()) ||
                        TextUtils.isEmpty(dataEdtphone1.getText().toString()) ||
                        TextUtils.isEmpty(dataEdtemail.getText().toString()) ||
                        TextUtils.isEmpty(dataEdtmoney.getText().toString())) {
                    Toast.makeText(QRCODE.this, "No text to copy", Toast.LENGTH_SHORT).show();
                } else {
                    bitmap = qrCodeIV1.getDrawingCache();

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

                            ContentValues values = new ContentValues();
                            values.put(MediaStore.Images.Media.DATA, filename.getAbsolutePath());
                            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                            getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

                            Toast.makeText(QRCODE.this, "File is Saved in " + filename.getAbsolutePath(), Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                }
            }
        });*/

       /* copyQrBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(dataEdt1.getText().toString()) ||TextUtils.isEmpty(dataEdtphone1.getText().toString()) || TextUtils.isEmpty(dataEdtemail.getText().toString())|| TextUtils.isEmpty(dataEdtmoney.getText().toString())  ) {
                    Toast.makeText(QRCODE.this, "No text to copy", Toast.LENGTH_SHORT).show();
                } else {
                    File filename;
                    try {
                        String path = Environment.getExternalStorageDirectory().toString();
                       // String path = requireActivity().getExternalFilesDir(null).getAbsolutePath();

                        new File(path + "/folder/subfolder").mkdirs();
                        filename = new File(path + "/folder/subfolder/image.jpg");

                        FileOutputStream out = new FileOutputStream(filename);

                        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
                        out.flush();
                        out.close();
                        MediaStore.Images.Media.insertImage(getContentResolver(), filename.getAbsolutePath(), filename.getName(), filename.getName());

                        Toast.makeText(QRCODE.this, "File is Saved in  " + filename, Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            }
        });*/
    }
}