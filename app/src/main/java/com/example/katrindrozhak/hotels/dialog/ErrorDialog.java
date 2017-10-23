package com.example.katrindrozhak.hotels.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.example.katrindrozhak.hotels.R;

public class ErrorDialog extends DialogFragment {

    private ErrorDialogListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        dialogBuilder.setTitle(R.string.string_error).setMessage(R.string.string_error_message)
                .setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        dismiss();
                        if (listener != null) {
                            listener.okPressed();
                        }
                    }
                });

        return dialogBuilder.create();
    }

    public void setListener(ErrorDialogListener listener) {
        this.listener = listener;
    }
}
