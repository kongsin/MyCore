package omg.mycore;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by kongsin on 9/7/15.
 */
public class Core {

    private AlertDialog.Builder dialog;
    private ProgressDialog progressDialog;
    private Context context;
    private static Core core;

    Core(Context context) {
        this.context = context;
    }

    public static Core getInstance(Context context) {
        if (core == null) {
            core = new Core(context);
        }
        return core;
    }

    public void showAlertDialog(String title, String detail, DialogInterface.OnClickListener listener) {
        dialog = new AlertDialog.Builder(context);
        dialog.setTitle(title);
        dialog.setMessage(detail);
        dialog.setNegativeButton("Close", listener);
        dialog.show();
    }

    public void showProgressDialog(String title, String details, boolean cancelable, DialogInterface.OnClickListener listener) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle(title);
        progressDialog.setMessage(details);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(cancelable);
        if (cancelable) {
            progressDialog.setButton("Cancel", listener);
        }
        progressDialog.show();
    }

    public void removeProgressDialog(DialogInterface.OnDismissListener listener) {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
            progressDialog.setOnDismissListener(listener);
        }
    }

    public void showConfirmDialog(String title, String detail, final ConfirmDialogListener listener) {
        dialog = new AlertDialog.Builder(context);
        dialog.setTitle(title);
        dialog.setMessage(detail);
        dialog.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.confirm();
            }
        });
        dialog.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.cancel();
            }
        });
        dialog.show();
    }

    public void showListDialog(String title, String details, CharSequence[] arrayList, DialogInterface.OnClickListener listener) {
        dialog = new AlertDialog.Builder(context);
        dialog.setTitle(title);
        dialog.setMessage(details);
        dialog.setItems(arrayList, listener);
        dialog.setNegativeButton("Cancel", null);
        dialog.show();
    }

    public void showListDialogSingleChoice(String title, String details, CharSequence[] arrayList, final SingleChoiceDialogListener listener) {
        final int[] choice = new int[1];
        dialog = new AlertDialog.Builder(context);
        dialog.setTitle(title);
        dialog.setMessage(details);
        dialog.setSingleChoiceItems(arrayList, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                choice[0] = which;
            }
        });
        dialog.setNegativeButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.confirm(choice[0]);
            }
        });
        dialog.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.cancel();
            }
        });
        dialog.show();
    }

    public void showListDialogMultiChoice(String title, String details, CharSequence[] arrayList, final MultiChoiceDialogListener listener) {
        final boolean[] choice = new boolean[arrayList.length];
        dialog = new AlertDialog.Builder(context);
        dialog.setTitle(title);
        dialog.setMessage(details);
        dialog.setMultiChoiceItems(arrayList, choice, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                choice[which] = isChecked;
            }
        });
        dialog.setNegativeButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.confirm(choice);
            }
        });
        dialog.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.cancel();
            }
        });
        dialog.show();
    }

    public interface MultiChoiceDialogListener {
        void confirm(boolean[] choice);

        void cancel();
    }

    public interface SingleChoiceDialogListener {
        void confirm(int choice);

        void cancel();
    }

    public interface ConfirmDialogListener {
        void confirm();

        void cancel();
    }

}
