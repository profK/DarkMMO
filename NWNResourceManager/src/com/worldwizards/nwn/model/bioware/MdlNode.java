package com.worldwizards.nwn.model.bioware;

import java.io.*;
import java.nio.*;
import java.util.*;
import java.util.logging.Logger;

import com.sun.org.apache.bcel.internal.generic.NEW;
import com.worldwizards.util.parse.TextParser;
import com.worldwizards.util.parse.TokenNotFoundException;

/**
 * This class is the super-class of all NWN MDL tree nodes. It is an abstract
 * class and may not be instantiated.
 * 
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
public abstract class MdlNode {
	private static final Logger logger = Logger.getLogger(MdlNode.class
			.getName());
	private static final int CONTROLLER_POSITION = 8;

	private static final int CONTROLLER_ORIENTATION = 20;

	private int inheritColotFLag;

	private int nodeNumber;

	public String name;

	private int nodeFlags;

	private List childOffsets = new ArrayList();

	private List<MdlNode> childNodes = new ArrayList<MdlNode>();

	private List<Controller> controllers = new ArrayList<Controller>();

	public MdlNode(int location, ByteBuffer modelDataBuff,
			ByteBuffer rawDataBuff, boolean trace) {
		modelDataBuff.position(location + 24); // skipp starting C func pointers
		inheritColotFLag = modelDataBuff.getInt();
		nodeNumber = modelDataBuff.getInt();
		byte[] nodeNameBytes = new byte[32];
		modelDataBuff.get(nodeNameBytes);
		name = new String(nodeNameBytes);
		int nullloc = name.indexOf(0);
		if (nullloc > 0) {
			name = name.substring(0, nullloc);
		}
		// skip geometryand parent node pointers, always null in file
		modelDataBuff.position(modelDataBuff.position() + 8);
		int childArrayOffset = modelDataBuff.getInt();
		int childArrayCount = modelDataBuff.getInt();
		int childArraySize = modelDataBuff.getInt();
		int controllerKeyOffset = modelDataBuff.getInt();
		int controllerKeyCount = modelDataBuff.getInt();
		int controllerKeySize = modelDataBuff.getInt();
		int controllerValueOffset = modelDataBuff.getInt();
		int controllerValueCount = modelDataBuff.getInt();
		int controllerValueSize = modelDataBuff.getInt();
		nodeFlags = modelDataBuff.getInt();
		// load child offsets
		for (int i = 0; i < childArrayCount; i++) {
			modelDataBuff.position(childArrayOffset + (i * 4));
			int childOffset = modelDataBuff.getInt();
			childOffsets.add(new Integer(childOffset));
		}
		for (int i = 0; i < controllerKeyCount; i++) {
			modelDataBuff.position(controllerKeyOffset + (i * 0x000C));
			int controllerType = modelDataBuff.getInt();
			short numberOfRows = modelDataBuff.getShort();
			short firstTimeIndex = modelDataBuff.getShort();
			short firstDataValue = modelDataBuff.getShort();
			byte numberOfDataColumns = modelDataBuff.get();
			Controller controller = null;
			float[][] data = new float[numberOfRows][numberOfDataColumns];
			float[] timekeys = new float[numberOfRows];
			modelDataBuff
					.position((firstDataValue * 4) + controllerValueOffset);
			for (int iy = 0; iy < numberOfRows; iy++) {
				for (int ix = 0; ix < numberOfDataColumns; ix++) {
					data[iy][ix] = modelDataBuff.getFloat();
				}
			}
			modelDataBuff
					.position((firstTimeIndex * 4) + controllerValueOffset);
			for (int it = 0; it < numberOfRows; it++) {
				timekeys[it] = modelDataBuff.getFloat();
			}
			Controller ctl = makeController(controllerType, timekeys, data,
					trace);
			if (ctl != null) {
				controllers.add(ctl);
			}
		}
	}

	private boolean nodeDone;

	public MdlNode(TextParser parser, boolean trace) throws IOException,
			TokenNotFoundException {
		nodeDone = false;
		name = parser.getString();
		while (!nodeDone) {
			String fieldName = parser.getString();
			if (trace){
				System.out.println("Parsing field: "+fieldName);
			}
			if (!parseField(fieldName, parser, trace)) {
				logger.warning("unexpected token for field name: " + fieldName);
			}
		}
	}

	public boolean parseField(String fieldName, TextParser parser, boolean trace) throws IOException {
		if (fieldName.equalsIgnoreCase("endnode")) {
			nodeDone = true;
		} else if (fieldName.equalsIgnoreCase("parent")) {
			String parent = parser.getString();
			logger.warning("parent field in ascii nodes not yet implemented");
		} else if (fieldName.equalsIgnoreCase("position")) {
			float[][] pos = new float[1][3];
			pos[0][0] = parser.getFloat();
			pos[0][1] = parser.getFloat();
			pos[0][2] = parser.getFloat();
			controllers.add(new Controller(Controller.Type.Position,
					new float[] { 0 }, pos, trace));
		} else if (fieldName.equalsIgnoreCase("orientation")) {
			float[][] quat = new float[1][4];
			quat[0][0] = parser.getFloat();
			quat[0][1] = parser.getFloat();
			quat[0][2] = parser.getFloat();
			quat[0][3] = parser.getFloat();
			controllers.add(new Controller(Controller.Type.Orientation,
					new float[] { 0 }, quat, trace));
		} else if (fieldName.equalsIgnoreCase("wirecolor")) {
			float[][] wcolor = new float[1][3];
			wcolor[0][0] = parser.getFloat();
			wcolor[0][1] = parser.getFloat();
			wcolor[0][2] = parser.getFloat();
			controllers.add(new Controller(Controller.Type.Wirecolor,
					new float[] { 0 }, wcolor, trace));
		} else {
			return false;
		}
		return true;
	}

	protected Controller makeController(int controllerID, float[] timekeys,
			float[][] data, boolean trace) {
		switch (controllerID) {
		case 8:
			return new Controller(Controller.Type.Position, timekeys, data,
					trace);
		case 20:
			return new Controller(Controller.Type.Orientation, timekeys, data,
					trace);
		case 36:
			return new Controller(Controller.Type.Scale, timekeys, data, trace);
		default:
			//logger.warning("Controller ID " + controllerID
			//		+ " unimplemented for this node type: "
			//		+ getClass().getName());
			return null;

		}
	}

	/**
	 * getChildOffsets
	 * 
	 * @return List
	 */
	public List getChildOffsets() {
		return childOffsets;
	}

	/**
	 * addChild
	 * 
	 * @param child
	 *            MdlNode
	 */
	public void addChild(MdlNode child) {
		childNodes.add(child);
	}

	/**
	 * addToJ3DGraph
	 * 
	 * @param scene
	 * 
	 * @param branchGroup
	 *            BranchGroup
	 * 
	 * 
	 *            public final void addToJ3DGraph(SceneBase scene, String
	 *            parentName, TextureFactory textureFactory, Group parent) {
	 *            String nodeName = parentName; if (!nodeName.equals("")) {
	 *            nodeName += "."; } nodeName += name; TransformGroup
	 *            positionNode = new TransformGroup(); TransformGroup
	 *            orientationNode = new TransformGroup();
	 *            scene.addNamedObject(nodeName, positionNode);
	 *            parent.addChild(positionNode);
	 *            positionNode.addChild(orientationNode);
	 *            addNodeToJ3DGraph(textureFactory, parent, positionNode,
	 *            orientationNode); for (Iterator i = controllers.iterator();
	 *            i.hasNext();) { ((Controller) i.next()).modifyNode(parent,
	 *            positionNode, orientationNode); } for (Iterator i =
	 *            childNodes.iterator(); i.hasNext();) { ((MdlNode)
	 *            i.next()).addToJ3DGraph(scene, nodeName, textureFactory,
	 *            orientationNode); } }
	 */

	/**
	 * dump
	 * 
	 * @param w
	 *            Writer
	 * @throws java.io.IOException
	 * @param prefix
	 *            String
	 */
	public void dump(Writer w, String prefix) throws IOException {
		w.write(prefix + "Node " + nodeNumber + ":\n");
		prefix += "    ";
		w.write(prefix + "Name: " + name + "\n");
		w.write(prefix + "Node Flags = " + Integer.toHexString(nodeFlags)
				+ "\n");
		// call to dump node specific stuff goes here
		w.write(prefix + "Controllers :\n");
		for (Iterator i = controllers.iterator(); i.hasNext();) {
			((Controller) i.next()).dump(w, prefix + "    ");
		}
		for (Iterator i = childNodes.iterator(); i.hasNext();) {
			((MdlNode) i.next()).dump(w, prefix);
		}
	}

	/**
	 * @return
	 */
	public List<Controller> listControllers() {
		return controllers;
	}

	public List<MdlNode> listChildren() {
		return childNodes;
	}
}
