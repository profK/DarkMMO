package com.worldwizards.nwn.files.resources.fields;

import com.worldwizards.nwn.files.resources.*;

public class ResRef
    extends GFFField {
  String value;
  public ResRef(String name, String value) {
  super(name);
  this.value = value.toLowerCase();
}

public void dump(String prefix){
  super.dump(prefix);
  System.out.println(prefix+"Type: ResRef");
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
