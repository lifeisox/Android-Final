package algonquin00336x.cst2335.finalproject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * The RemoconAddList is the class to add Remote control list which is used by user
 * @author Byung Seon Kim
 */
public class RemoconAddList extends AppCompatActivity {

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
        final List<String> manufacturerList;
        final ArrayAdapter<String> manufacturerAdapter;
        final EditText searchEditText;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remocon_add_list);

        // Find the toolbar view inside the activity layout
        Toolbar toolbar = (Toolbar) findViewById(R.id.search_toolbar);
        toolbar.showOverflowMenu();
        setSupportActionBar(toolbar);

        Log.i(LOG, "Start RemoconAddList!!!");
        // open remote control database
        RemoconDBHelper dbHelper = new RemoconDBHelper(RemoconAddList.this);
        // read all manufacturer data
        manufacturerList = dbHelper.getAllManufactLists();
        // close database
        dbHelper.closeDatabase();

        manufacturerAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, manufacturerList);

        /* read Manufacturer list and fill Listview */
        ListView manufacturerListView = (ListView) findViewById(R.id.search_manufacturer_listview);
        manufacturerListView.setAdapter(manufacturerAdapter);
        searchEditText = (EditText) findViewById(R.id.search_manufacturer);

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // When user changed the Text
                manufacturerAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        // define click event handler for CityListView
        manufacturerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                // Save data to database
                RemoconListStructure remocon = new RemoconListStructure();
                remocon.setTypeOfRemocon( typeOfRemocon );
                remocon.setPlaceToUse( installPlaceEditText.getText().toString() );
                remocon.setManufacturer( manufacturerAdapter.getItem(position) );
                remocon.setNumOfUsed( 0 );
                // open Remocon list database
                RemoconDBHelper dbHelper = new RemoconDBHelper(RemoconAddList.this);
                int keyId = (int) dbHelper.insertRemocon( remocon );
                // close database
                dbHelper.closeDatabase();
                // return to parent activity
                Intent intent = new Intent();
                intent.putExtra("keyId", keyId);
                setResult(Activity.RESULT_OK, intent);
                finish(); // finish activity
            }
        });

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        @SuppressLint("InflateParams") final View dialogView = inflater.inflate(R.layout.remocon_add_dialog, null);
        dialogBuilder.setView(dialogView);

        int color = ContextCompat.getColor(RemoconAddList.this, R.color.accent);
        installPlaceEditText = (EditText) dialogView.findViewById(R.id.place_to_use);
        type1ImageView = (ImageView) dialogView.findViewById(R.id.rcitem1_icon);
        type1ImageView.setBackgroundColor(color);
        type1ImageView.setOnClickListener(new TypeClickListener());
        type1TextView = (TextView) dialogView.findViewById(R.id.rcitem1_text);
        type1TextView.setBackgroundColor(color);
        type1TextView.setOnClickListener(new TypeClickListener());
        type2ImageView = (ImageView) dialogView.findViewById(R.id.rcitem2_icon);
        type2ImageView.setOnClickListener(new TypeClickListener());
        type2TextView = (TextView) dialogView.findViewById(R.id.rcitem2_text);
        type2TextView.setOnClickListener(new TypeClickListener());
        type3ImageView = (ImageView) dialogView.findViewById(R.id.rcitem3_icon);
        type3ImageView.setOnClickListener(new TypeClickListener());
        type3TextView = (TextView) dialogView.findViewById(R.id.rcitem3_text);
        type3TextView.setOnClickListener(new TypeClickListener());

        dialogBuilder.setPositiveButton(R.string.next_button, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String place = installPlaceEditText.getText().toString();
                if ( place.isEmpty() ) {
                    Toast.makeText(getApplicationContext(), "Input Installed Place...", Toast.LENGTH_SHORT).show();
                    dialog_status = false;
                } else {
                    dialog_status = true;
                }
            }
        });
        dialogBuilder.setNegativeButton(R.string.cancel_button, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                onBackPressed();
            }
        });

        final AlertDialog b = dialogBuilder.create();
        b.show();
        b.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if ( !dialog_status ) b.show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search_manufacturer, menu);

        return super.onCreateOptionsMenu(menu);
    }

    /*  When the user selects one of the app bar items, the system calls your activity's
        onOptionsItemSelected() callback method, and passes a MenuItem object to indicate
        which item was clicked. In your implementation of onOptionsItemSelected(),
        call the MenuItem.getItemId() method to determine which item was pressed.
        The ID returned matches the value you declared in the corresponding <item> element's
        android:id attribute.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_cancel:
                // return to parent activity
                Intent intent = new Intent();
                setResult(Activity.RESULT_CANCELED, intent);
                finish(); // finish activity
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * The TypeClickListener is the inner class to call back to change type of remote control
     * @author Byung Seon Kim
     */
    public class TypeClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            int color = ContextCompat.getColor(RemoconAddList.this, R.color.primary);
            switch ( typeOfRemocon ) {
                case 0:
                    type1ImageView.setBackgroundColor(color);
                    type1TextView.setBackgroundColor(color);
                    break;
                case 1:
                    type2ImageView.setBackgroundColor(color);
                    type2TextView.setBackgroundColor(color);
                    break;
                case 2:
                    type3ImageView.setBackgroundColor(color);
                    type3TextView.setBackgroundColor(color);
                    break;
            }

            color = ContextCompat.getColor(RemoconAddList.this, R.color.accent);
            switch ( view.getId() ) {
                case R.id.rcitem1_icon :
                case R.id.rcitem1_text :
                    typeOfRemocon = 0;
                    type1ImageView.setBackgroundColor(color);
                    type1TextView.setBackgroundColor(color);
                    break;
                case R.id.rcitem2_icon :
                case R.id.rcitem2_text :
                    typeOfRemocon = 1;
                    type2ImageView.setBackgroundColor(color);
                    type2TextView.setBackgroundColor(color);
                    break;
                case R.id.rcitem3_icon :
                case R.id.rcitem3_text :
                    typeOfRemocon = 2;
                    type3ImageView.setBackgroundColor(color);
                    type3TextView.setBackgroundColor(color);
                    break;
            }
        }
    }

    // HELPER METHODS ----------------------------------------- the document method of Regenald Dyer
    // ENTRY POINT for STAND-ALONE OPERATION ------------------ the document method of Regenald Dyer
    // ATTRIBUTES --------------------------------------------- the document method of Regenald Dyer
    // logcat constant variable of the activity
    private static final String LOG = "RemoconAddList";
    // reauest code constant variable for calling RemoconAddList
    protected static final int ADD_REMOCON_REQUEST_CODE = 401;
    /** The EditText to change the installed place of the remocon */
    private EditText installPlaceEditText;
    /** Type of Remote control in integer */
    private int typeOfRemocon = 0;
    /** Icon for the type of remote control (Lamp) */
    private ImageView type1ImageView;
    /** Icon for the type of remote control (TV) */
    private ImageView type2ImageView;
    /** Icon for the type of remote control (Projector) */
    private ImageView type3ImageView;
    /** Text for the type of remote control (Lamp) */
    private TextView type1TextView;
    /** Text for the type of remote control (TV) */
    private TextView type2TextView;
    /** Text for the type of remote control (Projector) */
    private TextView type3TextView;
    /** Boolean variable to use deciding to show a dialog or not */
    private boolean dialog_status;
}
