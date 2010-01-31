package com.worldwizards.nwn.model.bioware;

import java.io.IOException;
import java.nio.*;
import java.util.*;
import java.util.logging.Logger;

import com.worldwizards.util.VectorUtils;
import com.worldwizards.util.parse.TextParser;
import com.worldwizards.util.parse.TokenNotFoundException;




/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company:
 * </p>
 *
 * @author not attributable
 * @version 1.0
 */

public class MeshNode extends MdlNode {
	private static final Logger logger = Logger.getLogger(MeshNode.class
			.getName());
    public enum Controllers {
        Position(8),
        Orientation(20),
        Scale(36),
        SelfIllumColor(100),
        Alpha(128);
        
        public final int code;
        
        Controllers(int code){
            this.code = code;
        }
        
        public static Controllers withCode(int code){
            for(Controllers ctrl : Controllers.values()){
                if (ctrl.code == code){
                    return ctrl;
                }
            }
            return null;
        }
    }
    
    
    private float[] boundingBoxMin;
    
    private float[] boundingBoxMax;
    
    private float meshRadius;
    
    private float[] centroid;
    
    private float[] diffuseColor;
    
    private float[] ambientColor;
    
    private float[] specularColor;
    
    private float shiniess;
    
    private float shadowFlag;
    
    private int beamingFlag;
    
    private int renderFlag;
    
    private int transparencyHint;
    
    private int currentlyUnknownFlag;
    
    private String[] texturenames;
    
    private int tileFade;
    
    private byte triangleMode;
    
    private byte lightMappedFlag;
    
    private byte rotateTextureFlag;
    
    private float vertextNormalSumOver2;
    
    
    private float[] verts;
    
    private float[] textCoords;
    
    private Face[] faces;
    
    private static final boolean WIREFRAME = false;
    
    public int fileDataSize() {
        return 0x0270;
    }
    
    /**
     * TrimeshNode
     *
     * @param pos
     *            int
     * @param modelDataBuff
     *            ByteBuffer
     * @param rawDataBuff
     *            ByteBuffer
     */
    public MeshNode(int pos, ByteBuffer modelDataBuff,
            ByteBuffer rawDataBuff, boolean trace) {
        super(pos, modelDataBuff, rawDataBuff, trace);
        if (trace) {
            System.out.println("Tri Mesh Node: " + name);
        }
        // now laod mesh data
        // mesh begins after node header and 2 C func ptrs
        modelDataBuff.position(pos + 0x0070 + 8); // skip node header and 2 C
        // func ptrs
        int faceArrayOffset = modelDataBuff.getInt();
        int faceArrayCount = modelDataBuff.getInt();
        int faceArraySize = modelDataBuff.getInt();
        boundingBoxMin = new float[3];
        for (int i = 0; i < 3; i++) {
            boundingBoxMin[i] = modelDataBuff.getFloat();
        }
        boundingBoxMax = new float[3];
        for (int i = 0; i < 3; i++) {
            boundingBoxMax[i] = modelDataBuff.getFloat();
        }
        meshRadius = modelDataBuff.getFloat(); // may not be valid, always 0
        centroid = new float[3]; // may also be invalid
        for (int i = 0; i < 3; i++) {
            centroid[i] = modelDataBuff.getFloat();
        }
        diffuseColor = new float[3]; // may also be invalid
        for (int i = 0; i < 3; i++) {
            diffuseColor[i] = modelDataBuff.getFloat();
        }
        ambientColor = new float[3]; // may also be invalid
        for (int i = 0; i < 3; i++) {
            ambientColor[i] = modelDataBuff.getFloat();
        }
        specularColor = new float[3]; // may also be invalid
        for (int i = 0; i < 3; i++) {
            specularColor[i] = modelDataBuff.getFloat();
        }
        shiniess = modelDataBuff.getFloat();
        shadowFlag = modelDataBuff.getInt();
        beamingFlag = modelDataBuff.getInt();
        renderFlag = modelDataBuff.getInt();
        transparencyHint = modelDataBuff.getInt();
        currentlyUnknownFlag = modelDataBuff.getInt();
        texturenames = new String[4];
        byte[] texturenamebuff = new byte[64];
        for (int i = 0; i < 4; i++) {
            modelDataBuff.get(texturenamebuff);
            texturenames[i] = MDL.bytesToString(texturenamebuff).toLowerCase();
        }
        if (texturenames[0].equals("null")) {
            renderFlag = 0;
        }
        tileFade = modelDataBuff.getInt();
        // skip 2 invalid arrays, only used when compiled in memory
        modelDataBuff.position(modelDataBuff.position() + 24);
        int vertexIndicesCountOffset = modelDataBuff.getInt();
        int vertextIndicesCountCount = modelDataBuff.getInt();
        int vertextIndicesCountSize = modelDataBuff.getInt();
        int vertexIndicesRawOffsetOffset = modelDataBuff.getInt();
        int vertexIndicesRawOffsetCount = modelDataBuff.getInt();
        int vertexIndicesOffsetSize = modelDataBuff.getInt();
        //skip next two 32 bit values as unknowns
        modelDataBuff.position(modelDataBuff.position() + 8);
        triangleMode = modelDataBuff.get();
        for (int i = 0; i < 3; i++) {
            modelDataBuff.get(); // skip padding
        }
        //skip pointre to runtime structure
        modelDataBuff.getInt();
        int vertexDataRawOffset = modelDataBuff.getInt();
        short vertexDataCount = modelDataBuff.getShort();
        short textureCount = modelDataBuff.getShort();
        int texture0VertexDataRawOffset = modelDataBuff.getInt();
        long texture1VertexDataRawOffset = modelDataBuff.getInt();
        long texture2VertexDataRawOffset = modelDataBuff.getInt();
        long texture3VertexDataRawOffset = modelDataBuff.getInt();
        int vertexNormalRawOffset = modelDataBuff.getInt();
        int vertexRGBARawOffset = modelDataBuff.getInt();
        long textureAnimData0RawOffset = modelDataBuff.getInt();
        long textureAnimData1RawOffset = modelDataBuff.getInt();
        long textureAnimData2RawOffset = modelDataBuff.getInt();
        long textureAnimData3RawOffset = modelDataBuff.getInt();
        long textureAnimData4RawOffset = modelDataBuff.getInt();
        long textureAnimData5RawOffset = modelDataBuff.getInt();
        lightMappedFlag = modelDataBuff.get();
        rotateTextureFlag = modelDataBuff.get();
        // skip padding
        modelDataBuff.position(modelDataBuff.position() + 2);
        // lord knwos what this is for
        vertextNormalSumOver2 = modelDataBuff.getFloat();
        //skip unknown 32 bit value
        modelDataBuff.position(modelDataBuff.position() + 4);
        // store real vertex and texture info here
        verts = new float[vertexDataCount * 3];
        rawDataBuff.position(vertexDataRawOffset);
        for (int i = 0; i < vertexDataCount * 3; i++) {
            verts[i] = rawDataBuff.getFloat();
        }
        //now load texture coordinates
        if (texture0VertexDataRawOffset != -1) {
            textCoords = new float[vertexDataCount * 2];
            rawDataBuff.position(texture0VertexDataRawOffset);
            for (int i = 0; i < vertexDataCount * 2; i++) {
                textCoords[i] = rawDataBuff.getFloat();
            }
        }
        // load vertex normals
        if (vertexNormalRawOffset>=0) {
            vertexNormals = new float[vertexDataCount * 3];
            rawDataBuff.position(vertexNormalRawOffset);
            for (int i = 0; i < vertexDataCount * 3; i++) {
                vertexNormals[i] = rawDataBuff.getFloat();
            }
        } else {
            vertexNormals = null;
        }
        // load vertex colors
        if (vertexRGBARawOffset>=0) {
            vertexColors = new int[vertexDataCount];
            rawDataBuff.position((int)vertexRGBARawOffset);
            for (int i = 0; i < vertexDataCount; i++) {
                vertexColors[i] = rawDataBuff.getInt();
            }
        } else {
            vertexColors = null;
        }
        // do faces
        float[] normals = new float[3];
        faces = new Face[faceArrayCount];
        int[] vertexIndices = new int[3];
        short[] adjacentFaces = new short[3];
        for (int i = 0; i < faceArrayCount; i++) {
            modelDataBuff.position(faceArrayOffset + (i * 0x0020));
            for (int j = 0; j < 3; j++) {
                normals[j] = modelDataBuff.getFloat();
            }
            float planeDistance = modelDataBuff.getFloat();
            int surfaceID = modelDataBuff.getInt();
            
            for (int j = 0; j < 3; j++) {
                adjacentFaces[j] = modelDataBuff.getShort();
            }
            for (int j = 0; j < 3; j++) {
                vertexIndices[j] = modelDataBuff.getShort();
            }
            faces[i] = new Face(surfaceID, normals, planeDistance,
                    vertexIndices, adjacentFaces);
        }
    }
    
    public MeshNode(TextParser parser, boolean trace) throws IOException, TokenNotFoundException {
		super(parser,trace);
		calculateFaceNeighbors(trace);
	}
    
    

	private void calculateFaceNeighbors(boolean trace) {
		List<Integer> adjacentFaces = new ArrayList<Integer>();
		for(Face face: faces){
			adjacentFaces.clear(); // set empty
			for (int i=0;i<faces.length;i++){
				if (face != faces[i]){
					if (coinicdentSegments(face.vertexIndices,faces[i].vertexIndices)){
						adjacentFaces.add(i);
					}
				}
			}
			if (adjacentFaces.size()>3){
				logger.severe("ERROR: more then 3 adjacent faces found: "+
						adjacentFaces.size());
			}
			short[] neighbors = new short[3];
			int ncount=0;
			for(int i=0;i<3;i++){
				if (i<adjacentFaces.size()){
					neighbors[i] = adjacentFaces.get(i).shortValue();
					ncount++;
				} else {
					neighbors[i]= -1;
				}
			}
			if (trace){
				System.out.println("found "+ncount+" neighbors");
			}
			face.setAdjacentFaces(neighbors);
		}
		
	}

	private boolean coinicdentSegments(int[] vi1, int[] vi2) {
		for(int i=0;i<vi1.length;i++){
			int v1p1 = vi1[i];
			int v1p2 = vi1[(i+1)%vi1.length];
			for (int j=0;j<vi2.length;j++){
				int v2p1 = vi2[j];
				int v2p2 = vi2[(j+1)%vi2.length];
				if (
					((v1p1==v2p1)&&(v1p2==v2p2))||
					((v1p1==v2p2)&&(v1p2==v2p1))
					){
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean parseField(String fieldName, TextParser parser, boolean trace)
			throws IOException {
		if (fieldName.equalsIgnoreCase("multimaterial")){
			logger.warning("field multi-material on MeshNode not implemented yet");
			int count = parser.getInteger();
			String[] materialNames = new String[count];
			for(int i=0;i<count;i++){
				materialNames[i] = parser.getString();
			}
		} else if (fieldName.equalsIgnoreCase("bitmap")){
			texturenames = new String[1];
			texturenames[0] = parser.getString();
		} else if (fieldName.equalsIgnoreCase("verts")){
			int vertCount = parser.getInteger();
			verts = new float[vertCount*3];
			for(int i=0;i<verts.length;i++){
				verts[i] = parser.getFloat();
			}
		} else if (fieldName.equalsIgnoreCase("faces")){
			int faceCount = parser.getInteger();
			faces = new Face[faceCount];
			for(int i=0;i<faces.length;i++){
				int[] vertIndexes = new int[3];
				vertIndexes[0] = parser.getInteger();
				vertIndexes[1] = parser.getInteger();
				vertIndexes[2] = parser.getInteger();
				int surfaceID = parser.getInteger();
				// per face texture coords not currently used
				int[] textureIndexes = new int[3];
				textureIndexes[0] = parser.getInteger();
				textureIndexes[1] = parser.getInteger();
				textureIndexes[2] = parser.getInteger();
				// unknown field
				parser.getInteger();
				// normal must be calculated
				float[] p0 = getVertexCoords(vertIndexes[0]);
				float[] p1 = getVertexCoords(vertIndexes[1]);
				float[] p2 = getVertexCoords(vertIndexes[2]);
				float[] v1 = VectorUtils.subtract(p1,p0);
				float[] v2 = VectorUtils.subtract(p2,p0);
				float[] normal = VectorUtils.normalize(VectorUtils.cross(v1,v2));
				faces[i] = new Face(surfaceID,normal,0,vertIndexes,null);
			}
		} else {
			return super.parseField(fieldName, parser, trace);
		}
		return true;
	}

	public float[] getVertexCoords(int vertexIndex) {
		// TODO Auto-generated method stub
		vertexIndex = vertexIndex*3; // convert to base verts index
		return new float[]{verts[vertexIndex],verts[vertexIndex+1],
				verts[vertexIndex+2]};
	}

	protected Controller makeController(int controllerID, float[] timekeys,
            float[][] data, boolean trace){
        switch (controllerID) {
        case 8:
            return new Controller(Controller.Type.Position,timekeys,data,trace);
        case 20:
            return new Controller(Controller.Type.Orientation,timekeys,data,trace);
        case 36:
            return new Controller(Controller.Type.Scale,timekeys,data,trace);
        case 100:
            return new Controller(Controller.Type.SelfIllumColor,timekeys,
                    data,trace);
        case 128:
            return new Controller(Controller.Type.Alpha,timekeys,data,trace);
        }
        return null;
    }
    
    /**
     * @return
     */
    public int getTriangleMode() {
        return triangleMode;
    }
    
    /**
     * @return
     */
    public float[] getTextCoords() {
        return textCoords;
    }
    
    /**
     * @return
     */
    public float[] getVerts() {
        // TODO Auto-generated method stub
        return verts;
    }
    
    /**
     * @return
     */
    public float[] getVertexNormals() {
        // TODO Auto-generated method stub
        return vertexNormals;
    }
    
    public int[] getVertexColors(){
        return vertexColors;
    }
    
    /**
     * @return
     */
    public Face[] getFaces() {
        return faces;
    }
    
    public static class Face {
        float[] normal = new float[3];
        
        float planeDistance;
        
        int surfaceID;
        
        short[] adjacentFaces = new short[3];
        
        int[] vertexIndices = new int[3];
        
        public Face(int surfaceID, float[] normal, float planeDistance,
                int[] vertexIndices, short[] adjacentFaces) {
            this.surfaceID = surfaceID;
            System.arraycopy(normal, 0, this.normal, 0, 3);
            this.planeDistance = planeDistance;
            if (adjacentFaces != null){
            	System.arraycopy(adjacentFaces, 0, this.adjacentFaces, 0, 3);
            }
            System.arraycopy(vertexIndices, 0, this.vertexIndices, 0, 3);
        }
        
        public void setAdjacentFaces(short[] neighbors) {
			adjacentFaces = new short[neighbors.length];
			System.arraycopy(neighbors, 0, adjacentFaces, 0, neighbors.length);
		}

		/**
         * @return
         */
        public int[] getVertexIndices() {
            return vertexIndices;
        }
        
        /**
         * @return
         */
        public float[] getNormal() {
            return normal;
        }
        
        /**
         * @return
         */
        public short[] getAdjacentFaces() {
            // TODO Auto-generated method stub
            return adjacentFaces;
        }
        
        /**
         * @return
         */
        public int getSurfaceID() {
            return surfaceID;
        }
    }
    
    /**
     * @param i
     * @return
     */
    public String getTextureName(int i) {
        if ((i>texturenames.length-1)||(texturenames[1]==null)){
            return null;
        } else {
            return texturenames[i];
        }
    }
    
    /**
     * @return
     */
    public int getTransparencyHint() {
        return transparencyHint;
    }
    private float[] vertexNormals;
    private int[] vertexColors;
    
}
