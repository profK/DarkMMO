package com.worldwizards.nwn.files.resources.fields;

import java.util.*;
import java.util.Map.*;

import com.worldwizards.nwn.files.resources.*;

public class CExoLocString
    extends GFFField {
  int stringRef;
  Map localizedStrings = new HashMap();
  public CExoLocString(String name, int stringRef) {
    super(name);
    this.stringRef = stringRef;
  }

  public void addString(int strID, String string) {
    localizedStrings.put(new Integer(strID), string);
  }

  public void dump(String prefix) {
    super.dump(prefix);
    System.out.println(prefix + "Type: CExLocString");
    System.out.println(prefix + "StringRef: " + stringRef);
    for(Iterator i =localizedStrings.entrySet().iterator();i.hasNext();){
      Entry e = (Entry)i.next();
      System.out.println(prefix+"String "+((Integer)e.getKey())+": "+
                         ((String)e.getValue()));
    }
  }

  /**
   * getString
   *
   * @param i int
   * @return String
   */
  public String getString(int i) {
    return (String)localizedStrings.get(new Integer(i));

  }

}
