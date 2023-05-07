package com.example.barcode;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.mlkit.vision.barcode.common.Barcode;

public class ScanResultActivity extends AppCompatActivity {

    TextView text_textview;
    TextView datetime_textview;

    String text = "";
    String datetime = "";
    int type = 0;
    String typeString = "";

    Button viewCode;
    Button copy;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_result);

        text_textview = findViewById(R.id.text);
        datetime_textview = findViewById(R.id.datetime);
        viewCode = findViewById(R.id.viewCode);
        copy =  findViewById(R.id.copy);

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
            case Barcode.FORMAT_CODE_39:
                typeString = "Code 39";
                break;
            case Barcode.FORMAT_CODE_93:
                typeString = "Code 93";
                break;
            case Barcode.FORMAT_CODABAR:
                typeString = "Codabar";
                break;
            case Barcode.FORMAT_DATA_MATRIX:
                typeString = "Data Matrix";
                break;
            case Barcode.FORMAT_EAN_13:
                typeString = "EAN-13";
                break;
            case Barcode.FORMAT_EAN_8:
                typeString = "EAN-8";
                break;
            case Barcode.FORMAT_ITF:
                typeString = "ITF";
                break;
            case Barcode.FORMAT_QR_CODE:
                typeString = "QR Code";
                break;
            case Barcode.FORMAT_UPC_A:
                typeString = "UPC-A";
                break;
            case Barcode.FORMAT_UPC_E:
                typeString = "UPC-E";
                break;
            case Barcode.FORMAT_PDF417:
                typeString = "PDF417";
                break;
            case Barcode.FORMAT_AZTEC:
                typeString = "Aztec";
                break;
        }
        actionBar.setTitle(typeString);


        text_textview.setText(text);
        datetime_textview.setText(datetime);

        viewCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Barcode text", text);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(ScanResultActivity.this, "Copied to clipboard", Toast.LENGTH_LONG).show();
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
}
