package algonquin00336x.cst2335.finalproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * The RemoconMainActivity class is the main activity for the Remote Control System
 * @author Byung Seon Kim
 */
public class RemoconMainActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_remocon_main);

        // Find the toolbar view inside the activity layout
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        toolbar.showOverflowMenu(); // Enable Overflow Button
        setSupportActionBar(toolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Enable Previous Button. Should be defined the previous activity in AndroidManifest.xml

        // If pressed the add floating action button, call RemoconAddList to add a new list
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RemoconMainActivity.this, RemoconAddList.class);
                startActivityForResult(intent, RemoconAddList.ADD_REMOCON_REQUEST_CODE);
            }
        });

        // create and set adapter to listview
        remoconListAdapter = new RemoconListAdapter();
        ListView remoconListView = (ListView) findViewById(R.id.remocon_listview);
        remoconListView.setAdapter(remoconListAdapter);

        /* open Remocon list database
         *  Originally, I will use below statements, but because I have to use AsyncTask,
         *  I use RemoconReadData class.*/

        /* for initialize the very first data, open and then close */
        RemoconDBHelper dbHelper = new RemoconDBHelper(RemoconMainActivity.this);
//        // read all remocon list data user entered
//        List<RemoconListStructure> list = dbHelper.getAllRemoconLists();
//        // close database
        dbHelper.closeDatabase();
//        // display all data inputed by user
//        for (RemoconListStructure item : list) {
//            remoconListAdapter.addItem( item );
//        }
        new RemoconReadData(this, new RemoconReadData.AsyncResponse() {
            @Override
            public void processFinish(ArrayList<RemoconListStructure> result) {
                for (RemoconListStructure item : result) {
                    remoconListAdapter.addItem( item );
                }
            }
        }).execute(null, null, null);

        if (findViewById(R.id.remocon_detail_container) != null) {
            // The detail container view will be present only in the large-screen layouts (res/values-w900dp)
            // and the landscape mode. If this view is present, then the activity should be in two-pane mode.
            mTwoPane = true;
        }

        // if a user taps short, shows detail information according to the Type of Remocon
        remoconListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final int keyId = ((RemoconListStructure) remoconListAdapter.getItem(position)).getId();
                if (mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putInt(RemoconDetailFragment.ARG_KEY_ID, keyId);
                    RemoconDetailFragment fragment = new RemoconDetailFragment();
                    fragment.setArguments(arguments);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.remocon_detail_container, fragment)
                            .commit();
                } else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, RemoconDetailActivity.class);
                    intent.putExtra(RemoconDetailFragment.ARG_KEY_ID, keyId);
                    context.startActivity(intent);
                }
            }
        });

        // if a user taps long, delete the data, but if only one data remain, I can't delete it.
        remoconListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                final Context context = view.getContext();

                if (remoconListAdapter.getCount() > 1) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    // builder.setIcon(R.drawable.ic_weather);
                    builder.setTitle(R.string.title_confirm);
                    builder.setMessage(R.string.message_delete_confirm);
                    builder.setPositiveButton(R.string.positive_button, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Delete the item from database
                            RemoconDBHelper dbHelper = new RemoconDBHelper( context );
                            RemoconListStructure removeItem = (RemoconListStructure) remoconListAdapter.getItem( position );
                            dbHelper.deleteRemocon( removeItem );
                            dbHelper.closeDatabase();
                            // Remove the item from ListView
                            remoconListAdapter.removeItem( position );
                        }
                    });
                    builder.setNegativeButton(R.string.cancel_button, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) { }
                    });
                    builder.show();
                } else {
                    Snackbar.make(view, R.string.message_nomore, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                return true;
            }
        });
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
                intent = new Intent(RemoconMainActivity.this, RemoconMainActivity.class);
                startActivity(intent);
                return true;

            case R.id.menu_2:
                // User chose the "Second menu" action
                intent = new Intent(RemoconMainActivity.this, RemoconMainActivity.class);
                startActivity(intent);
                return true;

            case R.id.menu_3:
                // User chose the "Third menu" action
                intent = new Intent(RemoconMainActivity.this, RemoconMainActivity.class);
                startActivity(intent);
                return true;

            case R.id.menu_4:
                // User chose the "Living room"
                intent = new Intent(RemoconMainActivity.this, RemoconMainActivity.class);
                startActivity(intent);
                return true;

            case R.id.menu_about:
                // User chose the "Help" action
                AlertDialog.Builder builder = new AlertDialog.Builder(RemoconMainActivity.this);
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

    /**
     * Called when an activity you launched exits, giving you the requestCode you started it with,
     * the resultCode it returned, and any additional data from it.
     * The resultCode will be RESULT_CANCELED if the activity explicitly returned that,
     * didn't return any result, or crashed during its operation.
     * You will receive this call immediately before onResume() when your activity is re-starting.
     * This method is never invoked if your activity sets noHistory to true.
     * @param requestCode The integer request code originally supplied to startActivityForResult(),
     *                    allowing you to identify who this result came from.
     * @param resultCode The integer result code returned by the child activity through its setResult().
     * @param data An Intent, which can return result data to the caller (various data can be attached to Intent "extras").
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case RemoconAddList.ADD_REMOCON_REQUEST_CODE: // requestCode from RemoconAddList
                if (resultCode == Activity.RESULT_OK) {
                    Log.i(LOG, "RESULT_OK: " + data.getIntExtra("keyId", DEFAULT_KEY_ID));
                    // insert the data a user chose in RemoconAddList activity
                    int keyId = data.getIntExtra("keyId", DEFAULT_KEY_ID);
                    RemoconDBHelper dbHelper = new RemoconDBHelper(RemoconMainActivity.this);
                    RemoconListStructure remocon = dbHelper.getRemoconList( keyId );
                    dbHelper.closeDatabase();
                    remoconListAdapter.addItem( remocon );
                }
        }
    }

    // HELPER METHODS ----------------------------------------- the document method of Regenald Dyer
    // ENTRY POINT for STAND-ALONE OPERATION ------------------ the document method of Regenald Dyer
    // ATTRIBUTES --------------------------------------------- the document method of Regenald Dyer
    // logcat constant variable of the activity
    private static final String LOG = "RemoconMainActivity";
    // default key id
    private static final int DEFAULT_KEY_ID = 0;
    /**
     * The remoconListAdapter is the attribute for the Extended Adapter that is the bridge
     * between a ListView and the data that backs the list.
     */
    private RemoconListAdapter remoconListAdapter;
    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet device.
     */
    private boolean mTwoPane;
}