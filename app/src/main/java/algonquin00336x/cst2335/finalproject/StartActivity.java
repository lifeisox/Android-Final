package algonquin00336x.cst2335.finalproject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

public class StartActivity extends AppCompatActivity {

    // CONSTRUCTORS ------------------------------------------- the document method of Regenald Dyer
    // ACCESSORS ---------------------------------------------- the document method of Regenald Dyer
    // MODIFIERS ---------------------------------------------- the document method of Regenald Dyer
    // NORMAL BEHAVIOR ---------------------------------------- the document method of Regenald Dyer

    /**
     * Perform initialization of all fragments and loaders.
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down
     *                           then this Bundle contains the data it most recently supplied in
     *                           onSaveInstanceState(Bundle). Note: Otherwise it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        Log.i(LOG, "Start StartActivity!!!");

        // Find the toolbar view inside the activity layout
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        toolbar.showOverflowMenu();
        setSupportActionBar(toolbar);

        //  callback for clicking the "Home" item
        TextView author1 = (TextView) findViewById(R.id.author1_title_text);
        author1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity1();
            }
        });
        //  callback for clicking the "Kitchen" item
        TextView author2 = (TextView) findViewById(R.id.author2_title_text);
        author2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity2();
            }
        });
        //  callback for clicking the "Automobile" item
        TextView author3 = (TextView) findViewById(R.id.author3_title_text);
        author3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity3();
            }
        });
        //  callback for clicking the  "Living room" item
        TextView author4 = (TextView) findViewById(R.id.author4_title_text);
        author4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity4();
            }
        });

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    /**
     * Initialize the contents of the Activity's standard options menu.
     * This is only called once, the first time the options menu is displayed.
     * @param menu The options menu in which you place your items.
     * @return You must return true for the menu to be displayed; if you return false it will not be shown.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_actionbar, menu);
        // If current view is not Home, we will display "Help" instead of "About"
        menu.findItem(R.id.menu_1).setVisible(true);
        menu.findItem(R.id.menu_2).setVisible(true);
        menu.findItem(R.id.menu_3).setVisible(true);
        menu.findItem(R.id.menu_4).setVisible(true);
        menu.findItem(R.id.menu_about).setVisible(true);
        menu.findItem(R.id.menu_about).setTitle(R.string.title_about);

        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Often users are spamming the back button in your app to go back to the main screen.
     * This means they can exit your app by accident, and you don’t want that.
     * In order to prevent this, we are going to override the onBackPressed() method
     * so the user will exit the app only on double tapping the back button.
     */
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        // getWindow().getDecorView().getRootView() is Root view of the app
        Snackbar.make(getWindow().getDecorView().getRootView(), R.string.message_ask_back_button_again,
                Snackbar.LENGTH_SHORT).show();

        // We are using postDelayed(runnable, time) because we want the boolean variable to reset itself
        // if the user didn’t double tap.
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    /**
     * When the user selects one of the app bar items, the system calls your activity's
     * onOptionsItemSelected() callback method, and passes a MenuItem object to indicate
     * which item was clicked. In your implementation of onOptionsItemSelected(),
     * call the MenuItem.getItemId() method to determine which item was pressed.
     * The ID returned matches the value you declared in the corresponding <item> element's
     * android:id attribute.
     * @param item The menu item that was selected.
     * @return boolean Return false to allow normal menu processing to proceed, true to consume it here.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_1:
                // User chose the "Home" item
                startActivity1();
                break;
            case R.id.menu_2:
                // User chose the "Kitchen" item
                startActivity2();
                break;
            case R.id.menu_3:
                // User chose the "Automobile" item
                startActivity3();
                break;
            case R.id.menu_4:
                // User chose the "Living room" item
                startActivity4();
                break;
            case R.id.menu_about:
                // User chose the "About" action
                AlertDialog.Builder builder = new AlertDialog.Builder(StartActivity.this);
                // builder.setIcon(R.drawable.ic_weather);
                builder.setTitle(R.string.title_about);
                builder.setMessage(R.string.about_message);
                builder.setPositiveButton(R.string.positive_button, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), R.string.thank_message, Toast.LENGTH_LONG).show();
                    }
                });
                builder.show();
                break;
        }

        return true;
    }


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Start Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    /**
     * Called after onCreate(Bundle) — or after onRestart() when the activity had been stopped,
     * but is now again being displayed to the user. It will be followed by onResume().
     */
    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    /**
     * Called when you are no longer visible to the user. You will next receive either onRestart(),
     * onDestroy(), or nothing, depending on later user activity.
     */
    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    // HELPER METHODS ----------------------------------------- the document method of Regenald Dyer
    // intent "Home" app, Change RemoconMainActivity.class to your own Main Activity
    private void startActivity1() {
        Intent intent = new Intent(StartActivity.this, RemoconMainActivity.class);
        startActivity(intent);
    }
    // intent "Kitchen" app, Change RemoconMainActivity.class to your own Main Activity
    private void startActivity2() {
        Intent intent = new Intent(StartActivity.this, RemoconMainActivity.class);
        startActivity(intent);
    }
    // intent "Automobile" app, Change RemoconMainActivity.class to your own Main Activity
    private void startActivity3() {
        Intent intent = new Intent(StartActivity.this, RemoconMainActivity.class);
        startActivity(intent);
    }
    // intent "Living room" app
    private void startActivity4() {
        Intent intent = new Intent(StartActivity.this, RemoconMainActivity.class);
        startActivity(intent);
    }

    // ENTRY POINT for STAND-ALONE OPERATION ------------------ the document method of Regenald Dyer
    // ATTRIBUTES --------------------------------------------- the document method of Regenald Dyer
    // logcat constant variable of the activity
    private static final String LOG = "StartActivity";
    /** the attribute for checking if BACK button is pressed twice */
    private boolean doubleBackToExitPressedOnce = false;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
}