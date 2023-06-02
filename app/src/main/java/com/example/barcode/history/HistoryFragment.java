package com.example.barcode.history;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.barcode.R;
import com.example.barcode.create.AZTEC;
import com.example.barcode.create.Codabar;
import com.example.barcode.create.Code128;
import com.example.barcode.create.Code39;
import com.example.barcode.create.Code93;
import com.example.barcode.create.DATAMATRIX;
import com.example.barcode.create.EAN13;
import com.example.barcode.create.EAN8;
import com.example.barcode.create.ITF;
import com.example.barcode.create.PDF417;
import com.example.barcode.create.QRCODE;
import com.example.barcode.create.QRCODEMOMMO;
import com.example.barcode.create.UPCA;
import com.example.barcode.create.UPCE;
import com.google.zxing.qrcode.encoder.QRCode;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HistoryFragment extends Fragment {
    private RecyclerView recyclerView;
    private FileAdapter fileAdapter;
    private List<File> fileList;

    private static final int PERMISSION_REQUEST_CODE = 1001;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);


        fileList = new ArrayList<>();
        fileAdapter = new FileAdapter(fileList);
        recyclerView.setAdapter(fileAdapter);

        loadFileList();

        return view;
    }

    private void loadFileList() {
        // Kiểm tra quyền truy cập
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Yêu cầu quyền truy cập nếu chưa được cấp
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
            return;
        }

        // Đường dẫn thư mục
        String directoryPath = "/storage/emulated/0/Android/data/com.example.barcode/files";

        // Lấy danh sách tệp tin trong thư mục
        List<File> files = getFilesInDirectory(directoryPath);

        // Thêm danh sách tệp tin vào danh sách hiển thị
        fileList.addAll(files);
        fileAdapter.notifyDataSetChanged();
    }

    private List<File> getFilesInDirectory(String directoryPath) {
        List<File> files = new ArrayList<>();
        File directory = new File(directoryPath);
        if (directory.exists() && directory.isDirectory()) {
            File[] fileList = directory.listFiles();
            if (fileList != null) {
                files.addAll(Arrays.asList(fileList));
            }
        }
        return files;
    }

    private class FileAdapter extends RecyclerView.Adapter<FileAdapter.FileViewHolder> {

        private List<File> fileList;

        public FileAdapter(List<File> fileList) {
            this.fileList = fileList;
        }

        @NonNull
        @Override
        public FileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_file, parent, false);
            return new FileViewHolder(view);
        }


        @Override
        public void onBindViewHolder(@NonNull FileViewHolder holder, int position) {
            File file = fileList.get(position);
            String fileName = file.getName();
            String fileContent = readFileContent(file);

            holder.fileNameTextView.setText(fileName);
            holder.fileContentTextView.setText(fileContent);

            Button deleteButton = holder.itemView.findViewById(R.id.deleteButton);
            Button generateButton = holder.itemView.findViewById(R.id.generateButton);

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int adapterPosition = holder.getAdapterPosition();
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        onDeleteButtonClick(adapterPosition);
                    }
                }
            });

            generateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int adapterPosition = holder.getAdapterPosition();
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        onGenerateButtonClick(adapterPosition);
                    }
                }
            });
        }



        @Override
        public int getItemCount() {
            return fileList.size();
        }

        public class FileViewHolder extends RecyclerView.ViewHolder {
            public TextView fileNameTextView;
            public TextView fileContentTextView;

            public Button deleteButton;

            public Button generateButton;

            public FileViewHolder(@NonNull View itemView) {
                super(itemView);
                fileNameTextView = itemView.findViewById(R.id.fileNameTextView);
                fileContentTextView = itemView.findViewById(R.id.fileContentTextView);
                deleteButton = itemView.findViewById(R.id.deleteButton);
                generateButton = itemView.findViewById(R.id.generateButton);

            }
        }

        public void onDeleteButtonClick(int position) {
            File file = fileList.get(position);
            if (file.exists()) {
                file.delete();
                fileList.remove(position);
                fileAdapter.notifyItemRemoved(position);
            }
        }

        private String getFileTypeFromContent(String fileContent) {
            String[] lines = fileContent.split("\n");
            if (lines.length >= 3) {
                String thirdLine = lines[2];
                String fileType = getFileTypeFromLine(thirdLine);
                return fileType;
            }
            return "";
        }

        private String getFileTypeFromLine(String line) {
            Map<String, Class<?>> fileTypeMap = new HashMap<>();
            fileTypeMap.put("PDF417", PDF417.class);
            fileTypeMap.put("QR Code", QRCODE.class);
            fileTypeMap.put("AZTEC", AZTEC.class);
            fileTypeMap.put("Codabar", Codabar.class);
            fileTypeMap.put("Code39", Code39.class);
            fileTypeMap.put("Code93", Code93.class);
            fileTypeMap.put("Code128", Code128.class);
            fileTypeMap.put("DATAMATRIX", DATAMATRIX.class);
            fileTypeMap.put("EAN8", EAN8.class);
            fileTypeMap.put("EAN13", EAN13.class);
            fileTypeMap.put("ITF", ITF.class);
            fileTypeMap.put("QRCODEMOMMO", QRCODEMOMMO.class);
            fileTypeMap.put("UPCA", UPCA.class);
            fileTypeMap.put("UPCE", UPCE.class);

            for (Map.Entry<String, Class<?>> entry : fileTypeMap.entrySet()) {
                if (line.contains(entry.getKey())) {
                    return entry.getKey();
                }
            }

            return "";
        }

        private void onGenerateButtonClick(int position) {
            File file = fileList.get(position);
            if (file.exists()) {
                String fileContent = readFileContent(file);
                String fileType = getFileTypeFromContent(fileContent);
                String fileText = getFileContentFromFile(file);


                Class<?> activityClass = getFileTypeMap().get(fileType);
                if (activityClass != null) {
                    Intent intent = new Intent(getActivity(), activityClass);
                    // Truyền dữ liệu từ tệp tin (ví dụ: nội dung) qua Intent (nếu cần)
                    // intent.putExtra("key", value);
                    intent.putExtra("text",fileText);
                    startActivity(intent);
                } else {
                    // Xử lý các loại tệp tin khác
                }
            }

        }

        private Map<String, Class<?>> getFileTypeMap() {
            Map<String, Class<?>> fileTypeMap = new HashMap<>();
            fileTypeMap.put("PDF417", PDF417.class);
            fileTypeMap.put("QR Code", QRCODE.class);
            fileTypeMap.put("AZTEC", AZTEC.class);
            fileTypeMap.put("Codabar", Codabar.class);
            fileTypeMap.put("Code39", Code39.class);
            fileTypeMap.put("Code93", Code93.class);
            fileTypeMap.put("Code128", Code128.class);
            fileTypeMap.put("DATAMATRIX", DATAMATRIX.class);
            fileTypeMap.put("EAN8", EAN8.class);
            fileTypeMap.put("EAN13", EAN13.class);
            fileTypeMap.put("ITF", ITF.class);
            fileTypeMap.put("QRCODEMOMMO", QRCODEMOMMO.class);
            fileTypeMap.put("UPCA", UPCA.class);
            fileTypeMap.put("UPCE", UPCE.class);
            return fileTypeMap;
        }

        private String getFileContentFromFile(File file) {
            StringBuilder contentBuilder = new StringBuilder();

            try {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line;

                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("Text:")) {
                        String fileContent = line.substring(6).trim();
                        contentBuilder.append(fileContent);
                        break;
                    }
                }

                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return contentBuilder.toString();
        }

        private String readFileContent(File file) {
            StringBuilder contentBuilder = new StringBuilder();

            try {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line;

                while ((line = reader.readLine()) != null) {
                    contentBuilder.append(line);
                    contentBuilder.append("\n");
                }

                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return contentBuilder.toString();
        }
    }
}
