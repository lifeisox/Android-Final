package algonquin00336x.cst2335.finalproject;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import java.util.ArrayList;

/**
 * The RemoconReadData is useless class and it is only demonstrate AsyncTask, so I will not
 * add comment.
 */
public class RemoconReadData extends AsyncTask<String, Integer, ArrayList<RemoconListStructure>> {
    private ProgressDialog asyncDialog;
    private Context context;
    private ArrayList<RemoconListStructure> remoconList;
    private AsyncResponse delegate = null;

    public RemoconReadData(Context context, AsyncResponse delegate) {
        this.context = context;
        this.delegate = delegate;
        remoconList = new ArrayList<>();
    }

    @Override
    protected void onPreExecute() {
        asyncDialog = new ProgressDialog(context);
        asyncDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        asyncDialog.setMessage("Data Loading...");
        asyncDialog.show();
        super.onPreExecute();
    }

    @Override
    protected ArrayList<RemoconListStructure> doInBackground(String... strings) {
        int counter = 0;
        String selectQuery =
                "SELECT " + REMOCON_ID + ", " + REMOCON_TYPE +
                        ", " + REMOCON_NAME + ", " + REMOCON_MANUFACT + ", " + REMOCON_COUNT + " FROM " +
                        TABLE_REMOCON_LIST + " ORDER BY " + REMOCON_COUNT + " DESC;";
        RemoconDBHelper dbHelper = new RemoconDBHelper( context );
        // run the query
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        @SuppressLint("Recycle")
        Cursor raw = db.rawQuery(selectQuery, null);
        // Set max value of progress bar
        asyncDialog.setMax(raw.getCount());
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
                publishProgress( ++counter );
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (raw.moveToNext());
        }
        dbHelper.closeDatabase();
        return remoconList;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        asyncDialog.setProgress(values[0]);
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(ArrayList<RemoconListStructure> result) {
        asyncDialog.dismiss();
        delegate.processFinish( result );
        super.onPostExecute(result);
    }

    public interface AsyncResponse {
        void processFinish( ArrayList<RemoconListStructure> result );
    }

    // Table Names
    private static final String TABLE_REMOCON_LIST = "remocon_list";
    // remocon_list Table - column name
    private static final String REMOCON_ID = "id";
    private static final String REMOCON_TYPE = "type";
    private static final String REMOCON_NAME = "name";
    private static final String REMOCON_MANUFACT = "manufact";
    private static final String REMOCON_COUNT = "count";
}
