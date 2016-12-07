package algonquin00336x.cst2335.finalproject;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Create a AdapterView. A subclass of the AdapterView class uses an Adapter to bind data to its layout.
 * The Adapter behaves as a middleman between the data source and the AdapterView layout—the Adapter
 * retrieves the data and converts each entry into a view that can be added into the AdapterView layout.
 * @author Byung Seon Kim
 */
class RemoconListAdapter extends BaseAdapter {

    // CONSTRUCTORS ------------------------------------------- the document method of Regenald Dyer

    /**
     * The RemoconListAdapter is the constructor of RemoconListAdapter class
     */
    RemoconListAdapter() { itemList = new ArrayList<>(); }

    // ACCESSORS ---------------------------------------------- the document method of Regenald Dyer
    // MODIFIERS ---------------------------------------------- the document method of Regenald Dyer
    // NORMAL BEHAVIOR ---------------------------------------- the document method of Regenald Dyer

    /**
     * How many items are in the data set represented by this Adapter.
     * @return Count of items.
     */
    @Override
    public int getCount() {
        return itemList.size();
    }

    /**
     * Get a View that displays the data at the specified position in the data set.
     * @param position The position of the item within the adapter's data set of the item whose view we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that
     *                    this view is non-null and of an appropriate type before using.
     *                    If it is not possible to convert this view to display the correct data,
     *                    this method can create a new view. Heterogeneous lists can specify their
     *                    number of view types, so that this View is always of the right
     *                    type (see getViewTypeCount() and getItemViewType(int)).
     * @param parent The parent that this view will eventually be attached to
     * @return A View corresponding to the data at the specified position.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Context context = parent.getContext();

        DecimalFormat dec = new DecimalFormat("#,##0");

        if (convertView == null) {
            // Get convertView reference in the way of inflate remocon_listitem layout
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.remocon_listitem, parent, false);
        }

        // get the references of the widgets to be displayed on screen
        TextView placeToUseTextView = (TextView) convertView.findViewById(R.id.place_to_use);
        ImageView remoconImageView = (ImageView) convertView.findViewById(R.id.remocon_icon);
        TextView typeOfRemoconTextView = (TextView) convertView.findViewById(R.id.type_of_remocon);
        TextView manufactererTextView = (TextView) convertView.findViewById(R.id.manufacturer);

        // Get the reference of data located in the positionData from ArrayList
        RemoconListStructure listViewItem = itemList.get(position);
        Log.i(LOG, "ListView Set Position: " + position);

        // Assign data
        placeToUseTextView.setText(listViewItem.getPlaceToUse());
        remoconImageView.setImageDrawable(listViewItem.getIcon(context));
        typeOfRemoconTextView.setText(listViewItem.getNameOfType() + "  |  " + dec.format(listViewItem.getNumOfUsed()) + " used");
        manufactererTextView.setText(listViewItem.getManufacturer());

        Log.i(LOG, "ListView Data: (PlaceToUse) " + placeToUseTextView.getText() + " (TypeOfRemocon) "
                + typeOfRemoconTextView.getText());

        return convertView;
    }

    /**
     * Get the row id associated with the specified position in the list.
     * @param position The position of the item within the adapter's data set whose row id we want.
     * @return The id of the item at the specified position.
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * Get the data item associated with the specified position in the data set.
     * @param position Position of the item whose data we want within the adapter's data set.
     * @return The data at the specified position.
     */
    @Override
    public Object getItem(int position) {
        return itemList.get(position);
    }

    /**
     * Add a list to ListView
     * @param item RemoconListStructure type
     */
    void addItem(RemoconListStructure item) {
        itemList.add(item);
        this.notifyDataSetChanged();
    }

    /**
     * Remove a list from ListView
     * @param position Position of the item whose data we want within the adapter's data set.
     */
    void removeItem( int position ) {
        itemList.remove( getItem(position) );
        this.notifyDataSetChanged();
    }

    // ENTRY POINT for STAND-ALONE OPERATION ------------------ the document method of Regenald Dyer
    // ATTRIBUTES --------------------------------------------- the document method of Regenald Dyer

    // Logcat constant variable of the activity
    private static final String LOG = "RemoconListAdapter";
    /**
     * The itemList is the attribute for RemoconListStructure ArrayList
     */
    private ArrayList<RemoconListStructure> itemList;
}
