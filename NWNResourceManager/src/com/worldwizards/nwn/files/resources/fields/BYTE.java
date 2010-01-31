package com.worldwizards.nwn.files.resources.fields;

import com.worldwizards.nwn.files.resources.*;

public class BYTE extends GFFField{
  byte value;
  public BYTE(String name, byte value) {
    super(name);
    this.value = value;
  }

  public void dump(String prefix){
    super.dump(prefix);
    System.out.println(prefix+"Type: BYTE");
    System.out.println(prefix+"Value: "+value);
  }

  /**
   * byteValue
   *
   * @return byte
   */
  public byte byteValue() {
    return value;
  }
}
