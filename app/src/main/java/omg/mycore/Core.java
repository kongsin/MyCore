package omg.mycore;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by kongsin on 9/7/15.
 */
public class Core {

    private AlertDialog.Builder dialog;
    private ProgressDialog progressDialog;
    private Context context;
    private AlertDialog inputDialog;
    private TextView title, details;
    private EditText editText;
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

    public void singleInputDialog(String titleText, String detailText, final OnInputDialogCloseListener listener) {
        AlertDialog.Builder inputRouteDialog = new AlertDialog.Builder(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.single_input_dialog, null);
        title = (TextView) dialogView.findViewById(R.id.title);
        details = (TextView) dialogView.findViewById(R.id.details);
        editText = (EditText) dialogView.findViewById(R.id.input_text);

        if (detailText == null || detailText.equals("")) {
            details.setVisibility(View.GONE);
        } else {
            details.setText(detailText);
        }

        if (titleText == null || titleText.equals("")) {
            title.setVisibility(View.GONE);
        } else {
            title.setText(titleText);
        }

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (editText.getText().toString().length() > 0) {
                    inputDialog.getButton(android.support.v7.app.AlertDialog.BUTTON_NEUTRAL).setEnabled(true);
                } else {
                    inputDialog.getButton(android.support.v7.app.AlertDialog.BUTTON_NEUTRAL).setEnabled(false);
                }
            }
        });
        inputRouteDialog.setView(dialogView);
        inputRouteDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (editText.getText().length() > 0) {
                    listener.onOK(editText.getText().toString());
                } else {
                    listener.onCancel();
                    inputDialog.cancel();
                    inputDialog.dismiss();
                }
            }
        });
        inputRouteDialog.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onCancel();
                inputDialog.cancel();
                inputDialog.dismiss();
            }
        });

        inputRouteDialog.setCancelable(false);
        inputDialog = inputRouteDialog.create();
        inputDialog.show();
        if (editText.getText().toString().trim().length() <= 0) {
            inputDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setEnabled(false);
        }
    }

    public static boolean isServiceRunning(Context context,Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static boolean isNetworkConnected(Context context){
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public static double haversine(double lat1, double lon1, double lat2, double lon2) {
        double R = 6372.8; // In kilometers
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        double a = Math.pow(Math.sin(dLat / 2), 2) + Math.pow(Math.sin(dLon / 2), 2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.asin(Math.sqrt(a));
        return R * c;
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

    public interface OnInputDialogCloseListener {
        void onOK(String value);
        void onCancel();
    }

}
