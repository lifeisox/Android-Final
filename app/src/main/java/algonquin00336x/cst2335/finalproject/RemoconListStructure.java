package algonquin00336x.cst2335.finalproject;

import android.content.Context;
import android.graphics.drawable.Drawable;

/**
 * The RemoconListStructure establishes the structure using the Remote Control System. <br/>
 * The structure will use when I read the date from "RemoteCon.db" and when I use ListView.
 * @author Byung Seon Kim
 */
public class RemoconListStructure {

    // CONSTRUCTORS ------------------------------------------- the document method of Regenald Dyer

    /**
     * It is default constructor
     */
    public RemoconListStructure() {}

    /**
     * The RemoconListStructure is the constructor of RemoconListStructure class
     * @param typeOfRemocon int: the type of remote control
     * @param placeToUse String: the place to use
     */
    public RemoconListStructure( int typeOfRemocon, String placeToUse ) {
        this.id = 0;
        this.typeOfRemocon = typeOfRemocon;
        this.placeToUse = placeToUse;
        this.numOfUsed = 0;
        this.manufacturer = "Samsung";
    }

    // ACCESSORS ---------------------------------------------- the document method of Regenald Dyer

    /**
     * The getId returns the id identification number primitive type integer value.
     * @return Return value of identification number
     */
    public int getId() { return id; }
    /**
     * The getTypeOfRemocon returns the type of remote control in primitive type integer value.
     * @return Return value of the type of remote control.
     */
    public int getTypeOfRemocon() {
        return typeOfRemocon;
    }

    /**
     * The getPlaceToUse returns the place to use in String object value.
     * @return Return value of the place to use.
     */
    public String getPlaceToUse() {
        return placeToUse;
    }

    /**
     * The getNumOfUsed returns the number of used by user.
     * @return Return value of the number of used.
     */
    public int getNumOfUsed() { return numOfUsed; }

    /**
     * The getManufacturer returns the manufacturer
     * @return Return value of the manufacturer in String
     */
    public String getManufacturer() { return manufacturer; }

    // MODIFIERS ---------------------------------------------- the document method of Regenald Dyer

    /**
     * The setId sets the id of primitive type integer value.
     * @param id int: id
     */
    public void setId(int id) { this.id = id; }
    /**
     * The setTypeOfRemoteCon sets the type of remote control of primitive type integer value.
     * @param typeOfRemocon The parameter is the type of remote control of primitive type integer value.
     */
    public void setTypeOfRemocon(int typeOfRemocon) {
        this.typeOfRemocon = typeOfRemocon;
    }

    /**
     * The setPlaceToUse sets the place to use in String object value.
     * @param placeToUse The parameter is the place of String object type.
     */
    public void setPlaceToUse(String placeToUse) {
        this.placeToUse = placeToUse;
    }

    /**
     * The setNumOfUsed sets the number of used by user in primitive type interger value.
     * @param numOfUsed The parameter is the number of used of primitive type integer value
     */
    public void setNumOfUsed(int numOfUsed) { this.numOfUsed = numOfUsed; }

    /**
     * The setManufacturer sets the manufacturer in String
     * @param manufacturer The parameter is the manufacturer in String
     */
    public void setManufacturer(String manufacturer) { this.manufacturer = manufacturer; }
    // NORMAL BEHAVIOR ---------------------------------------- the document method of Regenald Dyer

    /**
     * The getIcon returns the icon that gets based on the type of remote control in Drawable object value.
     * @param context The parameter allows access to application-specific resources and classes,
     *                as well as up-calls for application-level operations such as launching activities,
     *                broadcasting and receiving intents, etc.
     * @return Return value of Drawable object type.
     */
    public Drawable getIcon( Context context ) {
        Drawable icon;
        switch ( typeOfRemocon ) {
            case 0 :
                icon = context.getDrawable(R.drawable.ic_lightbulb_outline_white_48dp);
                break;
            case 1 :
                icon = context.getDrawable(R.drawable.ic_live_tv_white_48dp);
                break;
            case 2 :
                icon = context.getDrawable(R.drawable.ic_videogame_asset_white_48dp);
                break;
            default :
                icon = context.getDrawable(R.drawable.ic_clear_white_48dp);
                break;
        }
        return icon;
    }

    /**
     * The getNameOfType returns the string that gets based on the type of remote control.
     * @return Return value of String object type.
     */
    public String getNameOfType() {
        String name;
        switch ( this.typeOfRemocon ) {
            case 0 :
                name = "Lamp";
                break;
            case 1 :
                name = "Television";
                break;
            case 2 :
                name = "Projector";
                break;
            default :
                name = "No Name";
                break;
        }
        return name;
    }

    // HELPER METHODS ----------------------------------------- the document method of Regenald Dyer
    // ENTRY POINT for STAND-ALONE OPERATION ------------------ the document method of Regenald Dyer
    // ATTRIBUTES --------------------------------------------- the document method of Regenald Dyer
    /**
     * Identification in table of database
     */
    private int id;
    /**
     * The typeOfRemoteCon attribute represents the type of Remote control <br/>
     * 0: Lamp, 1: Television, 2: Blind
     */
    private int typeOfRemocon;
    /**
     * The placeToUse attribute represents the place to use by user.
     */
    private String placeToUse;
    /**
     * The numOfUsed attribute represents the number of used by user.
     */
    private int numOfUsed;
    /**
     * The manufacturer attribute represents the manufacturer of the product.
     */
    private String manufacturer;
}
