package com.worldwizards.nwn.files;

import java.lang.reflect.*;
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
import com.worldwizards.nwn.files.resources.*;
import com.worldwizards.nwn.files.resources.NWNImage;
import java.io.Serializable;



public class NWNResource implements Serializable{
      public final static long serialVersionUID = 1L;
  static final Map resourcesByID = new HashMap();
  static final Map resourcesByExt = new HashMap();
  static final Map extensionsByID = new HashMap();
  static final Map IDsByExtension = new HashMap();

  String fileName;
  short resType;
  
  static {
      NWNImage.register();
      NWNText.register();
      Win32INI.register();
      GFF.register();
      NWN2DA.register();
      RawBytes.register();
      NWNPLT.register();
      NWNWOK.register();
     // NWNTilesetPalette.register(); as GFF for now
  }
  
  public static void registerHandler(int id, String ext, Class handler){
    System.out.println("Registering handler: "+id+","+ext+","+handler);
    Integer bi = new Integer(id);
    resourcesByID.put(bi,handler);
    resourcesByExt.put(ext,handler);
    extensionsByID.put(bi,ext);
    IDsByExtension.put(ext,bi);
  }
  
  public NWNResource(String name,short type){
    fileName = name;
    resType = type;
  }

  public String getName(){
    return fileName;
  }

  public String getResRef() {
    int dotPos = fileName.indexOf('.');
    if (dotPos>-1) {
      return fileName.substring(0, dotPos).toLowerCase();
    } else {
      return fileName.toLowerCase();
    }
  }

  public short getType(){
    return resType;
  }

  public String getTypeName(){
    return "Unknown";
  }

  public String getExtName(){
    String ext = (String)extensionsByID.get(new Integer(resType));
    if (ext  == null) {
      ext = Integer.toString(resType);
    }
    return ext;
  }

  /**
   * newResource
   *
   * @param resourceBuff ByteBuffer
   * @param resType short
   * @return ERFResource
   */
  public static NWNResource newResource(ByteBuffer resourceBuff,
                                        String name, short resType) {
    Class resourceClass = (Class)resourcesByID.get(new Integer(resType));
    if (resourceClass != null) { //registered
      String ext = (String)extensionsByID.get(new Integer(resType));
      try {
        Constructor constructorMethod =
            resourceClass.getConstructor(
            new Class[] {String.class, short.class, ByteBuffer.class});
        return (NWNResource) constructorMethod.newInstance(
            new Object[] {name+"."+ext,new Short(resType),resourceBuff});
      } catch (SecurityException ex) {
        ex.printStackTrace();
      } catch (NoSuchMethodException ex) {
        ex.printStackTrace();
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    }
    return new NWNResource(name+"."+resType,resType);
  }

  /**
   * dump
   */
  public void dump() {
    System.out.println("Name: "+getName());
    System.out.println("Type: "+getTypeName()+" ("+getType()+")");
  }

  public static String getExtName(int resType){
    String extname = (String)extensionsByID.get(new Integer(resType));
    return extname;
  }

  /**
   * getResType
   *
   * @param ext String
   * @return short
   */
  public static short getResType(String ext) {
    Integer id = (Integer)IDsByExtension.get(ext);
    if (id == null) {
      return -1;
    }
    return id.shortValue();
  }

  /**
   * getExtByID
   *
   * @param i int
   * @return String
   */
  public static String getExtByID(int i) {
    return (String)extensionsByID.get(new Integer(i));
  }
}
