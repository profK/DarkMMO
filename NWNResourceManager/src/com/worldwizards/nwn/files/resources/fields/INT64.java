package com.worldwizards.nwn.files.resources.fields;

import com.worldwizards.nwn.files.resources.GFFField;

public class INT64 extends GFFField {
  long value;
  public INT64(String name, long value) {
  super(name);
  this.value = value;
}

public void dump(String prefix){
  super.dump(prefix);
  System.out.println(prefix+"Type: INT64");
  System.out.println(prefix+"Value: "+value);
}
}
