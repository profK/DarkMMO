/*
 * Created on May 5, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.worldwizards.nwn.model.bioware;

/**
 * @author jeffpk
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class AnimationEvent {
	float time;
	String eventName;
	
	public AnimationEvent(float time, String event){
		this.time = time;
		eventName = event;		
	}
}
