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

public class NWNDoorInstance extends NWNSituated{
  float bearing;
  String templateResRef;
  float x,y,z;
  private int genericType;
  public NWNDoorInstance(Struct gffStruct) throws InstantiationException {
    super(gffStruct,0);
    if (gffStruct.getStructType() != 8) {
      throw new InstantiationException("Bad struct type = "+
                                       gffStruct.getStructType());
    }
   bearing = gffStruct.getFloat("Bearing").floatValue();
   templateResRef = gffStruct.getResRef("TemplateResRef").stringValue();
   x = gffStruct.getFloat("X").floatValue();
   y = gffStruct.getFloat("Y").floatValue();
   z = gffStruct.getFloat("Z").floatValue();
   genericType = gffStruct.getByte("GenericType").byteValue();

  }

  /**
   * getTemplate
   *
   * @return String
   */
  public String getTemplate() {
    return templateResRef;
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

  /**
   * getGenericType
   *
   * @return int
   */
  public int getGenericType() {
    return genericType;
  }

}
