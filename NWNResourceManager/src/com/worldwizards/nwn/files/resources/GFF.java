package com.worldwizards.nwn.files.resources;

import java.nio.*;
import java.util.*;

import com.worldwizards.nwn.files.*;
import com.worldwizards.nwn.files.resources.fields.*;
import java.io.Serializable;

public class GFF
    extends NWNResource implements Serializable {
      public final static long serialVersionUID = 1L;

public static void register() {
    NWNResource.registerHandler(2012, "are", GFF.class);
    NWNResource.registerHandler(2014, "ifo", GFF.class);
    NWNResource.registerHandler(2015, "bic", GFF.class);
    NWNResource.registerHandler(2023, "git", GFF.class);
    NWNResource.registerHandler(2025, "uti", GFF.class);
    NWNResource.registerHandler(2030, "itp", GFF.class);
  }

  byte[] fileType = new byte[4];
  byte[] versionString = new byte[4];
  Struct rootStruct;
  private static final boolean DEBUG = false;
  public GFF(String name, short type, ByteBuffer buff) {
    super(name, type);
    buff.get(fileType);
    buff.get(versionString);
    if (DEBUG) {
      System.out.println("GFF File Type: " + new String(fileType));
      System.out.println("GFF Version: " + new String(versionString));
    }
    int structArrayOffset = buff.getInt();
    int structCount = buff.getInt();
    int fieldOffset = buff.getInt();
    int fieldCount = buff.getInt();
    int labelOffset = buff.getInt();
    int labelCount = buff.getInt();
    int fieldDataOffset = buff.getInt();
    int fieldDataCount = buff.getInt();
    int fieldIndicesOffset = buff.getInt();
    int fieldIndicesCount = buff.getInt();
    int listIndicesOffset = buff.getInt();
    int listIndicesCount = buff.getInt();
    // now load the struct array
    // the struct array is conceptually a tree.  There is allways a top
    // level struct.  In in turn contains fields.  A Field can be of many types
    // including a struct.  All other structs descend thus in tree fashion
    // from the top level struct
    // Step 1: makebuffers for parsing file
    buff.position(structArrayOffset);
    ByteBuffer structBuff = buff.slice();
    structBuff.order(ByteOrder.LITTLE_ENDIAN);
    buff.position(fieldOffset);
    ByteBuffer fieldBuff = buff.slice();
    fieldBuff.order(ByteOrder.LITTLE_ENDIAN);
    buff.position(labelOffset);
    ByteBuffer labelBuff = buff.slice();
    labelBuff.order(ByteOrder.LITTLE_ENDIAN);
    buff.position(fieldDataOffset);
    ByteBuffer fieldDataBuff = buff.slice();
    fieldDataBuff.order(ByteOrder.LITTLE_ENDIAN);
    buff.position(fieldIndicesOffset);
    ByteBuffer fieldIndicesBuff = buff.slice();
    fieldIndicesBuff.order(ByteOrder.LITTLE_ENDIAN);
    buff.position(listIndicesOffset);
    ByteBuffer listIndicesBuff = buff.slice();
    listIndicesBuff.order(ByteOrder.LITTLE_ENDIAN);
    //Step 2: create struct and field tree
    rootStruct = GFFField.parseAll(structBuff, fieldBuff, labelBuff,
                                   fieldDataBuff,
                                   fieldIndicesBuff, listIndicesBuff);

  }

  public void dump() {
    super.dump();
    rootStruct.dump("    ");
  }

  public String getTypeName() {
    return "Unknown";
  }

  /**
   * getInt
   *
   * @param string String
   * @return int
   */
  public INT getInt(String string) {
    return (INT)getField(string);
  }
  
  /**
   * getDword
   *
   * @param string String
   * @return int
   */
  public DWORD getDword(String string) {
    return (DWORD)getField(string);
  }
  
  /**
   * getByte
   *
   * @param string String
   * @return BYTE
   */
  public BYTE getByte(String string) {
    return (BYTE)getField(string);
  }
  
  /**
  * getFloat
  *
  * @param string String
  * @return FLOAT
  */
 public FLOAT getFloat(String string) {
   return (FLOAT)getField(string);
 }
  
  /**
  * @param string
  * @return
  */
 public WORD getWord(String string) {
 	return (WORD)getField(string);
 }

  /**
  * getResRef
  *
  * @param string String
  * @return ResRef
  */
 public ResRef getResRef(String string) {
   return (ResRef)getField(string);
 }

 /**
  * getCExoString
  *
  * @param string String
  * @return ResRef
  */
 public CExoString getCExoString(String string) {
   return (CExoString)getField(string);
 }
 
 /**
  * getCExoLocString
  *
  * @param string String
  * @return ResRef
  */
 public CExoLocString getCExoLocString(String string) {
   return (CExoLocString)getField(string);
 }

  private GFFField getField(String string){
    GFFField field = rootStruct;
    StringTokenizer tok = new StringTokenizer(string,"!");
    while (tok.hasMoreTokens()){
      String label = tok.nextToken();
      if (field instanceof Struct) {
        field = ((Struct)field).getSubField(label);
      } else {
        return null;
      }
    }
    return field;
  }

  /**
   * getList
   *
   * @param string String
   * @return GFFList
   */
  public GFFList getList(String string) {
    return (GFFList)getField(string);
  }

  /**
   * getRootStruct
   *
   * @return Struct
   */
  public Struct getRootStruct() {
    return rootStruct;
  }

}
