package com.worldwizards.nwn.files.resources;

import java.io.BufferedReader;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.ArrayList;
import java.util.List;

import com.worldwizards.nwn.files.NWNResource;
import com.worldwizards.nwn.model.bioware.DummyNode;
import com.worldwizards.nwn.model.bioware.MdlNode;
import com.worldwizards.nwn.model.bioware.MeshNode;
import com.worldwizards.nwn.model.bioware.TrimeshNode;
import com.worldwizards.util.CharBufferReader;
import com.worldwizards.util.parse.TextParser;

public class NWNPWK {
	
	private static Charset charset = Charset.forName("ISO-8859-15");
	private static CharsetDecoder decoder = charset.newDecoder();
	
	private String rootName;
	private List<DummyNode> useNodes = new ArrayList<DummyNode>();
	private List<TrimeshNode> meshNodes = new ArrayList<TrimeshNode>();
	

	
	
	public NWNPWK(ByteBuffer buff, boolean trace) {
		CharBuffer cbuffer = null;
		try {
			cbuffer = decoder.decode(buff);
		} catch (CharacterCodingException ex) {
			ex.printStackTrace();
			return;
		}
		CharBufferReader rdr = new CharBufferReader(cbuffer);
		BufferedReader brdr = new BufferedReader(rdr);
		TextParser parser = new TextParser(brdr);
		try {
			parser.expectString("#");
			parser.expectString("MAXDOOR");
			parser.expectString("ASCII");
			parser.expectString("#");
			parser.expectString("model");
			parser.expectString(":");
			rootName = parser.getString();
			boolean done=false;
			while(parser.hasMore()&&!done){
				int section = parser.expectStrings(
					new String[] {"filedependancy","node",""});
				switch(section){
					case 0: // filedependnacy
						String fdependancy = parser.getString();
						break;
					case 1: //node
						int nodeType = parser.expectStrings(
								new String[]{"dummy","trimesh"});
						if (nodeType==0){ // dummy node
							useNodes.add(new DummyNode(parser,trace));
						} else if (nodeType==1){
							meshNodes.add(new TrimeshNode(parser,trace));
						}
						break;
					case 3: // end of file
						done=true;
						break;
				}
			}
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;




	public TrimeshNode[] getMeshNodes() {
		TrimeshNode[] array = new TrimeshNode[meshNodes.size()];
		meshNodes.toArray(array);
		return array;
	}

}
