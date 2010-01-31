package com.worldwizards.nwn.files;

import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.nio.channels.FileChannel.*;
import com.worldwizards.nwn.ResourceSource;

class BIFFVariableEntry {
  int ID;
  short resType;
  long offset;
  long size;
  /**
   * BIFFVariableEntry
   *
   * @param i int
   * @param i1 int
   * @param i2 int
   * @param i3 int
   */
  public BIFFVariableEntry(int ID, short resType, long offset, long size) {
    this.ID = ID;
    this.resType = resType;
    this.offset = offset;
    this.size = size;
  }

}

public class BIFFile  {
  String name;
  short driveMask;
  FileChannel chan;
  ByteOrder order;
  int variableTableOffset;
  BIFFVariableEntry[] variableEntries;
  private static final long BIF_HDR_SIZE = 20;
  private static final int VTABLE_ENTRY_SIZE = 16;

  public BIFFile(String name, short driveMask, ByteOrder order) {
    this.name = name;
    this.driveMask = driveMask;
    this.order = order;
    ByteBuffer buff;
    String fixName = name.replace("\\", java.io.File.separator);
    //System.out.println("************ "+fixName);
    try {
      chan =
          new FileInputStream(new File(fixName)).getChannel();
      buff = chan.map(FileChannel.MapMode.READ_ONLY, 0, BIF_HDR_SIZE);
      buff.order(order);
    }
    catch (Exception e) {
      e.printStackTrace();
      return;
    }
    byte[] fourbytes = new byte[4];
    buff.get(fourbytes);
    if (!new String(fourbytes).equals("BIFF")) {
      System.err.println("Error trying to open " + name +
                         " as BIFF (Not type BIFF)");
      return;
    }
    buff.get(fourbytes);
    if (!new String(fourbytes).equals("V1  ")) {
      System.err.println("Error trying to open " + name +
                         " as BIFF (Not version V1)");
      return;
    }
    int variableResourceCount = buff.getInt();
    int fixedResourceCount = buff.getInt();
    variableTableOffset = buff.getInt();
    try {
      buff = chan.map(MapMode.READ_ONLY, BIF_HDR_SIZE,
                      VTABLE_ENTRY_SIZE * variableResourceCount);
      buff.order(order);
    }
    catch (Exception ex) {
      ex.printStackTrace();
      return;
    }
    variableEntries = new BIFFVariableEntry[variableResourceCount];
    for(int i=0;i<variableResourceCount;i++){
      int ID = buff.getInt();
      long offset = buff.getInt();
      long size = buff.getInt();
      int resType = buff.getInt();
      variableEntries[i] = new BIFFVariableEntry(ID,(short)resType,offset,size);
    }
  }

  /**
   * getName
   *
   * @return String
   */
  public String getName() {
    return "";
  }

  /**
   * getResource
   *
   * @param resRef String
   * @param resType short
   * @return NWNResource
   */
  public ByteBuffer getRawResource(String resName, int resourceIdx) {
    BIFFVariableEntry entry = variableEntries[resourceIdx];
    try {
     ByteBuffer buff = chan.map(MapMode.READ_ONLY, entry.offset,
                                entry.size);
     buff.order(order);
     return buff;
   }
   catch (Exception ex) {
     ex.printStackTrace();
     return null;
   }

      

  }

    long getRawResourceSize(String resRef, int resIdx) {
        BIFFVariableEntry entry = variableEntries[resIdx];
        return entry.size;
    }
}
