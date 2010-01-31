package com.worldwizards.nwn.files.resources.fields;

import com.worldwizards.nwn.files.resources.GFFField;

public class DOUBLE
    extends GFFField {
  double value;
  public DOUBLE(String name, double value) {
  super(name);
  this.value = value;
}

public void dump(String prefix){
  super.dump(prefix);
  System.out.println(prefix+"Type: DOUBLE");
  System.out.println(prefix+"Value: "+value);
}
}
