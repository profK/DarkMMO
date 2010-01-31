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

public class AnimmeshNode extends MdlNode {

  public int fileDataSize() {
    return 0x02A8;
  }

  /**
   * AnimmeshNode
   *
   * @param pos int
   * @param modelByteBuff ByteBuffer
   * @param rawByteBuff ByteBuffer
   */
  public AnimmeshNode(int pos, ByteBuffer modelByteBuff, ByteBuffer rawByteBuff,
                      boolean trace) {
    super(pos,modelByteBuff,rawByteBuff,trace);
    if (trace){
      System.out.println("Anim Mesh node: "+name);
    }
  }

  

}
