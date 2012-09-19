package com.tumblr.Hussain1993.CustomMapView;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
/**
 * This is the class for the custom
 * map view, this will allow the programmer
 * to add a long press listener to the map
 * so they can add features for example; when long
 * pressing on the map can bring up a dialog box
 * asking what view they would like to see the map
 * in for example in Standard View or Satellite View
 * @author Hussain
 *
 */
public class MyCustomMapView extends MapView {
	/*
	 * This is the time in milliseconds the user has to hold
	 * the screen down for, for the long press
	 * listener to be called. 600 milliseconds is a good
	 * benchmark
	 */
	public static final int LONG_PRESS_THRESHOLD = 600;
	private GeoPoint lastMapCentre;//This is the last map centre
	private Timer longPressTimer = new Timer();//This is the timer for the long press hold
	private MyCustomMapView.onLongPressListener longPressListener;//This is the long press listener
	
	/**
	 * This is the public interface for a 
	 * long press listener
	 * @author Hussain
	 *
	 */
	public interface onLongPressListener{
		public void onLongPress(MapView mapView, GeoPoint longPressLocation);
	}
	
	/*
	 * Here are the constructors for the class
	 * MyCustomMapView these are required because
	 * you are extending the map view.
	 */
	public MyCustomMapView(Context context, String apiKey){
		super(context, apiKey);
	}
	
	public MyCustomMapView(Context context, AttributeSet attrs){
		super(context, attrs);
	}
	
	public MyCustomMapView(Context context, AttributeSet attrs, int defStyle){
		super(context, attrs, defStyle);
	}
	
	/**
	 * This is setting the long press listener
	 * for the this class
	 * @param longPressListener this is the long press
	 * listener that is being passed
	 */
	public void setOnLongPressListener(MyCustomMapView.onLongPressListener longPressListener){
		this.longPressListener = longPressListener;
	}
	
	/* (non-Javadoc)
	 * @see com.google.android.maps.MapView#onTouchEvent(android.view.MotionEvent)
	 */
	@Override
	public boolean onTouchEvent(MotionEvent motionEvent) {
		handleLongPress(motionEvent);
		return super.onTouchEvent(motionEvent);
	}

	/**
	 * This is the method where the long press
	 * action will be handled
	 * @param motionEvent This is the motion event
	 * this is the user pressing down on the screen.
	 */
	private void handleLongPress(final MotionEvent motionEvent){
		/*
		 * This is the user pressing down on the 
		 * screen
		 */
		if(motionEvent.getAction() == MotionEvent.ACTION_DOWN)
		{
			longPressTimer = new Timer();
			longPressTimer.schedule(new TimerTask() {
				
				@Override
				public void run() {
					GeoPoint longPressLocation = getProjection().fromPixels((int)motionEvent.getX(), (int)motionEvent.getY());
					longPressListener.onLongPress(MyCustomMapView.this, longPressLocation);
				}
			}, LONG_PRESS_THRESHOLD);
			lastMapCentre = getMapCenter();
		}
		/*
		 * This is the user moving their finger
		 * on the screen
		 */
		if(motionEvent.getAction() == MotionEvent.ACTION_MOVE)
		{
			if(!getMapCenter().equals(lastMapCentre))
			{
				longPressTimer.cancel();
			}
			lastMapCentre = getMapCenter();
		}
		/*
		 * This is the user lifting their
		 * finger off the screen
		 */
		if(motionEvent.getAction() == MotionEvent.ACTION_UP)
		{
			longPressTimer.cancel();
		}
		/*
		 * This is the case when the user has more than one
		 * finger on the screen
		 */
		if(motionEvent.getPointerCount() > 1)
		{
			longPressTimer.cancel();
		}
	}
}
