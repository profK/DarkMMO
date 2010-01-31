/*
 * TrimeshNode.java
 * 
 * Created on Jun 8, 2007, 10:19:14 PM
 * 
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.worldwizards.nwn.model.bioware;

import java.io.IOException;
import java.nio.ByteBuffer;

import com.worldwizards.util.parse.TextParser;
import com.worldwizards.util.parse.TokenNotFoundException;

/**
 *
 * @author Jeff
 */
public class TrimeshNode extends MeshNode {
    
    public TrimeshNode(int pos, ByteBuffer modelDataBuff,
            ByteBuffer rawDataBuff, boolean trace) {
            super(pos,modelDataBuff,rawDataBuff,trace);
    }

	public TrimeshNode(TextParser parser, boolean trace) throws IOException, TokenNotFoundException {
		super(parser,trace);
	}
}
