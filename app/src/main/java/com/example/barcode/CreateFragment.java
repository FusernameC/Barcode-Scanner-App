package com.example.barcode;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;



import java.util.ArrayList;


public class CreateFragment extends Fragment {
    public CreateFragment() {
        // Required empty public constructor
    }
    int image[]={R.drawable.qrcode,R.drawable.datamatrix,R.drawable.barcode,R.drawable.qrcode,R.drawable.barcode,R.drawable.barcode,R.drawable.barcode,R.drawable.barcode,R.drawable.barcode,R.drawable.barcode,R.drawable.barcode,R.drawable.barcode,R.drawable.barcode,R.drawable.barcode  };

    String name[] ={"QR Code", "Data Matrix", "PDF 417", "Aztec","EAN13", "EAN 8", "UPC E", "UPC A","Code 128","Code 93", "Code 39", "Codabar", "ITF"};
    String content [] ={"text", "text without special characters", "text", "text without special characters", "12digits + 1 checksum digit", "8 digits","7 digits + 1 checksum digit", "11 digits + 1 checksum digit",
    "text without special characters", "text in upper case without special characters","text in upper case without special characters", "digits", " even number of digits"};
    ArrayList<Createlist> mylist;
    MyArrayAdapter myadapter;
    ListView lv;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_create, container, false);

        lv = view.findViewById(R.id.lv);
        MyArrayAdapter myArrayAdapter = new MyArrayAdapter(requireContext(),name,image,content);
        lv.setAdapter(myArrayAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if(position == 0){
                    Intent intent = new Intent();
                    intent.setClass(requireContext(),QRCODE.class);
                    startActivity(intent);
                }
                if(position == 1){
                    Intent intent = new Intent();
                    intent.setClass(requireContext(), DATAMATRIX.class);
                    startActivity(intent);
                }
                if(position == 2){
                    Intent intent = new Intent();
                    intent.setClass(requireContext(), PDF417.class);
                    startActivity(intent);
                }
                if(position == 3){
                    Intent intent = new Intent();
                    intent.setClass(requireContext(), AZTEC.class);
                    startActivity(intent);
                }
                if(position == 4){
                    Intent intent = new Intent();
                    intent.setClass(requireContext(), EAN13.class);
                    startActivity(intent);
                }
                if(position == 5){
                    Intent intent = new Intent();
                    intent.setClass(requireContext(), EAN8.class);
                    startActivity(intent);
                }
                if(position == 6){
                    Intent intent = new Intent();
                    intent.setClass(requireContext(), UPCE.class);
                    startActivity(intent);
                }
                if(position == 7){
                    Intent intent = new Intent();
                    intent.setClass(requireContext(), UPCA.class);
                    startActivity(intent);
                }
                if(position == 8){
                    Intent intent = new Intent();
                    intent.setClass(requireContext(), Code128.class);
                    startActivity(intent);
                }
                if(position == 9){
                    Intent intent = new Intent();
                    intent.setClass(requireContext(), Code93.class);
                    startActivity(intent);
                }
                if(position == 10){
                    Intent intent = new Intent();
                    intent.setClass(requireContext(), Code39.class);
                    startActivity(intent);
                }
                if(position == 11){
                    Intent intent = new Intent();
                    intent.setClass(requireContext(), Codabar.class);
                    startActivity(intent);
                }
                if(position == 12){
                    Intent intent = new Intent();
                    intent.setClass(requireContext(), ITF.class);
                    startActivity(intent);
                }
            }
        });
        /*mylist = new ArrayList<>();
        mylist.add(new Createlist(R.drawable.qrcode,"QR Code"));
        mylist.add(new Createlist(R.drawable.datamatrix,"Data Matrix"));
        mylist.add(new Createlist(R.drawable.barcode,"PDF 417"));
        mylist.add(new Createlist(R.drawable.qrcode,"Aztec"));
        mylist.add(new Createlist(R.drawable.barcode,"EAN13"));
        mylist.add(new Createlist(R.drawable.barcode,"EAN 8"));
        mylist.add(new Createlist(R.drawable.barcode,"UPC E"));
        mylist.add(new Createlist(R.drawable.barcode,"UPC A"));
        mylist.add(new Createlist(R.drawable.barcode,"Code 128"));
        mylist.add(new Createlist(R.drawable.barcode,"Code 93"));
        mylist.add(new Createlist(R.drawable.barcode,"Code 39"));
        mylist.add(new Createlist(R.drawable.barcode,"Codabar"));
        mylist.add(new Createlist(R.drawable.barcode,"PDF ITF"));

        myadapter = new MyArrayAdapter(requireContext(),R.layout.fragment_create,mylist);
        lv.setAdapter(myadapter);*/


       /* mylist = new ArrayList<>(); //tao moi mang rong
        for (int i = 0; i < name.length; i++){
            mylist.add(new Createlist(image[i], name[i]));
        }
        myadapter = new MyArrayAdapter(requireContext(), R.layout.fragment_create, mylist);

        lv.setAdapter(myadapter);*/

        return view;
    }
}