package com.example.bloodgenix;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

import com.google.android.material.bottomsheet.BottomSheetDialog;

public class LoadingDialog {

    Activity activity;
    AlertDialog alertDialog;
    BottomSheetDialog sheetDialog;

    public LoadingDialog(Activity myActivity){
        activity = myActivity;
    }


    public void startDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.loading_layout,null));
        builder.setCancelable(false);

        alertDialog= builder.create();
        alertDialog.show();

    }


    public void dismissDialog(){
        alertDialog.dismiss();
    }


}
