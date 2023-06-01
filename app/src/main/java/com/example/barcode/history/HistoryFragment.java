package com.example.barcode.history;

import android.content.pm.PackageManager;
import android.os.Bundle;

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
import android.widget.TextView;

import com.example.barcode.R;

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
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

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
        }

        @Override
        public int getItemCount() {
            return fileList.size();
        }

        public class FileViewHolder extends RecyclerView.ViewHolder {
            public TextView fileNameTextView;
            public TextView fileContentTextView;

            public FileViewHolder(@NonNull View itemView) {
                super(itemView);
                fileNameTextView = itemView.findViewById(R.id.fileNameTextView);
                fileContentTextView = itemView.findViewById(R.id.fileContentTextView);
            }
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
