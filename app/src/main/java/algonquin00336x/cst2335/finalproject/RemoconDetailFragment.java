package algonquin00336x.cst2335.finalproject;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Camera;
import android.hardware.ConsumerIrManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A fragment representing lamp, television, projector and another detail screen.
 * This fragment is either contained in a {@link RemoconMainActivity}
 * in two-pane mode (on tablets) or a {@link RemoconDetailActivity} on handsets.
 * @author Byung Seon Kim
 */
public class RemoconDetailFragment extends Fragment {

    // CONSTRUCTORS ------------------------------------------- the document method of Regenald Dyer
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RemoconDetailFragment() { }

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments().containsKey(ARG_KEY_ID)) { keyId = getArguments().getInt(ARG_KEY_ID); }
    }

    /**
     * Called to have the fragment instantiate its user interface view.
     * This is optional, and non-graphical fragments can return null (which
     * is the default implementation).  This will be called between
     * {@link #onCreate(Bundle)} and {@link #onActivityCreated(Bundle)}.
     * <p>
     * <p>If you return a View from here, you will later be called in
     * {@link #onDestroyView} when the view is being released.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate
     *                           any views in the fragment,
     * @param container          If non-null, this is the parent view that the fragment's
     *                           UI should be attached to.  The fragment should not add the view itself,
     *                           but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     *                           from a previous saved state as given here.
     * @return Return the View for the fragment's UI, or null.
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // read a record using keyId
        RemoconDBHelper dbHelper = new RemoconDBHelper(getContext());
        remocon = dbHelper.getRemoconList( keyId );
        dbHelper.closeDatabase();

        switch ( remocon.getTypeOfRemocon() ) {
            case 0:
                return lampView( inflater, container );
            case 1:
                return televisionView( inflater, container );
            default: // case 2
                return projectorView( inflater, container );
        }
    }

    /**
     * The TypeClickListener is the inner class to call back to change type of remote control
     * @author Byung Seon Kim
     */
    public class TypeClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            // Get instance of Vibrator from current Context
            Vibrator v = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
            // Vibrate for 300 milliseconds
            v.vibrate(300);

            String data = irData.get(view.getId());
            if (data != null) {
                String values[] = data.split(",");
                int[] pattern = new int[values.length-1];

                for (int i=0; i<pattern.length; i++){
                    pattern[i] = Integer.parseInt(values[i+1]);
                }
                mCIR.transmit(Integer.parseInt(values[0]), pattern);
            }
        }
    }

    // HELPER METHODS ----------------------------------------- the document method of Regenald Dyer

    /**
     * On and Off the flash light on face back camera. Should be set Camera permission on of the app
     * if you want to test in over Android 6.0M (MarshMellow)
     * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's UI should be attached to.
     *                  The fragment should not add the view itself,
     * @return Return the View for the fragment's UI, or null.
     */
    private View lampView(LayoutInflater inflater, @Nullable ViewGroup container) {
        // get a layout file to use
        final View rootView = inflater.inflate(R.layout.remocon_lamp_detail, container, false);
        // Define objects to use
        TextView manufactTextView = (TextView) rootView.findViewById(R.id.manufacturer);
        manufactTextView.setText( remocon.getManufacturer() );
        Switch flashSwitch = (Switch) rootView.findViewById(R.id.flash_sw);
        final LinearLayout back = (LinearLayout) rootView.findViewById(R.id.back);
        SeekBar dimmerBar = (SeekBar) rootView.findViewById(R.id.dimmer_bar);
        SeekBar redBar = (SeekBar) rootView.findViewById(R.id.red_bar);
        SeekBar greenBar = (SeekBar) rootView.findViewById(R.id.green_bar);
        SeekBar blueBar = (SeekBar) rootView.findViewById(R.id.blue_bar);
        // get background color
        ColorDrawable backColor = (ColorDrawable) back.getBackground();
        int color = backColor.getColor();
        alphaColor = Color.alpha(color);
        dimmerBar.setProgress(alphaColor);
        redColor = Color.red(color);
        redBar.setProgress(redColor);
        greenColor = Color.green(color);
        greenBar.setProgress(greenColor);
        blueColor = Color.blue(color);
        blueBar.setProgress(blueColor);
        Log.i(LOG, "Alpha: " + alphaColor + " Red: " + redColor + " Green: " + greenColor + " Blue: " + blueColor);

        flashSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                // build a Camera object using Driver4Camera class
                Driver4Camera driver4Camera = new Driver4Camera();
                Camera camera = driver4Camera.getCamera();
                if ( isChecked ) {
                    Camera.Parameters p = camera.getParameters(); // Create Parameters Class Object of the Camera
                    p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                    camera.setParameters(p); // Set flash light on the camera
                    camera.startPreview();  // Run the camera
                    Snackbar.make(rootView, R.string.flash_on, Snackbar.LENGTH_LONG).show();
                } else {
                    camera.stopPreview();
                    camera.release();
                    Snackbar.make(rootView, R.string.flash_off, Snackbar.LENGTH_LONG).show();
                }
            }
        });

        dimmerBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                alphaColor = progress;
                back.setBackgroundColor(Color.argb( alphaColor, redColor, greenColor, blueColor ));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });

       redBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                redColor = progress;
                back.setBackgroundColor(Color.argb( alphaColor, redColor, greenColor, blueColor ));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });

        greenBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                greenColor = progress;
                back.setBackgroundColor(Color.argb( alphaColor, redColor, greenColor, blueColor ));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });


        blueBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                blueColor = progress;
                back.setBackgroundColor(Color.argb( alphaColor, redColor, greenColor, blueColor ));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });

        return rootView;
    }

    /**
     * It is the remote control of Television. Now support only Samsung TV
     * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's UI should be attached to.
     *                  The fragment should not add the view itself,
     * @return Return the View for the fragment's UI, or null.
     */
    private View televisionView(LayoutInflater inflater, @Nullable ViewGroup container) {
        // get a layout file to use
        View rootView = inflater.inflate(R.layout.remocon_tv_detail, container, false);
        // Define objects to use
        TextView manufactTextView = (TextView) rootView.findViewById(R.id.manufacturer);
        manufactTextView.setText( remocon.getManufacturer() );
        ImageButton volUpImageButton = (ImageButton) rootView.findViewById(R.id.volume_up);
        ImageButton powerImageButton = (ImageButton) rootView.findViewById(R.id.power);
        ImageButton chUpImageButton = (ImageButton) rootView.findViewById(R.id.channel_up);
        ImageButton volDownImageButton = (ImageButton) rootView.findViewById(R.id.volume_down);
        ImageButton muteImageButton = (ImageButton) rootView.findViewById(R.id.mute);
        ImageButton chDownImageButton = (ImageButton) rootView.findViewById(R.id.channel_down);
        ImageButton backImageButton = (ImageButton) rootView.findViewById(R.id.previous);
        ImageButton okImageButton = (ImageButton) rootView.findViewById(R.id.okay);
        Button num0Button = (Button) rootView.findViewById(R.id.num0);
        Button num1Button = (Button) rootView.findViewById(R.id.num1);
        Button num2Button = (Button) rootView.findViewById(R.id.num2);
        Button num3Button = (Button) rootView.findViewById(R.id.num3);
        Button num4Button = (Button) rootView.findViewById(R.id.num4);
        Button num5Button = (Button) rootView.findViewById(R.id.num5);
        Button num6Button = (Button) rootView.findViewById(R.id.num6);
        Button num7Button = (Button) rootView.findViewById(R.id.num7);
        Button num8Button = (Button) rootView.findViewById(R.id.num8);
        Button num9Button = (Button) rootView.findViewById(R.id.num9);
        // Read remote control data from the database in next version
        irData = new SparseArray<>();
        irData.put( R.id.power, hex2dec("0000 006d 0022 0003 00a9 00a8 0015 003f 0015 003f 0015 003f 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 003f 0015 003f 0015 003f 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 003f 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0040 0015 0015 0015 003f 0015 003f 0015 003f 0015 003f 0015 003f 0015 003f 0015 0702 00a9 00a8 0015 0015 0015 0e6e") );
        irData.put( R.id.volume_up, hex2dec("0000 006d 0022 0003 00a9 00a8 0015 003f 0015 003f 0015 003f 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 003f 0015 003f 0015 003f 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 003f 0015 003f 0015 003f 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 003f 0015 003f 0015 003f 0015 003f 0015 003f 0015 0702 00a9 00a8 0015 0015 0015 0e6e") );
        irData.put( R.id.volume_down, hex2dec("0000 006d 0022 0003 00a9 00a8 0015 003f 0015 003f 0015 003f 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 003f 0015 003f 0015 003f 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 003f 0015 003f 0015 0015 0015 003f 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 003f 0015 0015 0015 003f 0015 003f 0015 003f 0015 003f 0015 0702 00a9 00a8 0015 0015 0015 0e6e") );
        irData.put( R.id.channel_up, hex2dec("0000 006d 0022 0003 00a9 00a8 0015 003f 0015 003f 0015 003f 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 003f 0015 003f 0015 003f 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 003f 0015 0015 0015 0015 0015 003f 0015 0015 0015 0015 0015 0015 0015 003f 0015 0015 0015 003f 0015 003f 0015 0015 0015 0040 0015 003f 0015 003f 0015 0702 00a9 00a8 0015 0015 0015 0e6e") );
        irData.put( R.id.channel_down, hex2dec("0000 006d 0022 0003 00a9 00a8 0015 003f 0015 003f 0015 003f 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 003f 0015 003f 0015 003f 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 003f 0015 0015 0015 0015 0015 0015 0015 003f 0015 003f 0015 003f 0015 003f 0015 0015 0015 003f 0015 003f 0015 003f 0015 0702 00a9 00a8 0015 0015 0015 0e6e") );
        irData.put( R.id.mute, hex2dec("0000 006d 0022 0003 00a9 00a8 0015 003f 0015 003f 0015 003f 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 003f 0015 003f 0015 003f 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 003f 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 003f 0015 003f 0015 0015 0015 003f 0015 003f 0015 003f 0015 003f 0015 003f 0015 0702 00a9 00a8 0015 0015 0015 0e6e") );
        irData.put( R.id.previous, hex2dec("0000 006d 0022 0003 00a9 00a8 0015 003f 0015 003f 0015 003f 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 003f 0015 003f 0015 003f 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 003f 0015 0015 0015 003f 0015 003f 0015 0015 0015 0015 0015 0015 0015 003f 0015 0015 0015 003f 0015 0015 0015 0015 0015 003f 0015 003f 0015 003f 0015 0703 00a9 00a8 0015 0015 0015 0e6e") );
        irData.put( R.id.okay, hex2dec("0000 006c 0022 0003 00ab 00aa 0015 003f 0015 003f 0015 003f 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 003f 0015 0040 0015 003f 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0040 0015 003f 0015 0015 0015 0015 0015 0015 0015 003f 0015 0015 0015 0015 0015 0015 0015 0015 0015 003f 0015 003f 0015 003f 0015 0015 0015 003f 0015 003f 0015 0712 00ab 00aa 0015 0015 0015 0e91") );
        irData.put( R.id.num0, hex2dec("0000 006c 0022 0003 00ab 00aa 0015 003f 0015 003f 0015 003f 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 003f 0015 0040 0015 003f 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 003f 0015 0015 0015 0015 0015 0015 0015 003f 0015 0015 0015 0015 0015 0015 0015 0016 0015 003f 0015 003f 0015 003f 0015 0015 0015 0040 0015 003f 0015 003f 0015 0713 00ab 00aa 0015 0015 0015 0e91") );
        irData.put( R.id.num1, hex2dec("0000 006d 0022 0003 00a9 00a8 0015 003f 0015 003f 0015 003f 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 003f 0015 003f 0015 003f 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 003f 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 003f 0015 003f 0015 0015 0015 003f 0015 003f 0015 003f 0015 003f 0015 003f 0015 0702 00a9 00a8 0015 0015 0015 0e6e") );
        irData.put( R.id.num2, hex2dec("0000 006c 0022 0003 00ab 00aa 0015 003f 0015 003f 0015 003f 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 003f 0015 0040 0015 003f 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 003f 0015 0015 0015 003f 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0016 0015 003f 0015 0015 0015 003f 0015 003f 0015 0040 0015 003f 0015 003f 0015 0713 00ab 00aa 0015 0015 0015 0e91") );
        irData.put( R.id.num3, hex2dec("0000 006c 0022 0003 00ab 00aa 0015 003f 0015 003f 0015 003f 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 003f 0015 0040 0015 003f 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 003f 0015 003f 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 003f 0015 0015 0015 0015 0015 003f 0015 003f 0015 0040 0015 003f 0015 003f 0015 0714 00ab 00aa 0015 0015 0015 0e91") );
        irData.put( R.id.num4, hex2dec("0000 006d 0022 0003 00a9 00a8 0015 003f 0015 003f 0015 003f 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 003f 0015 003f 0015 003f 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 003f 0015 0015 0015 0015 0015 0015 0015 0015 0015 003f 0015 003f 0015 003f 0015 0015 0015 003f 0015 003f 0015 003f 0015 003f 0015 0702 00a9 00a8 0015 0015 0015 0e6e") );
        irData.put( R.id.num5, hex2dec("0000 006d 0022 0003 00a9 00a8 0015 003f 0015 003f 0015 003f 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 003f 0015 003f 0015 003f 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 003f 0015 0015 0015 0015 0015 003f 0015 0015 0015 0015 0015 0015 0015 0015 0015 0016 0015 003f 0015 003f 0015 0015 0015 003f 0015 003f 0015 003f 0015 003f 0015 0702 00a9 00a8 0015 0015 0015 0e6e") );
        irData.put( R.id.num6, hex2dec("0000 006d 0022 0003 00a9 00a8 0015 003f 0015 003f 0015 003f 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 003f 0015 003f 0015 003f 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 003f 0015 0015 0015 003f 0015 0015 0015 0015 0015 0015 0015 0015 0015 003f 0015 0015 0015 003f 0015 0015 0015 003f 0015 003f 0015 003f 0015 003f 0015 0703 00a9 00a8 0015 0015 0015 0e6e") );
        irData.put( R.id.num7, hex2dec("0000 006d 0023 0003 0001 5a59 00a9 00a8 0015 003f 0015 003f 0015 003f 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 003f 0015 003f 0015 003f 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 003f 0015 003f 0015 0015 0015 0015 0015 0015 0015 0015 0015 003f 0015 003f 0015 0015 0015 0015 0015 003f 0015 003f 0015 003f 0015 003f 0015 0703 00a9 00a8 0015 0015 0015 0e6e") );
        irData.put( R.id.num8, hex2dec("0000 006d 0022 0003 00a9 00a8 0015 003f 0015 003f 0015 003f 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 003f 0015 003f 0015 003f 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 003f 0015 0015 0015 003f 0015 003f 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 003f 0015 0015 0015 0015 0015 003f 0015 003f 0015 003f 0015 003f 0015 0703 00a9 00a8 0015 0015 0015 0e6e") );
        irData.put( R.id.num9, hex2dec("0000 006d 0022 0003 00a9 00a8 0015 003f 0015 003f 0015 003f 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 003f 0015 003f 0015 003f 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 003f 0015 003f 0015 003f 0015 0015 0015 0015 0015 0015 0015 0015 0015 003f 0015 0015 0015 0015 0015 0015 0015 003f 0015 003f 0015 003f 0015 003f 0015 0703 00a9 00a8 0015 0015 0015 0e6e") );
        // Set onClickListener for all components
        volUpImageButton.setOnClickListener(new TypeClickListener());
        powerImageButton.setOnClickListener(new TypeClickListener());
        chUpImageButton.setOnClickListener(new TypeClickListener());
        volDownImageButton.setOnClickListener(new TypeClickListener());
        muteImageButton.setOnClickListener(new TypeClickListener());
        chDownImageButton.setOnClickListener(new TypeClickListener());
        backImageButton.setOnClickListener(new TypeClickListener());
        okImageButton.setOnClickListener(new TypeClickListener());
        num0Button.setOnClickListener(new TypeClickListener());
        num1Button.setOnClickListener(new TypeClickListener());
        num2Button.setOnClickListener(new TypeClickListener());
        num3Button.setOnClickListener(new TypeClickListener());
        num4Button.setOnClickListener(new TypeClickListener());
        num5Button.setOnClickListener(new TypeClickListener());
        num6Button.setOnClickListener(new TypeClickListener());
        num7Button.setOnClickListener(new TypeClickListener());
        num8Button.setOnClickListener(new TypeClickListener());
        num9Button.setOnClickListener(new TypeClickListener());

        irInit4KitKat();

        return rootView;
    }

    /**
     * It is the remote control of Projector. Now support only Panasonic
     * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's UI should be attached to.
     *                  The fragment should not add the view itself,
     * @return Return the View for the fragment's UI, or null.
     */
    private View projectorView(LayoutInflater inflater, @Nullable ViewGroup container) {
        // get a layout file to use
        View rootView = inflater.inflate(R.layout.remocon_projector_detail, container, false);
        // Define objects to use
        TextView manufactTextView = (TextView) rootView.findViewById(R.id.manufacturer);
        manufactTextView.setText( remocon.getManufacturer() );
        ImageButton volUpImageButton = (ImageButton) rootView.findViewById(R.id.volume_up);
        ImageButton powerImageButton = (ImageButton) rootView.findViewById(R.id.power);
        ImageButton volDownImageButton = (ImageButton) rootView.findViewById(R.id.volume_down);
        ImageButton muteImageButton = (ImageButton) rootView.findViewById(R.id.mute);
        ImageButton backImageButton = (ImageButton) rootView.findViewById(R.id.previous);
        ImageButton okImageButton = (ImageButton) rootView.findViewById(R.id.okay);
        // Read remote control data from the database in next version
        irData = new SparseArray<>();
        irData.put( R.id.power, hex2dec("0000 0070 0000 0032 0080 0040 0010 0010 0010 0030 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0030 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0030 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0030 0010 0010 0010 0030 0010 0030 0010 0030 0010 0030 0010 0010 0010 0010 0010 0030 0010 0010 0010 0030 0010 0030 0010 0030 0010 0030 0010 0010 0010 0030 0010 0aa6") );
        irData.put( R.id.volume_up, hex2dec("0000 0070 0000 0032 0081 003f 0011 000f 0011 002f 0011 000f 0011 000f 0011 000f 0011 000f 0011 000f 0011 000f 0011 000f 0011 000f 0011 000f 0011 000f 0011 000f 0011 002f 0011 000f 0011 000f 0011 000f 0011 000f 0011 000f 0011 000f 0011 000f 0011 000f 0011 000f 0011 002f 0011 000f 0011 000f 0011 000f 0011 000f 0011 000f 0011 000f 0011 000f 0011 000f 0011 000f 0011 000f 0011 000f 0011 000f 0011 000f 0011 002f 0011 000f 0011 000f 0011 000f 0011 000f 0011 000f 0011 000f 0011 000f 0011 002f 0011 000f 0011 002f 0011 0aa6") );
        irData.put( R.id.volume_down, hex2dec("0000 0070 0000 0032 0080 0040 0010 0010 0010 0030 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0030 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0030 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0030 0010 0010 0010 0010 0010 0010 0010 0010 0010 0030 0010 0010 0010 0010 0010 0030 0010 0010 0010 0010 0010 0010 0010 0010 0010 0030 0010 0010 0010 0030 0010 0aa6") );
        irData.put( R.id.mute, hex2dec("0000 0070 0000 0032 0080 0040 0010 0010 0010 0030 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0030 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0030 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0030 0010 0010 0010 0010 0010 0030 0010 0030 0010 0010 0010 0010 0010 0010 0010 0030 0010 0010 0010 0010 0010 0030 0010 0030 0010 0010 0010 0030 0010 0aa7") );
        irData.put( R.id.previous, hex2dec("0000 006f 0000 001a 003c 001e 000f 002d 000f 000f 000f 002d 000f 000f 000f 002d 000f 000f 000f 000f 000f 000f 000f 000f 000f 000f 000f 000f 000f 000f 000f 000f 000f 000f 000f 002d 000f 000f 000f 000f 000f 000f 000f 000f 000f 000f 000f 000f 000f 000f 000f 000f 000f 002d 000f 01c3") );
        // menu button
        irData.put( R.id.okay, hex2dec("0000 0070 0000 0032 0080 0040 0010 0010 0010 0030 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0030 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0030 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0010 0030 0010 0010 0010 0010 0010 0030 0010 0010 0010 0030 0010 0010 0010 0010 0010 0030 0010 0010 0010 0010 0010 0030 0010 0010 0010 0030 0010 0030 0010 0aa6") );

        // Set onClickListener for all components
        volUpImageButton.setOnClickListener(new TypeClickListener());
        powerImageButton.setOnClickListener(new TypeClickListener());
        volDownImageButton.setOnClickListener(new TypeClickListener());
        muteImageButton.setOnClickListener(new TypeClickListener());
        backImageButton.setOnClickListener(new TypeClickListener());
        okImageButton.setOnClickListener(new TypeClickListener());

        irInit4KitKat();
        return rootView;
    }

    // Initialize Consumer IR service
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void irInit4KitKat() {
        // Get a reference to the ConsumerIrManager
        mCIR = (ConsumerIrManager) getActivity().getSystemService(Context.CONSUMER_IR_SERVICE);
        if (!mCIR.hasIrEmitter()) {
            Log.e(LOG, "No IR Emitter found!\n");
        }
    }

    // convert the string of hexa decimal to decimal string
    private String hex2dec(String irData) {
        List<String> list = new ArrayList<>(Arrays.asList(irData.split(" ")));
        list.remove(0); // dummy
        int frequency = Integer.parseInt(list.remove(0), 16); // frequency
        list.remove(0); // seq1
        list.remove(0); // seq2

        for (int i = 0; i < list.size(); i++) {
            list.set(i, Integer.toString(Integer.parseInt(list.get(i), 16)));
        }

        frequency = (int) (1000000 / (frequency * 0.241246));
        list.add(0, Integer.toString(frequency));

        irData = "";
        for (String s : list) {
            irData += s + ",";
        }
        return irData;
    }

    // ENTRY POINT for STAND-ALONE OPERATION ------------------ the document method of Regenald Dyer
    // ATTRIBUTES --------------------------------------------- the document method of Regenald Dyer
    /** The fragment argument representing the item ID that this fragment represents. */
    public static final String ARG_KEY_ID = "keyId";
    /** Saved remote control */
    private RemoconListStructure remocon;
    /** It is the primary key for Remocon table */
    private int keyId;
    // logcat constant variable of the activity
    private static final String LOG = "RemoconDetailFragment";
    /** the alpha component of a color int. */
    private int alphaColor;
    /** the red component of a color int */
    private int redColor;
    /** the green component of a color int */
    private int greenColor;
    /** the blue component of a color int */
    private int blueColor;
    /** the SparseArray for saving irData */
    private SparseArray<String> irData;
    /** ConsumerIrManager Object */
    private ConsumerIrManager mCIR;
}
