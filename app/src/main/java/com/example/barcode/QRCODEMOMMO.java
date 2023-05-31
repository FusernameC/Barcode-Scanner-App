package com.example.barcode;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.media.MediaScannerConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import androidmads.library.qrgenearator.QRGEncoder;

public class QRCODEMOMMO extends AppCompatActivity {
    private ImageView qrCodeIV14;
    private Button generateQrBtn14;
    private EditText dataEdt14, dataEdtphone14,dataEdtemail14, dataEdtmoney14;
    private Button copyQrBtn14;
    private static final int STORAGE_PERMISSION_CODE = 100;
    Bitmap bitmap;
    QRGEncoder qrgEncoder;

    private void saveImage(Bitmap bitmap) {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = "QRCODEMOMMO_" + timeStamp + ".jpg";

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
            MediaScannerConnection.scanFile(QRCODEMOMMO.this, new String[]{file.getAbsolutePath()}, null, null);

            Toast.makeText(QRCODEMOMMO.this, "QR CODE MOMO code image saved to " + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(QRCODEMOMMO.this, "Error saving QR CODE MOMO image", Toast.LENGTH_SHORT).show();
        }
    }
    public Bitmap resizeImage(Bitmap image, int new_height, int new_width) {
        return Bitmap.createScaledBitmap(image, new_width, new_height, true);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcodemommo);
        qrCodeIV14 = findViewById(R.id.ImageViewId14);
        dataEdt14 = findViewById(R.id.idEdt14);
        dataEdtphone14 = findViewById(R.id.idEdtphone14);
        dataEdtemail14 = findViewById(R.id.idEdtemail14);
        dataEdtmoney14 = findViewById(R.id.idEdtmoney14);
        generateQrBtn14 = findViewById(R.id.idBtnGenerateQR14);
        copyQrBtn14 = findViewById(R.id.idBtnCopyQR14);

        generateQrBtn14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String qrcode_text = "2|99|" + dataEdtphone14.getText().toString().trim() + "|" +
                        dataEdt14.getText().toString().trim() + "|" + dataEdtemail14.getText().toString().trim() + "|0|0|" +
                        dataEdtmoney14.getText().toString().trim();

                QRCodeWriter barcodeWriter = new QRCodeWriter();
                com.google.zxing.common.BitMatrix matrix;
                try {
                    matrix = barcodeWriter.encode(qrcode_text, BarcodeFormat.QR_CODE, 270, 270);
                } catch (WriterException e) {
                    e.printStackTrace();
                    return;
                }

                bitmap = Bitmap.createBitmap(270, 270, Bitmap.Config.ARGB_8888);
                for (int i = 0; i < 270; i++) {
                    for (int j = 0; j < 270; j++) {
                        bitmap.setPixel(i, j, matrix.get(i, j) ? 0xFF000000 : 0xFFFFFFFF);
                    }
                }
                Bitmap logo = resizeImage(BitmapFactory.decodeResource(getResources(), R.drawable.logo_momo), 50, 50);
                Canvas canvas = new Canvas(bitmap);
                canvas.drawBitmap(logo, (bitmap.getWidth() - logo.getWidth()) / 2, (bitmap.getHeight() - logo.getHeight()) / 2, null);

                qrCodeIV14.setImageBitmap(bitmap);
            }

        });

        copyQrBtn14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bitmap == null) {
                    Toast.makeText(QRCODEMOMMO.this, "No QR code generated", Toast.LENGTH_SHORT).show();
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