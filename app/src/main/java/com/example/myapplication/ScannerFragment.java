package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

public class ScannerFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scanner,container,false);
        Button scanButton = view.findViewById(R.id.scan_button);
        scanButton.setOnClickListener(view1 -> {
            Intent intent = new Intent(getActivity(), CameraActivity.class);
            startActivity(intent);
        });
        return view;
    }
}