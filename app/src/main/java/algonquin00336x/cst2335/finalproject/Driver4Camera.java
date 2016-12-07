package algonquin00336x.cst2335.finalproject;

import android.hardware.Camera;
import android.util.Log;

/**
 * The Driver4Camera class is for using Camera function of Android device.
 * Should be set Camera permission on of the app if you want to test in over Android 6.0M (MarshMellow)
 * @author Byung Seon Kim
 */
public class Driver4Camera {

    // CONSTRUCTORS ------------------------------------------- the document method of Regenald Dyer
    /** The default constructor is to create an instance of Camera */
    public Driver4Camera() { camera = getCameraInstance( getCameraId() ); }

    // ACCESSORS ---------------------------------------------- the document method of Regenald Dyer

    /**
     * The getCamera returns the instance variable of camera
     * @return Camera object
     */
    public Camera getCamera() { return camera; }
    // MODIFIERS ---------------------------------------------- the document method of Regenald Dyer
    // NORMAL BEHAVIOR ---------------------------------------- the document method of Regenald Dyer

    /**
     * The getCameraInstance builds the instance of Camera
     * @param cameraId Camera identification
     * @return Camera Object
     */
    public static Camera getCameraInstance(int cameraId) {
        Log.d(LOG_TAG, "getCameraInstance(" + cameraId + ")");
        Camera c = null;
        try {
            c = Camera.open(cameraId); // attempt to get a Camera instance
        } catch (Exception e) {
            Log.d(LOG_TAG, "Camera.open(" + cameraId + ") exception=" + e.getMessage());
        }
        return c; // returns null if camera is unavailable
    }

    /**
     * The releaseCamera releases the object of Camera
     */
    public void releaseCamera() {
        if (camera != null) {
            camera.release(); // release the camera for other applications
            camera = null;
        }
    }

    /** The onPause released the object of camere when the activity is paused */
    public void onPause() { releaseCamera(); }

    /** The onResume released the object of camere when the activity is resumed */
    public void onResume() {
        if (camera == null) { camera = getCameraInstance( getCameraId() ); }
    }

    // HELPER METHODS ----------------------------------------- the document method of Regenald Dyer

    /**
     * Search for the back facing camera ( or any camera )
     * @return the camera id of the back facing camera
     */
    private static int getCameraId() {
        Log.d(LOG_TAG, "getCameraId()");
        int cameraId = -1;
        // Search for the back facing camera (or any camera)
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK || numberOfCameras == 1) {
                Log.d(LOG_TAG, "CameraInfo.CAMERA_FACING_BACK = "
                        + (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK));
                cameraId = i;
                break;
            }
        }
        return cameraId;
    }

    // ENTRY POINT for STAND-ALONE OPERATION ------------------ the document method of Regenald Dyer
    // ATTRIBUTES --------------------------------------------- the document method of Regenald Dyer
    /** The camera is the attribute to control Camera object */
    private static Camera camera;
    // logcat constant variable of the activity
    private static final String LOG_TAG = "Driver4Camera";
}