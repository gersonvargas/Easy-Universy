package com.herprogramacion.geekyweb;

import android.app.AlertDialog;
import android.app.Dialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class Dialogo extends DialogFragment {
    /*
    * FragmentManager fragmentManager = getSupportFragmentManager();
                Dialogo dialogo = new Dialogo();
                dialogo.show(fragmentManager, "tagAlerta");
    * */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder =
                new AlertDialog.Builder(getActivity());

        builder.setMessage("Esto es un mensaje de alerta.")
                .setTitle("Información")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        return builder.create();
    }
}
