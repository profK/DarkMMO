package com.worldwizards.nwn.files.resources;

import java.nio.*;

import com.worldwizards.nwn.files.resources.fields.*;
import java.io.Serializable;

public class GFFField implements Serializable{
      public final static long serialVersionUID = 1L;
  private static final int FIELD_SIZE = 12;
  private static final int LABEL_SIZE = 16;
  private String fieldName;
  private static final int STRUCT_SIZE = 12;
  private static final boolean DEBUG = false;
  public GFFField(String name) {
    fieldName = name;
  }

  public void dump(String prefix) {
    System.out.println(prefix + "Field: " + fieldName);
  }

  public static Struct parseAll(ByteBuffer structBuff, ByteBuffer fieldBuff,
                                ByteBuffer labelBuff,
                                ByteBuffer fieldDataBuff,
                                ByteBuffer fieldIndicesBuff,
                                ByteBuffer listIndicesBuff) {
    return (Struct)parseField(14,"ROOT",0,
                      structBuff, fieldBuff, labelBuff, fieldDataBuff,
                      fieldIndicesBuff, listIndicesBuff);

  }

  //

  /**
   * newField
   *
   * @param structBuff ByteBuffer
   * @param fieldBuff ByteBuffer
   * @param labelBuff ByteBuffer
   * @param fieldDataBuff ByteBuffer
   * @param fieldIndicesBuff ByteBuffer
   * @param listIndicesBuff ByteBuffer
   * @return GFFField
   */
  private static GFFField newField(ByteBuffer structBuff, ByteBuffer fieldBuff,
                                   ByteBuffer labelBuff,
                                   ByteBuffer fieldDataBuff,
                                   ByteBuffer fieldIndicesBuff,
                                   ByteBuffer listIndicesBuff) {
    int fieldType = fieldBuff.getInt();
    int labelIndex = fieldBuff.getInt();
    int fieldDataOrOffset = fieldBuff.getInt();
    labelBuff.position(labelIndex * LABEL_SIZE);
    byte[] fieldLabel = new byte[LABEL_SIZE];
    labelBuff.get(fieldLabel);
    String fieldName = new String(fieldLabel);
    int nullpos = fieldName.indexOf(0);
    if (nullpos!=-1) {
      fieldName = fieldName.substring(0, nullpos);
    }
    return parseField(fieldType, fieldName, fieldDataOrOffset,
                      structBuff, fieldBuff, labelBuff, fieldDataBuff,
                      fieldIndicesBuff, listIndicesBuff);
  }

  private static GFFField parseField(int fieldType, String fieldName,
                                     int fieldDataOrOffset,
                                     ByteBuffer structBuff,
                                     ByteBuffer fieldBuff,
                                     ByteBuffer labelBuff,
                                     ByteBuffer fieldDataBuff,
                                     ByteBuffer fieldIndicesBuff,
                                     ByteBuffer listIndicesBuff) {
    if (DEBUG){
      System.out.println("Parsing field named "+fieldName+" of type "+
                         fieldType);
    }
    switch (fieldType) {
      case 0: // BYTE
        return new BYTE(fieldName, (byte) fieldDataOrOffset);
      case 1: // CHAR
        return new CHAR(fieldName, (char) fieldDataOrOffset);
      case 2: // WORD
        return new WORD(fieldName, (short) fieldDataOrOffset);
      case 3: // SHORT
        return new SHORT(fieldName, (short) fieldDataOrOffset);
      case 4: // DWORD
        return new DWORD(fieldName, fieldDataOrOffset);
      case 5: //INT
        return new INT(fieldName, fieldDataOrOffset);
      case 6: //DWORD64
        long lval = fieldDataBuff.getLong(fieldDataOrOffset);
        return new DWORD64(fieldName, lval);
      case 7: // INT64
        lval = fieldDataBuff.getLong(fieldDataOrOffset);
        return new INT64(fieldName, lval);
      case 8: // float

        // look back back and read field data as a float
        float fval = fieldBuff.getFloat(fieldBuff.position() - 4);
        return new FLOAT(fieldName, fval);
      case 9: //DOUBLE
        double dval = fieldDataBuff.getDouble(fieldDataOrOffset);
        return new DOUBLE(fieldName, dval);
      case 10: //CExoString
        fieldDataBuff.position(fieldDataOrOffset);
        int strlen = fieldDataBuff.getInt();
        byte[] strbytes = new byte[strlen];
        fieldDataBuff.get(strbytes);
        return new CExoString(fieldName, new String(strbytes));
      case 11: // ResRef
        fieldDataBuff.position(fieldDataOrOffset);
        strlen = (int) fieldDataBuff.get();
        strbytes = new byte[strlen];
        fieldDataBuff.get(strbytes);
        return new ResRef(fieldName, new String(strbytes));
      case 12: // CExoLocString
        fieldDataBuff.position(fieldDataOrOffset);
        int totalSize = fieldDataBuff.getInt();
        int dlogTxtIdx = fieldDataBuff.getInt();
        int strCount = fieldDataBuff.getInt();
        String[] strarray = new String[strCount];
        CExoLocString exoLoc = new CExoLocString(fieldName, dlogTxtIdx);
        for (int i = 0; i < strarray.length; i++) {
          int strID = fieldDataBuff.getInt();
          strlen = fieldDataBuff.getInt();
          strbytes = new byte[strlen];
          fieldDataBuff.get(strbytes);
          exoLoc.addString(strID, new String(strbytes));
        }
        return exoLoc;
      case 13: // VOID
        fieldDataBuff.position(fieldDataOrOffset);
        int voidsz = fieldDataBuff.getInt();
        byte[] voidData = new byte[voidsz];
        fieldDataBuff.get(voidData);
        return new VOID(fieldName, voidData);
      case 14: // STRUCT
        structBuff.position(fieldDataOrOffset*STRUCT_SIZE);
        int structType = structBuff.getInt();
        int structDataOrOffset = structBuff.getInt();
        int structFieldCount = structBuff.getInt();
        Struct newStruct = new Struct(fieldName, structType);
        if (structFieldCount == 1) {
          fieldBuff.position(structDataOrOffset * FIELD_SIZE);
          newStruct.addField(newField(structBuff, fieldBuff, labelBuff,
                                      fieldDataBuff,
                                      fieldIndicesBuff, listIndicesBuff));
        }
        else {
          for (int i = 0; i < structFieldCount; i++) {
            fieldIndicesBuff.position(structDataOrOffset+(i*4));
            int fieldpos = fieldIndicesBuff.getInt();
            fieldBuff.position(fieldpos * FIELD_SIZE);
            newStruct.addField(newField(structBuff, fieldBuff, labelBuff,
                                        fieldDataBuff, fieldIndicesBuff,
                                        listIndicesBuff));
          }
        }
        return newStruct;
      case 15: //LIST
        listIndicesBuff.position(fieldDataOrOffset);
        int structCount = listIndicesBuff.getInt();
        GFFList newList = new GFFList(fieldName);
        for (int i = 0; i < structCount; i++) {
           listIndicesBuff.position(fieldDataOrOffset+((i+1)*4));
          int structIdx = listIndicesBuff.getInt();
          newStruct =
              (Struct) parseField(14, Integer.toString(i), structIdx,
                                  structBuff, fieldBuff, labelBuff,
                                  fieldDataBuff, fieldIndicesBuff,
                                  listIndicesBuff);
          newList.addStruct(newStruct);
        }
        return newList;
    }
    return null;
  }

  /**
   * getFieldName
   *
   * @return Object
   */
  public String getFieldName() {
    return fieldName;
  }



}
