package net.worldwizards.darkmmo.tools

import java.io.Writer
import java.util.Date
import com.worldwizards.nwn.model.bioware.MeshNode.Face
import com.worldwizards.nwn.model.bioware._
import scala.collection.mutable.Set


/**
 * Created by IntelliJ IDEA.
 * User: jeff
 * Date: Dec 23, 2009
 * Time: 5:12:16 PM
 * To change this template use File | Settings | File Templates.
 */

object MDL2Collada {
  implicit def iteratorToWrapper[T](iter: java.util.Iterator[T]): IteratorWrapper[T] = new IteratorWrapper[T](iter)


  def convert(resRef: String, mdl: MDL, w: Writer): Set[String] = {
    val cw = new ColladaWriter(w)
    cw.openCollada("Neverinter Nights", "NWNConvert", "resref://mdl/" + resRef, new Date())
    cw.writeComment("Library Section")
    val imagesSet: Set[String] = doImagesLibrary(mdl, cw)
    doEffectsLibrary(cw, imagesSet)
    doMaterialsLibrary(cw, imagesSet)
    doLightsLibrary(mdl,cw)
    doGeometryLibrary(mdl, cw)
    doSceneLibrary(mdl, cw)
    cw.writeComment("Scene Section")
    cw.openScene
    cw.writeSceneInstance("#model_scene")
    cw.closeScene
    cw.closeCollada()
    return imagesSet
  }

  def doGeometryLibrary(mdl: MDL, cw: ColladaWriter): Unit = {
    val nodeset: Set[String] = Set()
    def processNodeGeometry(node: MdlNode, cw: ColladaWriter): Unit = {
      def getVertexIndicies(tnode: TrimeshNode): Array[Int] = {
        val faces: Array[Face] = tnode.getFaces()
        val indices: Array[Int] = new Array(faces.length * 3);
        var faceCount = 0;
        faces.foreach((face: Face) => {
          val faceIndexes = face.getVertexIndices();
          System.arraycopy(faceIndexes, 0, indices, faceCount * 3, 3);
          faceCount += 1;
        })
        return indices
      }

      def writeXYZSource(cw: ColladaWriter, nodename: String, sourceName: String, points: Array[Float]): Unit = {
        cw.openSource(nodename, sourceName)
        cw.writeFloatArrayNode(nodename, sourceName, points)
        cw.openCommonTechnique
        cw.openAccessor(nodename, sourceName, points.length, 3)
        cw.writeParameter("X", "float")
        cw.writeParameter("Y", "float")
        cw.writeParameter("Z", "float")
        cw.closeAccessor()
        cw.closeCommonTechnique()
        cw.closeSource()
      }

      def writeSTSource(cw: ColladaWriter, nodename: String, sourceName: String, points: Array[Float]): Unit = {
        cw.openSource(nodename, sourceName)
        cw.writeFloatArrayNode(nodename, sourceName, points)
        cw.openCommonTechnique
        cw.openAccessor(nodename, sourceName, points.length, 2)
        cw.writeParameter("S", "float")
        cw.writeParameter("T", "float")
        cw.closeAccessor()
        cw.closeCommonTechnique()
        cw.closeSource()
      }

      node match {
        case tnode: TrimeshNode =>
          if (tnode.getTextureName(0) != null) { //skip nodes with no texture
            val nodename = tnode.name
            cw.openGeometry(nodename)
            cw.openMesh()
            writeXYZSource(cw, nodename, "position", tnode.getVerts)
            if (tnode.getVertexNormals != null) {
              writeXYZSource(cw, nodename, "normal", tnode.getVertexNormals)
            }
            if (tnode.getTextCoords != null) {
              writeSTSource(cw, nodename, "texcoord", tnode.getTextCoords)
            }
            cw.openVertices(tnode.name)
            cw.writeInput("POSITION", "#" + tnode.name + "-lib-position")
            cw.closeVertices()
            val texname: String = tnode.getTextureName(0)
            cw.openTriangles(tnode.getFaces().length, texname + "SG")
            cw.writeInput(0, "VERTEX", "#" + tnode.name + "-lib-vertices")
            if (tnode.getVertexNormals != null) {
              cw.writeInput(0, "NORMAL", "#" + tnode.name + "-lib-normal")
            }
            if (tnode.getTextCoords != null) {
              cw.writeInput(0, "TEXCOORD", "#" + tnode.name + "-lib-texcoord")
            }
            val vi: Array[Int] = getVertexIndicies(tnode)
            cw.writeComment("point index count = " + vi.length)
            cw.writePointIndexes(vi)
            cw.closeTriangles
            cw.closeMesh()
            cw.closeGeometry()
          }
        case _ => // node not interesting
      }
      for (child: MdlNode <- node.listChildren.iterator) {
        if (!nodeset.contains(child.name)) {
          nodeset += child.name
          processNodeGeometry(child, cw)
        }
      }
    }
    cw.openGeometryLibrary()
    processNodeGeometry(mdl.getRootModelNode, cw)
    cw.closeGeometryLibrary()
  }

  def doImagesLibrary(mdl: MDL, cw: ColladaWriter): Set[String] = {
    def collectAllTextures(node: MdlNode, set: Set[String]): Unit = {
      for (child: MdlNode <- node.listChildren.iterator) {
        collectAllTextures(child, set)
      }
      node match {
        case mnode: MeshNode =>
          val txname = mnode.getTextureName(0) // currently assuems a single texture per node
          if ((txname != null) && (!txname.equals("null")) &&
                  (mnode.getTextCoords != null) && (mnode.getTextCoords.length > 0)) {
            set += txname
          }
        case _ => // node not interesting
      }
    }
    cw.openImagesLibrary
    val textures: Set[String] = Set()
    collectAllTextures(mdl.getRootModelNode, textures)
    textures.foreach(texfile => {
      cw.writeImage(texfile + "-image", texfile, "../png/" + texfile + ".png")
    })
    cw.closeImagesLibrary
    return textures
  }

  def doLightsLibrary(mdl:MDL, cw:ColladaWriter){
    val nodeset: Set[String] = Set()
    def doLightNode(node:MdlNode,cw:ColladaWriter){
      node match {
        case lightNode:LightNode =>
          cw.openLight(lightNode.name)
          cw.openCommonTechnique
          cw.openPointLight
          cw.writeColor(1.0f,1.0f,1.0f)
          cw.closePointLight
          cw.closeCommonTechnique
          cw.closeLight
        case _  => //uninteresrting
      }
      for (child: MdlNode <- node.listChildren.iterator) {
        if (!nodeset.contains(child.name)) {
          nodeset += child.name
          doLightNode(child, cw)
        }
      }
    }
    cw.openLightsLibrary
    doLightNode(mdl.getRootModelNode,cw)
    cw.closeLightsLibrary
  }

  def doMaterialsLibrary(cw: ColladaWriter, texnames: Set[String]): Unit = {
    cw.openMaterialsLibrary
    texnames.foreach(texname => {
      cw.openMaterial(texname + "-textured-material", texname + "-textured-material")
      cw.writeEffectInstance(texname + "-textured")
      cw.closeMaterial
    })
    cw.closeMaterialsLibrary
  }

  /**
   *
   */
  def doEffectsLibrary(cw: ColladaWriter, textures: Set[String]): Unit = {
    cw.openEffectsLibrary
    //setup textures
    textures.foreach(texname => {
      cw.openEffect(texname + "-textured")
      cw.openCommonProfile
      cw.openNewParam(texname + "-surface")
      cw.openSurface("2D")
      cw.writeInitFrom(texname + "-image")
      cw.writeFormat("A8R8G8B8")
      cw.closeSurface
      cw.closeNewParam
      cw.openNewParam(texname + "-sampler")
      cw.openSampler2D
      cw.writeSource(texname + "-surface")
      cw.writeMinFilter("LINEAR_MIPMAP_LINEAR")
      cw.writeMagFilter("LINEAR")
      cw.closeSampler2D
      cw.closeNewParam
      cw.openTechnique("common")
      cw.openPhong
      cw.openEmission
      cw.writeColor(0, 0, 0, 1)
      cw.closeEmission
      cw.openAmbient
      cw.writeColor(0, 0, 0, 1)
      cw.closeAmbient
      cw.openDiffuse
      cw.writeTexture(texname)
      cw.closeDiffuse
      cw.openSpecular
      cw.writeColor(0.5f, 0.5f, 0.5f, 1)
      cw.closeSpecular
      cw.openShininess
      cw.writeFloat(16)
      cw.closeShininess
      cw.openReflective
      cw.writeColor(0, 0, 0, 1)
      cw.closeReflective
      cw.openReflectivity
      cw.writeFloat(0.5f)
      cw.closeReflectivity
      cw.openTransparent
      cw.writeColor(0, 0, 0, 1)
      cw.closeTransparent
      cw.openTransparency
      cw.writeFloat(1)
      cw.closeTransparency
      cw.openRefraction
      cw.writeFloat(0)
      cw.closeRefraction
      cw.closePhong
      cw.closeTechnique
      cw.closeCommonProfile
      cw.closeEffect
    })
    cw.closeEffectsLibrary
  }

  def convertQuatToAxisAngle(x: Float, y: Float, z: Float, w: Float): Array[Float] = {
    val result = new Array[Float](4)
    val scale = scala.Math.sqrt(x * x + y * y + z * z).asInstanceOf[Float]
    if (scale == 0) {
      return Array(0f, 0f, 0f, 0f)
    }
    result(0) = x / scale
    result(1) = y / scale
    result(2) = z / scale
    result(3) = scala.Math.acos(w).asInstanceOf[Float] * 2.0f;
    // convert NaNs to 0
    for (i <- 0 to result.length - 1) {
      if (result(i) == scala.Math.NaN_FLOAT) result(i) = 0
    }
    return result
  }

  def radiansToDegrees(rad: Float): Float = {
    rad * 180 / 3.14159f
  }

  def doSceneLibrary(mdl: MDL, cw: ColladaWriter): Unit = {
    val nodeset: Set[String] = Set()
    def writeModelNode(node: MdlNode, cw: ColladaWriter): Unit = {
      cw.openSceneNode(node.name, node.name)
      // write controllers
      for (ctl: Controller <- node.listControllers.iterator) {
        ctl.getControllerType match {
          case Controller.Type.Position =>
            val data = ctl.getData
            if (data.length > 1) {
              data.foreach(tlation =>
                cw.writeTranslate("translate-" + tlation(0), tlation(1), tlation(2), tlation(3))
                )
            } else {
              val tlation = data(0)
              cw.writeTranslate("translate-0", tlation(0), tlation(1), tlation(2))
            }
          case Controller.Type.Orientation =>
            val data = ctl.getData
            if (data.length > 1) {
              data.foreach(rot => {
                val axisAngle = convertQuatToAxisAngle(rot(1), rot(2), rot(3), rot(4))
                cw.writeRotate("rotate-" + rot(0), axisAngle(0), axisAngle(1), axisAngle(2),
                  radiansToDegrees(axisAngle(3)))
              })
            } else {
              val rot = data(0)
              val axisAngle = convertQuatToAxisAngle(rot(0), rot(1), rot(2), rot(3))
              cw.writeRotate("rotate-0", axisAngle(0), axisAngle(1), axisAngle(2),
                radiansToDegrees(axisAngle(3)))
            }
          case _ =>
            //System.err.println("Unknown controller: " + ctl)
        }
      }
      node match {
        case meshNode: MeshNode =>
          val texname = node.asInstanceOf[MeshNode].getTextureName(0)
          if ((!texname.equals("null")) && (meshNode.getTextCoords != null)) { // skip nodes withtu textures}
            // write instance geometry
            cw.openInstanceGeometry("#" + node.name + "-lib")
            cw.openBindMaterial
            cw.openCommonTechnique
            cw.writeMaterialInstance(texname + "SG", "#" + texname + "-textured-material")
            cw.closeCommonTechnique
            cw.closeBindMaterial
            cw.closeInstanceGeometry
          }
        case lightNode:LightNode =>
           cw.writeLightInstance(lightNode.name)
        case _ => // uninteresting
      }
      for (child: MdlNode <- node.listChildren.iterator) {
        if (!nodeset.contains(child.name)) {
          nodeset += child.name
          writeModelNode(child, cw)
        }
      }
      cw.closeSceneNode
    }

    cw.openSceneLibrary
    cw.openLibraryScene("model_scene", "model_scene")
    cw.openSceneNode("model_root", "model_root")
    writeModelNode(mdl.getRootModelNode, cw)
    cw.closeSceneNode
    cw.closeLibraryScene()
    cw.closeSceneLibrary
  }
}