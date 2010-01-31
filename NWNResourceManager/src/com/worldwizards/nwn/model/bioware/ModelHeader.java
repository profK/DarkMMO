package com.worldwizards.nwn.model.bioware;

import java.io.*;
import java.nio.*;
import java.util.*;




/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class ModelHeader extends GeometryHeader {
  private byte modelType;
  private byte fogFlag;
  private int animHeaderArrayOffset;
  private int animHeaderArrayCount;
  private int animHeaderArraySize;
  private int pointerToParent;
  private float[] boundingBoxMin;
  private float[] boundingBoxMax;
  private float radius;
  private String superModelName;
  private List animHeaderOffsets;
  private float animationScale;

  /*
  * ModelHeader
  *
  * @param byteBuffer ByteBuffer
  * @param byteBuffer1 ByteBuffer
  */
 public ModelHeader(ByteBuffer modelDataBuff, ByteBuffer rawDataBuff) {
   this(modelDataBuff,rawDataBuff,false);
 }

  /**
   * ModelHeader
   *
   * @param byteBuffer ByteBuffer
   * @param byteBuffer1 ByteBuffer
   */
  public ModelHeader(ByteBuffer modelDataBuff, ByteBuffer rawDataBuff,
                     boolean trace) {
    super(modelDataBuff,rawDataBuff,trace);

  }

  /**
   * loadSpecificHdrInfo
   *
   * @param modelDataBuffer ByteBuffer
   */
  public void loadSpecificHdrInfo(ByteBuffer modelDataBuff,boolean trace) {
    // rest of model header
    if (trace){
      System.out.println("Model Header: "+name);
    }
    modelDataBuff.position(modelDataBuff.position() + 2); // skip 2 ukn bytes
    modelType = modelDataBuff.get();
    fogFlag = modelDataBuff.get();
    modelDataBuff.position(modelDataBuff.position() + 4); // skip ukn long
    animHeaderArrayOffset = modelDataBuff.getInt();
    animHeaderArrayCount = modelDataBuff.getInt();
    animHeaderArraySize = modelDataBuff.getInt();
    pointerToParent = modelDataBuff.getInt();
    boundingBoxMin = new float[3];
    for (int i = 0; i < 3; i++) {
      boundingBoxMin[i] = modelDataBuff.getFloat();
    }
    boundingBoxMax = new float[3];
    for (int i = 0; i < 3; i++) {
      boundingBoxMax[i] = modelDataBuff.getFloat();
    }
    radius = modelDataBuff.getFloat();
    if (trace){
        System.out.println("Radius = "+radius);
    }
    animationScale = modelDataBuff.getFloat();
    byte[] superModelNameBytes = new byte[64];
    modelDataBuff.get(superModelNameBytes);
    superModelName = MDL.bytesToString(superModelNameBytes);
    // convert animation header offsets
    modelDataBuff.position(animHeaderArrayOffset);
    animHeaderOffsets = new ArrayList();
    for(int i=0;i<animHeaderArrayCount;i++){
      animHeaderOffsets.add(new Integer(modelDataBuff.getInt()));
    }
  }

  /**
   * getAnimHeaderOffsets
   *
   * @return Iterator
   */
  public List getAnimHeaderOffsets() {
    return animHeaderOffsets;
  }

  /*
  public void addToJ3DScene(SceneBase scene, TextureFactory tfactory, BranchGroup branchGroup) {
    rootNode.addToJ3DGraph(scene, "", tfactory,branchGroup);
  }
*/
  
  /**
   * dump
   *
   * @param w Writer
   * @param prefix String
   */
  public void dump(Writer w, String prefix) throws IOException {
    w.write(prefix+"Model Header:\n");
    prefix+="    ";
    w.write(prefix+"Bounding Box Min = "+boundingBoxMin[0]+","+
            boundingBoxMin[1]+","+boundingBoxMin[2]+"\n");
    w.write(prefix+"Bounding Box Max = "+boundingBoxMax[0]+","+
           boundingBoxMax[1]+","+boundingBoxMax[2]+"\n");
   w.write(prefix+"Fog Flag = "+fogFlag+"\n");
   w.write(prefix+"Model Type = "+modelType+"\n");
   w.write(prefix+"Radius = "+radius+"\n");
   w.write(prefix+"Super Model = "+superModelName+"\n");
   super.dump(w,prefix);
  }

}
