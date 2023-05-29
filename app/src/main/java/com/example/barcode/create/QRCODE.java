package com.example.barcode.create;

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

import com.example.barcode.R;
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
import java.text.SimpleDateFormat;
import java.util.Date;

import androidmads.library.qrgenearator.QRGEncoder;

public class QRCODE extends AppCompatActivity {
    private ImageView qrCodeIV1;
    private Button generateQrBtn1;
    private EditText dataEdt1, dataEdtphone1,dataEdtemail, dataEdtmoney;
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
        dataEdtphone1 = findViewById(R.id.idEdtphone1);
        dataEdtemail = findViewById(R.id.idEdtemail1);
        dataEdtmoney = findViewById(R.id.idEdtmoney1);
        generateQrBtn1 = findViewById(R.id.idBtnGenerateQR1);
        copyQrBtn1 = findViewById(R.id.idBtnCopyQR1);

        generateQrBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String qrcode_text = "2|99|" + dataEdtphone1.getText().toString().trim() + "|" +
                        dataEdt1.getText().toString().trim() + "|" + dataEdtemail.getText().toString().trim() + "|0|0|" +
                        dataEdtmoney.getText().toString().trim();

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

                qrCodeIV1.setImageBitmap(bitmap);
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