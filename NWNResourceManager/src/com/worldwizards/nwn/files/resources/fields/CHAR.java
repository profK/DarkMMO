package com.worldwizards.nwn.files.resources.fields;

import com.worldwizards.nwn.files.resources.GFFField;

public class CHAR extends GFFField{
 char value;
 public CHAR(String name, char value) {
   super(name);
   this.value = value;
 }

 public void dump(String prefix){
   super.dump(prefix);
   System.out.println(prefix+"Type: CHAR");
   System.out.println(prefix+"Value: "+value);
 }
}
