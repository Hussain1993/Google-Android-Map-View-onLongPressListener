package com.tumblr.Hussain1993.CustomMapView;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MyLocationOverlay;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
/**
 * This is an application where the user has the
 * ability to the press and hold on the map
 * and this will then display a dialog box to the
 * user where they will have the chance to choose the
 * view of the map that they would like to see.
 * @author Hussain
 *
 */
public class MapView extends MapActivity {
	private static final int DIALOG_MAP_VIEW = 0;
	private MyLocationOverlay myLocationOverlay;
	private MyCustomMapView mapView;
	private MapController mapController;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_view);
        //Find the elements on the screen
        mapView = (MyCustomMapView)findViewById(R.id.mapView);
        //The default view for the map will be the standard view
        mapView.setSatellite(false);
        //Add the zoom controls for the map
        mapView.setBuiltInZoomControls(true);
        //Get the map controller
        mapController = mapView.getController();
        //Initialize the myLocationOverlay
        myLocationOverlay = new MyLocationOverlay(this, mapView);
        //Add the overlay to the map
        mapView.getOverlays().add(myLocationOverlay);
        //Display the users location and place a pin there
        myLocationOverlay.runOnFirstFix(new Runnable() {
			
			@Override
			public void run() {
				mapView.getOverlays().add(myLocationOverlay);//Add the overlay
				mapController.animateTo(myLocationOverlay.getMyLocation());//Animate to the users location and place a pin there
				mapController.setZoom(17);//Set the zoom level
			}
		});
        
        //Add the long press listener to the map Satellite 
        mapView.setOnLongPressListener(new MyCustomMapView.onLongPressListener() {
			
			@Override
			public void onLongPress(com.google.android.maps.MapView mapView,
					GeoPoint longPressLocation) {
				runOnUiThread(new Runnable() {
					
					@SuppressWarnings("deprecation")
					@Override
					public void run() {
						showDialog(DIALOG_MAP_VIEW);
					}
				});
			}
		});
    }
    
    /* (non-Javadoc)
     * @see com.google.android.maps.MapActivity#onResume()
     */
    @Override
    protected void onResume() {
    	super.onResume();
    	//myLocationOverlay.enableCompass();
    	myLocationOverlay.enableMyLocation();
    }
    
    /* (non-Javadoc)
	 * @see com.google.android.maps.MapActivity#onPause()
	 */
	@Override
	protected void onPause() {
		super.onPause();
		//myLocationOverlay.disableCompass();
		myLocationOverlay.disableMyLocation();
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreateDialog(int)
	 */
	@Override
	@Deprecated
	protected Dialog onCreateDialog(int id) {
		/*
		 * This creates a new dialog box asking the
		 * user which map view that want
		 */
		switch(id)
		{
		case DIALOG_MAP_VIEW: return new AlertDialog.Builder(this)
		.setTitle("Choose Map View Option")
		.setSingleChoiceItems(R.array.map_view_options, 0, new MapViewListener())
		.create();
		}
		return null;
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.map_view, menu);
        return true;
    }

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
	
	/**
	 * This is the private class for the
	 * listener for when the user chooses
	 * the map view they want
	 * @author Hussain
	 *
	 */
	private final class MapViewListener implements DialogInterface.OnClickListener{
		/*
		 * (non-Javadoc)
		 * @see android.content.DialogInterface.OnClickListener#onClick(android.content.DialogInterface, int)
		 */
		@Override
		public void onClick(DialogInterface dialog, int which) {
			switch(which)
			{
			case 0: mapView.setSatellite(false); break;
			case 1: mapView.setSatellite(true); break;
			}
			dialog.dismiss();//This closes the dialog box
		}
		
	}
}
