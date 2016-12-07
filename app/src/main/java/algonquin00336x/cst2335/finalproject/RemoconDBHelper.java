package algonquin00336x.cst2335.finalproject;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * The RemoconDBHelper class manages all things relate to the database needs Remote Control System
 * @author Byung Seon Kim
 */
class RemoconDBHelper extends SQLiteOpenHelper {

    // CONSTRUCTORS ------------------------------------------- the document method of Regenald Dyer

    /**
     * The RemoconDBHelper is the constructor of RemoconDBHelper class
     * @param context The parameter allows access to application-specific resources and classes,
     *                as well as up-calls for application-level operations such as launching activities,
     *                broadcasting and receiving intents, etc.
     */
    RemoconDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // ACCESSORS ---------------------------------------------- the document method of Regenald Dyer
    // MODIFIERS ---------------------------------------------- the document method of Regenald Dyer
    // NORMAL BEHAVIOR ---------------------------------------- the document method of Regenald Dyer

    /**
     * Called when the database is created for the first time.
     * This is where the creation of tables and the initial population of the tables should happen.<br/>
     * Create the remocon list table.
     * @param db The database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // creating required tables AND city ID for Ottawa in Canada
        Log.i(LOG, CREATE_TABLE_REMOCON_LIST);
        db.execSQL(CREATE_TABLE_REMOCON_LIST);
        db.execSQL(CREATE_TABLE_MANUFACT_LIST);
        for ( String manufacturer : manufact ) {
            String query = "INSERT INTO " + TABLE_MANUFACT_LIST + " ( " + MANUFACT_NAME + " ) VALUES ( '" +
                    manufacturer + "' ); ";
            db.execSQL(query);
        }
        String query = "INSERT INTO " + TABLE_REMOCON_LIST + " ( " + REMOCON_TYPE + ", " + REMOCON_NAME + ", "
                + REMOCON_MANUFACT + ", " + REMOCON_COUNT + ", " + KEY_CREATED_AT
                + " ) VALUES ( 0, 'Living Room', 'Samsung', 0, DateTime('now') ); ";
        db.execSQL(query);
    }

    /**
     * Called when the database needs to be upgraded.
     * The implementation should use this method to drop tables, add tables, or do anything else
     * it needs to upgrade to the new schema version.
     * @param db The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REMOCON_LIST);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MANUFACT_LIST);
        // create new tables
        onCreate(db);
    }

    /**
     * Get a remote control list in the table using keyId
     * @param keyId primary key of remote control list table
     * @return Return RemoconListStructure type
     */
    RemoconListStructure getRemoconList(int keyId) {
        RemoconListStructure remocon = new RemoconListStructure();
        String selectQuery =
                "SELECT " + REMOCON_ID + ", " + REMOCON_TYPE +
                        ", " + REMOCON_NAME + ", " + REMOCON_MANUFACT + ", " + REMOCON_COUNT + " FROM " +
                        TABLE_REMOCON_LIST + " WHERE " + REMOCON_ID + " = " + keyId;

        // display a log message which is query
        Log.i(LOG, selectQuery);
        // run the query
        SQLiteDatabase db = this.getReadableDatabase();
        @SuppressLint("Recycle")
        Cursor raw = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (raw.moveToFirst()) {
            remocon.setId(raw.getInt(raw.getColumnIndex(REMOCON_ID)));
            remocon.setTypeOfRemocon(raw.getInt(raw.getColumnIndex(REMOCON_TYPE)));
            remocon.setPlaceToUse(raw.getString(raw.getColumnIndex(REMOCON_NAME)));
            remocon.setManufacturer(raw.getString(raw.getColumnIndex(REMOCON_MANUFACT)));
            remocon.setNumOfUsed(raw.getInt(raw.getColumnIndex(REMOCON_COUNT)));
        }
        return remocon;
    }

    /**
     * Get all remote control lists in the table saving the data a user entered
     * @return list of remote control
     */
    List<RemoconListStructure> getAllRemoconLists() {
        final List<RemoconListStructure> remoconList = new ArrayList<>();
        String selectQuery =
                "SELECT " + REMOCON_ID + ", " + REMOCON_TYPE +
                        ", " + REMOCON_NAME + ", " + REMOCON_MANUFACT + ", " + REMOCON_COUNT + " FROM " +
                        TABLE_REMOCON_LIST + " ORDER BY " + REMOCON_COUNT + " DESC;";
        // display a log message which is query
        Log.i(LOG, selectQuery);
        // run the query
        SQLiteDatabase db = this.getReadableDatabase();
        @SuppressLint("Recycle")
        Cursor raw = db.rawQuery(selectQuery, null);
        Log.i(LOG, "Record count: " + raw.getCount());
        // looping through all rows and adding to list
        if (raw.moveToFirst()) {
            do {
                RemoconListStructure remocon = new RemoconListStructure();
                remocon.setId(raw.getInt(raw.getColumnIndex(REMOCON_ID)));
                remocon.setTypeOfRemocon(raw.getInt(raw.getColumnIndex(REMOCON_TYPE)));
                remocon.setPlaceToUse(raw.getString(raw.getColumnIndex(REMOCON_NAME)));
                remocon.setManufacturer(raw.getString(raw.getColumnIndex(REMOCON_MANUFACT)));
                remocon.setNumOfUsed(raw.getInt(raw.getColumnIndex(REMOCON_COUNT)));
                // add to remote control list
                remoconList.add(remocon);
            } while (raw.moveToNext());
        }

        return remoconList;
    }


    List<String> getAllManufactLists() {
        final List<String> manufactList = new ArrayList<>();
        String selectQuery =
                "SELECT " + MANUFACT_ID + ", " + MANUFACT_NAME + " FROM " +
                        TABLE_MANUFACT_LIST + " ORDER BY " + MANUFACT_NAME + " ASC;";
        // display a log message which is query
        Log.i(LOG, selectQuery);
        // run the query
        SQLiteDatabase db = this.getReadableDatabase();
        @SuppressLint("Recycle")
        Cursor raw = db.rawQuery(selectQuery, null);
        Log.i(LOG, "Record count: " + raw.getCount());
        // looping through all rows and adding to list
        if (raw.moveToFirst()) {
            do {
                manufactList.add(raw.getString(raw.getColumnIndex(MANUFACT_NAME)));
            } while (raw.moveToNext());
        }
        return manufactList;
    }

    /**
     * The insert is used to add new rows of data into a table in the database.
     * @param remocon the remocon structure to insert
     * @return Return last insert row id
     */
    long insertRemocon(RemoconListStructure remocon) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(REMOCON_TYPE, remocon.getTypeOfRemocon());
        values.put(REMOCON_NAME, remocon.getPlaceToUse());
        values.put(REMOCON_MANUFACT, remocon.getManufacturer());
        values.put(REMOCON_COUNT, remocon.getNumOfUsed());
        values.put(KEY_CREATED_AT, getDateTime());

        return db.insert(TABLE_REMOCON_LIST, null, values);
    }

    /**
     * The delete is used to delete the existing records from a table.
     * @param remocon the remocon structure to delete
     */
    void deleteRemocon(RemoconListStructure remocon) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_REMOCON_LIST, REMOCON_ID + " = ?",
                new String[]{String.valueOf(remocon.getId())});
    }

    /**
     * Closing database
     */
    void closeDatabase() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen()) db.close();
    }

    // HELPER METHODS ----------------------------------------- the document method of Regenald Dyer

    /**
     * Get current locale datetime
     * @return Return current data and time in String object type
     */
    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    // ENTRY POINT for STAND-ALONE OPERATION ------------------ the document method of Regenald Dyer
    // ATTRIBUTES --------------------------------------------- the document method of Regenald Dyer

    // Logcat tag
    private static final String LOG = "RemoconDBHelper";
    // Database Version
    private static final int DATABASE_VERSION = 2;
    // Initialize Manufacturer
    private static final String manufact[] = { "3M", "ABB", "Acer", "ADA", "Adcom", "Aethra", "Aiwa",
            "Akai", "Alpine", "AMC", "Ampro", "Anthem", "Apex", "Apti", "Aragon", "Ask", "Atlantic Technology",
            "Audio Design Associates", "Audio Ease", "Audio International", "Audio Request", "Audioaccess",
            "Audioease", "Audiosource", "Auton", "Auto-Vue", "B & K", "Bang & Olufsen", "Barco",
            "Biamp", "BMB", "Bogen", "Bose", "Broksonic", "BTX", "California Audio Labs", "Canon",
            "Carver", "Channel Master", "Chaparral", "Chiro", "Chisholm", "Christie Digital Systems",
            "Clarion", "Classe", "Counterpoint", "CTX", "CTX-Optima", "Daewoo", "Dalite", "Davis",
            "Dbox", "Denon", "Digital Projection", "Dish Network", "DMX", "Draper", "Dual Cassette",
            "Dukane", "Dwin", "Echostar", "Eike", "Eiki", "Elan", "Electrohomen", "Electronics",
            "Elero", "Elmo", "Epson", "Escient", "Everquest", "Extron", "Fanfaren", "Fanon", "Farenheit",
            "Faroudja", "Fisher", "Fosgate Audionics", "Fostex", "Fox", "Fujitsu", "Funai", "GE",
            "GC Electronics", "General Instruments", "Getner", "Globecast", "Go Video", "Grand Channel",
            "Grand Tech", "Grundig", "Harmon Kardon", "Hitachi", "Houston Tracking Systems", "Hughes",
            "Hunter Douglas", "Imerge", "Infocus", "Integra", "I-Point", "Jaton", "JBL", "Jerrold", "JVC",
            "KDS", "Kenavision", "Kenwood", "Kinergetics Research", "Kloss", "MTX", "Kodak", "Krell",
            "Kustom", "Lexicon", "Linn", "Litetouch", "Loewe", "Lutron", "Luxman", "Magnavox", "Makita Electric Works",
            "Marantz", "Mark Levinson", "Matrix Systems", "Mcintosh", "Mediland", "Megapower", "Meridian",
            "Microsoft", "Mindpath", "Mitsubishi", "Monivision", "Monovision", "Motorola", "Multi Video Labs",
            "NAD", "Nakamichi", "NEC", "Netware", "Nikko", "Niles", "Novaplex", "NSM", "Nuview", "Nview",
            "Oak", "On Command", "Ong Corporation", "Onkyo", "Pace", "Pace Sky Manual", "Panasat",
            "Panasonic", "Parasound", "Philips", "Piano Disc", "Picturetel", "Pinnacle", "Pioneer",
            "Plus", "Poloroid", "Polycom", "Primare", "Primestar", "Princeton", "Pro Presenter",
            "Proceed", "Projectavision", "Proscan", "Proton", "Proxima", "QRS", "Quasar", "RCA", "Replay TV",
            "Request Multimedia", "Revox", "RFT", "Rock-OLA", "Rotel", "Runco", "Russound", "Samsung", "Sanyo",
            "Scientific Atlanta", "Sears", "Seleco", "Sharp", "Sherwood", "Silent Gliss", "Sima", "Sky",
            "Somfy", "Sony", "Sonic Blue", "Southerland", "Sunfire", "Sunteca", "Tagmclaren", "Tandberg",
            "Tascam", "Teac", "Technics", "Telex", "Theta Casablanca", "Theta Digital", "Toshiba",
            "Totevision", "Travelors", "Turtle Beach", "Uniden", "US Electronics", "Vantage", "Vaux",
            "Velodyne", "Videolabs", "Vidikron", "Viewsonic", "Viewtech", "Vu-Tec", "Wireless Mouse Inc",
            "Wisetech", "Wolf", "X-10", "Xantech", "Yamaha", "Zenith" };
    // Database Name
    private static final String DATABASE_NAME = "RemoteCon.db";
    // Table Names
    private static final String TABLE_REMOCON_LIST = "remocon_list";
    private static final String TABLE_MANUFACT_LIST = "manufact_list";
    // remocon_list Table - column name
    private static final String REMOCON_ID = "id";
    private static final String REMOCON_TYPE = "type";
    private static final String REMOCON_NAME = "name";
    private static final String REMOCON_MANUFACT = "manufact";
    private static final String REMOCON_COUNT = "count";
    // manufact_list Table - column name
    private static final String MANUFACT_ID = "id";
    private static final String MANUFACT_NAME = "name";
    // Common column names
    private static final String KEY_CREATED_AT = "created_at";
    // Table Create Statements
    // remocon_list table create statement
    private static final String CREATE_TABLE_REMOCON_LIST = "CREATE TABLE IF NOT EXISTS " +
            TABLE_REMOCON_LIST + " (" + REMOCON_ID + " INTEGER PRIMARY KEY," + REMOCON_TYPE +
            " INTEGER," + REMOCON_NAME + " VARCHAR(50)," + REMOCON_MANUFACT + " VARCHAR(30),"
            + REMOCON_COUNT + " INTEGER," + KEY_CREATED_AT + " DATETIME" + "); ";
    // manufact_list table create statement
    private static final String CREATE_TABLE_MANUFACT_LIST = "CREATE TABLE IF NOT EXISTS " +
            TABLE_MANUFACT_LIST + " (" + MANUFACT_ID + " INTEGER PRIMARY KEY," + MANUFACT_NAME +
            " VARCHAR(50)," + KEY_CREATED_AT + " DATETIME" + "); ";
}
