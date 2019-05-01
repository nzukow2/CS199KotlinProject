package com.example.thechallen_ge;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;

public class Dialog extends AppCompatDialogFragment {

    private DialogListener listener;
    AlertDialog.Builder builder;

    public interface DialogListener {
        void onOkayClicked();
    }

    @Override
    public android.app.Dialog onCreateDialog(Bundle savedInstanceState) {
        builder = new AlertDialog.Builder(getActivity());
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onOkayClicked();
            }
        });

        if (getArguments() != null) {
            String title = getArguments().getString("title");
            String message = getArguments().getString("message");

            builder.setTitle(title);
            builder.setMessage(message);
        }

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (DialogListener) context;
        } catch (ClassCastException e) {

        }
    }
}