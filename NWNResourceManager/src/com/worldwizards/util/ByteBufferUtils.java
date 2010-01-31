/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.worldwizards.util;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jeff Kesselman
 */
public class ByteBufferUtils {
   
    private ByteBufferUtils(){
        
       
    }

    static public ByteBuffer fromInputStream(InputStream strm){
        try {
            byte[] bytes = new byte[strm.available()];
            strm.read(bytes);
            return ByteBuffer.wrap(bytes);
        } catch (IOException ex) {
            Logger.getLogger(ByteBufferUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
