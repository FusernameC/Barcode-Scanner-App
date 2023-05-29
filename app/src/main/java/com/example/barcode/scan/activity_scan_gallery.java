package com.example.barcode.scan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.barcode.R;
import com.example.barcode.scan.ScanResultActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.common.InputImage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;

public class activity_scan_gallery extends AppCompatActivity {

    TextView detect;
    ImageView image;
    BarcodeScannerOptions options;
    BarcodeScanner detector;
    Barcode result;
    String uri;
    boolean isDetected = false;

    public static final int SCAN_RESULT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_gallery);

        detect = findViewById(R.id.detect);
        image = findViewById(R.id.gallery_image);

        detect.setText("No barcode found");

        Bundle extras = getIntent().getExtras();
        uri = extras.getString("uri");
        if (extras != null) {
            image.setImageURI(Uri.parse(uri));
        }

        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();

        InputImage input;

        options = new BarcodeScannerOptions.Builder()
                .setBarcodeFormats(Barcode.FORMAT_ALL_FORMATS)
                .build();
        detector = BarcodeScanning.getClient(options);
        try {
            input = InputImage.fromFilePath(this, Uri.parse(uri));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        processImage(input);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Scan Image");
    }

    private void processImage(InputImage image) {

        detector.process(image)
                .addOnSuccessListener(new OnSuccessListener<List<Barcode>>() {
                    @Override
                    public void onSuccess(List<Barcode> barcodes) {
                        if(!isDetected) {
                            processResult(barcodes);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void processResult(List<Barcode> Barcodes) {
        if(Barcodes.size() > 0)
        {
            isDetected = true;
            for(Barcode item: Barcodes)
            {
                result = item;
            }
            detect.setText("Barcode detected");
            detect.setTextColor(ContextCompat.getColor(this, R.color.orange));
            invalidateOptionsMenu();
        }


    }

    private void createResult(String text, int type) {
        LocalDateTime ldt = LocalDateTime.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm", Locale.ENGLISH);
        String dateTime = format.format(ldt);
        Intent intent = new Intent(this, ScanResultActivity.class);
        intent.putExtra("text", text)
                .putExtra("type", type)
                .putExtra("dateTime", dateTime);
        startActivityForResult(intent, SCAN_RESULT);
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_action_bar, menu);
        for(int i = 0; i < menu.size(); i++){
            Drawable drawable = menu.getItem(i).getIcon();
            if(drawable != null) {
                drawable.mutate();
                drawable.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
            }
        }
        MenuItem item = menu.findItem(R.id.delete_button);
        item.setVisible(false);
        if (isDetected) {
            menu.findItem(R.id.ok_button).setVisible(true);
        } else {
            menu.findItem(R.id.ok_button).setVisible(false);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                break;
            case R.id.ok_button:
                createResult(result.getRawValue(), result.getFormat());
                break;
        }

        return super.onOptionsItemSelected(item);
    }

}