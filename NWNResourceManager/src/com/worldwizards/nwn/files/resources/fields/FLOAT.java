package com.worldwizards.nwn.files.resources.fields;

import com.worldwizards.nwn.files.resources.*;

public class FLOAT extends GFFField {
  float value;
  public FLOAT(String name, float value) {
  super(name);
  this.value = value;
}

public void dump(String prefix){
  super.dump(prefix);
  System.out.println(prefix+"Type: FLOAT");
  System.out.println(prefix+"Value: "+value);
}

  /**
   * floatValue
   *
   * @return float
   */
  public float floatValue() {
    return value;
  }
}
