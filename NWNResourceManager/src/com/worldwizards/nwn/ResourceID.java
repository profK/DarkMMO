/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.worldwizards.nwn;

import com.worldwizards.nwn.files.resources.NWN2DA;
import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 *
 * @author Jeff Kesselman
 */
public class ResourceID {

    static final Map<String, Short> extToIDMap = new HashMap<String, Short>();
    static final Map<Short,String> idToExtMap = new HashMap<Short,String>();

    static {
        NWN2DA resID2da = new NWN2DA("resIDs", (short) 2017,
                ResourceID.class.getResourceAsStream("/res/ResIDs.2da"));
        for (List<String> row : resID2da.getRows()) {
            //System.out.println("Row: " + row.toString());
            extToIDMap.put(row.get(1), Short.valueOf(row.get(0)));
            idToExtMap.put(Short.valueOf(row.get(0)),row.get(1));
        }
    }

    

    private ResourceID() {
    // static class
    }

    public static short idForExt(String ext) {
        if (extToIDMap.containsKey(ext)) {
            return extToIDMap.get(ext);
        } else if (ext.startsWith("res")){
            return Short.valueOf(ext.substring(3));
        } else {
            throw new InvalidParameterException(
                    "Extension " + ext + " unknown.");
        }
    }
    
    static String extForID(short id) {
        if (idToExtMap.containsKey(id)) {
            return idToExtMap.get(id);
        } else {
            return "res"+id;
        }
    }


// testing
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.print("Extension: ");
        String input = in.nextLine();
        while(!input.equals("")){
            System.out.println(input+": "+ResourceID.idForExt(input));
            System.out.print("Extension: ");
            input = in.nextLine();
        }

    }
}
