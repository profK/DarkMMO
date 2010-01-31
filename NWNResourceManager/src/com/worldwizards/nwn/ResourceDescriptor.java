package com.worldwizards.nwn;

import com.worldwizards.nwn.files.*;

public class ResourceDescriptor implements Comparable {
  String resRef;
  int resType;
  public ResourceDescriptor(String resRef,int resType) {
    this.resRef = resRef;
    this.resType = resType;
  }

    @Override
  public String toString(){
    String typeName = ResourceID.extForID((short)resType);
    if (typeName != null) { // means there is a handler for it
      return resRef+" ("+typeName+")";
    } else {
      return resRef+" (Unknown Type: "+resType+")";
    }
  }

  /**
   * compareTo
   *
   * @param o Object
   * @return int
   */
  public int compareTo(Object o) {
    ResourceDescriptor rd = (ResourceDescriptor)o;
    int result = resRef.compareTo(rd.resRef);
    if (result !=0 ) return result;
    if (resType < rd.resType) {
      return -1;
    } else if (resType>rd.resType) {
      return 1;
    } else {
      return 0;
    }
  }

  public boolean equals(Object o) {
    return compareTo(o) == 0;
  }

  public int hashCode(){
    return resRef.hashCode()+resType;
  }

  /**
   * getResRef
   *
   * @return Object
   */
  public String getResRef() {
    return resRef;
  }

  /**
   * getExt
   *
   * @return Object
   */
  public String getExt() {
     String typeName = ResourceID.extForID((short)resType);
    if (typeName == null) {
      typeName =  "res"+resType;
    }
    return typeName;
  }

  /**
   * getResType
   *
   * @return Object
   */
  public int getResType() {
    return resType;
  }
}
