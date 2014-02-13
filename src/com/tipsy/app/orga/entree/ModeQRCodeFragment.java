package com.tipsy.app.orga.entree;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.abhi.barcode.frag.libv2.BarcodeFragment;
import com.abhi.barcode.frag.libv2.IScanResultHandler;
import com.abhi.barcode.frag.libv2.ScanResult;
import com.tipsy.app.R;

/**
 * Created by tech on 12/02/14.
 */
public class ModeQRCodeFragment extends Fragment {

    BarcodeFragment fragment;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_scan_qrcode, container, false);
        return view;
    }

    /*
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 0) {
            if (resultCode == getActivity().RESULT_OK) {
                String contents = intent.getStringExtra("SCAN_RESULT");
                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
                Log.i("xZing", "contents: " + contents + " format: " + format);
            }
            else if(resultCode == getActivity().RESULT_CANCELED) {
                Log.i("xZing", "Cancelled");
            }
        }

    }
    */
}


