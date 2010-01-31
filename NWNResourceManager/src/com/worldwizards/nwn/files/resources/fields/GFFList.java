package com.worldwizards.nwn.files.resources.fields;

import com.worldwizards.nwn.files.resources.GFFField;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import com.worldwizards.nwn.files.resources.fields.Struct;
import java.util.Map;
import java.util.HashMap;

public class GFFList
    extends GFFField  {
  List structList = new ArrayList();
  public GFFList(String name) {
    super(name);

  }

  public void addStruct(Struct s){
    structList.add(s);
  }

  public void dump(String prefix) {
    super.dump(prefix);
    System.out.println(prefix + "Type: List");
    prefix += "    ";
    for (Iterator i = structList.iterator(); i.hasNext(); ) {
      Struct struct = (Struct)i.next();
      struct.dump(prefix);
    }
  }



  public Struct getStruct(int idx){
    return (Struct)structList.get(idx);
  }

  public int length(){
    return structList.size();
  }
}
