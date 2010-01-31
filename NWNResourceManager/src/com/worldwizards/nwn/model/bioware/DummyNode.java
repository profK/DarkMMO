package com.worldwizards.nwn.model.bioware;


import java.io.IOException;
import java.nio.ByteBuffer;

import com.worldwizards.util.parse.TextParser;
import com.worldwizards.util.parse.TokenNotFoundException;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class DummyNode extends MdlNode {

  /**
   * DummyNode
   *
   * @param modelDataBuff ByteBuffer
   * @param byteBuffer1 ByteBuffer
   */
  public DummyNode(int location, ByteBuffer modelDataBuff,
                   ByteBuffer rawDataBuff, boolean trace) {
    super(location,modelDataBuff,rawDataBuff,trace);
    if (trace){
      System.out.println("Dummy Node: "+name);
    }
  }

  public DummyNode(TextParser parser,boolean trace) throws IOException, TokenNotFoundException {
	super(parser,trace);
	if (trace){
	      System.out.println("Dummy Node: "+name);
	    }
}

/**
   * fileDataSize
   *
   * @return int
   */
  public int fileDataSize() {
    return 0x0070;
  }


 

}
