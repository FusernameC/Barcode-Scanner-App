package com.example.barcode;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.media.Image;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PackageManagerCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.common.InputImage;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;
import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.controls.Flash;
import com.otaliastudios.cameraview.frame.Frame;
import com.otaliastudios.cameraview.frame.FrameProcessor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;


public class ScanFragment extends Fragment {

    CameraView cameraView;
    boolean isDetected = false;

    BarcodeScannerOptions options;
    BarcodeScanner detector;


    Button lightButton;
    Button scanImageButton;

    public static final int SCAN_RESULT = 1;

    public ScanFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_scan, container, false);

        Dexter.withContext(getContext())
                .withPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO})
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        setupCamera(view);
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {

                    }
                }).check();

        lightButton = view.findViewById(R.id.light_button);
        scanImageButton = view.findViewById(R.id.scan_image_button);

        lightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cameraView.getFlash() == Flash.OFF)
                    cameraView.setFlash(Flash.TORCH);
                else cameraView.setFlash(Flash.OFF);
            }
        });


        scanImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent storageIntent= new Intent();
                storageIntent.setType("image/*");
                storageIntent.setAction(Intent.ACTION_GET_CONTENT);
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    private void setupCamera(View view) {
        cameraView = view.findViewById (R.id.camera_view);
        cameraView.setLifecycleOwner(getActivity());
        cameraView.addFrameProcessor(new FrameProcessor() {
            @Override
            public void process(@NonNull Frame frame) {
                processImage(getVisionImageFromFrame(frame));
            }
        });

        options = new BarcodeScannerOptions.Builder()
                .setBarcodeFormats(Barcode.FORMAT_ALL_FORMATS)
                .build();
        detector = BarcodeScanning.getClient(options);
    }

    private void processImage(InputImage image) {

        detector.process(image)
                .addOnSuccessListener(new OnSuccessListener<List<Barcode>>() {
                    @Override
                    public void onSuccess(List<Barcode> barcodes) {
                        if(!isDetected)
                        {
                            processResult(barcodes);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void processResult(List<Barcode> Barcodes) {
        if(Barcodes.size() > 0)
        {
            isDetected = true;
            for(Barcode item: Barcodes)
            {
                createResult(item.getRawValue(), item.getFormat());
            }
        }

    }

    private void createResult(String text, int type) {
        LocalDateTime ldt = LocalDateTime.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm", Locale.ENGLISH);
        String dateTime = format.format(ldt);
        Intent intent = new Intent(getActivity(), ScanResultActivity.class);
        intent.putExtra("text", text)
                .putExtra("type", type)
                .putExtra("dateTime", dateTime);
        startActivityForResult(intent, SCAN_RESULT);
    }

    @Override
    public void onActivityResult (int requestCode, int resultCode, Intent data) {
        if (requestCode == SCAN_RESULT) {
            isDetected = false;
            Log.e("isDetected", String.valueOf(isDetected));
        }
    }

    private InputImage getVisionImageFromFrame(Frame frame) {
        byte[] data = frame.getData();
        InputImage image = InputImage.fromByteArray(
                data,
                /* image width */frame.getSize().getWidth(),
                /* image height */frame.getSize().getHeight(),
                0,
                InputImage.IMAGE_FORMAT_NV21);
        return image;
    }
}