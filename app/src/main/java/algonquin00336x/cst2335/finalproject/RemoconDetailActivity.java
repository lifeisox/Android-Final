package algonquin00336x.cst2335.finalproject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

/**
 * RemoconDetailActivity is the class for showing the detail screen for one-pain mode
 * like portrait mode of smart phone.
 * @author Byung Seon Kim
 */
public class RemoconDetailActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_remocon_detail);

        /* Call the activity's setSupportActionBar() method, and pass the activity's toolbar.
           This method sets the toolbar as the app bar for the activity.
        */
        // Find the toolbar view inside the activity layout
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        toolbar.showOverflowMenu(); // Enable Overflow Button
        setSupportActionBar(toolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Enable Previous Button

        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putInt(RemoconDetailFragment.ARG_KEY_ID,
                    getIntent().getIntExtra(RemoconDetailFragment.ARG_KEY_ID, 0));
            RemoconDetailFragment fragment = new RemoconDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.remocon_detail_container, fragment)
                    .commit();
        }
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
        // We will be disable itself on Action bar
        menu.findItem(R.id.menu_1).setVisible(true);
        menu.findItem(R.id.menu_2).setVisible(true);
        menu.findItem(R.id.menu_3).setVisible(true);
        menu.findItem(R.id.menu_4).setVisible(true);
        menu.findItem(R.id.menu_about).setTitle(R.string.title_help);
        return true;
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
        Intent intent;
        switch (item.getItemId()) {
            case R.id.menu_1:
                // User chose the "First menu" action
                intent = new Intent(RemoconDetailActivity.this, RemoconMainActivity.class);
                startActivity(intent);
                return true;

            case R.id.menu_2:
                // User chose the "Second menu" action
                intent = new Intent(RemoconDetailActivity.this, RemoconMainActivity.class);
                startActivity(intent);
                return true;

            case R.id.menu_3:
                // User chose the "Third menu" action
                intent = new Intent(RemoconDetailActivity.this, RemoconMainActivity.class);
                startActivity(intent);
                return true;

            case R.id.menu_4:
                // User chose the "Living room"
                intent = new Intent(RemoconDetailActivity.this, RemoconMainActivity.class);
                startActivity(intent);
                return true;

            case R.id.menu_about:
                // User chose the "Help" action
                AlertDialog.Builder builder = new AlertDialog.Builder(RemoconDetailActivity.this);
                // builder.setIcon(R.drawable.ic_weather);
                builder.setTitle(R.string.title_help);
                builder.setMessage(R.string.help_4);
                builder.setPositiveButton(R.string.positive_button, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), R.string.thank_message, Toast.LENGTH_LONG).show();
                    }
                });
                builder.show();
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }
}
