package com.worldwizards.nwn.files.resources;

import java.io.*;
import java.nio.*;
import java.nio.charset.*;
import java.util.*;
import java.util.Map.*;

import com.worldwizards.nwn.files.*;
import com.worldwizards.util.*;
import com.worldwizards.util.parse.TextParser;
import com.worldwizards.util.parse.TokenNotFoundException;

public class NWNWOK extends NWNResource {

	public static void register() {
		NWNResource.registerHandler(2016, "wok", NWNWOK.class);
	}

	public class AABBNode {
		float[] min;

		float[] max;

		int leafFace = -1;

		AABBNode left = null;

		AABBNode right = null;

		public AABBNode(float[] min, float[] max, int face) {
			this.min = min;
			this.max = max;
			this.leafFace = face;
		}

		public void setLeft(AABBNode l) {
			left = l;
		}

		public void setRight(AABBNode r) {
			right = r;
		}
	}

	private static AABBNode aabbRoot = null;

	private static Charset charset = Charset.forName("ISO-8859-15");

	private static CharsetDecoder decoder = charset.newDecoder();

	private static final boolean DEBUG = false;

	Map iniParagraphs = new HashMap();

	private String fileName;

	private String nodeName;

	private float[] wirecolor;

	private String parent;

	private float[] position;

	private float[] orientation;

	private float[][] verts;

	private int[][] faces;

	public NWNWOK(String name, short type, ByteBuffer buff) {
		super(name, type);
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
			parser.expectString("MAXWALKMESH");
			parser.expectString("ASCII");
			parser.expectString("beginwalkmeshgeom");
			fileName = parser.getString();
			parser.expectString("node");
			parser.expectString("aabb");
			nodeName = parser.getString();
			String[] sections = new String[] { "wirecolor", "parent",
					"position", "orientation", "verts", "aabb", "faces",
					"endnode" };
			int section = parser.expectStrings(sections);
			while (section != 7) {
				switch (section) {
				case 0: // wirecolor
					wirecolor = new float[3];
					for (int i = 0; i < 3; i++) {
						wirecolor[i] = parser.getFloat();
					}
					break;
				case 1: // parent
					parent = parser.getString();
					break;
				case 2: // position
					position = new float[3];
					for (int i = 0; i < 3; i++) {
						position[i] = parser.getFloat();
					}
					break;
				case 3: // orientation
					orientation = new float[4];
					for (int i = 0; i < 4; i++) {
						orientation[i] = parser.getFloat();
					}
					break;
				case 4: // verts
					readVerts(parser);
					break;
				case 5: // aabb
					readAABB(parser);
					break;
				case 6: // faces
					readFaces(parser);
				}
				section = parser.expectStrings(sections);
			}
		} catch (IOException e) {

			e.printStackTrace();
		} catch (TokenNotFoundException e) {

			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * @param parser
	 * @throws IOException
	 */
	private void readFaces(TextParser parser) throws IOException {

		int numFaces = parser.getInteger();
		faces = new int[numFaces][];
		for (int i = 0; i < numFaces; i++) {
			int pt1 = parser.getInteger();
			int pt2 = parser.getInteger();
			int pt3 = parser.getInteger();
			for(int c=0;c<4;i++){ //grou p and texture, irellevent
				parser.getInteger();
			}
			int terrain = parser.getInteger();
			faces[i] = new int[] { pt1, pt2, pt3, terrain };
		}

	}

	/**
	 * @param parser
	 * @throws IOException
	 * 
	 */
	private void readAABB(TextParser parser) throws IOException {
		aabbRoot = readAABBnode(parser);
	}

	/**
	 * @param parser
	 * @return
	 */
	private AABBNode readAABBnode(TextParser parser) throws IOException {
		float[] min = new float[3];
		for (int i = 0; i < 3; i++) {
			min[i] = parser.getFloat();
		}
		float[] max = new float[3];
		for (int i = 0; i < 3; i++) {
			max[i] = parser.getFloat();
		}
		int leaf = parser.getInteger();
		AABBNode node = new AABBNode(min, max, leaf);
		if (leaf == -1) {
			node.setLeft(readAABBnode(parser));
			node.setRight(readAABBnode(parser));
		}
		return node;
	}

	/**
	 * @throws IOException
	 * 
	 */
	private void readVerts(TextParser parser) throws IOException {
		int numVerts = parser.getInteger();
		verts = new float[numVerts][];
		for (int i = 0; i < numVerts; i++) {
			float x = parser.getFloat();
			float y = parser.getFloat();
			float z = parser.getFloat();
			verts[i] = new float[] { x, y, z };
		}

	}

	/**
	 * @return
	 */
	public String getParent() {	
		return parent;
	}

	/**
	 * @return
	 */
	public float[] getPosition() {		
		return position;
	}

	/**
	 * @return
	 */
	public float[] getOrientation() {
		return orientation;
	}

	/**
	 * @return
	 */
	public float[] getWireColor() {		
		return wirecolor;
	}

	/**
	 * @return
	 */
	public float[][] getVerts() {	
		return verts;
	}

}
