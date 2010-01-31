package com.worldwizards.nwn.files;

import com.worldwizards.nwn.files.resources.fields.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class NWNPlaceableInstance
    extends NWNSituated {
  byte animState = 0;
  boolean useable = false;
  float bearing;
  String templateResRef;
  float x, y, z;
  public NWNPlaceableInstance(Struct gffStruct) throws InstantiationException {
    super(gffStruct, 0);
    if (gffStruct.getStructType() != 9) {
      throw new InstantiationException("Bad struct type = " +
                                       gffStruct.getStructType());
    }
    animState = gffStruct.getByte("AnimationState").byteValue();
    useable = (gffStruct.getByte("Useable").byteValue() == 1);
    bearing = gffStruct.getFloat("Bearing").floatValue();
    templateResRef = gffStruct.getResRef("TemplateResRef").stringValue();
    x = gffStruct.getFloat("X").floatValue();
    y = gffStruct.getFloat("Y").floatValue();
    z = gffStruct.getFloat("Z").floatValue();

  }

  /**
   * getX
   *
   * @return float
   */
  public float getX() {
    return x;
  }

  public float getY() {
   return y;
 }


 public float getZ() {
    return z;
  }

  public float getBearing(){
    return bearing;
  }


}
