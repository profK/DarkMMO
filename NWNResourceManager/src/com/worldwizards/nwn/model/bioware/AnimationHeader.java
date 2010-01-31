package com.worldwizards.nwn.model.bioware;

import java.nio.*;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */

public class AnimationHeader extends GeometryHeader {
	float animationLength;

	float transTime;

	String rootNode;

	AnimationEvent[] animationEvents = null;;

	public AnimationHeader() {
	}

	/**
	 * AnimationHeader
	 * 
	 * @param modelDataBuff
	 *            ByteBuffer
	 * @param rawDataBuff
	 *            ByteBuffer
	 */
	public AnimationHeader(ByteBuffer modelDataBuff, ByteBuffer rawDataBuff,
			boolean trace) {

		super(modelDataBuff, rawDataBuff, trace);
	}

	/**
	 * loadSpecificHdrInfo
	 * 
	 * @param modelDataBuffer
	 *            ByteBuffer
	 */
	public void loadSpecificHdrInfo(ByteBuffer modelDataBuffer, boolean trace) {
		animationLength = modelDataBuffer.getFloat();
		transTime = modelDataBuffer.getFloat();
		byte[] namebytes = new byte[64];
		modelDataBuffer.get(namebytes);
		int nullpos = 0;
		while ((nullpos < 64) && (namebytes[nullpos] != 0)) {
			nullpos++;
		}
		rootNode = new String(namebytes, 0, nullpos);
		if (trace) {
			System.out.println("Animation Header: " + name);
		}
		int animEventArrayOffset = modelDataBuffer.getInt();
	    int animEventArrayCount = modelDataBuffer.getInt();
	    int animEventArraySize = modelDataBuffer.getInt();
	    modelDataBuffer.position(animEventArrayOffset);
	    List eventList = new ArrayList();
	    byte[] strbytes = new byte[32];
	    for(int i=0;i<animEventArrayCount;i++){
	      float time = modelDataBuffer.getFloat();
	      modelDataBuffer.get(strbytes);
	      int strlen = 0;
	      while((strlen<32)&&(strbytes[strlen]!=0)){
	      	strlen++;
	      }
	      eventList.add(new AnimationEvent(time,new String(strbytes,0,strlen)));
	    }
	    if (eventList.size()>0){
	    	animationEvents = new AnimationEvent[eventList.size()];
	    	eventList.toArray(animationEvents);
	    }
	}

	public float getAnimationLength() {
		return animationLength;
	}
	

}
