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

public class SkinmeshNode extends MdlNode {

  public int fileDataSize() {
    return 0x02D4;
  }

  /**
   * SkinmeshNode
   *
   * @param pos int
   * @param modelDataBuff ByteBuffer
   * @param rawDataBuff ByteBuffer
   */
  public SkinmeshNode(int pos, ByteBuffer modelDataBuff, ByteBuffer rawDataBuff,
                      boolean trace) {
    super(pos,modelDataBuff,rawDataBuff,trace);
    if (trace) {
      System.out.println("Skin Mesh Node: "+name);
    }
  }

  

}
