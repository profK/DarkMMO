package com.worldwizards.nwn.files;

import java.nio.*;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.io.OutputStream;
import java.util.Map.Entry;
import java.io.PrintStream;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class StringList {
  Map stringMap = new HashMap();
  public StringList() {
  }

  /**
   * StringList
   *
   * @param byteBuffer ByteBuffer
   * @param i int
   */
  public StringList(ByteBuffer buff, int entries) {
    for(int  i=0;i<entries;i++){
      int languageID = buff.getInt();
      int len = buff.getInt();
      byte[] inbytes = new byte[len];
      buff.get(inbytes);
      String str = new String(inbytes);
      stringMap.put(new Integer(languageID),str);
    }
  }

  public String getString(Integer languageID){
    return  (String) stringMap.get(languageID);
  }

  public void dump(PrintStream out){
    for(Iterator i = stringMap.entrySet().iterator();i.hasNext();){
      out.println("=================== String List ==========");
      Entry e = (Entry)i.next();
      out.println("LanguageID="+((Integer)e.getKey())+":");
      out.println((String)e.getValue());
    }
  }
}
