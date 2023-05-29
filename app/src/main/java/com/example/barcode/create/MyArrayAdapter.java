package com.example.barcode.create;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.barcode.R;

import java.util.ArrayList;
import java.util.List;

public class MyArrayAdapter extends BaseAdapter
{
    private Context context;
    private int listImages[];
    private String listnames[];
    private String listcontents [];
    //private List<CreateList> arraylist;



    public MyArrayAdapter(Context context, String[] name, int[] image, String [] content) {
        this.context = context;
        this.listImages = image;
        this.listnames = name;
        this.listcontents = content;
    }

    @Override
    public int getCount() {
        return listnames.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        convertView = inflater.inflate(R.layout.fragment_list_creat,null);
        //CreateList createlist = arraylist.get(position);
        TextView textView1 = convertView.findViewById(R.id.textcode);
        ImageView imageViewV = convertView.findViewById(R.id.imgqrcode);
        TextView textView2 = convertView.findViewById(R.id.content);

        textView1.setText(listnames[position]);
        textView2.setText(listcontents[position]);
        imageViewV.setImageResource(listImages[position]);
        return convertView;
    }

    /*@NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // tao de chua layout
        LayoutInflater myflater = LayoutInflater.from(context);
        *//*LayoutInflater myflater = context.getLayoutInflater();*//*
        // dat layout leen ddeer tao thanh null
        convertView = myflater.inflate(idLayout,null);
        //lay 1 phan tu trong mang
        Createlist mycreate = myList.get(position);
        ImageView img_create = convertView.findViewById(R.id.imgqrcode);
        //img_create.setImageResource(mycreate.getImage());
        //khai bao tham chieu id va hien thi ten qrcode va barcode len textView
        TextView txt_create = convertView.findViewById(R.id.textcode);
//        txt_create.setText(mycreate.getName());
        return convertView;

    }*/
}
