package io.sihrc.irksome;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.sihrc.irksome.fragments.SearchFragment;
import io.sihrc.irksome.network.IrksomeClient;

public class MainActivity extends FragmentActivity {
    final static public int REQ_CODE_SPEECH_INPUT = 1;
    static public boolean requestCancelled = false;

    IrksomeClient client;
    FragmentManager fragmentManager;

    @Bind(R.id.header)
    TextView header;

    public IrksomeClient getClient() {
        return client;
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            Toast.makeText(this, "Sorry, something went wrong with the microphone", Toast.LENGTH_SHORT).show();
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override public void onBackPressed() {
        if (fragmentManager == null) {
            return;
        }
        Fragment fragment = fragmentManager.findFragmentById(R.id.container);
        if (!(fragment instanceof SearchFragment)) {
            switchFragment(SearchFragment.getInstance(this), false);
            requestCancelled = true;
            setHeader("SEARCH");
        } else {
            new AlertDialog.Builder(this).setTitle("Exit").setMessage("Are you sure?")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialog, int which) {
                        MainActivity.this.finish();
                    }
                }).setNegativeButton("Go Back", new DialogInterface.OnClickListener() {
                @Override public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).show();
        }
    }

    public void switchFragment(Fragment fragment, boolean open) {
        fragmentManager.beginTransaction()
            .replace(R.id.container, fragment).setTransition(
            open ? FragmentTransaction.TRANSIT_FRAGMENT_OPEN : FragmentTransaction.TRANSIT_FRAGMENT_CLOSE
        ).commit();
    }

    public void setHeader(String text) {
        header.setText(text);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        client = new IrksomeClient();

        ButterKnife.bind(this);
        fragmentManager = getSupportFragmentManager();
        if (savedInstanceState == null) {
            Fragment fragment = SearchFragment.getInstance(this);
            fragmentManager.beginTransaction()
                .replace(R.id.container, fragment, fragment.getClass().getName())
                .commit();
        }
    }
}
