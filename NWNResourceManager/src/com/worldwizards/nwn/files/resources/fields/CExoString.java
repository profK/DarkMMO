package com.worldwizards.nwn.files.resources.fields;

import com.worldwizards.nwn.files.resources.*;

public class CExoString
    extends GFFField {
  String value;
  public CExoString(String name, String value) {
  super(name);
  this.value = value;
}

public void dump(String prefix){
  super.dump(prefix);
  System.out.println(prefix+"Type: CExoString");
  System.out.println(prefix+"Value: "+value);
}

  /**
   * stringValue
   *
   * @return String
   */
  public String stringValue() {
    return value;
  }
}
