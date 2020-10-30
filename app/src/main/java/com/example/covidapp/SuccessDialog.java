package com.example.covidapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDialogFragment;

public class SuccessDialog extends AppCompatDialogFragment{

    public Dialog onCreateDialog (Bundle savedInstanceState)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Verification Status").setMessage("Your Verification Code has been ACCEPTED and your status is shared").setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });

        return builder.create();
    }

}
