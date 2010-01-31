package net.worldwizards.darkmmo.tools

import java.util.Date
//import javax.mail.internet.MailDateFormat
import java.io.Writer
import java.text.SimpleDateFormat

/**
 * Created by IntelliJ IDEA.
 * User: jeff
 * Date: Dec 25, 2009
 * Time: 9:42:18 AM
 * To change this template use File | Settings | File Templates.
 */


class ColladaWriter(w: Writer) {
  var indent = "";

  implicit def iteratorToWrapper[T](iter: java.util.Iterator[T]): IteratorWrapper[T] = new IteratorWrapper[T](iter)
  val m_ISO8601Local = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

  def dateAsISO8601(date: Date): String = {

    if (date == null) {
      return dateAsISO8601(new Date())
    }

    // format in (almost) ISO8601 format
    var result: String = m_ISO8601Local.format(date)
    // remap the timezone from 0000 to 00:00
    return result.substring(0, result.length()-2) + ":" + result.substring(result.length()-2)
  }

  def openCollada(author: String, tool: String, source: String, date: Date): Unit = {
    w.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n")
    w.write("<COLLADA xmlns=\"http://www.collada.org/2005/11/COLLADASchema\"\n")
    w.write(" version=\"1.4.1\">\n")
    w.write("   <!-- Header Section -->\n")
    w.write("    <asset>\n")
    w.write("      <contributor>\n")
    w.write("         <author>" + author + "</author>\n")
    w.write("         <authoring_tool>" + tool + "</authoring_tool>\n")
    w.write("         <source_data>" + source + "</source_data>\n")
    w.write("      </contributor>\n")
    val d = dateAsISO8601(date)
    w.write("      <created>" + d + "</created>\n")
    w.write("      <modified>" + d + "</modified>\n")
    w.write("      <unit name=\"unit\" meter=\"3.048\" />\n")
    w.write("      <up_axis>Z_UP</up_axis>\n")
    w.write("   </asset>\n")
    indent += "   "
  }

  def closeCollada(): Unit = {
    indent = indent.substring(3)
    w.write("</COLLADA>\n")
  }

  def openGeometry(nodename: String): Unit = {
    w.write(indent + "<geometry id=\"" + nodename + "-lib\" name=\"" + nodename + "\">\n")
    indent += "   "
  }

  def closeGeometry(): Unit = {
    indent = indent.substring(3)
    w.write(indent + "</geometry>\n")
  }

  def openSource(nodename: String, sourceName: String): Unit = {
    w.write(indent + "<source id=\"" + nodename + "-lib-" + sourceName + "\" name=\"" + sourceName + "\" >\n")
    indent += "   "
  }

  def closeSource(): Unit = {
    indent = indent.substring(3)
    w.write(indent + "</source>\n")
  }

  def writeFloatArrayNode(nodename: String, arrayName: String, verts: Array[Float]): Unit = {
    openFloatArray(nodename, arrayName, verts)
    writeFloats(verts)
    closeFloatArray()
  }

  def openFloatArray(nodename: String, arrayName: String, verts: Array[Float]): Unit = {

    w.write(indent + "<float_array id=\"" + nodename + "-lib-" + arrayName + "-array\" count=\"" +
            verts.length + "\" >\n")
    indent += "   "
  }

  def writeFloats(verts: Array[Float]): Unit = {
    if (verts.length > 0) {
      w.write(indent)
      for (i <- 0 to verts.length - 1) {
        w.write(verts(i) + " ");
        if ((i + 1) % 10 == 0) {
          w.write("\n" + indent)
        }
      }
    }
    if (verts.length - 1 % 10 != 0) {
      w.write("\n")
    }
  }

  def writeInts(verts: Array[Int], span: Int): Unit = {
    if (verts.length > 0) {
      w.write(indent)
      for (i <- 0 to verts.length - 1) {
        w.write(verts(i) + " ");
        if ((i + 1) % span == 0) {
          w.write("\n" + indent)
        }
      }
    }
    if (verts.length - 1 % 10 != 0) {
      w.write("\n")
    }
  }

  def closeFloatArray(): Unit = {
    indent = indent.substring(3)
    w.write(indent + "</float_array>\n")
  }

  def openCommonTechnique(): Unit = {
    w.write(indent + "<technique_common>\n")
    indent = indent + "   "
  }

  def closeCommonTechnique(): Unit = {
    indent = indent.substring(3)
    w.write(indent + "</technique_common>\n")
  }

  def openAccessor(nodename: String, target: String, dataLength: Int, stride: Int): Unit = {
    w.write(indent + "<accessor count=\"" + dataLength / stride + "\" offset=\"0\" source=\"" +
            "#" + nodename + "-lib-" + target + "-array\" stride=\"" + stride + "\">\n")
    indent = indent + "   "
  }

  def closeAccessor(): Unit = {
    indent = indent.substring(3)
    w.write(indent + "</accessor>\n")
  }

  def writeParameter(label: String, ptype: String): Unit = {
    w.write(indent + "<param name=\"" + label + "\" type=\"" + ptype + "\"></param>\n")
  }

  def openMesh(): Unit = {
    w.write(indent + "<mesh>\n")
    indent += "   "
  }

  def closeMesh(): Unit = {
    indent = indent.substring(3)
    w.write(indent + "</mesh>\n")
  }

  def writeComment(c: String): Unit = {
    w.write(indent + "<!-- " + c + " -->\n")
  }

  def openGeometryLibrary(): Unit = {
    w.write(indent + "<library_geometries>\n")
    indent += "   "
  }

  def closeGeometryLibrary(): Unit = {
    indent = indent.substring(3)
    w.write(indent + "</library_geometries>\n")
  }

  def openVertices(nodeName: String): Unit = {
    w.write(indent + "<vertices id=\"" + nodeName + "-lib-vertices\">\n")
    indent += "   "
  }

  def closeVertices(): Unit = {
    indent = indent.substring(3)
    w.write(indent + "</vertices>\n")
  }

  def writeInput(offset: Int, semantic: String, source: String): Unit = {
    w.write(indent + "<input offset=\"" + offset + "\" semantic=\"" + semantic + "\" source=\"" + source + "\"/>\n")
  }

  def writeInput(semantic: String, source: String): Unit = {
    w.write(indent + "<input semantic=\"" + semantic + "\" source=\"" + source + "\"/>\n")
  }

  def openEffectsLibrary(): Unit = {
    w.write(indent + "<library_effects>\n")
    indent += "   "
  }

  def closeEffectsLibrary(): Unit = {
    indent = indent.substring(3)
    w.write(indent + "</library_effects>\n")
  }

  def openEffect(effectName: String): Unit = {
    w.write(indent + "<effect id=\"" + effectName + "\">\n")
    indent += "   "
  }

  def closeEffect(): Unit = {
    indent = indent.substring(3)
    w.write(indent + "</effect>\n")
  }

  def openCommonProfile(): Unit = {
    w.write(indent + "<profile_COMMON>\n")
    indent += "   "
  }

  def closeCommonProfile(): Unit = {
    indent = indent.substring(3)
    w.write(indent + "</profile_COMMON>\n")
  }

  def openTechnique(sid: String): Unit = {
    w.write(indent + "<technique sid=\"" + sid + "\">\n")
    indent += "   "
  }

  def closeTechnique(): Unit = {
    indent = indent.substring(3)
    w.write(indent + "</technique>\n")
  }

  def openPhong(): Unit = {
    w.write(indent + "<phong>\n")
    indent += "   "
  }

  def closePhong(): Unit = {
    indent = indent.substring(3)
    w.write(indent + "</phong>\n")
  }

  def openEmission(): Unit = {
    w.write(indent + "<emission>\n")
    indent += "   "
  }

  def closeEmission(): Unit = {
    indent = indent.substring(3)
    w.write(indent + "</emission>\n")
  }

  def openAmbient(): Unit = {
    w.write(indent + "<ambient>\n")
    indent += "   "
  }

  def closeAmbient(): Unit = {
    indent = indent.substring(3)
    w.write(indent + "</ambient>\n")
  }

  def openDiffuse(): Unit = {
    w.write(indent + "<diffuse>\n")
    indent += "   "
  }

  def closeDiffuse(): Unit = {
    indent = indent.substring(3)
    w.write(indent + "</diffuse>\n")
  }

  def openSpecular(): Unit = {
    w.write(indent + "<specular>\n")
    indent += "   "
  }

  def closeSpecular(): Unit = {
    indent = indent.substring(3)
    w.write(indent + "</specular>\n")
  }

  def openShininess(): Unit = {
    w.write(indent + "<shininess>\n")
    indent += "   "
  }

  def closeShininess(): Unit = {
    indent = indent.substring(3)
    w.write(indent + "</shininess>\n")
  }

  def openReflective(): Unit = {
    w.write(indent + "<reflective>\n")
    indent += "   "
  }

  def closeReflective(): Unit = {
    indent = indent.substring(3)
    w.write(indent + "</reflective>\n")
  }

  def openReflectivity(): Unit = {
    w.write(indent + "<reflectivity>\n")
    indent += "   "
  }

  def closeReflectivity(): Unit = {
    indent = indent.substring(3)
    w.write(indent + "</reflectivity>\n")
  }

  def openTransparent(): Unit = {
    w.write(indent + "<transparent>\n")
    indent += "   "
  }

  def closeTransparent(): Unit = {
    indent = indent.substring(3)
    w.write(indent + "</transparent>\n")
  }

  def openTransparency(): Unit = {
    w.write(indent + "<transparency>\n")
    indent += "   "
  }

  def closeTransparency(): Unit = {
    indent = indent.substring(3)
    w.write(indent + "</transparency>\n")
  }

  def openRefraction(): Unit = {
    w.write(indent + "<index_of_refraction>\n")
    indent += "   "
  }

  def closeRefraction(): Unit = {
    indent = indent.substring(3)
    w.write(indent + "</index_of_refraction>\n")
  }

  def writeColor(r: Float, g: Float, b: Float, a: Float): Unit = {
    w.write(indent + " <color>" + r + " " + g + " " + b + " " + a + "</color>\n")
  }

  def writeFloat(f: Float): Unit = {
    w.write(indent + "<float>" + f + "</float>\n")
  }

  def openMaterialsLibrary(): Unit = {
    w.write(indent + "<library_materials>\n")
    indent += "   "
  }

  def closeMaterialsLibrary(): Unit = {
    indent = indent.substring(3)
    w.write(indent + "</library_materials>\n")
  }

  def openMaterial(id: String, name: String): Unit = {
    w.write(indent + "<material id=\"" + id + "\" name=\"" + name + "\">\n")
    indent += "   "
  }

  def closeMaterial(): Unit = {
    indent = indent.substring(3)
    w.write(indent + "</material>\n")
  }

  def writeEffectInstance(effectUrl: String): Unit = {
    w.write(indent + "<instance_effect url=\"#" + effectUrl + "\"/>\n")
  }

  def openTriangles(count: Int, material: String): Unit = {
    w.write(indent + "<triangles count=\"" + count + "\" material=\"" + material + "\">\n")
    indent += "   "
  }

  def closeTriangles() {
    indent = indent.substring(3)
    w.write(indent + "</triangles>\n")
  }

  def writePointIndexes(indexes: Array[Int]): Unit = {
    w.write(indent + "<p>\n")
    writeInts(indexes, 30)
    w.write(indent + "</p>\n")
  }

  def openSceneLibrary(): Unit = {
    w.write(indent + "<library_visual_scenes>\n")
    indent += "   "
  }

  def closeSceneLibrary(): Unit = {
    indent = indent.substring(3)
    w.write(indent + "</library_visual_scenes>\n")
  }

  def openLibraryScene(id: String, name: String): Unit = {
    w.write(indent + "<visual_scene id=\"" + id + "\" name=\"" + name + "\">\n")
    indent += "   "
  }

  def closeLibraryScene(): Unit = {
    indent = indent.substring(3)
    w.write(indent + "</visual_scene>\n")
  }

  def openSceneNode(id: String, name: String): Unit = {
    w.write(indent + "<node id=\"" + id + "\" name=\"" + name + "\">\n")
    indent += "   "
  }

  def closeSceneNode(): Unit = {
    indent = indent.substring(3)
    w.write(indent + "</node>\n")
  }

  def writeTranslate(sid: String, x: Float, y: Float, z: Float): Unit = {
    w.write(indent + "<translate sid=\"" + sid + "\">" + x + " " + y + " " + z + "</translate>\n")
  }

  def writeRotate(sid: String, x: Float, y: Float, z: Float, rot: Float): Unit = {
    w.write(indent + "<rotate sid=\"" + sid + "\">" + x + " " + y + " " + z + " " + rot + "</rotate>\n")
  }

  def openScene(): Unit = {
    w.write(indent + "<scene>\n")
    indent += "   "
  }

  def closeScene(): Unit = {
    indent = indent.substring(3)
    w.write(indent + "</scene>\n")
  }

  def writeSceneInstance(url: String): Unit = {
    w.write(indent + "<instance_visual_scene url=\"" + url + "\"/>\n")
  }

  def openBindMaterial(): Unit = {
    w.write(indent + "<bind_material>\n")
    indent += "   "
  }

  def closeBindMaterial(): Unit = {
    indent = indent.substring(3)
    w.write(indent + "</bind_material>\n")

  }

  def writeMaterialInstance(symbol: String, target: String): Unit = {
    w.write(indent + "<instance_material symbol=\"" + symbol + "\" target=\"" + target + "\"/>\n")
  }

  def openInstanceGeometry(url: String): Unit = {
    w.write(indent + "<instance_geometry url=\"" + url + "\">\n")
    indent += "   "
  }

  def closeInstanceGeometry(): Unit = {
    indent = indent.substring(3)
    w.write(indent + "</instance_geometry>\n")
  }

  def openImagesLibrary(): Unit = {
    w.write(indent + "<library_images>\n")
    indent += "   "
  }

  def closeImagesLibrary(): Unit = {
    indent = indent.substring(3)
    w.write(indent + "</library_images>\n")
  }

  def writeImage(id: String, name: String, filename: String): Unit = {
    w.write(indent + "<image id=\"" + id + "\" name=\"" + name + "\">\n")
    w.write(indent + "   <init_from>" + filename + "</init_from>\n")
    w.write(indent + "</image>\n")
  }

  def openNewParam(sid: String): Unit = {
    w.write(indent + "<newparam sid=\"" + sid + "\">\n")
    indent += "   "
  }

  def closeNewParam = {
    indent = indent.substring(3)
    w.write(indent + "</newparam>\n")
  }

  def openSurface(surfaceType: String) = {
    w.write(indent + "<surface type=\"" + surfaceType + "\">\n")
    indent += "   "
  }

  def closeSurface = {
    indent = indent.substring(3)
    w.write(indent + "</surface>\n")
  }

  def writeInitFrom(sourceName: String) = {
    w.write(indent + "<init_from>" + sourceName + "</init_from>\n")
  }

  def writeFormat(format: String) = {
    w.write(indent + "<format>" + format + "</format>")
  }

  def openSampler2D = {
    w.write(indent + "<sampler2D>\n")
    indent += "   "
  }

  def closeSampler2D = {
    indent = indent.substring(3)
    w.write(indent + "</sampler2D>\n")
  }

  def writeSource(sourceName: String) = {
    w.write(indent + "<source>" + sourceName + "</source>\n")
  }

  def writeMinFilter(filter: String) = {
    w.write(indent + "<minfilter>" + filter + "</minfilter>\n")
  }

  def writeMagFilter(filter: String) = {
    w.write(indent + "<magfilter>" + filter + "</magfilter>\n")
  }

  def writeTexture(texname: String) = {
    w.write(indent + " <texture texture=\"" + texname + "-sampler\" texcoord=\"" + texname + "texcoord\"/>")
  }

  def openLightsLibrary(){
    w.write(indent+"<library_lights>\n")
    indent+="   "
  }

  def closeLightsLibrary(){
    indent=indent.substring(3)
    w.write(indent+"</library_lights>\n")
  }

  def openLight(name:String){
    w.write(indent+"<light id=\""+name+"-light\" >\n")
    indent+="   "
  }

  def openPointLight(){
    w.write(indent+"<point>\n")
    indent+="   "
  }

  def closePointLight(){
    indent = indent.substring(3)
    w.write(indent+"</point>\n")
  }

  def closeLight(){
    indent = indent.substring(3)
    w.write(indent+"</light>\n")
  }

  def writeColor(r:Float,g:Float,b:Float){
    w.write(indent+"<color>"+r+" "+g+" "+b+"</color>\n")
  }

  def writeLightInstance(name:String){
    w.write(indent+"<instance_light url=\"#"+name+"-light\" />\n")
  }
}
