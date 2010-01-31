package com.worldwizards.nwn.files;

import java.util.*;

import com.worldwizards.nwn.files.resources.*;
import com.worldwizards.nwn.files.resources.fields.*;

public class NWNGit {
  GFF gff;
  private List<NWNDoorInstance> doorInstances = new ArrayList();
  private List<NWNPlaceableInstance> placeableInstances = new ArrayList();
  public NWNGit(GFF resource) {
    gff = resource;
    // get door descriptions
    GFFList doorList = gff.getList("Door List");
    for (int i = 0; i < doorList.length(); i++) {
      Struct gffDoorStruct = doorList.getStruct(i);
      NWNDoorInstance doorInstance = null;
      try {
        doorInstance = new NWNDoorInstance(gffDoorStruct);
        doorInstances.add(doorInstance);
      }
      catch (InstantiationException ex) {
        ex.printStackTrace();
      }
    }
    // get placeable descriptions
    GFFList placeableList = gff.getList("Placeable List");
    for (int i = 0; i < placeableList.length(); i++) {
      Struct gffPlaceableStruct = placeableList.getStruct(i);
      NWNPlaceableInstance placeableInstance = null;
      try {
        placeableInstance = new NWNPlaceableInstance(gffPlaceableStruct);
        placeableInstances.add(placeableInstance);
      }
      catch (InstantiationException ex) {
        ex.printStackTrace();
      }
    }

  }


  /**
   * getDoorInstances
   *
   * @return List
   */
  public List<NWNDoorInstance> getDoorInstances() {
    return doorInstances;
  }

  /**
   * getPlaceableInstances
   *
   * @return List
   */
  public List<NWNPlaceableInstance> getPlaceableInstances() {
    return placeableInstances;
  }

}
