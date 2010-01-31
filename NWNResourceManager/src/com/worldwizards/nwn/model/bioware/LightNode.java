package com.worldwizards.nwn.model.bioware;

import java.nio.ByteBuffer;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class LightNode extends MdlNode {

  public int fileDataSize() {
    return 0x00CC;
  }

  /**
   * LightNode
   *
   * @param i int
   * @param byteBuffer ByteBuffer
   * @param byteBuffer1 ByteBuffer
   */
  public LightNode(int pos, ByteBuffer modelDataBuff, ByteBuffer rawDataBuff,
                   boolean trace) {
    super(pos,modelDataBuff,rawDataBuff,trace);
    if (trace){
      System.out.println("Light Node: "+name);
    }
  }

  

}
