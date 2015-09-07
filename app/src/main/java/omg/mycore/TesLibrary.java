package omg.mycore;

import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Locale;

public class TesLibrary extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Core.getInstance(this).singleInputDialog("input", "Please input", new Core.OnInputDialogCloseListener() {
            @Override
            public void onOK(String value) {
                Toast.makeText(TesLibrary.this, "" + value, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {

            }
        });

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Core.getInstance(TesLibrary.this).showAlertDialog("Title", "Details", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Core.getInstance(TesLibrary.this).showProgressDialog("Loading", "Please wait", true, null);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(5000);
                                    Core.getInstance(TesLibrary.this).removeProgressDialog(new DialogInterface.OnDismissListener() {
                                        @Override
                                        public void onDismiss(DialogInterface dialog) {

                                        }
                                    });
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    }
                });
            }
        });

        Button confirm = (Button) findViewById(R.id.button2);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Core.getInstance(TesLibrary.this).showConfirmDialog("Delete", "Are you sure to delete", new Core.ConfirmDialogListener() {
                    @Override
                    public void confirm() {
                        Toast.makeText(TesLibrary.this, "Confirm", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void cancel() {
                        Toast.makeText(TesLibrary.this, "Cancel", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        Button button1 = (Button) findViewById(R.id.button3);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] val = {"A", "C", "D", "E"};
                Core.getInstance(TesLibrary.this).showListDialog("List", null, val, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(TesLibrary.this, val[which].toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        Button btn4 = (Button) findViewById(R.id.button4);
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] val = {"A", "C", "D", "E"};
                Core.getInstance(TesLibrary.this).showListDialogSingleChoice("List", null, val, new Core.SingleChoiceDialogListener() {
                    @Override
                    public void confirm(int choice) {
                        Log.d("Select", val[choice]);
                    }

                    @Override
                    public void cancel() {

                    }
                });
            }
        });

        Button btn5 = (Button) findViewById(R.id.button5);
        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] val = {"A", "C", "D", "E"};
                Core.getInstance(TesLibrary.this).showListDialogMultiChoice("List", null, val, new Core.MultiChoiceDialogListener() {
                    @Override
                    public void confirm(boolean[] choice) {
                        for (int i = 0; i < choice.length; i++) {
                            if (choice[i]) {
                                Log.d("Select", val[i]);
                            }
                        }
                    }

                    @Override
                    public void cancel() {

                    }
                });
            }
        });

        EditText editText = (EditText) findViewById(R.id.editText);
        editText.setText(Core.getInstance(this).numberCurrency(12000,new Locale("th_TH")));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
