package com.worldwizards.nwn.files.resources.fields;

import com.worldwizards.nwn.files.resources.GFFField;

public class WORD extends GFFField {
  short value;
  public WORD(String name, short value) {
  super(name);
  this.value = value;
}

public void dump(String prefix){
  super.dump(prefix);
  System.out.println(prefix+"Type: WORD");
  System.out.println(prefix+"Value: "+value);
}

/**
 * @return
 */
public short shortValue() {
	return value;
}
}
