package tech.mlsn.eatgo.fragment.barcode;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;


public class MessageDialogFragment extends DialogFragment {
    public interface MessageDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogNegativeClick(DialogFragment dialog);
    }

    private String mTitle;
    private String mMessage;
    private MessageDialogListener mListener;

    public void onCreate(Bundle state) {
        super.onCreate(state);
        setRetainInstance(true);
    }

    public static MessageDialogFragment newInstance(String title, String message, MessageDialogListener listener) {
        MessageDialogFragment fragment = new MessageDialogFragment();
        fragment.mTitle = title;
        fragment.mMessage = message;
        fragment.mListener = listener;
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(mMessage)
                .setTitle(mTitle);

        builder.setPositiveButton("VIEW", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if(mListener != null) {
                    mListener.onDialogPositiveClick(MessageDialogFragment.this);
                }
            }
        });

        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(mListener != null) {
                    mListener.onDialogNegativeClick(MessageDialogFragment.this);
                }
            }
        });
        return builder.create();
    }
}
