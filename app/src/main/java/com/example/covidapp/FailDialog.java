package com.example.covidapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDialogFragment;

public class FailDialog extends AppCompatDialogFragment{

    public Dialog onCreateDialog (Bundle savedInstanceState)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Verification Status").setMessage("Your Verification Code has been REJECTED or the server is under maintenance, Please try again later").setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });

        return builder.create();
    }

}
