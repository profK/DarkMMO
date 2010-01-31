package com.worldwizards.nwn.files.resources.fields;

import java.util.*;

import com.worldwizards.nwn.files.resources.*;
import java.io.Serializable;


public class Struct extends GFFField  implements Serializable {
      public final static long serialVersionUID = 1L;
  Map<String,GFFField> fields = new HashMap();
  int structType;


  public Struct(String name, int structType) {
    super(name);
    this.structType = structType;
  }

  public void addField(GFFField field){
    fields.put(field.getFieldName().toUpperCase(),field);
  }

  public void dump(String prefix) {
    super.dump(prefix);
    System.out.println(prefix+"Type: Struct ("+structType+")");
    prefix +="    ";
    for(Iterator i = fields.values().iterator();i.hasNext();){
      ((GFFField)i.next()).dump(prefix);
    }
  }

  /**
   * getSubField
   *
   * @param label String
   * @return GFFField
   */
  public GFFField getSubField(String label) {
    return (GFFField) fields.get(label.toUpperCase());
  }

  /**
   * getInt
   *
   * @param string String
   * @return Object
   */
  public INT getInt(String string) {
    return (INT)getSubField(string);
  }

  /**
   * getByte
   *
   * @param string String
   * @return Object
   */
  public BYTE getByte(String string) {
    return (BYTE)getSubField(string);
  }

  /**
   * getCExoLocString
   *
   * @param string String
   * @return CExoLocString
   */
  public CExoLocString getCExoLocString(String string) {
    return (CExoLocString)getSubField(string);
  }

  /**
   * getCExoString
   *
   * @param string String
   * @return Object
   */
  public CExoString getCExoString(String string) {
   return (CExoString)getSubField(string);
  }

  /**
   * getStructType
   *
   * @return int
   */
  public int getStructType() {
    return structType;
  }

  /**
   * getFloat
   *
   * @param string String
   * @return float
   */
  public FLOAT getFloat(String string) {
    return (FLOAT)getSubField(string);
  }

  /**
   * getResRef
   *
   * @param string String
   * @return Object
   */
  public ResRef getResRef(String string) {
    return (ResRef)getSubField(string);
  }

  /**
   * getDWORD
   *
   * @param string String
   * @return Object
   */
  public DWORD getDWORD(String string) {
    return (DWORD)getSubField(string);
  }
  
  /**
   * getList
   *
   * @param string String
   * @return Object
   */
  public GFFList getList(String string) {
    return (GFFList)getSubField(string);
  }

public boolean hasField(String string) {
	return fields.containsKey(string.toUpperCase());
}

}
