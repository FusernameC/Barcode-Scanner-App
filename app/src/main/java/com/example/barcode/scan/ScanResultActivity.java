package com.example.barcode.scan;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Environment;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.barcode.R;
import com.google.mlkit.vision.barcode.common.Barcode;

public class ScanResultActivity extends AppCompatActivity {

    TextView text_textview;
    TextView datetime_textview;

    String text = "";
    String datetime = "";
    int type = 0;
    int typeType = 0;
    String typeString = "";
    Button copy;

    Button save;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_result);

        text_textview = findViewById(R.id.text);
        datetime_textview = findViewById(R.id.datetime);
        copy =  findViewById(R.id.copy);
        save = findViewById(R.id.save_button);
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            text = extras.getString("text");
            datetime = extras.getString("dateTime");
            type = extras.getInt("type");
            //The key argument here must match that used in the other activity
        }

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        switch (type) {
            case Barcode.FORMAT_UNKNOWN:
                typeString = "Unknown";
                break;
            case Barcode.FORMAT_ALL_FORMATS:
                typeString = "All";
                break;
            case Barcode.FORMAT_CODE_128:
                typeString = "Code128";
                break;
            case Barcode.FORMAT_CODE_39:
                typeString = "Code39";
                break;
            case Barcode.FORMAT_CODE_93:
                typeString = "Code93";
                break;
            case Barcode.FORMAT_CODABAR:
                typeString = "Codabar";
                break;
            case Barcode.FORMAT_DATA_MATRIX:
                typeString = "DATAMATRIX";
                break;
            case Barcode.FORMAT_EAN_13:
                typeString = "EAN13";
                break;
            case Barcode.FORMAT_EAN_8:
                typeString = "EAN8";
                break;
            case Barcode.FORMAT_ITF:
                typeString = "ITF";
                break;
            case Barcode.FORMAT_QR_CODE:
                typeString = "QR Code";
                break;
            case Barcode.FORMAT_UPC_A:
                typeString = "UPCA";
                break;
            case Barcode.FORMAT_UPC_E:
                typeString = "UPCE";
                break;
            case Barcode.FORMAT_PDF417:
                typeString = "PDF417";
                break;
            case Barcode.FORMAT_AZTEC:
                typeString = "AZTEC";
                break;
        }


        actionBar.setTitle(typeString);


        text_textview.setText(text);
        datetime_textview.setText(datetime);


        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Barcode text", text);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(ScanResultActivity.this, "Copied to clipboard", Toast.LENGTH_LONG).show();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveDataToExternalStorage(text, datetime, typeString);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_action_bar, menu);

        for(int i = 0; i < menu.size(); i++){
            Drawable drawable = menu.getItem(i).getIcon();
            if(drawable != null) {
                drawable.mutate();
                drawable.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
            }
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
            case R.id.delete_button:
        }

        return super.onOptionsItemSelected(item);
    }

    private void saveDataToExternalStorage(String text, String datetime, String typeString) {
        String filename = "barcode_data_" + System.currentTimeMillis() + ".txt";
        String data = "Text: " + text + "\nDatetime: " + datetime + "\nType: " + typeString + "\n\n";

        File file = new File(getExternalFilesDir(null), filename);

        try {
            FileWriter writer = new FileWriter(file, true);
            writer.append(data);
            writer.flush();
            writer.close();
            Toast.makeText(ScanResultActivity.this, "Data saved to external storage", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(ScanResultActivity.this, "Error saving data", Toast.LENGTH_SHORT).show();
        }
    }


}
